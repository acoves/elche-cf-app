package es.elchecf.app.feature.game.datigol

import kotlin.random.Random

data class Platform(
    val x: Float,
    val y: Float,
    val width: Float,
)

enum class DatigolPose { Idle, Jump, RunLeft, RunRight }

/**
 * Física y estado del minijuego "Datigol Jump" (Perfil → Extra), estilo Doodle Jump: el
 * personaje rebota solo al tocar una plataforma, el jugador solo controla el movimiento
 * horizontal. Clase mutable a propósito (bucle de juego por fotograma, no encaja con
 * `StateFlow`/`copy()` inmutable del resto de la app — ver [es.elchecf.app.feature.game.datigol.DatigolJumpScreen]
 * para cómo se fuerza el redibujado en Compose).
 */
class DatigolGameState(
    private val canvasWidth: Float,
    private val canvasHeight: Float,
) {
    var playerX: Float = canvasWidth / 2f - PLAYER_SIZE / 2f
        private set
    var playerY: Float = canvasHeight - 160f
        private set
    var velocityY: Float = JUMP_VELOCITY
        private set
    var cameraY: Float = 0f
        private set
    var gameOver: Boolean = false
        private set
    var score: Int = 0
        private set
    var pose: DatigolPose = DatigolPose.Idle
        private set

    val platforms: MutableList<Platform> = mutableListOf()
    private var highestPlatformY: Float = playerY

    init {
        platforms.add(Platform(playerX, playerY + PLAYER_SIZE * 0.6f, PLATFORM_WIDTH))
        highestPlatformY = playerY + PLAYER_SIZE * 0.6f
        generateUpTo(cameraY - canvasHeight)
    }

    private fun generateUpTo(targetTopY: Float) {
        while (highestPlatformY > targetTopY) {
            val gap = Random.nextFloat() * (MAX_GAP - MIN_GAP) + MIN_GAP
            highestPlatformY -= gap
            val x = Random.nextFloat() * (canvasWidth - PLATFORM_WIDTH)
            platforms.add(Platform(x, highestPlatformY, PLATFORM_WIDTH))
        }
    }

    fun update(
        deltaSeconds: Float,
        horizontalDirection: Int,
    ) {
        if (gameOver) return

        pose =
            when {
                horizontalDirection > 0 -> DatigolPose.RunRight
                horizontalDirection < 0 -> DatigolPose.RunLeft
                velocityY < JUMP_VELOCITY * 0.4f -> DatigolPose.Idle
                else -> DatigolPose.Jump
            }

        playerX += horizontalDirection * HORIZONTAL_SPEED * deltaSeconds
        if (playerX < -PLAYER_SIZE) playerX = canvasWidth
        if (playerX > canvasWidth) playerX = -PLAYER_SIZE

        velocityY += GRAVITY * deltaSeconds
        playerY += velocityY * deltaSeconds

        if (velocityY > 0f) {
            val feetY = playerY + PLAYER_SIZE
            for (platform in platforms) {
                val withinX = playerX + PLAYER_SIZE > platform.x && playerX < platform.x + platform.width
                val landing = feetY >= platform.y && feetY <= platform.y + PLATFORM_HEIGHT + velocityY * deltaSeconds
                if (withinX && landing) {
                    velocityY = JUMP_VELOCITY
                    playerY = platform.y - PLAYER_SIZE
                    break
                }
            }
        }

        cameraY = minOf(cameraY, playerY - canvasHeight * 0.4f)

        generateUpTo(cameraY - PLATFORM_MARGIN)
        platforms.removeAll { it.y > cameraY + canvasHeight + PLATFORM_MARGIN }

        score = maxOf(score, (-cameraY / SCORE_SCALE).toInt())

        if (playerY - cameraY > canvasHeight + PLAYER_SIZE * 3) {
            gameOver = true
        }
    }

    companion object {
        const val GRAVITY = 1500f
        const val JUMP_VELOCITY = -820f
        const val HORIZONTAL_SPEED = 300f
        const val PLAYER_SIZE = 72f
        const val PLATFORM_WIDTH = 100f
        const val PLATFORM_HEIGHT = 18f
        const val MIN_GAP = 90f
        const val MAX_GAP = 170f
        const val PLATFORM_MARGIN = 200f
        const val SCORE_SCALE = 10f
    }
}
