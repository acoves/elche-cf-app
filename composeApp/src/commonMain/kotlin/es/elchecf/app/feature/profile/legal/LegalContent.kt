package es.elchecf.app.feature.profile.legal

/** Un apartado de una pantalla legal: título + párrafos (o puntos de una lista). */
data class LegalSection(
    val heading: String,
    val paragraphs: List<String>,
    val bullets: List<String> = emptyList(),
)

data class LegalPage(
    val title: String,
    val subtitle: String,
    val sections: List<LegalSection>,
    val sourceNote: String,
)

/**
 * Contenido real de elchecf.es (no una WebView: el usuario pidió el texto extraído e integrado
 * con el estilo de la app). Verificado y copiado en agosto 2026 desde elchecf.es/lopd — apartado
 * "LOPD Aficionados Aplicación Móvil", la parte de esa página que aplica a esta app.
 */
val PrivacyPolicyContent =
    LegalPage(
        title = "Política de privacidad",
        subtitle = "Aficionados · Aplicación móvil",
        sections =
            listOf(
                LegalSection(
                    heading = "Publicidad",
                    paragraphs =
                        listOf(
                            "En cumplimiento de lo previsto en el artículo 21 de la Ley 34/2002 de Servicios de la " +
                                "Sociedad de la Información y Comercio Electrónico (LSSICE) y las normativas de " +
                                "privacidad vigentes, le informamos del tratamiento de los datos suministrados para " +
                                "la suscripción y envío de nuestra newsletter.",
                        ),
                ),
                LegalSection(
                    heading = "Fines y legitimación del tratamiento",
                    paragraphs =
                        listOf(
                            "Por consentimiento del interesado (RGPD, art. 6.1.f): el envío de comunicaciones " +
                                "relacionadas con las actividades del club y sus diferentes divisiones y " +
                                "categorías. También se remitirán informaciones sobre productos y servicios, " +
                                "promociones y descuentos propios como de terceros colaboradores o patrocinadores.",
                        ),
                ),
                LegalSection(
                    heading = "Criterios de conservación de los datos",
                    paragraphs =
                        listOf(
                            "Se conservarán durante no más tiempo del necesario para mantener el fin del " +
                                "tratamiento y, cuando ya no sea necesario para tal fin, se suprimirán con medidas " +
                                "de seguridad adecuadas para garantizar la seudonimización de los datos o la " +
                                "destrucción total de los mismos.",
                        ),
                ),
                LegalSection(
                    heading = "Comunicación de los datos",
                    paragraphs =
                        listOf(
                            "No se comunicarán los datos a terceros, salvo obligación legal. El tratamiento de " +
                                "los datos se realiza con la aplicación de un tercero en concreto (WhatsApp). El " +
                                "usuario consiente y manifiesta que conoce y acepta las políticas de privacidad de " +
                                "esta empresa y que autoriza su uso.",
                        ),
                ),
                LegalSection(
                    heading = "Derechos que asisten al interesado",
                    paragraphs = emptyList(),
                    bullets =
                        listOf(
                            "Derecho a retirar el consentimiento en cualquier momento.",
                            "Derecho de acceso, rectificación, portabilidad y supresión de sus datos y a la " +
                                "limitación u oposición a su tratamiento.",
                            "Derecho a presentar una reclamación ante la Autoridad de control (www.aepd.es) si " +
                                "considera que el tratamiento no se ajusta a la normativa vigente.",
                        ),
                ),
                LegalSection(
                    heading = "Datos de contacto para ejercer sus derechos",
                    paragraphs =
                        listOf(
                            "ELCHE CF SAD. Avenida Manuel Martínez Valero, 3 — 03208 Elche (Alicante).",
                        ),
                ),
            ),
        sourceNote = "Contenido de elchecf.es/lopd — verificado agosto 2026.",
    )

/**
 * Contenido real de elchecf.es (no una WebView). Verificado y copiado en agosto 2026 desde
 * elchecf.es/nota-legal, con el apartado de propiedad intelectual y política de enlaces
 * completado desde elchecf.es/lopd (ambas páginas del club comparten ese bloque).
 */
val LegalTermsContent =
    LegalPage(
        title = "Aviso legal",
        subtitle = "Condiciones de uso del sitio",
        sections =
            listOf(
                LegalSection(
                    heading = "Introducción y datos de la compañía",
                    paragraphs =
                        listOf(
                            "El presente se constituye como el Aviso Legal y las Condiciones Generales de " +
                                "Contratación que regulan el acceso, navegación y uso de los servicios ofrecidos " +
                                "por www.elchecf.es (en adelante \"el Sitio Web\"), titularidad de la sociedad " +
                                "Elche Club de Fútbol SAD con CIF A-03039104 (en adelante \"Elche CF\"), y " +
                                "domicilio social en Estadio Martínez Valero, Avda. Manuel Martínez Valero 3, " +
                                "03208 Elche, inscrita en el Registro Mercantil de Alicante, tomo 2.003 general, " +
                                "folio 56, hoja número A-42.149, inscripción 1ª.",
                        ),
                ),
                LegalSection(
                    heading = "Datos identificativos",
                    paragraphs = emptyList(),
                    bullets =
                        listOf(
                            "Dominio: elchecf.es",
                            "Nombre comercial: Elche CF SAD",
                            "NIF: A03039104",
                            "Domicilio: Avda. Manuel Martínez Valero 3, 03208 Elche (Alicante)",
                            "Teléfono: 965 45 97 14",
                            "Registro Mercantil de Alicante, tomo 2.003, folio 56, hoja A-42.149",
                        ),
                ),
                LegalSection(
                    heading = "Menores de edad",
                    paragraphs =
                        listOf(
                            "Para navegar por el Sitio Web no hace falta tener ningún requisito de mayoría de edad.",
                        ),
                ),
                LegalSection(
                    heading = "Política de enlaces",
                    paragraphs =
                        listOf(
                            "El Sitio Web pone a disposición de los usuarios dispositivos técnicos de enlace " +
                                "(tales como, entre otros, links, banners, botones), directorios y herramientas " +
                                "de búsqueda que permiten a los usuarios acceder a sitios web pertenecientes y/o " +
                                "gestionados por terceros.",
                            "Elche CF no ofrece ni comercializa por sí ni por medio de tercero la información, " +
                                "contenidos y servicios disponibles en esos sitios enlazados, ni los controla " +
                                "previamente, aprueba, recomienda, vigila ni hace propios. El usuario, por tanto, " +
                                "debe extremar la prudencia en la valoración y utilización de la información, " +
                                "contenidos y servicios existentes en los sitios enlazados.",
                        ),
                ),
                LegalSection(
                    heading = "Propiedad intelectual e industrial",
                    paragraphs =
                        listOf(
                            "El Sitio Web y los diferentes elementos que lo integran, tales como bases de datos, " +
                                "aplicaciones informáticas, signos distintivos, logotipos, fotografías, " +
                                "fragmentos de obras audiovisuales, diseños gráficos o cualesquiera otros, están " +
                                "sujetos a derechos de propiedad intelectual e industrial de los que Elche CF es " +
                                "titular exclusivo o cesionario con el alcance requerido.",
                            "Quedan expresamente prohibidos al usuario los actos de reproducción, distribución, " +
                                "transformación, comunicación pública, puesta a disposición, extracción, " +
                                "reutilización, reenvío o la explotación por cualquier medio o procedimiento del " +
                                "Sitio Web o de sus elementos integrantes, salvo en los casos en que esté " +
                                "legalmente permitido o medie autorización expresa y por escrito de Elche CF.",
                            "El usuario podrá visualizar y obtener una copia privada temporal de los contenidos " +
                                "disponibles a través del Sitio Web para su exclusivo uso personal y privado en " +
                                "sus sistemas informáticos, siempre que no sea con la finalidad de desarrollar " +
                                "actividades de carácter comercial o profesional.",
                        ),
                ),
                LegalSection(
                    heading = "Uso de cookies",
                    paragraphs =
                        listOf(
                            "El sitio utiliza cookies técnicas temporales para su correcto funcionamiento. Las " +
                                "cookies utilizadas tienen, en todo caso, carácter temporal, con la única " +
                                "finalidad de hacer más eficaz la navegación. También emplea cookies analíticas " +
                                "de terceros. El usuario puede configurar su navegador para rechazarlas.",
                        ),
                ),
                LegalSection(
                    heading = "Ley aplicable y jurisdicción",
                    paragraphs =
                        listOf(
                            "Será de aplicación la legislación española para las controversias relacionadas con " +
                                "el Sitio Web. Los juzgados competentes serán los del domicilio del usuario o el " +
                                "lugar de cumplimiento de la obligación.",
                        ),
                ),
            ),
        sourceNote = "Contenido de elchecf.es/nota-legal y elchecf.es/lopd — verificado agosto 2026.",
    )
