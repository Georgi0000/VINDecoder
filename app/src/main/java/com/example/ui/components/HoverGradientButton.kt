package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ButtonNormal
import com.example.ui.theme.HighlightPrimary

@Composable
fun HoverGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    // Track official hover and press events
    val isHoveredByInteraction by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Also track raw hover pointer input for the streaming emulator and browser mouse movements
    var isPointerOver by remember { mutableStateOf(false) }
    
    val activeState = isHoveredByInteraction || isPressed || isPointerOver
    
    // Animate transition for the high-saturation flowing blue gradient
    val hoverProgress by animateFloatAsState(
        targetValue = if (activeState) 1f else 0f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "button_hover_progress"
    )

    // Layout representation: Rectangle Bubble
    Box(
        modifier = modifier
            .testTag("check_intervals_button")
            .size(width = 320.dp, height = 58.dp)
            .clip(RoundedCornerShape(8.dp))
            .hoverable(interactionSource)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            PointerEventType.Enter -> isPointerOver = true
                            PointerEventType.Exit -> isPointerOver = false
                        }
                    }
                }
            }
            // Draw custom overlay gradient flowing from Top-Left to Bottom-Right
            .drawWithContent {
                // 1. Draw solid original normal background (RGB 20, 30, 39)
                drawRect(color = ButtonNormal)
                
                // 2. Overlap the flowing hover gradient (RGB 10, 83, 146) starting at top-left
                if (hoverProgress > 0f) {
                    val gradientBrush = Brush.linearGradient(
                        colors = listOf(
                            HighlightPrimary.copy(alpha = hoverProgress),
                            HighlightPrimary.copy(alpha = hoverProgress * 0.45f),
                            Color.Transparent
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, size.height)
                    )
                    drawRect(brush = gradientBrush)
                }

                drawContent()
            }
            // Standard clickable handler
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .wrapContentSize(Alignment.Center),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = text.uppercase(),
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
        }
    }
}
