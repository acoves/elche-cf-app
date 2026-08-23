# CLAUDE.md — App Elche CF (Kotlin Multiplatform + Compose Multiplatform)

> Documento maestro del proyecto. Léelo entero al inicio de **cada** sesión antes de escribir código.
> Fecha de redacción: agosto 2026. Última fase completada: **Fase 5** (2026-08-23).

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
| 6 | Tienda: `AppWebView` cross-platform + sub-tabs + estados de carga/error | pendiente |
| 7 | Perfil: cabecera, beneficios, configuración, auth mock | pendiente |
| 8 | Red: Ktor, DTOs, mappers, sustitución de mocks, caché y manejo de errores | pendiente |

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
- **Pendiente:** selector de equipo (Primer equipo/Femenino/Ilicitano) en bottom sheet — solo hay datos del primer equipo.
- Verificado en emulador: las 3 top-tabs, el segmentado LaLiga/Copa y la navegación de mes funcionan de verdad.
