package es.elchecf.app.feature.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import elcheapp.composeapp.generated.resources.Res
import elcheapp.composeapp.generated.resources.membership_hero
import es.elchecf.app.core.webview.openUrlExternally
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheButtonVariant
import es.elchecf.app.designsystem.icon.ElcheShopIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import org.jetbrains.compose.resources.painterResource

/** Precio real de temporada 2026/2027, mismo dato ya usado en los beneficios de Perfil
 * (ver `data/mock/MockProfileDataSource.kt`). */
private const val MEMBERSHIP_PRICE = "49,99 €"

/** CLAUDE.md §5.4 / conversación: el CTA lleva a la web real de abonados, nunca a un WebView
 * dentro de la app — el usuario lo pidió explícitamente para que el alta pase por el flujo
 * oficial del club, no por una copia dentro de la app. */
private const val MEMBERSHIP_SIGNUP_URL = "https://abonados.elchecf.es/hazte-franjiverde/layout/datos-personales"

private val CARD_BACKGROUND = Color(0xFF10140F)
private val ON_DARK_MUTED = Color(0xFFBFC6C0)

private val membershipChecklist =
    listOf(
        "Pack de bienvenida: bufanda, pulsera y carnet físico",
        "Acceso prioritario a entradas, 24h antes que el resto",
        "Entradas para partidos fuera de casa antes que nadie",
        "10% de descuento en la tienda oficial del Elche CF",
    )

/**
 * Tarjeta del Carnet Franjiverde en Tienda → Membership: foto real del usuario (recortada,
 * `composeResources/drawable/membership_hero.jpg`, no la imagen completa) con degradado para que
 * el texto blanco se lea bien encima, checklist con el icono "Verified" de Reicon en dorado, y
 * CTA que abre la web oficial de abonados fuera de la app (ver [MEMBERSHIP_SIGNUP_URL]).
 */
@Composable
fun MembershipContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(ElcheSpacing.screenMargin),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(ElcheShape.CardLarge)
                    .background(CARD_BACKGROUND),
        ) {
            Column {
                Box {
                    Image(
                        painter = painterResource(Res.drawable.membership_hero),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(320.dp),
                    )
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                                .background(
                                    Brush.verticalGradient(
                                        0f to Color.Transparent,
                                        0.55f to CARD_BACKGROUND.copy(alpha = 0.55f),
                                        1f to CARD_BACKGROUND,
                                    ),
                                ),
                    )
                    Column(modifier = Modifier.align(Alignment.BottomStart).padding(ElcheSpacing.lg)) {
                        Text(text = "CARNET", style = ElcheTheme.typography.titleM, color = ElcheColor.Gold)
                        Text(text = "FRANJIVERDE", style = ElcheTheme.typography.displayL, color = Color.White)
                    }
                }

                Column(modifier = Modifier.padding(ElcheSpacing.lg)) {
                    membershipChecklist.forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = ElcheSpacing.xs),
                        ) {
                            Icon(
                                imageVector = ElcheShopIcon.Verified,
                                contentDescription = null,
                                tint = ElcheColor.Gold,
                                modifier = Modifier.height(22.dp),
                            )
                            Text(
                                text = item,
                                style = ElcheTheme.typography.bodyS,
                                color = Color.White,
                                modifier = Modifier.padding(start = ElcheSpacing.sm),
                            )
                        }
                    }

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = ElcheSpacing.lg)
                                .clip(RoundedCornerShape(ElcheSpacing.sm))
                                .background(Color.White.copy(alpha = 0.08f))
                                .padding(ElcheSpacing.md),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(text = "Carnet Franjiverde", style = ElcheTheme.typography.body, color = Color.White)
                            Text(
                                text = "Cuota de socio, temporada 2026/2027",
                                style = ElcheTheme.typography.label,
                                color = ON_DARK_MUTED,
                            )
                        }
                        Text(text = MEMBERSHIP_PRICE, style = ElcheTheme.typography.titleL, color = ElcheColor.Gold)
                    }

                    ElcheButton(
                        text = "Hazte con el carnet",
                        onClick = { openUrlExternally(MEMBERSHIP_SIGNUP_URL) },
                        variant = ElcheButtonVariant.Accent,
                        modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
                    )
                    Text(
                        text = "Se abre la web oficial de abonados del Elche CF para completar el alta.",
                        style = ElcheTheme.typography.label,
                        color = ON_DARK_MUTED,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.sm),
                    )
                }
            }
        }
    }
}
