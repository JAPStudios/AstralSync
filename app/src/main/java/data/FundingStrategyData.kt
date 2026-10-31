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

    data class LegalPrecedent(
        val caseName: String,
        val citation: String,
        val keyPrinciple: String,
        val applicationToAstralSync: String
    )

    val GRANT_PILLARS = listOf(
        GrantPillar(
            category = "1. Circadian Biology & Public Health Grants",
            targetFunders = listOf(
                "National Institutes of Health (NIH - NIGMS / NHLBI)",
                "Wellcome Trust (Mental Health & Biological Rhythms)",
                "National Science Foundation (NSF - Division of Integrative Organismal Systems)",
                "Society for Research on Biological Rhythms (SRBR)"
            ),
            valueProposition = "Traditional 24-hour artificial time zones induce chronic 'social jetlag'. AstralSync roots biological sleep-wake cycles in authentic local solar noon and sunrise, directly reducing circadian misalignment and sleep disorders.",
            grantSizeRange = "$150,000 – $2,500,000",
            actionSteps = listOf(
                "Register as a 501(c)(3) tax-exempt scientific/educational organization or partner with a 501(c)(3) Fiscal Sponsor (e.g., Code for Science & Society, Open Collective Foundation).",
                "Apply for NIH Small Business Innovation Research (SBIR / STTR) or R21 exploratory health grants targeting circadian rhythm synchronization.",
                "Co-author validation studies with university chronobiology and sleep research departments."
            )
        ),
        GrantPillar(
            category = "2. Open Source & Digital Public Infrastructure Grants",
            targetFunders = listOf(
                "NLnet Foundation (NGI0 Entrust / NGI Zero Core - EU Horizon)",
                "Open Technology Fund (OTF)",
                "Mozilla Technology Fund & MOSS (Mozilla Open Source Support)",
                "Sloan Foundation (Data & Computational Research)"
            ),
            valueProposition = "AstralSync provides open, vendor-neutral astronomical chronometry protocols and open wearable APIs, freeing citizens from closed surveillance ecosystems (Google/Apple/Samsung).",
            grantSizeRange = "$30,000 – $500,000 (No equity, pure grant)",
            actionSteps = listOf(
                "Submit grant applications to NLnet's rolling NGI (Next Generation Internet) funding rounds for open-source digital commons.",
                "Publish open astronomical algorithms under GPLv3/Apache 2.0 on GitHub (https://github.com/JAPStudios/AstralSync).",
                "Release open Rust / Kotlin / Python libraries for astronomical time computation."
            )
        ),
        GrantPillar(
            category = "3. Community & Transparent Philanthropic Crowdfunding",
            targetFunders = listOf(
                "Open Collective (Tax-deductible public ledger)",
                "GitHub Sponsors (with matching funds)",
                "Kickstarter / BackerKit (Community Pre-launch for Wearable AstralSync OS)",
                "Patreon / Substack Scientific Community"
            ),
            valueProposition = "Direct backing by astronomy enthusiasts, photographers (golden hour seekers), biohackers, shift workers, and circadian health pioneers.",
            grantSizeRange = "$10,000 – $250,000 / year recurring",
            actionSteps = listOf(
                "Launch an Open Collective profile connected to the GitHub repository for 100% financial transparency.",
                "Build tiered community memberships (Astronomer, Chrono-Engineer, Planetary Citizen).",
                "Offer open-source Astral watch faces for Wear OS, Galaxy Watch, and Pixel Watch as early supporter perks."
            )
        )
    )

    val COMPETITOR_MATRIX = listOf(
        CompetitorDisruption(
            category = "Time & Alarms",
            proprietaryMonopolies = listOf("Google Clock", "Samsung Clock", "Apple Clock"),
            corporateFlaw = "Hardcoded to arbitrary political time zones drawn for train schedules in 1883; completely disconnected from the sun and planetary rotation.",
            astralSyncAdvantage = "00:00 begins at true local sunrise; time changes fluidly with coordinates so humans receive 100% full daylight productivity."
        ),
        CompetitorDisruption(
            category = "Calendars & Scheduling",
            proprietaryMonopolies = listOf("Google Calendar", "Samsung Calendar", "Microsoft Outlook"),
            corporateFlaw = "Rigid Gregorian calendar with irregular month lengths (28-31 days) ignoring lunar cycles, planetary seasons, and Earth epoch.",
            astralSyncAdvantage = "Equi-seasonal 91.3125-day astronomical seasons (Lent, Sem, Autumnus, Winter) and continuous Earth-age tracking since planetary birth."
        ),
        CompetitorDisruption(
            category = "Sleep & Circadian Tracking",
            proprietaryMonopolies = listOf("Sleep Cycle", "Fitbit", "Pokémon Sleep", "Samsung Health", "Apple Sleep"),
            corporateFlaw = "Gamified or paywalled sleep metrics that track sleep after the fact without synchronizing morning light exposure to real horizon dawn.",
            astralSyncAdvantage = "Astronomical milestone guidance: automated alerts for morning golden hour photon intake and night start astronomical twilight wind-down."
        ),
        CompetitorDisruption(
            category = "Mindfulness & Vitality",
            proprietaryMonopolies = listOf("Headspace", "Noom", "Google Fit"),
            corporateFlaw = "Expensive subscriptions ($70-$150/yr) pushing artificial meditation routines disconnected from cosmic solar energy and seasonal biological needs.",
            astralSyncAdvantage = "Free, non-profit public utility aligned with nature, solar elevation azimuth, and mythological archetype grounding."
        ),
        CompetitorDisruption(
            category = "Compass & Wearable Sensors",
            proprietaryMonopolies = listOf("Samsung Compass", "Galaxy Watch", "Apple Watch", "Pixel Watch", "Forecast"),
            corporateFlaw = "Siloed proprietary hardware trapping health metrics into proprietary advertising clouds.",
            astralSyncAdvantage = "Open standard chronometer combining real-time magnetic heading, live solar azimuth, and true astronomical horizon angles."
        )
    )

    val LEGAL_FRAMEWORK = LegalPrecedent(
        caseName = "Melzer et al. v. Johnson & Johnson Consumer Inc.",
        citation = "Case No. 3:22-cv-03149 (U.S. District Court for the District of New Jersey)",
        keyPrinciple = "Consumer protection action regarding deceptive labeling, unverified wellness claims, and scientific accuracy in corporate health representations.",
        applicationToAstralSync = "AstralSync establishes absolute scientific accuracy by grounding its clock in verifiable NOAA astronomical algorithms, planetary physics, and published chronobiology rather than proprietary marketing metrics. As a 501(c)(3) not-for-profit, AstralSync eliminates profit-driven manipulation, positioning itself as the trusted, objective public standard for truthful timekeeping."
    )
}
