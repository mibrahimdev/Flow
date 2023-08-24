package io.github.mohamedisoliman.flow.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.mohamedisoliman.flow.R

@Composable
fun CardSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.primarySurface,
        shape = shape,
        elevation = 0.dp
    ) {
        content()
    }
}


@Composable
fun TagView(
    modifier: Modifier = Modifier,
    tagName: String,
    tagColor: Color = MaterialTheme.colors.surface,
    tagStyle: TagStyle = TagStyle.Background,
) {

    val border =
        if (tagStyle == TagStyle.Bordered)
            BorderStroke(width = 1.dp, color = tagColor)
        else
            null

    val textColor = MaterialTheme.colors.contentColorFor(tagColor)

    Surface(modifier = modifier,
        color = if (tagStyle == TagStyle.Background) tagColor.copy(alpha = 0.2f) else
            MaterialTheme.colors.surface,
        border = border,
        shape = RoundedCornerShape(8.dp)) {

        Text(modifier = Modifier.padding(8.dp),
            text = tagName,
            style = MaterialTheme.typography.caption.copy(color = textColor, fontSize = 12.sp)
        )
    }
}

sealed class TagStyle {
    object Bordered : TagStyle()
    object Background : TagStyle()
}


@Composable
fun ProjectView(
    modifier: Modifier = Modifier,
    projectName: String = "",
    projectTint: Color = Color.Transparent,
) {
    Row(modifier = Modifier
        .padding(top = 24.dp)
        .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        Icon(
            modifier = Modifier
                .padding(end = 12.dp)
                .size(12.dp),
            imageVector = ImageVector.vectorResource(R.drawable.eclipse),
            contentDescription = "",
            tint = projectTint
        )
        Text(text = projectName)
    }
}