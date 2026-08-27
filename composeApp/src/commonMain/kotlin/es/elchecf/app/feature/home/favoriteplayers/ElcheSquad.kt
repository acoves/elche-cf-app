package es.elchecf.app.feature.home.favoriteplayers

enum class PlayerPosition(
    val label: String,
) {
    Goalkeeper("Porteros"),
    Defender("Defensas"),
    Midfielder("Centrocampistas"),
    Forward("Delanteros"),
}

data class SquadPlayer(
    val number: Int,
    val name: String,
    val position: PlayerPosition,
)

/**
 * Primer equipo del Elche CF, temporada 2026/27 (LaLiga): dorsales y nombres tomados de las
 * fichas públicas del club/LaLiga en el momento de escribir esto. Sin foto real de cada
 * jugador — no hay licencia para redistribuir las fotos oficiales, así que [FavoritePlayerAvatar]
 * usa siempre un icono genérico para todos, tal y como pidió el usuario.
 */
val elcheSquad2627: List<SquadPlayer> =
    listOf(
        SquadPlayer(1, "Matías Dituro", PlayerPosition.Goalkeeper),
        SquadPlayer(43, "Alejandro Iturbe", PlayerPosition.Goalkeeper),
        SquadPlayer(2, "Buba Sangaré", PlayerPosition.Defender),
        SquadPlayer(4, "Bambo Diaby", PlayerPosition.Defender),
        SquadPlayer(6, "Pedro Bigas", PlayerPosition.Defender),
        SquadPlayer(18, "John Donald", PlayerPosition.Defender),
        SquadPlayer(22, "David Affengruber", PlayerPosition.Defender),
        SquadPlayer(23, "Víctor Chust", PlayerPosition.Defender),
        SquadPlayer(26, "Matia Barzic", PlayerPosition.Defender),
        SquadPlayer(5, "Federico Redondo", PlayerPosition.Midfielder),
        SquadPlayer(8, "Marc Aguado", PlayerPosition.Midfielder),
        SquadPlayer(10, "Facundo Buonanotte", PlayerPosition.Midfielder),
        SquadPlayer(12, "Gonzalo Villar", PlayerPosition.Midfielder),
        SquadPlayer(16, "Martim Neto", PlayerPosition.Midfielder),
        SquadPlayer(17, "Josan", PlayerPosition.Midfielder),
        SquadPlayer(47, "Javi Morcillo", PlayerPosition.Midfielder),
        SquadPlayer(7, "Yago", PlayerPosition.Forward),
        SquadPlayer(9, "Ezequiel Ponce", PlayerPosition.Forward),
        SquadPlayer(11, "Germán Valera", PlayerPosition.Forward),
        SquadPlayer(14, "Fer Niño", PlayerPosition.Forward),
        SquadPlayer(19, "Grady Diangana", PlayerPosition.Forward),
        SquadPlayer(20, "Tete Morente", PlayerPosition.Forward),
        SquadPlayer(21, "Lucas Cepeda", PlayerPosition.Forward),
        SquadPlayer(29, "Ali Houary", PlayerPosition.Forward),
        SquadPlayer(32, "Adam Boayar", PlayerPosition.Forward),
    )
