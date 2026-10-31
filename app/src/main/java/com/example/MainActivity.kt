package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AstralViewModel
import com.example.ui.NavigationScreen
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AstralApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AstralApp(viewModel: AstralViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    fun fetchDeviceLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    viewModel.setGpsLocation(location.latitude, location.longitude, "Local Device Horizon")
                }
            }
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            fetchDeviceLocation()
        }
    }

    fun requestGps() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchDeviceLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(CosmicBackground),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "ASTRALSYNC",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 2.sp
                                ),
                                color = SolarGold
                            )
                            Text(
                                text = "ASTRONOMICAL CHRONOMETER",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    letterSpacing = 1.sp
                                ),
                                color = TextSecondary
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { requestGps() },
                        modifier = Modifier.testTag("action_gps_sync")
                    ) {
                        Icon(
                            imageVector = if (state.isGpsActive) Icons.Default.GpsFixed else Icons.Default.GpsNotFixed,
                            contentDescription = "Sync GPS Horizon",
                            tint = if (state.isGpsActive) AuroraGreen else SolarGoldLight
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CosmicBackground,
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = CosmicSurface,
                contentColor = TextPrimary,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                NavigationBarItem(
                    selected = state.currentScreen == NavigationScreen.CLOCK,
                    onClick = { viewModel.setScreen(NavigationScreen.CLOCK) },
                    icon = { Icon(Icons.Default.AccessTime, contentDescription = "Chronometer") },
                    label = { Text("Clock", maxLines = 1) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CosmicBackground,
                        selectedTextColor = SolarGold,
                        indicatorColor = SolarGold,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_clock")
                )
                NavigationBarItem(
                    selected = state.currentScreen == NavigationScreen.DIAL,
                    onClick = { viewModel.setScreen(NavigationScreen.DIAL) },
                    icon = { Icon(Icons.Default.WbSunny, contentDescription = "Solar Dial") },
                    label = { Text("Dial", maxLines = 1) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CosmicBackground,
                        selectedTextColor = SolarGold,
                        indicatorColor = SolarGold,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_dial")
                )
                NavigationBarItem(
                    selected = state.currentScreen == NavigationScreen.CALENDAR,
                    onClick = { viewModel.setScreen(NavigationScreen.CALENDAR) },
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Planetary Calendar") },
                    label = { Text("Calendar", maxLines = 1) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CosmicBackground,
                        selectedTextColor = SolarGold,
                        indicatorColor = SolarGold,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_calendar")
                )
                NavigationBarItem(
                    selected = state.currentScreen == NavigationScreen.HEALTH,
                    onClick = { viewModel.setScreen(NavigationScreen.HEALTH) },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Circadian Suite") },
                    label = { Text("Circadian", maxLines = 1) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CosmicBackground,
                        selectedTextColor = SolarGold,
                        indicatorColor = SolarGold,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_circadian")
                )
                NavigationBarItem(
                    selected = state.currentScreen == NavigationScreen.CALCULATOR,
                    onClick = { viewModel.setScreen(NavigationScreen.CALCULATOR) },
                    icon = { Icon(Icons.Default.Calculate, contentDescription = "Converter") },
                    label = { Text("Convert", maxLines = 1) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CosmicBackground,
                        selectedTextColor = SolarGold,
                        indicatorColor = SolarGold,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_calculator")
                )
                NavigationBarItem(
                    selected = state.currentScreen == NavigationScreen.STRATEGY,
                    onClick = { viewModel.setScreen(NavigationScreen.STRATEGY) },
                    icon = { Icon(Icons.Default.AccountBalance, contentDescription = "Funding & Legal") },
                    label = { Text("Strategy", maxLines = 1) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CosmicBackground,
                        selectedTextColor = SolarGold,
                        indicatorColor = SolarGold,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_strategy")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CosmicBackground)
        ) {
            when (state.currentScreen) {
                NavigationScreen.CLOCK -> ClockScreen(
                    state = state,
                    onSelectLocation = { viewModel.setLocation(it) },
                    onRequestGps = { requestGps() },
                    onToggleQuantum = { viewModel.toggleQuantumPrecision() }
                )
                NavigationScreen.DIAL -> SolarDialScreen(state = state)
                NavigationScreen.CALENDAR -> CalendarScreen(
                    state = state,
                    onToggleHemisphere = { viewModel.toggleHemisphere() }
                )
                NavigationScreen.HEALTH -> CircadianHealthScreen(state = state)
                NavigationScreen.CALCULATOR -> CalculatorScreen(
                    state = state,
                    onCalculate = { y, m, d, h, min, loc ->
                        viewModel.updateCalcInputs(y, m, d, h, min, loc)
                    },
                    onLoadPreset = { y, m, d, h, min, loc ->
                        viewModel.loadCalculatorPreset(y, m, d, h, min, loc)
                    }
                )
                NavigationScreen.STRATEGY -> FundingStrategyScreen()
            }
        }
    }
}
