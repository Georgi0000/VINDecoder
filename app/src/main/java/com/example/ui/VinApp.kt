package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.IntervalType
import com.example.model.ManufacturerInfo
import com.example.model.ServiceInterval
import com.example.ui.components.HoverGradientButton
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.VinCheckState
import com.example.viewmodel.VinViewModel
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun VinApp(viewModel: VinViewModel) {
    //delegating each view model and collecting
    //the properties defined as observable state
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val vinInput by viewModel.vinInput.collectAsStateWithLifecycle()
    val checkState by viewModel.checkState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DeepBlueDark
    ) { innerPadding ->
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                (fadeIn(animationSpec = tween(350)) + slideInVertically(
                    initialOffsetY = { 60 },
                    animationSpec = tween(350, easing = EaseOutQuad)
                )) togetherWith
                        fadeOut(animationSpec = tween(250))
            },
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                AppScreen.HOME -> {
                    HomeScreen(
                        onNavigateToCheck = { viewModel.navigateTo(AppScreen.VIN_CHECK) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                AppScreen.VIN_CHECK -> {
                    VinCheckScreen(
                        vinInput = vinInput,
                        checkState = checkState,
                        onVinChange = { viewModel.updateVin(it) },
                        onCheckIntervals = { viewModel.checkServiceIntervals() },
                        onPresetClick = { prefix -> viewModel.prefillVin(prefix) },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) },
                        modifier = Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    onNavigateToCheck: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top branding title block - aligned with Artistic-Flair luxury minimalist tracking
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(
                text = "OCTANE DIAGNOSTICS",
                color = BrightHighlight,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "V I N  S e r v i c e  C h e c k e r",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.5).sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Artistic calibration engine",
                color = TextSecondary.copy(alpha = 0.5f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 1.sp
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "wireframe_pulse")
            val rotatingAngle by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(12000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "rotation"
            )
            val breathingScale by infiniteTransition.animateFloat(
                initialValue = 0.95f,
                targetValue = 1.05f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2500, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "breathing"
            )

            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .testTag("visual_wireframe_canvas")
            ) {
                // Outer ring
                drawCircle(
                    color = AccentSuccess.copy(alpha = 0.12f),
                    radius = size.minDimension / 2 * breathingScale,
                    style = Stroke(width = 2f)
                )

                // Sub-outer ring
                drawCircle(
                    color = BrightHighlight.copy(alpha = 0.3f * breathingScale),
                    radius = size.minDimension / 2.3f,
                    style = Stroke(width = 3f)
                )

                // Inner core ring
                drawCircle(
                    color = HighlightPrimary.copy(alpha = 0.4f),
                    radius = size.minDimension / 4f,
                    style = Stroke(width = 2f)
                )

                // Moving crosshairs centered on rotation
                val radius = size.minDimension / 2.3f
                val radAngle = Math.toRadians(rotatingAngle.toDouble())
                val endX = center.x + radius * cos(radAngle).toFloat()
                val endY = center.y + radius * sin(radAngle).toFloat()
                val startX = center.x - radius * cos(radAngle).toFloat()
                val startY = center.y - radius * sin(radAngle).toFloat()

                drawLine(
                    color = BrightHighlight.copy(alpha = 0.2f),
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = 2f
                )

                drawLine(
                    color = BrightHighlight.copy(alpha = 0.2f),
                    start = Offset(
                        center.x + radius * cos(radAngle + Math.PI / 2).toFloat(),
                        center.y + radius * sin(radAngle + Math.PI / 2).toFloat()
                    ),
                    end = Offset(
                        center.x - radius * cos(radAngle + Math.PI / 2).toFloat(),
                        center.y - radius * sin(radAngle + Math.PI / 2).toFloat()
                    ),
                    strokeWidth = 3.6f
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.offset(y = 145.dp)
            ) {
                Text(
                    text = "A c c e s s   p r e s e t   i n t e r v a l s",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "BY DECODING MANUFACTURER WMI",
                    color = BrightHighlight,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }
        }

        // Bottom section: "CHECK SERVICE INTERVALS" Rectangle Bubble Gradient Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HoverGradientButton(
                text = "Check Service Intervals",
                onClick = onNavigateToCheck,
                modifier = Modifier.fillMaxWidth(0.95f)
            )
        }
    }
}

@Composable
fun VinCheckScreen(
    vinInput: String,
    checkState: VinCheckState,
    onVinChange: (String) -> Unit,
    onCheckIntervals: () -> Unit,
    onPresetClick: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        // Navigation Bar/Header with back arrow
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .testTag("back_to_home_button")
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(ButtonNormal)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to main menu",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "OCTANE PLATFORM",
                    color = HighlightPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "WMI Decoder",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }

        // Search Input at center of top layout
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = ButtonNormal),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0x0DFFFFFF)) // 5% glass white border
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "VEHICLE IDENTIFICATION NUMBER (VIN)",
                    color = TextSecondary.copy(alpha = 0.5f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
                
                Spacer(modifier = Modifier.height(10.dp))
                
                OutlinedTextField(
                    value = vinInput,
                    onValueChange = onVinChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("vin_input_field"),
                    placeholder = {
                        Text(
                            text = "Enter first 3 characters or full VIN...",
                            color = TextSecondary.copy(alpha = 0.4f),
                            fontSize = 14.sp
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = DeepBlueDark,
                        unfocusedContainerColor = DeepBlueDark,
                        focusedBorderColor = HighlightPrimary,
                        unfocusedBorderColor = Color(0x0DFFFFFF)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onCheckIntervals()
                            focusManager.clearFocus()
                        }
                    ),
                    trailingIcon = {
                        if (vinInput.isNotEmpty()) {
                            IconButton(onClick = { onVinChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear input",
                                    tint = TextSecondary
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Action Button to decode
                Button(
                    onClick = {
                        onCheckIntervals()
                        focusManager.clearFocus()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HighlightPrimary,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("decode_vin_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DECODE SERVICE INTERVALS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                }
            }
        }

        // Preset testers - helpful to verify manufacturers
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(
                text = "TAP TO TEST TYPICAL MANUFACTURERS:",
                color = TextSecondary.copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val presets = listOf(
                    "WBA" to "BMW",
                    "WDB" to "M-Benz",
                    "5YJ" to "Tesla",
                    "JTD" to "Toyota",
                    "WAU" to "Audi"
                )
                presets.forEach { (prefix, name) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ButtonNormal)
                            .clickable {
                                onPresetClick(prefix)
                                focusManager.clearFocus()
                            }
                            .border(1.dp, Color(0x0DFFFFFF), RoundedCornerShape(12.dp))
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = prefix,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = name,
                                color = BrightHighlight,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // State Results / Stylistic waiting circle
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (checkState) {
                is VinCheckState.Idle -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            tint = DividerColor,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Awaiting VIN input above",
                            color = TextSecondary,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Enter first 3 characters (e.g., Mercedes space 'WDB' or click mock presets) to see thresholds.",
                            color = TextSecondary.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                is VinCheckState.Loading -> {
                    // Stylistic Waiting Circle
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = BrightHighlight,
                            strokeWidth = 3.dp,
                            modifier = Modifier
                                .size(44.dp)
                                .testTag("vin_checking_indicator")
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Analyzing WMI Signature...",
                            color = BrightHighlight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.sp
                        )
                    }
                }
                is VinCheckState.Success -> {
                    IntervalResultList(
                        manufacturer = checkState.info,
                        typedVin = checkState.vin
                    )
                }
                is VinCheckState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = AccentWarning,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Validation Alert",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = checkState.message,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IntervalResultList(
    manufacturer: ManufacturerInfo,
    typedVin: String
) {
    //lazily loading every interval card, in order to keep the composable light
    //and show only what we need on the screen
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("result_intervals_list"),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Detected Manufacturer Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("manufacturer_header_card"),
                colors = CardDefaults.cardColors(containerColor = SlateBlueSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0x19FFFFFF)) // Slightly brighter glass border for header
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Accent Symbol Circle
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(ButtonNormal)
                            .border(1.5.dp, BrightHighlight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = manufacturer.logoAccentChar,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "MANUFACTURER",
                            color = BrightHighlight,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = manufacturer.name.uppercase(),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light,
                            letterSpacing = (-0.3).sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "VIN WMI: ${typedVin.take(3).uppercase()} • FULL: $typedVin",
                            color = TextSecondary.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Done/Status Tag
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentSuccess.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "VERIFIED",
                            color = AccentSuccess,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }

        // List Header label
        item {
            Text(
                text = "FACTORY SERVICE INTERVAL THRESHOLDS",
                color = TextSecondary.copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 4.dp, start = 4.dp)
            )
        }

        // Part Cards
        items(manufacturer.intervals) { interval ->
            IntervalCard(interval)
        }
    }
}

@Composable
fun IntervalCard(interval: ServiceInterval) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("interval_item_card_${interval.partName.replace(" ", "_").lowercase()}"),
        colors = CardDefaults.cardColors(containerColor = ButtonNormal),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0x0DFFFFFF)) // 5% glass white border
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Dynamic colorful low-opacity indicator boxes matching respective parts
            val (iconBg, iconTint) = when (interval.type) {
                IntervalType.OIL_CHANGE -> Pair(HighlightPrimary.copy(alpha = 0.2f), HighlightPrimary)
                IntervalType.BRAKES_FRONT, IntervalType.BRAKES_REAR -> Pair(Color(0xFFF97316).copy(alpha = 0.15f), Color(0xFFF97316))
                IntervalType.CABIN_FILTER, IntervalType.ENGINE_FILTER -> Pair(Color(0xFFA855F7).copy(alpha = 0.15f), Color(0xFFA855F7))
                IntervalType.SPARK_PLUGS -> Pair(Color(0xFFEC407A).copy(alpha = 0.15f), Color(0xFFEC407A))
                IntervalType.TRANSMISSION_FLUID -> Pair(Color(0xFF26A69A).copy(alpha = 0.15f), Color(0xFF26A69A))
                else -> Pair(BrightHighlight.copy(alpha = 0.15f), BrightHighlight)
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                val iconVector = when (interval.type) {
                    IntervalType.OIL_CHANGE -> Icons.Default.Refresh
                    IntervalType.BRAKES_FRONT -> Icons.Default.Info
                    IntervalType.BRAKES_REAR -> Icons.Default.Info
                    IntervalType.CABIN_FILTER -> Icons.Default.Check
                    IntervalType.ENGINE_FILTER -> Icons.Default.Check
                    IntervalType.SPARK_PLUGS -> Icons.Default.Settings
                    IntervalType.TRANSMISSION_FLUID -> Icons.Default.Build
                    IntervalType.COOLANT_FLUSH -> Icons.Default.Check
                    IntervalType.BRAKE_FLUID -> Icons.Default.CheckCircle
                }
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = interval.partName.uppercase(),
                    color = TextSecondary.copy(alpha = 0.5f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = formatIntervalKm(interval.intervalKm),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Interval",
                        color = TextSecondary.copy(alpha = 0.4f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = interval.detail,
                    color = TextSecondary.copy(alpha = 0.75f),
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

private fun formatIntervalKm(km: Int): String {
    if (km >= 1000) {
        // format with thousand separator: 15000 -> "15 000 km"
        val formatted = String.format("%,d", km).replace(",", " ")
        return "$formatted km"
    }
    return "$km km"
}
