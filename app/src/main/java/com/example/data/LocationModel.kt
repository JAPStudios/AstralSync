package com.example.data

data class LocationCoordinate(
    val name: String,
    val region: String,
    val latitude: Double,
    val longitude: Double,
    val timeZoneId: String = "UTC"
)

object LocationPresets {
    val WINDHAM_NY = LocationCoordinate(
        name = "Windham, Greene County",
        region = "New York, USA",
        latitude = 42.345996,
        longitude = -74.246748,
        timeZoneId = "America/New_York"
    )

    val ALBANY_NY = LocationCoordinate(
        name = "Albany Medical Center",
        region = "New York, USA",
        latitude = 42.6532,
        longitude = -73.7745,
        timeZoneId = "America/New_York"
    )

    val GREENWICH_UK = LocationCoordinate(
        name = "Royal Observatory Greenwich",
        region = "London, UK",
        latitude = 51.4769,
        longitude = 0.0005,
        timeZoneId = "Europe/London"
    )

    val TOKYO_JP = LocationCoordinate(
        name = "Tokyo",
        region = "Japan",
        latitude = 35.6762,
        longitude = 139.6503,
        timeZoneId = "Asia/Tokyo"
    )

    val SYDNEY_AU = LocationCoordinate(
        name = "Sydney (Southern Hemisphere)",
        region = "Australia",
        latitude = -33.8688,
        longitude = 151.2093,
        timeZoneId = "Australia/Sydney"
    )

    val CAIRO_EG = LocationCoordinate(
        name = "Cairo",
        region = "Egypt",
        latitude = 30.0444,
        longitude = 31.2357,
        timeZoneId = "Africa/Cairo"
    )

    val SAN_FRANCISCO_CA = LocationCoordinate(
        name = "San Francisco",
        region = "California, USA",
        latitude = 37.7749,
        longitude = -122.4194,
        timeZoneId = "America/Los_Angeles"
    )

    val ALL_PRESETS = listOf(
        WINDHAM_NY,
        ALBANY_NY,
        GREENWICH_UK,
        TOKYO_JP,
        SYDNEY_AU,
        CAIRO_EG,
        SAN_FRANCISCO_CA
    )
}
