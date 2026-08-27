package es.elchecf.app.feature.home.news

data class ClubNews(
    val category: String,
    val title: String,
    val url: String,
)

private const val NEWS_BASE = "https://www.elchecf.es/noticias/"

/** Enlace real a la portada de noticias del club — usado por el "Ver todas" de [NewsSection]. */
const val ELCHE_NEWS_URL = "https://www.elchecf.es/noticias"

/**
 * Selección de 5 noticias reales del Elche CF (elchecf.es, tomadas en el momento de escribir
 * esto), variadas a propósito: fichaje/renovación, rueda de prensa, declaraciones, cesión y
 * campaña de club. Cada tarjeta abre el navegador con la noticia real — no hay pantalla de
 * detalle de noticia en la app.
 */
val elcheClubNews: List<ClubNews> =
    listOf(
        ClubNews(
            category = "Fichajes",
            title = "El Elche CF ejecuta la opción de compra de Buba Sangaré y firma hasta 2031",
            url = NEWS_BASE + "el-elche-cf-ejecuta-la-opcion-de-compra-de-buba-sangare-y-firma-hasta-2031",
        ),
        ClubNews(
            category = "Rueda de prensa",
            title = "Aleix Febas comunica en rueda de prensa su decisión de no continuar en el Elche CF",
            url = NEWS_BASE + "aleix-febas-comunica-en-rueda-de-prensa-su-decision-de-no-continuar-en-el-elche-cf",
        ),
        ClubNews(
            category = "Declaraciones",
            title = "Martín Anselmi: \"Estoy en el lugar en el que quiero estar\"",
            url = NEWS_BASE + "martin-anselmi-estoy-en-el-lugar-en-el-que-quiero-estar",
        ),
        ClubNews(
            category = "Cesiones",
            title = "Tiziano Perrotta jugará cedido en Defensa y Justicia durante la temporada 2026/27",
            url = NEWS_BASE + "tiziano-perrotta-jugara-cedido-en-defensa-y-justicia-durante-la-temporada-202627",
        ),
        ClubNews(
            category = "Club",
            title = "El Elche CF lanza la campaña de abonos 2026/27 bajo el lema 'Siempre a lo Elche'",
            url = NEWS_BASE + "el-elche-cf-lanza-la-campana-de-abonos-202627-bajo-el-lema-siempre-a-lo-elche",
        ),
    )
