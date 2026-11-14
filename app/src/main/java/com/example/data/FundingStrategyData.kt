package com.example.data

/**
 * Strategy, Grant Portals, Legal Foundations & Competitor Analysis
 * for AstralSync's Non-Profit Public Benefit Mission.
 */
object FundingStrategyData {

    data class GrantPillar(
        val category: String,
        val targetFunders: List<String>,
        val valueProposition: String,
        val grantSizeRange: String,
        val actionSteps: List<String>
    )

    data class CompetitorDisruption(
        val category: String,
        val proprietaryMonopolies: List<String>,
        val corporateFlaw: String,
        val astralSyncAdvantage: String
    )

    data class ScopeNote(
        val citation: String,
        val keyPrinciple: String,
        val applicationToAstralSync: String
    )

    val GRANT_PILLARS = listOf(
        GrantPillar(
            category = "1. Scientific Validation & Research Collaboration",
            targetFunders = listOf(
                "National Institutes of Health (program eligibility varies)",
                "National Science Foundation (program eligibility varies)",
                "Society for Research on Biological Rhythms (research community)"
            ),
            valueProposition = "AstralSync implements sunrise-referenced timekeeping and solar-event estimates. Independent astronomy and chronobiology review could validate the calculations and assess potential research use; health outcomes are not established by the app.",
            grantSizeRange = "Depends on the active funding call",
            actionSteps = listOf(
                "Review current official funding notices, applicant eligibility, and permitted project costs before pursuing a program.",
                "Obtain independent review of solar calculations, time-zone handling, and the app's circadian-related descriptions.",
                "Publish reproducible test cases, model assumptions, and known limitations."
            )
        ),
        GrantPillar(
            category = "2. Open-Source Software & Digital Infrastructure",
            targetFunders = listOf(
                "NLnet Foundation (check current calls)",
                "Open Technology Fund (check current programs)",
                "Mozilla open-source support programs (check current calls)"
            ),
            valueProposition = "The project can contribute open-source software for astronomical time calculations and local solar-event visualization. Funding availability and eligibility depend on each program's current rules.",
            grantSizeRange = "Depends on the active funding call",
            actionSteps = listOf(
                "Check each funder's official call, geographic scope, applicant rules, and deadlines.",
                "Document the repository's software license, maintenance plan, privacy practices, and security process.",
                "Keep proposed libraries and integrations distinct from functionality that is already implemented."
            )
        ),
        GrantPillar(
            category = "3. Testing, Accessibility & Release Support",
            targetFunders = listOf(
                "Android device and platform testing resources",
                "Accessibility testing communities",
                "Astronomy and software quality reviewers"
            ),
            valueProposition = "Structured testing can improve device compatibility, accessibility, calculation reliability, and release readiness without assuming a particular legal or tax classification.",
            grantSizeRange = "Cash or in-kind support; case-specific",
            actionSteps = listOf(
                "Establish repeatable tests across Android versions, screen sizes, and time-zone transitions.",
                "Review contrast, text scaling, screen-reader labels, and touch-target sizes.",
                "Verify release signing, backup behavior, permissions, and privacy disclosures before distribution."
            )
        )
    )

    val COMPETITOR_MATRIX = listOf(
        CompetitorDisruption(
            category = "Time & Alarms",
            proprietaryMonopolies = listOf("Google Clock", "Samsung Clock", "Apple Clock"),
            corporateFlaw = "Civil clocks display local legal time and generally do not make sunrise the basis of the displayed hour.",
            astralSyncAdvantage = "AstralSync adds a sunrise-referenced clock and estimated solar milestones; it does not replace civil time or a full alarm application."
        ),
        CompetitorDisruption(
            category = "Calendars & Scheduling",
            proprietaryMonopolies = listOf("Google Calendar", "Samsung Calendar", "Microsoft Outlook"),
            corporateFlaw = "General-purpose calendars prioritize civil dates and scheduling rather than a custom solar-based date sequence.",
            astralSyncAdvantage = "AstralSync includes a custom twelve-month sequence and equal-duration season model alongside Gregorian date conversion."
        ),
        CompetitorDisruption(
            category = "Sleep & Circadian Tracking",
            proprietaryMonopolies = listOf("Sleep Cycle", "Fitbit", "Samsung Health", "Apple Sleep"),
            corporateFlaw = "Sleep products estimate sleep-related metrics using their own sensors and algorithms; those measurements are outside this app's current scope.",
            astralSyncAdvantage = "AstralSync supplies solar-time context only. It does not measure sleep, diagnose circadian disorders, or provide clinical telemetry."
        ),
        CompetitorDisruption(
            category = "Mindfulness & Wellness",
            proprietaryMonopolies = listOf("Headspace", "Noom", "Google Fit"),
            corporateFlaw = "These products provide separate wellness and activity features that are not part of astronomical timekeeping.",
            astralSyncAdvantage = "AstralSync focuses on local solar calculations and does not claim to replace meditation, nutrition, or fitness services."
        ),
        CompetitorDisruption(
            category = "Compass & Solar Direction",
            proprietaryMonopolies = listOf("Phone magnetic compass applications", "Wearable compass applications"),
            corporateFlaw = "Magnetic compasses measure magnetic-field direction and may require calibration; solar azimuth is a different quantity.",
            astralSyncAdvantage = "AstralSync calculates solar azimuth from location and time. It does not measure magnetic heading or provide certified navigation."
        )
    )

    val SCOPE_NOTE = ScopeNote(
        citation = "AstralSync calculation and feature scope",
        keyPrinciple = "Solar-event and solar-position values are estimates based on NOAA-style equations. Accuracy depends on the model assumptions, coordinates, date, and time-zone data.",
        applicationToAstralSync = "The application does not measure personal light exposure, sleep, melatonin, or circadian phase. It does not diagnose or treat a medical condition and its solar azimuth is not a magnetic compass or navigation instrument. Funding eligibility is determined by each program's current official rules; no tax classification or grant award is asserted here."
    )
}
