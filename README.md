# AstralSync

AstralSync is an Android astronomical timekeeping application. It provides a sunrise-referenced chronometer, calculated solar milestones and solar position, an Astral calendar, a date converter, circadian information screens, and a funding-strategy reference screen.

## Build

Install Android Studio with Android SDK Platform 36.1 and Build Tools 36.0.0. The repository includes the Gradle 9.3.1 wrapper.

On macOS or Linux:

```sh
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
```

On Windows:

```bat
gradlew.bat :app:assembleDebug
gradlew.bat :app:testDebugUnitTest
```

The debug APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

The project retains its AI Studio release and debug signing configurations. The debug keystore is supplied by the build environment; CI creates a temporary one for verification. Do not commit signing keys or passwords.

## Location

The application can calculate solar data for its bundled location presets. Device location is optional and is requested only when the location action is selected. Grant either fine or coarse location permission to use the device's last known location.

## Calculation notes

Solar calculations use the NOAA-style fractional-year approximation. They are estimates, not an ephemeris-grade replacement for an observatory or navigation almanac. The calendar currently implements the 12-month sequence defined in `AstralCalendarEngine`; its month and season durations are part of that implementation.

`System.nanoTime()` is a monotonic elapsed-time source with an arbitrary origin. It is not synchronized to the wall clock, so its digits must not be represented as the fractional part of UTC or Astral wall-clock time.

### Calendar model

- Gregorian dates are mapped proportionally into a fixed 365.25-day Astral year.
- The custom calendar uses twelve equal 30.4375-day months and four equal 91.3125-day calendar seasons. These are model intervals, not measured astronomical season lengths.
- The twelve archetypes follow equal Astral-year segments rather than standard tropical-zodiac date boundaries.
- The Chinese zodiac label is a Gregorian-year approximation; the lunar-new-year boundary is not calculated.
- The Mayan display is a 20-day name rotation anchored to 0001-01-01 in the proleptic Gregorian calendar. It does not calculate the full 13-number GMT-correlated Tzolk'in date.

## Tests

Run the unit and Robolectric tests from Android Studio's test runner. Run instrumented tests on an Android device or emulator with the connected-device test runner.
