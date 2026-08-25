package es.elchecf.app.feature.profile.legal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.feature.profile.ProfileSubScreenHeader

@Composable
fun LegalScreen(
    page: LegalPage,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        ProfileSubScreenHeader(
            title = page.title,
            onBack = onBack,
            modifier = Modifier.padding(horizontal = ElcheSpacing.lg),
        )
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = ElcheSpacing.screenMargin),
        ) {
            Text(text = page.title.uppercase(), style = ElcheTheme.typography.displayM)
            Text(
                text = page.subtitle,
                style = ElcheTheme.typography.body,
                color = ElcheColor.InkMuted,
                modifier = Modifier.padding(top = ElcheSpacing.xs, bottom = ElcheSpacing.xl),
            )
            page.sections.forEach { section -> LegalSectionView(section) }
            Text(
                text = page.sourceNote,
                style = ElcheTheme.typography.label,
                color = ElcheColor.InkMuted,
                modifier = Modifier.padding(top = ElcheSpacing.lg, bottom = ElcheSpacing.xxxl),
            )
        }
    }
}

@Composable
private fun LegalSectionView(section: LegalSection) {
    Column(modifier = Modifier.padding(bottom = ElcheSpacing.xl)) {
        Text(text = section.heading, style = ElcheTheme.typography.titleM)
        section.paragraphs.forEach { paragraph ->
            Text(
                text = paragraph,
                style = ElcheTheme.typography.body,
                modifier = Modifier.padding(top = ElcheSpacing.sm),
            )
        }
        if (section.bullets.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(top = ElcheSpacing.sm),
                verticalArrangement = Arrangement.spacedBy(ElcheSpacing.sm),
            ) {
                section.bullets.forEach { bullet ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "•",
                            style = ElcheTheme.typography.body,
                            modifier = Modifier.padding(end = ElcheSpacing.sm),
                        )
                        Text(text = bullet, style = ElcheTheme.typography.body, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
