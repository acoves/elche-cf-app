package es.elchecf.app.data.mock

import es.elchecf.app.domain.model.Team

// FASE 5: nombres de clubes reales usados como datos de ejemplo de texto plano (sin escudos ni
// tipografía oficial — CLAUDE.md §10). Se sustituye por datos reales en Fase 8.
internal object DemoTeams {
    val elche = Team(Team.ELCHE_ID, "Elche CF", "ELCHE", "", "#05642C")
    val realMadrid = Team("real-madrid", "Real Madrid", "R. MADRID", "", "#1B458F")
    val barcelona = Team("barcelona", "FC Barcelona", "BARÇA", "", "#A50044")
    val atleticoMadrid = Team("atletico-madrid", "Atlético de Madrid", "ATLÉTICO", "", "#CB3524")
    val athleticClub = Team("athletic-club", "Athletic Club", "ATHLETIC", "", "#EE2523")
    val realSociedad = Team("real-sociedad", "Real Sociedad", "R. SOCIEDAD", "", "#0067B1")
    val villarreal = Team("villarreal", "Villarreal CF", "VILLARREAL", "", "#FFE667")
    val realBetis = Team("real-betis", "Real Betis", "BETIS", "", "#00954C")
    val sevilla = Team("sevilla", "Sevilla FC", "SEVILLA", "", "#D0021B")
    val valencia = Team("valencia", "Valencia CF", "VALENCIA", "", "#EE7203")
    val rayoVallecano = Team("rayo-vallecano", "Rayo Vallecano", "RAYO", "", "#E0141E")
    val celtaVigo = Team("celta-vigo", "Celta de Vigo", "CELTA", "", "#8AC3EE")
    val gironaFc = Team("girona-fc", "Girona FC", "GIRONA", "", "#CB1D2E")
    val osasuna = Team("osasuna", "CA Osasuna", "OSASUNA", "", "#D2151F")
    val getafe = Team("getafe", "Getafe CF", "GETAFE", "", "#005CA9")
    val alaves = Team("alaves", "Deportivo Alavés", "ALAVÉS", "", "#1F4C9C")
    val mallorca = Team("mallorca", "RCD Mallorca", "MALLORCA", "", "#E20613")
    val lasPalmas = Team("las-palmas", "UD Las Palmas", "LAS PALMAS", "", "#FFE100")
    val espanyol = Team("espanyol", "RCD Espanyol", "ESPANYOL", "", "#0A4C96")
    val leganes = Team("leganes", "CD Leganés", "LEGANÉS", "", "#1B4A97")

    val all =
        listOf(
            elche,
            realMadrid,
            barcelona,
            atleticoMadrid,
            athleticClub,
            realSociedad,
            villarreal,
            realBetis,
            sevilla,
            valencia,
            rayoVallecano,
            celtaVigo,
            gironaFc,
            osasuna,
            getafe,
            alaves,
            mallorca,
            lasPalmas,
            espanyol,
            leganes,
        )
}
