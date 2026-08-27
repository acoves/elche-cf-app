package es.elchecf.app.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import elcheapp.composeapp.generated.resources.Res
import elcheapp.composeapp.generated.resources.press_conference_banner
import es.elchecf.app.core.webview.openUrlExternally
import es.elchecf.app.designsystem.component.ElcheLivePulse
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import org.jetbrains.compose.resources.painterResource

private const val PRESS_CONFERENCE_YOUTUBE_URL = "https://www.youtube.com/live/FP29Yw4ouOI?si=Tp-0TNfFhwT1dJgg"

/**
 * Banner de rueda de prensa en directo, encima de "Noticias" y "Game Zone" en Para ti: foto real
 * (`composeResources/drawable/press_conference_banner.jpg`, gráfica oficial "La previa" del
 * club) con la insignia "EN DIRECTO" ([ElcheLivePulse]) arriba a la derecha. Al pulsar abre el
 * directo de YouTube en el navegador — no es una noticia como las de [es.elchecf.app.feature.
 * home.news.NewsSection], por eso va aparte y arriba del todo.
 */
@Composable
fun PressConferenceBanner(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f)
                .clip(ElcheShape.CardLarge)
                .clickable(onClick = { openUrlExternally(PRESS_CONFERENCE_YOUTUBE_URL) }),
    ) {
        Image(
            painter = painterResource(Res.drawable.press_conference_banner),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        ElcheLivePulse(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(ElcheSpacing.md)
                    .background(ElcheColor.White.copy(alpha = 0.92f), ElcheShape.Pill)
                    .padding(horizontal = ElcheSpacing.sm, vertical = ElcheSpacing.xs),
        )
    }
}
