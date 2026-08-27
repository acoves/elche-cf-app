package es.elchecf.app.feature.home.news

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import es.elchecf.app.core.webview.openUrlExternally
import es.elchecf.app.designsystem.icon.ElcheIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

/**
 * "Noticias" en Para ti: una noticia grande + 4 pequeñas, estilo de la referencia (sección
 * "United Women" del Chelsea/Man Utd) en verde Elche. Cada tarjeta abre el navegador con la
 * noticia real de elchecf.es — sin pantalla de detalle propia. Sin foto real de cada noticia (no
 * hay licencia para redistribuirlas): fondo degradado verde con el escudo como marca de agua,
 * mismo criterio que los avatares genéricos de jugadores favoritos.
 */
@Composable
fun NewsSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Noticias", style = ElcheTheme.typography.titleL, modifier = Modifier.weight(1f))
            Text(
                text = "Ver todas",
                style = ElcheTheme.typography.bodyS,
                color = ElcheColor.Green,
                modifier = Modifier.clickable(onClick = { openUrlExternally(ELCHE_NEWS_URL) }),
            )
        }
        val big = elcheClubNews.first()
        BigNewsCard(news = big, modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg))
        Column(modifier = Modifier.padding(top = ElcheSpacing.md)) {
            elcheClubNews.drop(1).forEach { news ->
                SmallNewsRow(news = news, modifier = Modifier.padding(bottom = ElcheSpacing.md))
            }
        }
    }
}

@Composable
private fun BigNewsCard(
    news: ClubNews,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.clickable(onClick = { openUrlExternally(news.url) })) {
        NewsThumbnail(modifier = Modifier.fillMaxWidth().aspectRatio(16f / 10f).clip(ElcheShape.CardLarge))
        Text(
            text = news.category.uppercase(),
            style = ElcheTheme.typography.label,
            color = ElcheColor.Green,
            modifier = Modifier.padding(top = ElcheSpacing.sm),
        )
        Text(
            text = news.title,
            style = ElcheTheme.typography.titleM,
            modifier = Modifier.padding(top = ElcheSpacing.xs),
        )
    }
}

@Composable
private fun SmallNewsRow(
    news: ClubNews,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().clickable(onClick = { openUrlExternally(news.url) }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NewsThumbnail(modifier = Modifier.size(72.dp).clip(ElcheShape.Card))
        Column(modifier = Modifier.weight(1f).padding(start = ElcheSpacing.md)) {
            Text(text = news.category.uppercase(), style = ElcheTheme.typography.label, color = ElcheColor.Green)
            Text(
                text = news.title,
                style = ElcheTheme.typography.body,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = ElcheSpacing.xs),
            )
        }
    }
}

@Composable
private fun NewsThumbnail(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier.background(Brush.linearGradient(listOf(ElcheColor.Green, ElcheColor.GreenDeep))),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = ElcheIcon.ForYou,
            contentDescription = null,
            tint = ElcheColor.White.copy(alpha = 0.25f),
            modifier = Modifier.fillMaxSize(0.5f),
        )
    }
}
