package io.github.mohamedisoliman.flow.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        elevation = 4.dp
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