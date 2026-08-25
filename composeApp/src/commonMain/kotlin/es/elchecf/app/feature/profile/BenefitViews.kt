package es.elchecf.app.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheButtonVariant
import es.elchecf.app.designsystem.component.ElcheCard
import es.elchecf.app.designsystem.component.ElcheSheetHeader
import es.elchecf.app.designsystem.icon.ElcheProfileIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.Benefit

@Composable
internal fun BenefitRow(
    benefit: Benefit,
    onMoreClick: () -> Unit,
) {
    ElcheCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = benefit.imageUrl,
                contentDescription = benefit.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(56.dp).clip(ElcheShape.Card).background(ElcheColor.GreenSoft),
            )
            Column(modifier = Modifier.padding(start = ElcheSpacing.md).weight(1f)) {
                Text(
                    text = benefit.title.uppercase(),
                    style = ElcheTheme.typography.bodyS.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    text = benefit.subtitle,
                    style = ElcheTheme.typography.bodyS,
                    color = ElcheColor.InkMuted,
                )
            }
            IconButton(onClick = onMoreClick) {
                Icon(imageVector = ElcheProfileIcon.More, contentDescription = "Más opciones")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BenefitDetailSheet(
    benefit: Benefit,
    onDismiss: () -> Unit,
    onCta: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(modifier = Modifier.fillMaxWidth().padding(ElcheSpacing.lg)) {
            ElcheSheetHeader(title = benefit.title, onClose = onDismiss)
            Text(
                text = benefit.detail,
                style = ElcheTheme.typography.body,
                color = ElcheColor.InkMuted,
                modifier = Modifier.padding(top = ElcheSpacing.lg, bottom = ElcheSpacing.xl),
            )
            ElcheButton(
                text = "Consigue el Carnet Franjiverde",
                onClick = onCta,
                variant = ElcheButtonVariant.Accent,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
