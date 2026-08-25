package es.elchecf.app.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

private val TRACK_HEIGHT = 44.dp
private val TRACK_PADDING = 4.dp
private const val ANIMATION_MS = 220

/**
 * Control segmentado en píldora con una franja verde que se desliza al cambiar de pestaña
 * (sustituye a `TabRow`, deprecado en Material3 a favor de `PrimaryTabRow`/`SecondaryTabRow` —
 * este componente propio se aparta de ambos: sigue la identidad de marca en vez del genérico
 * de Material, ver CLAUDE.md §12).
 */
@Composable
fun <T> ElcheSegmentedTabRow(
    tabs: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    label: (T) -> String,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier =
            modifier
                .fillMaxWidth()
                .height(TRACK_HEIGHT)
                .clip(ElcheShape.Pill)
                .background(ElcheColor.GreenSoft)
                .padding(TRACK_PADDING),
    ) {
        val segmentWidth = maxWidth / tabs.size
        val selectedIndex = tabs.indexOf(selected).coerceAtLeast(0)
        val offset by
            animateDpAsState(
                targetValue = segmentWidth * selectedIndex,
                animationSpec = tween(durationMillis = ANIMATION_MS, easing = FastOutSlowInEasing),
                label = "elcheSegmentedTabOffset",
            )

        Box(
            modifier =
                Modifier
                    .offset(x = offset)
                    .width(segmentWidth)
                    .fillMaxHeight()
                    .clip(ElcheShape.Pill)
                    .background(ElcheColor.Green),
        )

        Row(modifier = Modifier.fillMaxSize()) {
            tabs.forEach { tab ->
                val isSelected = tab == selected
                val textColor by
                    animateColorAsState(
                        targetValue = if (isSelected) ElcheColor.White else ElcheColor.InkMuted,
                        animationSpec = tween(durationMillis = ANIMATION_MS),
                        label = "elcheSegmentedTabTextColor",
                    )
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(ElcheShape.Pill)
                            .clickable(onClick = { onSelect(tab) }),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label(tab),
                        style = ElcheTheme.typography.label,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = ElcheSpacing.xs),
                    )
                }
            }
        }
    }
}

private enum class PreviewTab { Calendario, Clasificaciones, Jugadores }

@Preview
@Composable
private fun ElcheSegmentedTabRowPreview() {
    ElcheTheme {
        ElcheSegmentedTabRow(
            tabs = PreviewTab.entries,
            selected = PreviewTab.Clasificaciones,
            onSelect = {},
            label = { it.name },
            modifier = Modifier.padding(ElcheSpacing.lg),
        )
    }
}
