# CLAUDE.md — App Elche CF (Kotlin Multiplatform + Compose Multiplatform)

> Documento maestro del proyecto. Léelo entero al inicio de **cada** sesión antes de escribir código.
> Fecha de redacción: agosto 2026. Última fase completada: **Fase 8** (2026-08-24), parcial — ver detalle abajo.

---

## 0. Cómo trabajar en este proyecto (reglas para el asistente)

1. **Se trabaja por fases.** No adelantes código de fases futuras. Al terminar una fase, para y espera "Siguiente Fase".
2. **Cada respuesta de fase entrega:** archivos completos (ruta + contenido), qué comando ejecutar para verificar, y una checklist de "esto debería funcionar ahora".
3. **Nada de pseudocódigo ni `// TODO: implementar aquí`** en código que se supone compilable. Si algo es un stub intencional, se marca con `// FASE N:` indicando cuándo se completa.
4. **Verifica versiones antes de fijarlas.** Las versiones de §2 son las vigentes en agosto de 2026; antes de escribir `libs.versions.toml` confirma en Maven Central / release notes que siguen siendo las últimas estables y ajusta.
5. **Un cambio de decisión técnica = una entrada en §12 (Registro de decisiones).**
6. **Idioma:** código, nombres de clases y comentarios en inglés. UI, textos de usuario y conversación en español.
7. **Datos:** mientras no exista la capa de red (Fase 8), todo se alimenta de `Mock*DataSource` con la **misma interfaz** que tendrá la implementación real. Cambiar de mock a real debe ser cambiar una línea en el módulo de Koin.

---

## 1. Producto

App móvil oficiosa del **Elche CF** para Android e iOS, con una sola base de código en Kotlin.
Referencia funcional y de estructura: la app del FC Barcelona (capturas aportadas por el usuario). Referencia de identidad visual: **solo Elche CF** — nada de azulgrana en ningún token.

Audiencia: aficionado franjiverde que abre la app antes, durante y después del partido. El trabajo principal de la Home es responder "¿cuándo es el próximo partido y qué puedo hacer ahora mismo?".

**Cinco pestañas** (bottom navigation):

| # | Pestaña | Icono | Contenido |
|---|---------|-------|-----------|
| 1 | Para ti | Escudo | Match center: cuenta atrás, predictor de resultado, quiz, valorar partido |
| 2 | Calendario | Calendario | Top-tabs: Calendario · Clasificaciones · Jugadores |
| 3 | Clips | Play | Feed de vídeos cortos / noticias |
| 4 | Tienda | Bolsa | Sub-tabs: Tienda · Entradas · Membership (**WebView**, no nativo) |
| 5 | Perfil | Usuario | Avatar, beneficios, configuración |

---

## 2. Stack técnico (versiones objetivo, agosto 2026)

Verificar antes de usar. Fuente: kotlinlang.org (compatibilidad KMP/CMP), blog de JetBrains, insert-koin.io, Maven Central.

| Área | Elección | Versión |
|------|----------|---------|
| Lenguaje | Kotlin (K2) | **2.4.10** (stable) |
| UI | Compose Multiplatform | **1.11.1** |
| Build | Gradle | 9.5.x + AGP estable compatible |
| JDK | Toolchain | 17 |
| Android | compileSdk / minSdk | 36 / 26 (verificar si ya hay 37) |
| iOS | Deployment target mínimo | **14.0** (CMP 1.11 subió el mínimo de 13 a 14) |
| Targets | `androidTarget`, `iosArm64`, `iosSimulatorArm64` | *(`iosX64` eliminado en CMP 1.11)* |
| Red | Ktor Client | **3.4.0** |
| Serialización | kotlinx-serialization JSON | última estable |
| Concurrencia | kotlinx-coroutines | última estable |
| Fechas | kotlinx-datetime | última estable (crítico para la cuenta atrás) |
| DI | Koin (vía BOM) | **4.2.x** — `koin-core`, `koin-compose`, `koin-compose-viewmodel`, `koin-android` |
| Navegación | `org.jetbrains.androidx.navigation:navigation-compose` | última estable |
| ViewModel | `org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose` | última estable |
| Imágenes | Coil 3 (`coil-compose`, `coil-network-ktor3`) | **3.4.0** |
| WebView | `io.github.kevinnzou:compose-webview-multiplatform` | **2.0.3** |
| Persistencia | DataStore (preferencias) + Room KMP *(solo si hace falta caché offline)* | última estable |
| Logging | Napier o Kermit | última estable |

### Decisiones cerradas sobre el stack

- **Navegación: `navigation-compose` de JetBrains, no Voyager.** El prompt inicial mencionaba Voyager/Decompose; Voyager ha perdido tracción y esta app tiene una jerarquía simple (5 tabs + detalles). Navigation multiplataforma con rutas `@Serializable` es lo estándar en 2026 y lo que mejor documentación tiene. *Alternativa si la navegación anidada se complica: Decompose 3.x. Navigation 3 existe pero su integración con Koin sigue en alpha — no lo usamos todavía.*
- **Un solo módulo de código compartido `composeApp` con paquetes por capa/feature.** Multi-módulo Gradle por feature es correcto para equipos grandes, pero aquí multiplica el tiempo de build y la fricción sin beneficio real. Si el proyecto crece, se extraen módulos por feature (ver §12). *Actualizado en Fase 1 (ver §12, entrada AGP 9): existe un segundo módulo `androidApp`, fino y sin lógica propia, exigido por AGP 9 — no es una modularización por feature, sigue siendo "un solo módulo de código" a efectos de esta decisión.*
- **WebView por librería, no `expect/actual` a mano.** `compose-webview-multiplatform` envuelve `WebView` (Android) y `WKWebView` (iOS) con una API Compose. Se aísla detrás de nuestro propio `AppWebView` composable para poder cambiar de librería sin tocar las pantallas.

---

## 3. Arquitectura

**Clean Architecture pragmática + MVVM con flujo unidireccional.**

```
UI (Composable, stateless)
  ↕ UiState / UiEvent
ViewModel (androidx lifecycle KMP, StateFlow)
  ↕ modelos de dominio
Domain (interfaces de repositorio, modelos, casos de uso solo si aportan)
  ↕
Data (Repository impl → RemoteDataSource(Ktor) / MockDataSource / LocalDataSource)
```

Reglas:

- **Domain no conoce a nadie.** Sin Ktor, sin Compose, sin Android en `domain/`.
- **DTO ≠ modelo de dominio.** Todo DTO vive en `data/remote/dto`, con `@Serializable`, y se convierte con un mapper en `data/mapper`. La UI jamás ve un DTO.
- **Un `UiState` por pantalla**, `data class` con `isLoading`, `error`, y los datos. Nada de `Loading/Success/Error` como sealed class si la pantalla puede mostrar datos y error a la vez.
- **Errores tipados:** `Result<T>` propio o `AppResult<T, AppError>`; nunca excepciones cruzando a la UI.
- **Los composables no llaman a repositorios.** State hoisting siempre: `Screen(state, onEvent)` + `ScreenRoute(viewModel)` que conecta.
- **`koinViewModel<T>()`** para obtener ViewModels en Compose; declarados con `viewModelOf(::X)`.

### Estructura de carpetas

```
elche-app/
├─ gradle/libs.versions.toml
├─ settings.gradle.kts
├─ build.gradle.kts
├─ iosApp/                          # proyecto Xcode (entry point iOS)
├─ androidApp/                      # FASE 1: módulo fino exigido por AGP 9 (ver §12). Solo entry point Android.
│  ├─ build.gradle.kts              #   plugin com.android.application, sin lógica propia
│  └─ src/main/
│     ├─ AndroidManifest.xml        #   declara Application + Activity
│     └─ kotlin/es/elchecf/app/     #   MainActivity, ElcheApplication (invoca initKoin)
└─ composeApp/                      # módulo de código compartido (KMP), sigue siendo "el módulo único" a efectos de §2
   ├─ build.gradle.kts              # plugin com.android.kotlin.multiplatform.library (no com.android.application)
   └─ src/
      ├─ commonMain/
      │  ├─ kotlin/es/elchecf/app/
      │  │  ├─ App.kt               # root composable + tema + NavHost
      │  │  ├─ di/                  # AppModule, DataModule, ViewModelModule, initKoin()
      │  │  ├─ core/
      │  │  │  ├─ result/           # AppResult, AppError
      │  │  │  ├─ util/             # formatters, extensiones de fecha
      │  │  │  └─ webview/          # AppWebView (wrapper)
      │  │  ├─ designsystem/
      │  │  │  ├─ theme/            # ElcheColors, ElcheTypography, ElcheShapes, ElcheTheme
      │  │  │  ├─ component/        # ElcheButton, ElcheCard, VersusCard, SectionHeader…
      │  │  │  └─ icon/
      │  │  ├─ navigation/          # Routes (@Serializable), RootNavHost, BottomBar
      │  │  ├─ domain/
      │  │  │  ├─ model/            # Match, Team, StandingRow, Player, CupTie, Benefit…
      │  │  │  └─ repository/       # MatchRepository, StandingsRepository, PlayerRepository…
      │  │  ├─ data/
      │  │  │  ├─ remote/           # HttpClientFactory, api/, dto/
      │  │  │  ├─ mock/             # MockMatchDataSource…  (fuente única hasta Fase 8)
      │  │  │  ├─ local/            # DataStore, caché
      │  │  │  ├─ mapper/
      │  │  │  └─ repository/       # implementaciones
      │  │  └─ feature/
      │  │     ├─ home/             # ForYouScreen, ForYouViewModel, componentes propios
      │  │     ├─ calendar/         # + standings/ + players/
      │  │     ├─ clips/
      │  │     ├─ shop/
      │  │     └─ profile/
      │  └─ composeResources/       # drawable/, font/, values/strings.xml (multi-idioma)
      ├─ androidMain/               # actuals específicos de Android (ya NO MainActivity/Application, viven en androidApp/)
      └─ iosMain/                   # MainViewController, actuals
```

Convención: **un componente reutilizable vive en `designsystem/component`; si solo lo usa una pantalla, vive junto a esa feature.**

---

## 4. Identidad visual — Elche CF

### 4.1 Paleta

Derivada del escudo oficial (valores de referencia del crest).

| Token | Hex | Uso |
|-------|-----|-----|
| `ElcheGreen` | `#05642C` | Color primario. Barras, franjas, estados activos, iconos seleccionados |
| `ElcheGreenDeep` | `#04431E` | Superficies oscuras, degradados, headers |
| `ElcheGreenSoft` | `#E6F1E9` | Fondos de chip, filas alternas, estados hover |
| `White` | `#FFFFFF` | Fondo principal (la app es **clara** por defecto: camiseta blanca) |
| `Gold` | `#E6C777` | Acento: CTA principales, cifras de cuenta atrás, badges |
| `GoldDeep` | `#866B10` | Texto sobre dorado, bordes de acento |
| `CrestRed` | `#C5112E` | Solo elementos heráldicos / indicador "FUERA" |
| `CrestBlue` | `#1B458F` | Solo elementos heráldicos / indicador "CASA" |
| `Ink` | `#101410` | Texto principal |
| `InkMuted` | `#6B7269` | Texto secundario, cabeceras de tabla |
| `Divider` | `#E4E7E3` | Separadores de 1dp |

Reglas de color:
- El **verde manda, el dorado interrumpe**. El dorado solo en el elemento accionable más importante de cada pantalla (nunca dos CTA dorados a la vez visibles).
- Rojo y azul del escudo **no** son colores de UI: solo etiquetas casa/fuera y el propio escudo.
- Modo oscuro: `ElcheGreenDeep` como fondo, blanco como texto, dorado idéntico. Se define en Fase 2 pero no se pule hasta el final.

### 4.2 Tipografía

El club no publica su tipografía corporativa. Aproximación (ambas SIL OFL, seguras para distribuir dentro de la app):

- **Display / titulares:** `Archivo Condensed` (o `Barlow Condensed`) en 600–700, **siempre en MAYÚSCULAS**, tracking ligeramente negativo. Es el registro deportivo condensado de la cartelería de club.
- **Cuerpo y datos:** `Inter` 400/500/600. Números con `tabular-nums` en tablas y marcadores.

Escala (`ElcheTypography`): `displayL 40 / displayM 32 / titleL 24 / titleM 20 / body 16 / bodyS 14 / label 12 / mono-num 28`.
Las fuentes se cargan desde `composeResources/font` con `Font(Res.font.…)` — nunca fuentes del sistema por defecto para titulares.

### 4.3 Formas, espaciado y motion

- Radios: tarjetas `20.dp`, tarjetas grandes `24.dp`, chips/pills `50%`, botones `28.dp`.
- Espaciado: escala de 4 → `4, 8, 12, 16, 24, 32, 48`. Margen lateral de pantalla: `16.dp`.
- Elevación: sombra mínima; la separación se hace con color y espacio, no con drop shadows pesadas.
- Motion: transiciones de 200–250 ms, `FastOutSlowIn`. Solo un momento animado por pantalla (el contador que cambia, el marcador del predictor). Respetar reduce-motion.

### 4.4 Elemento firma: **la franja**

El Elche es "el franjiverde". La franja horizontal verde es el recurso gráfico recurrente de toda la app:
- Indicador de tab activo en los top-tabs y en la bottom bar.
- Divisor entre secciones (banda de 6dp en vez de línea de 1dp en los `SectionHeader`).
- Indicador de carga: una franja que recorre horizontalmente, en vez de un spinner circular.
- Fondo de la tarjeta de partido: la franja separa los dos lados del *versus*.

### 4.5 Tarjeta de partido (`VersusCard`)

Componente insignia (equivalente a la tarjeta Elche–Barça de la referencia):
- Fondo dividido verticalmente en dos: color primario del equipo local | color primario del visitante. Los colores del rival vienen del modelo `Team.primaryColor` (hex), **no hardcodeados por pantalla**.
- Centro: cuenta atrás en 4 bloques `DÍAS · H · MIN · S` con números en dorado sobre fondo translúcido, y CTA "Ficha del partido".
- Pie blanco: fecha, hora, competición.
- Si el partido está en juego: los bloques de cuenta atrás se sustituyen por el marcador en vivo (previsto para Fase 8).

---

## 5. Mapa de pantallas

### 5.1 Para ti (`feature/home`)
- Header: escudo + "HOLA {nombre}" + avatar circular.
- Fila de accesos rápidos (chips horizontales scrollables).
- Historias circulares con borde verde/dorado (opcional, Fase 4 solo maqueta).
- `VersusCard` con cuenta atrás al próximo partido.
- **Acierta el resultado:** escudos de ambos equipos + marcador con `+` / `−` por equipo, botón "Enviar predicción". Estado local hasta Fase 8; bloqueado al empezar el partido.
- **El Quiz del Partido:** tarjeta con puntuación `3/5` y CTA.
- **Valora el partido:** CTA, visible solo si hay partido finalizado en las últimas 48 h.

### 5.2 Calendario (`feature/calendar`)
Top-tabs: **Calendario · Clasificaciones · Jugadores**. Selector superior de equipo (Primer equipo, Femenino, Ilicitano…) en bottom sheet.
- **Calendario:** vista mensual L–D con selector de mes; el día con partido muestra el escudo del rival y una etiqueta `CASA` (azul) / `FUERA` (rojo). Tocar el día → ficha del partido.
- **Clasificaciones:** segmented control `LA LIGA · COPA` (más `CHAMPIONS` solo si aplica). Tabla con columnas `J V E D DG PTS`; fila del Elche resaltada con la franja verde. Barra lateral de color por zona (europa / descenso).
- **Copa:** bracket octavos → cuartos → semis → final, con líneas conectoras. Scroll horizontal + vertical; los marcadores en cajas cuadradas, ganador en oscuro.
- **Jugadores:** grid de 2 columnas agrupado por `PORTEROS / DEFENSAS / CENTROCAMPISTAS / DELANTEROS`, dorsal grande en la esquina y nombre en dos líneas (nombre normal + apellido en bold mayúsculas).

### 5.3 Clips (`feature/clips`)
Feed vertical de vídeos cortos / noticias. Fase inicial: lista de tarjetas con miniatura, duración y título. Reproducción real fuera de alcance hasta que se defina proveedor.

### 5.4 Tienda (`feature/shop`) — **WebViews**
Sub-tabs: `Tienda · Entradas · Membership`, cada uno un `AppWebView` a:
- Tienda → `https://tienda.elchecf.es`
- Entradas → `https://entradas.elchecf.es`
- Membership / abonos → sección de abonados de `https://www.elchecf.es`

Requisitos del wrapper: barra superior con título y botón atrás que **navega dentro del WebView** si hay historial; indicador de carga (la franja); manejo de error con reintento; los enlaces `mailto:`, `tel:` y pasarelas externas se abren fuera de la app.

### 5.5 Perfil (`feature/profile`)
- Cabecera: avatar circular, nombre completo, chip de estado ("Socio · 2 años").
- **Beneficios:** lista con miniatura, título en mayúsculas, subtítulo descriptivo y botón `···`.
- **Configuración:** Información personal · Notificaciones · Cookies · Inicio de sesión · Política de privacidad · Condiciones legales.
- Auth mockeada en Fase 7; sin backend real hasta que se decida proveedor.

---

## 6. Datos y APIs

**Estrategia mock-first.** El dominio y los repositorios se diseñan ahora; las fuentes reales entran en Fase 8.

Modelos de dominio mínimos: `Team(id, name, shortName, crestUrl, primaryColorHex)`, `Match(id, home, away, kickoffInstant, competition, venue, status, score?)`, `StandingRow(position, team, played, won, drawn, lost, goalDiff, points)`, `Player(id, firstName, lastName, number, position, photoUrl)`, `CupTie(round, home, away, legs, aggregate?)`, `Prediction(matchId, homeGoals, awayGoals)`.

Candidatos de proveedor de datos (evaluar en Fase 8 — **verificar plan gratuito, cuota y si LaLiga está incluida antes de comprometerse**): API-Football (api-sports.io), football-data.org, SportMonks. LaLiga no ofrece API pública abierta. Alternativa de bajo coste para un proyecto no comercial: JSON estático propio actualizado a mano o mediante scraping propio.

Notas de diseño de red (Fase 8):
- Un único `HttpClient` con `ContentNegotiation(json { ignoreUnknownKeys = true })`, `Logging`, timeouts y `DefaultRequest` con base URL y cabecera de API key.
- La API key **nunca** en el repositorio: `local.properties` → `BuildKonfig`/campo generado.
- Caché: `Cache-Control` + capa local para clasificación y calendario (cambian poco); la cuenta atrás se calcula en cliente con `Clock.System.now()`.

**Zona horaria:** todos los `kickoff` se guardan como `Instant` UTC y se formatean con la zona del dispositivo. La cuenta atrás usa un `flow` de tick de 1 s que se cancela con el ciclo de vida.

---

## 7. Convenciones de código

- `ktlint` o `spotless` desde la Fase 1; formateo automático antes de cada entrega.
- Nombres: `XxxScreen` (stateless), `XxxRoute` (conecta ViewModel), `XxxViewModel`, `XxxUiState`, `XxxEvent`, `XxxRepository` / `XxxRepositoryImpl`, `XxxDto`, `toDomain()` para mappers.
- Composables públicos con `modifier: Modifier = Modifier` como primer parámetro opcional.
- `@Preview` (multiplataforma) para cada componente del design system.
- Textos siempre desde `composeResources` (`stringResource(Res.string.…)`), nunca literales en la UI — la app se prepara para español y valenciano.
- Accesibilidad: `contentDescription` en escudos e iconos accionables; tamaño mínimo táctil 48dp; contraste AA sobre verde y dorado.
- Tests: `commonTest` con `kotlin.test` + `turbine` para los ViewModels a partir de la Fase 4. Sin tests de UI hasta que la UI esté estable.

---

## 8. Fases

| Fase | Contenido | Estado |
|------|-----------|--------|
| 1 | Setup: `settings.gradle.kts`, `libs.versions.toml`, `build.gradle.kts`, targets, estructura de carpetas, `initKoin`, app corriendo en Android e iOS con una pantalla vacía | **completada** — Android verificado (emulador, screenshot); iOS escrito y sin verificar (sin Mac, ver §12) |
| 2 | UI Kit: `ElcheTheme` (colores, tipografías, formas), componentes base (botón, card, franja, section header, top bar) + previews | **completada** — verificado en emulador Android |
| 3 | Navegación: bottom bar de 5 tabs, `NavHost`, rutas serializables, esqueletos de las 5 pantallas | **completada** — verificado en emulador Android (cambio de tab real) |
| 4 | Para ti: `VersusCard` + cuenta atrás real, predictor interactivo, tarjetas de quiz y valoración, `MatchRepository` con mocks | **completada** — verificado en emulador (ViewModel real, envío de predicción bloquea el formulario) |
| 5 | Calendario: top-tabs, vista mensual, tabla de clasificación, bracket de Copa, grid de jugadores | **completada** — verificado en emulador (3 top-tabs + segmentado LaLiga/Copa) |
| 6 | Tienda: `AppWebView` cross-platform + sub-tabs + estados de carga/error | **completada** — verificado en emulador cargando las webs reales del club |
| 7 | Perfil: cabecera, beneficios, configuración, auth mock | **completada** — verificado en emulador (login/logout mock reactivo) |
| 8 | Red: Ktor, DTOs, mappers, sustitución de mocks, caché y manejo de errores | **completada parcialmente** — Partido y Clasificación con datos reales de football-data.org, verificado en emulador; Jugadores y Copa siguen en mock (el plan gratuito no los cubre, ver §13) |

**Criterio de "fase terminada":** compila en Android **y** en iOS, sin warnings nuevos, y el usuario ha podido ejecutarlo.

---

## 9. Entorno necesario

- Android Studio (última estable) + plugin **Kotlin Multiplatform**.
- JDK 17.
- Xcode 26.x en macOS para el target iOS (sin Mac, se desarrolla solo Android y se deja iOS para más tarde — **avisar si este es el caso**).
- CocoaPods no es necesario con este stack; el `iosApp` consume el framework directamente.

---

## 10. Riesgos y aspectos legales

- **Escudos y marca Elche CF son propiedad del club.** Uso educativo/personal aceptable; cualquier publicación en stores requiere permiso escrito. No incluir assets oficiales descargados de fuentes no autorizadas en el repositorio público.
- **Tipografías:** usar solo fuentes con licencia de incrustación (OFL). No replicar la tipografía corporativa del club sin licencia.
- **Datos deportivos:** casi todos los proveedores prohíben la redistribución; revisar términos antes de publicar.
- **WebViews:** cumplir la política de las stores (no envolver una web entera como si fuera app; aquí es una sección secundaria, lo cual es aceptable).

---

## 11. Glosario del proyecto

*Franjiverde* (aficionado/equipo del Elche) · *Martínez Valero* (estadio) · *Ficha del partido* (detalle de encuentro) · *Casa/Fuera* (local/visitante) · *DG* (diferencia de goles) · *J V E D PTS* (jugados, victorias, empates, derrotas, puntos).

---

## 12. Registro de decisiones (ADR ligero)

| Fecha | Decisión | Motivo |
|-------|----------|--------|
| 2026-08 | Navigation Compose de JetBrains en lugar de Voyager | Voyager sin tracción; jerarquía simple; mejor soporte y documentación |
| 2026-08 | Módulo único `composeApp` | Velocidad de build y simplicidad; se modulariza solo si crece |
| 2026-08 | Verde `#05642C` como primario (no `#006022`) | Tomado del escudo oficial; se mantiene coherencia con los assets del club |
| 2026-08 | Mock-first con interfaces de repositorio definidas desde Fase 4 | Permite maquetar sin depender de contratar API |
| 2026-08-23 | Proyecto movido fuera de OneDrive (`C:\Users\Usuario\Dev\elche-cf-app`); `git init` + remoto `github.com/acoves/elche-cf-app` | OneDrive causa locks de archivo y caché corrupta intermitentes con Gradle en Windows |
| 2026-08-23 | Módulo `androidApp` (fino, solo `com.android.application` + entry point) añadido junto a `composeApp` | AGP 9.2.0 prohíbe aplicar `com.android.application`/`com.android.library` en el mismo módulo que `org.jetbrains.kotlin.multiplatform`. `composeApp` pasa a usar el plugin `com.android.kotlin.multiplatform.library` (Android Library, sin `MainActivity`/`Application`, que se mudan a `androidApp`). No es una vuelta al multi-módulo por feature (§2/§3): sigue habiendo un único módulo de código, `androidApp` no tiene lógica propia |
| 2026-08-24 | football-data.org como proveedor de datos reales (Fase 8) | Único candidato de §6 con plan gratuito confirmado que incluye LaLiga (`PD`) sin tarjeta de crédito; no cubre Copa del Rey ni dorsales de jugadores, así que esas dos áreas se quedan en mock hasta encontrar otra fuente |
| 2026-08-24 | `Team.ELCHE_ID` cambia de `"elche-cf"` a `"285"` | El valor antiguo era un id inventado para los mocks; con datos reales nunca coincidía con el id que manda la API, así que "CASA/FUERA" en el calendario y el resaltado de la fila del Elche en la clasificación fallaban en silencio. `285` es el id real de Elche CF en football-data.org |
| 2026-08-24 | Fotos de Beneficios en Perfil desde Wikimedia Commons, URL directa de `upload.wikimedia.org` (no `Special:FilePath`) | Única fuente de imágenes con licencia libre verificable a mano; `Special:FilePath` devuelve 429 (rate limit) al pedir varias miniaturas a la vez porque pasa por el servidor de la wiki antes de redirigir — la URL final del CDN no tiene ese problema |
| 2026-08-24 | Tienda puede arrancar en una sub-pestaña concreta (`ShopScreen(initialTab, onInitialTabConsumed)`) en vez de siempre en Tienda | El pop-up de un beneficio en Perfil necesita poder abrir directamente en Membership; se hoisted un `ShopTab?` a `App.kt` en vez de meter routing anidado solo para este caso |
| 2026-08-25 | `navigate()` del bottom bar usa `popUpTo(start){saveState=true}` + `restoreState=true` | Sin esto cada cambio de pestaña creaba una entrada nueva en el back stack → ViewModel nuevo → "Cargando" otra vez y pantallas mezcladas un instante al no haber transición. Patrón estándar de bottom nav, reutiliza la instancia y el estado ya cargado de cada pestaña |
| 2026-08-25 | `ClubTeam` (Primer equipo/Femenino/Ilicitano): Femenino e Ilicitano en mock, no football-data.org | Verificado con la API (`GET /v4/competitions`, 13 competiciones en el plan): no hay Liga F ni categorías regionales españolas. `FootballDataMatchDataSource`/`FootballDataStandingsDataSource` delegan en el mock correspondiente para esos dos equipos en vez de fallar o devolver vacío |
| 2026-08-25 | Política de privacidad y Condiciones legales: pantalla nativa con contenido real de elchecf.es, no WebView | El usuario lo pidió explícitamente — contenido extraído a mano de elchecf.es/lopd y elchecf.es/nota-legal (verificado agosto 2026) e integrado como texto en `feature/profile/legal/LegalContent.kt`, con el estilo tipográfico de la app |
| 2026-08-25 | Perfil (nombre y avatar) pasa a ser mutable en memoria (`ProfileDataSource.profile: StateFlow`) | Mismo patrón que `AuthRepository.isLoggedIn`: editar en "Información personal" o el selector de avatar necesita reflejarse al momento en toda la pantalla, no solo al recargar |
| 2026-08-25 | `withHostTest {}` añadido al bloque `android {}` de `composeApp` | El plugin `com.android.kotlin.multiplatform.library` (AGP 9) ignora `commonTest` para Android sin esto — sin él, los tests solo se compilaban para iOS (no ejecutable aquí) y nunca se llegaban a correr de verdad |

---

## 13. Changelog

### Fase 1 — 2026-08-23

- Proyecto movido fuera de OneDrive; `git init` + remoto GitHub.
- Setup Gradle: `settings.gradle.kts`, `gradle/libs.versions.toml` (versiones verificadas en Maven Central, no de memoria), `build.gradle.kts` raíz, wrapper Gradle 9.7.1.
- Módulos `composeApp` (KMP, `com.android.kotlin.multiplatform.library`) + `androidApp` (thin, `com.android.application`) — split forzado por AGP 9 (ver §12).
- `App.kt` (pantalla vacía con `safeDrawingPadding`) + `InitKoin.kt` en `composeApp/commonMain`.
- `MainActivity`/`ElcheApplication`/`AndroidManifest.xml` en `androidApp`.
- Verificado en emulador Android (Pixel 10 Pro XL): instala, arranca, muestra "Elche CF".
- Entry point iOS (`MainViewController.kt`, `iOSApp.swift`, `ContentView.swift`, `Info.plist`) escrito, sin verificar — no hay Mac disponible.
- ktlint (`org.jlleitschuh.gradle.ktlint`) + `.editorconfig`, `ktlintCheck` en verde.

### Fase 2 — 2026-08-23

- `designsystem/theme`: `ElcheColor` (paleta §4.1), `ElcheColorScheme` (light + dark sin pulir), `ElcheShape` (20/24/28dp + pill), `ElcheSpacing` (4→48), `ElcheTypography` (displayL/M, titleL/M, body, bodyS, label, monoNum) + `ElcheTheme`.
- Fuentes OFL descargadas de google/fonts: Barlow Condensed SemiBold/Bold (titulares) e Inter variable con pesos 400/500/600 vía `FontVariation` (soportado en iOS/desktop desde CMP 1.8, confirmado antes de usarlo).
- `designsystem/component`: `ElcheButton` (variantes Primary/Accent), `ElcheCard`, `Franja`, `SectionHeader`, `ElcheTopBar`, cada uno con `@Preview`.
- Verificado en emulador Android con una galería temporal en `App()` (se sustituye en Fase 3).

### Fase 3 — 2026-08-23

- `navigation/Route.kt` (5 rutas `@Serializable`), `RootNavHost.kt`, `BottomBar.kt` (indicador verde de marca).
- `App()` ahora es `Scaffold` real: `ElcheBottomBar` + `RootNavHost`, sustituye la galería de la Fase 2.
- Esqueletos de las 5 pantallas (`feature/home|calendar|clips|shop|profile`), cada uno con `SectionHeader` + nota de qué fase lo completa. **Pendiente aclarar con el usuario:** Clips no tiene fase numerada asignada en la tabla de §8.
- `designsystem/icon/ElcheIcon.kt` con Material Icons Extended (nota: pinned a 1.7.3 por JetBrains, migrar a Material Symbols cuando haga falta — ver warning de build).
- Añadidas dependencias `navigation-compose` + `kotlinx-serialization-json` y plugin de serialización al catálogo.
- `gradle.properties` nuevo: más heap para Gradle (D8 se quedaba sin memoria dexeando `material-icons-extended`) y `kotlin.native.ignoreDisabledTargets` para silenciar el aviso de tests iOS en Windows.
- Verificado en emulador Android: las 5 tabs cambian de pantalla y el indicador activo se resalta en verde.

### Fase 4 — 2026-08-23

- `core/result` (`AppResult`/`AppError`), `core/util` (`CountdownParts`/`countdownFlow`, `toColorOrNull`, `toKickoffLabel` con `kotlinx-datetime`).
- Dominio: `Team`, `Match`/`Score`/`MatchStatus`, `Prediction`, `MatchRepository`.
- Datos: `MatchDataSource` + `MockMatchDataSource` (datos de ejemplo, no oficiales — CLAUDE.md §10) + `MatchRepositoryImpl`, `dataModule` en Koin.
- `VersusCard` (`designsystem/component`): fondo dividido por colores de equipo, cuenta atrás real (tick de 1s verificado), CTA dorado, pie blanco.
- `PredictorCard`, `QuizCard`, `ValorateMatchCard` en `feature/home`.
- `ForYouViewModel` + `ForYouUiState` + `ForYouRoute`/`ForYouScreen` (state hoisting), `viewModelModule` en Koin.
- Nota técnica: `kotlinx.datetime.Instant`/`Clock` fueron eliminados (ahora son `kotlin.time.Instant`/`kotlin.time.Clock`) — usar siempre `kotlin.time`, no `kotlinx.datetime`, para esos dos tipos.
- **Pendiente de decidir con el usuario:** fase de Clips sigue sin numerar (arrastrado desde Fase 3).

### Fase 5 — 2026-08-23

- Dominio/datos: `StandingRow`, `Player`/`PlayerPosition`, `CupTie`/`CupRound`; `StandingsRepository`, `PlayerRepository`, `CupRepository` con mocks (`DemoTeams`: 20 clubes de ejemplo, `Team.ELCHE_ID` para identificar "nuestro" equipo en `Match.home/away`). `MatchRepository.getSeasonMatches()` para el calendario.
- `CalendarScreen`: top-tabs Calendario · Clasificaciones · Jugadores (`TabRow` clásico — deprecado en favor de `SecondaryTabRow`, pendiente de migrar; de momento indicador por defecto en verde vía `primary`).
- `MonthlyCalendarScreen`: grid mensual real (kotlinx-datetime), navegación de mes, etiqueta CASA (azul)/FUERA (rojo) por partido.
- `StandingsScreen`: segmented LaLiga/Copa, tabla J V E D DG PTS con Elche resaltado + barra lateral de zona (Europa/descenso, reutiliza `CrestBlue`/`CrestRed`).
- `CupBracketView`: bracket octavos→final con alineación geométrica entre rondas (cada cruce centrado sobre sus dos predecesores); **líneas conectoras dibujadas quedan pendientes** (polish visual, no bloquea la lectura).
- `PlayersScreen`: grid 2 columnas agrupado por posición, dorsal grande + nombre en dos líneas.
- Selector de equipo (Primer equipo/Femenino/Ilicitano) en bottom sheet: implementado en la mejora post-Fase 5 del 2026-08-25 (ver más abajo) — Jugadores sigue sin separar por equipo, no pedido todavía.
- Verificado en emulador: las 3 top-tabs, el segmentado LaLiga/Copa y la navegación de mes funcionan de verdad.

### Fase 6 — 2026-08-23

- `core/webview/AppWebView.kt`: envuelve `compose-webview-multiplatform` 2.0.3 — topbar propio con atrás (navega dentro del WebView si hay historial), franja de carga (progreso real, no indeterminada), error con reintentar.
- `core/webview/ExternalLinkOpener.kt` (expect/actual): `mailto:`/`tel:` se detectan tras el intento de carga fallido y se abren fuera de la app. **Limitación documentada:** la librería no expone `shouldOverrideUrlLoading`, así que pasarelas de pago externas (siempre http/https) no se pueden distinguir de una navegación normal y se cargan dentro del WebView.
- `ShopScreen` con sub-tabs Tienda · Entradas · Membership (`TabRow` clásico, mismo aviso de deprecación que Fase 5).
- `AndroidManifest.xml`: añadido `INTERNET` (obligatorio para WebView).
- Verificado en emulador cargando `tienda.elchecf.es` y `entradas.elchecf.es` reales (con su banner de cookies), botón atrás confirmado tras navegar dentro de la web.

### Fase 7 — 2026-08-24

- `UserProfile`, `Benefit`; `ProfileRepository` + `AuthRepository` (sesión en memoria vía `MockAuthDataSource`, `AuthDataSource.isLoggedIn: StateFlow<Boolean>`).
- `ProfileScreen`: cabecera (avatar con inicial, nombre, chip "Socio · X años"), sección Beneficios (oculta si no hay sesión), sección Configuración con 6 filas (Información personal, Notificaciones, Cookies, Cerrar/Iniciar sesión, Política de privacidad, Condiciones legales).
- Fila de sesión reactiva a `AuthRepository.isLoggedIn`: cambia icono/texto y dispara `login()`/`logout()`.
- **Pendiente:** las 5 filas de Configuración salvo sesión son inertes (`onClick = {}`), no numeradas — cada una necesitará su propia pantalla en una fase futura sin asignar.
- Verificado en emulador: cerrar sesión oculta beneficios y cambia la cabecera a "Iniciar sesión"; volver a iniciar sesión lo restaura — probado en ambas direcciones.

### Fase 8 — 2026-08-24 (parcial: Partido + Clasificación)

- Proveedor elegido: **football-data.org** (plan gratuito, LaLiga incluida, sin tarjeta — ver §12). API key en `local.properties` (`FOOTBALL_DATA_API_KEY`, nunca versionada) inyectada vía tarea Gradle `generateApiKeys` que escribe `core/network/ApiKeys.kt` en `build/generated/`, añadido como source dir extra de `commonMain`.
- `core/network/HttpClientFactory.kt`: `HttpClient` único con `ContentNegotiation(json)`, `Logging`, `HttpTimeout(10s)` y cabecera `X-Auth-Token` por defecto.
- `data/remote/dto/{TeamDto,MatchDto,StandingsDto}.kt` (formas reales del JSON de la API, verificadas con `curl` antes de escribir Kotlin) + `data/mapper/{MatchMapper,StandingsMapper}.kt`.
- `FootballDataMatchDataSource` (`/teams/285/matches?competitions=PD`) y `FootballDataStandingsDataSource` (`/competitions/PD/standings`, filtra tabla `TOTAL`) sustituyen a los mocks en `DataModule` — el cambio mock↔real sigue siendo esas 2 líneas, tal como exige §0.7.
- `MatchRepository`/`StandingsRepository` (interfaz, impl, data source, mocks) devuelven ahora `AppResult<T, AppError>` con el nuevo caso `AppError.Network`; `ForYouViewModel`/`MonthlyCalendarViewModel`/`StandingsViewModel` y sus `UiState` propagan el error a la pantalla en vez de fallar en silencio.
- **Se quedan en mock, a propósito:** `PlayerRepository` (la API no da dorsales) y la Copa del Rey en `StandingsRepository`/`CupRepository` (no está en el plan gratuito) — devuelve lista vacía para `Competition.Copa` en vez de fabricar datos falsos.
- `sendPrediction()` sigue mock: la API es de solo lectura, no hay backend propio de predicciones (sin proveedor decidido todavía).
- **Bug encontrado y corregido durante la verificación en emulador:** `Team.ELCHE_ID` valía `"elche-cf"` (id de mock) y nunca coincidía con el id real (`"285"`) que manda la API — CASA/FUERA en el calendario y el resaltado de la fila del Elche en la clasificación no funcionaban con datos reales aunque compilaban sin error. Corregido en `domain/model/Team.kt` (ver §12).
- Verificado en emulador con datos reales: próximo partido real (Elche–Racing de Santander, 28/08), calendario mensual con CASA/FUERA correctos tras el fix, clasificación real de LaLiga 2026-27 con la fila del Elche resaltada en verde.

### Mejoras post-Fase 8 — 2026-08-24 (Tienda: carga y precarga)

- `AppWebView` muestra un overlay de carga de marca (spinner verde + "Cargando…") mientras el motor del WebView pinta su primer frame, en vez de dejar ver el blanco del propio motor.
- `prefetchShopWebView()` (expect/actual, `core/webview/ShopWebViewPrefetch.kt`): en Android usa `Profile.prefetchUrlAsync` de `androidx.webkit` (feature-gated con `WebViewFeature.isFeatureSupported`) para precargar la URL de Tienda a la caché real del motor del WebView desde el arranque de la app, sin montar ningún WebView ni bloquear el arranque. Sin equivalente en iOS todavía (no-op, ver comentario `// FASE 6 (iOS)`).
- Se probó primero precargar las 3 pestañas de Tienda manteniéndolas siempre montadas (ocultas con tamaño 0dp): rompía el renderizado de verdad (pantalla en blanco permanente pese a que el árbol de layout era correcto) y el arranque en frío pasaba de ~6s a +10s — revertido. `prefetchUrlAsync` es la alternativa segura: no mantiene vistas nativas vivas, solo caché de red.
- Solo se precarga Tienda; Entradas sigue su carga normal al seleccionarla (precargar varias a la vez sí se notó en el arranque).
- Membership se queda sin URL (antes cargaba la home del club, que no era su sección real) — contenido pendiente de decidir.

### Mejora post-Fase 7 — 2026-08-24 (Perfil: beneficios reales, imágenes, pop-up)

- `ProfileScreen` reorganizado siguiendo el formato de referencia: cabecera, "Beneficios" con acción "Ver todo" (decorativa, sin pantalla propia todavía), "Configuración" y "Legal" como secciones separadas, "Cerrar sesión"/"Eliminar la cuenta" (stub)/versión de la app al pie.
- Los 6 beneficios pasan a ser los reales del Carnet Franjiverde (abonados.elchecf.es, verificados agosto 2026 vía búsqueda web — la propia web de abonados es una SPA sin contenido accesible por fetch simple), cada uno con foto real cargada por red: Coil 3 (`coil-compose` + `coil-network-ktor3`, `SingletonImageLoaderFactory` en `App.kt`) contra imágenes de Wikimedia Commons con licencia libre (CLAUDE.md §10).
- `Benefit` gana el campo `detail`: el texto largo del pop-up, distinto del `subtitle` corto de la fila.
- Tocar "···" en una fila abre un `ModalBottomSheet` (título, detalle, botón dorado "Consigue el Carnet Franjiverde") que navega a Tienda → Membership. `ShopScreen` acepta ahora `initialTab`/`onInitialTabConsumed` para poder abrir directamente en una sub-pestaña concreta cuando se llega desde fuera (ver §12).
- Verificado en emulador: header y beneficios con contenido e imágenes reales, pop-up con el formato exacto de la referencia, botón del pop-up navega de verdad a Tienda con Membership ya seleccionada.

### Mejoras post-Fase 3/5 — 2026-08-25 (navegación, calendario, selector de equipo)

- **Fix de fondo en la navegación del bottom bar:** `navigate()` no usaba `popUpTo`/`restoreState`, así que cada cambio de pestaña creaba una entrada nueva en el back stack en vez de reutilizar la existente — cada pantalla volvía a arrancar de cero (ViewModel nuevo, "Cargando" otra vez) y, al quitar la transición por defecto, la pantalla anterior y la nueva (vacía) se veían mezcladas un instante. Corregido con el patrón estándar de bottom nav (`popUpTo(start){saveState=true}` + `launchSingleTop` + `restoreState=true`, ver §12). De paso se afinó la transición entre pestañas a un fundido rápido (120ms) en vez del fundido/tamaño por defecto de la librería.
- **Calendario:** en los días de partido, el número del día se sustituye por el escudo real del rival (Coil, viene de football-data.org) — se omite a propósito, el escudo ya identifica el día. Celdas cuadradas (`aspectRatio(1f)`, el calendario ocupa más alto a propósito) para que el escudo tenga sitio. Sin escudo en la API: inicial del equipo sobre círculo en vez de dejarlo vacío.
- **Selector de equipo (Primer equipo/Femenino/Ilicitano):** bottom sheet encima de Calendario/Clasificaciones/Jugadores (CLAUDE.md §5.2, pendiente desde Fase 5). `ClubTeam` nuevo en `domain/model`; `MatchRepository.getSeasonMatches(team)` y `StandingsRepository.getStandings(competition, team)` ganan el parámetro. Verificado con la API real de football-data.org (`GET /v4/competitions`) que el plan gratuito no cubre Liga F ni las categorías regionales españolas — Femenino e Ilicitano se sirven con datos de ejemplo (`MockMatchDataSource`/`MockStandingsDataSource`, nombres de clubes reales de esas competiciones pero calendario/clasificación inventados, ver §12) en vez de fallar o dejarlos vacíos. Jugadores se queda con el primer equipo, no pedido todavía.
- Verificado en emulador: cambio de pestaña sin mezcla visible ni "Cargando" al volver; calendario con escudos reales del rival; los 3 equipos del selector cargan su calendario y clasificación propios, con el Elche siempre resaltado en la tabla.

### Mejora post-Fase 7 (3) — 2026-08-25 (Perfil: configuración funcional al completo)

- Cabecera centrada: avatar circular, nombre en mayúsculas con la tipografía de titulares (Barlow Condensed) y chip de socio, todo centrado — antes iba en fila a la izquierda.
- Avatar tocable: abre un selector (`AvatarPickerSheet`, grid de 3 columnas) con 9 fotos reales — escudo del Elche (football-data.org), Estadio Martínez Valero, Palmeral de Elche, escudo antiguo de la ciudad, jugadores, afición, bota, panorámica — todas de Wikimedia Commons con licencia libre (CLAUDE.md §10). Se guarda al momento.
- "Información personal" abre una hoja con nombre y apellidos editables (`PersonalInfoSheet`); "¿Eres socio?" abre una hoja de inicio de sesión con clave y PIN (`MemberLoginSheet`); "Cookies" abre una hoja con categorías necesarias/rendimiento/publicidad (`CookiesSheet`).
- "Notificaciones" pasa a ser una pantalla propia dentro de Perfil (`NotificationsScreen` + `NotificationsDirectoScreen`, con cabecera "‹ VOLVER" — `ProfileSubScreenHeader`, patrón nuevo reutilizable), con acceso a los ajustes de notificaciones del sistema (`core/platform/NotificationSettings`, expect/actual) y la sub-pantalla "Directo" con sus 4 interruptores. Estado de los interruptores solo en memoria de la pantalla, sin backend de notificaciones todavía.
- Política de privacidad y Condiciones legales dejan de estar inertes: pantalla nativa (`feature/profile/legal/`) con el contenido real de elchecf.es/lopd y elchecf.es/nota-legal (verificado agosto 2026), no una WebView — el usuario lo pidió explícitamente (ver §12).
- `UserProfile` pasa de `fullName` a `firstName`/`lastName` (con `fullName` calculado) para que la edición por separado tenga sentido. El perfil entero pasa a ser mutable en memoria vía `ProfileDataSource.profile: StateFlow`, mismo patrón que `AuthRepository.isLoggedIn` (ver §12) — editar nombre o avatar se ve reflejado al instante en toda la pantalla.
- Verificado en emulador: selector de avatar aplica el escudo al momento, Notificaciones y Directo iguales a la referencia de diseño aportada, Política de privacidad muestra el texto real navegando con "Volver", Cookies con sus 3 interruptores.

### Limpieza de deuda técnica — 2026-08-25

- **Deduplicación de UI:** `ElcheSheetHeader` (`designsystem/component`) sustituye tres copias manuales del patrón "título + botón cerrar" repetidas en `ProfileScreen.kt`, `ProfileSheets.kt` y `AvatarPickerSheet.kt` — mismo aspecto, un solo sitio para mantenerlo.
- **`ProfileScreen.kt` partido** (era un único archivo de 472 líneas mezclando orquestación, pantalla principal y beneficios): `ProfileScreen.kt` se queda solo con la máquina de estados de sub-pantallas/hojas; `ProfileMainScreen.kt` (cabecera, secciones, `ConfigRow`) y `BenefitViews.kt` (fila y hoja de detalle de un beneficio) pasan a ser archivos propios.
- **Tests de ViewModels** (CLAUDE.md §7 lo daba por hecho desde la Fase 4, nunca se había implementado): `commonTest` con `kotlin.test` + `kotlinx-coroutines-test` + Turbine (ver §12 sobre `withHostTest`). Cobertura de los 5 ViewModels existentes con fakes de sus repositorios — `ProfileViewModel`, `ForYouViewModel`, `StandingsViewModel`, `MonthlyCalendarViewModel`, `PlayersViewModel` (22 tests, `./gradlew :composeApp:testAndroidHostTest`). Solo se ejecutan en el target Android (JVM) en este entorno; el target iOS compila pero no se puede correr sin Mac.
- **`TabRow` deprecado → `ElcheSegmentedTabRow` propio:** el `TabRow` clásico de Material3 (deprecado, sustituido oficialmente por `PrimaryTabRow`/`SecondaryTabRow`) se quita de `CalendarScreen.kt` y `ShopScreen.kt`. En vez de migrar al reemplazo de Material, se construye un control propio (`designsystem/component/ElcheSegmentedTabRow.kt`): pestañas en formato píldora sobre una pista `GreenSoft`, con una franja verde de marca que se desliza animada (`animateDpAsState`, 220ms) tras la pestaña seleccionada en vez del indicador subrayado por defecto — look más moderno y coherente con la identidad de marca que el genérico de Material. Verificado en emulador en Calendario (Calendario/Clasificaciones/Jugadores) y Tienda (Tienda/Entradas/Membership): la animación desliza correctamente y el texto cambia a blanco en la pestaña activa.
