package com.hograna.liga.data.catalog

data class CatalogTeam(
    val name: String,
    val leagueOrigin: String,
    /** Public URL loaded by Coil, or a drawable resource name prefixed with "drawable://" */
    val crestUrl: String
)

object PredefinedTeamsCatalog {

    private const val SHIELD_BASE = "https://media.api-sports.io/football/teams"

    val UCL: List<CatalogTeam> = listOf(
        CatalogTeam("Real Madrid", "UCL", "$SHIELD_BASE/541.png"),
        CatalogTeam("FC Barcelona", "UCL", "$SHIELD_BASE/529.png"),
        CatalogTeam("Manchester City", "UCL", "$SHIELD_BASE/50.png"),
        CatalogTeam("Bayern München", "UCL", "$SHIELD_BASE/157.png"),
        CatalogTeam("Paris Saint-Germain", "UCL", "$SHIELD_BASE/85.png"),
        CatalogTeam("Liverpool", "UCL", "$SHIELD_BASE/40.png"),
        CatalogTeam("Juventus", "UCL", "$SHIELD_BASE/496.png"),
        CatalogTeam("Chelsea", "UCL", "$SHIELD_BASE/49.png"),
        CatalogTeam("Inter Milan", "UCL", "$SHIELD_BASE/505.png"),
        CatalogTeam("Borussia Dortmund", "UCL", "$SHIELD_BASE/165.png"),
        CatalogTeam("FC Porto", "UCL", "$SHIELD_BASE/212.png"),
        CatalogTeam("Ajax", "UCL", "$SHIELD_BASE/194.png"),
        CatalogTeam("Benfica", "UCL", "$SHIELD_BASE/211.png"),
        CatalogTeam("Atlético de Madrid", "UCL", "$SHIELD_BASE/530.png"),
        CatalogTeam("Arsenal", "UCL", "$SHIELD_BASE/42.png"),
        CatalogTeam("AC Milan", "UCL", "$SHIELD_BASE/489.png"),
        CatalogTeam("Manchester United", "UCL", "$SHIELD_BASE/33.png"),
        CatalogTeam("Tottenham Hotspur", "UCL", "$SHIELD_BASE/47.png"),
        CatalogTeam("RB Leipzig", "UCL", "$SHIELD_BASE/173.png"),
        CatalogTeam("Napoli", "UCL", "$SHIELD_BASE/492.png"),
        CatalogTeam("Sevilla FC", "UCL", "$SHIELD_BASE/536.png"),
        CatalogTeam("Sporting CP", "UCL", "$SHIELD_BASE/228.png"),
        CatalogTeam("Celtic", "UCL", "$SHIELD_BASE/264.png"),
        CatalogTeam("Rangers", "UCL", "$SHIELD_BASE/257.png")
    )

    val UEL: List<CatalogTeam> = listOf(
        CatalogTeam("Bayer Leverkusen", "UEL", "$SHIELD_BASE/168.png"),
        CatalogTeam("AS Roma", "UEL", "$SHIELD_BASE/497.png"),
        CatalogTeam("Villarreal CF", "UEL", "$SHIELD_BASE/533.png"),
        CatalogTeam("Feyenoord", "UEL", "$SHIELD_BASE/197.png"),
        CatalogTeam("West Ham United", "UEL", "$SHIELD_BASE/48.png"),
        CatalogTeam("Eintracht Frankfurt", "UEL", "$SHIELD_BASE/169.png"),
        CatalogTeam("Atalanta", "UEL", "$SHIELD_BASE/499.png"),
        CatalogTeam("Lazio", "UEL", "$SHIELD_BASE/487.png"),
        CatalogTeam("Real Betis", "UEL", "$SHIELD_BASE/543.png"),
        CatalogTeam("Olympique de Marseille", "UEL", "$SHIELD_BASE/81.png"),
        CatalogTeam("Olympique Lyonnais", "UEL", "$SHIELD_BASE/80.png"),
        CatalogTeam("PSV Eindhoven", "UEL", "$SHIELD_BASE/196.png")
    )

    val LIBERTADORES: List<CatalogTeam> = listOf(
        CatalogTeam("River Plate", "LIBERTADORES", "$SHIELD_BASE/1098.png"),
        CatalogTeam("Boca Juniors", "LIBERTADORES", "$SHIELD_BASE/1101.png"),
        CatalogTeam("Flamengo", "LIBERTADORES", "$SHIELD_BASE/127.png"),
        CatalogTeam("Palmeiras", "LIBERTADORES", "$SHIELD_BASE/121.png"),
        CatalogTeam("Nacional (Uruguay)", "LIBERTADORES", "$SHIELD_BASE/1112.png"),
        CatalogTeam("Peñarol", "LIBERTADORES", "$SHIELD_BASE/1113.png"),
        CatalogTeam("Olimpia", "LIBERTADORES", "$SHIELD_BASE/1118.png"),
        CatalogTeam("LDU Quito", "LIBERTADORES", "$SHIELD_BASE/1122.png"),
        CatalogTeam("Colo-Colo", "LIBERTADORES", "$SHIELD_BASE/1134.png"),
        CatalogTeam("Universidad de Chile", "LIBERTADORES", "$SHIELD_BASE/1135.png"),
        CatalogTeam("Independiente", "LIBERTADORES", "$SHIELD_BASE/1103.png"),
        CatalogTeam("San Lorenzo", "LIBERTADORES", "$SHIELD_BASE/1104.png"),
        CatalogTeam("Fluminense", "LIBERTADORES", "$SHIELD_BASE/119.png"),
        CatalogTeam("Santos", "LIBERTADORES", "$SHIELD_BASE/131.png"),
        CatalogTeam("Athletico Paranaense", "LIBERTADORES", "$SHIELD_BASE/118.png"),
        CatalogTeam("Deportivo Táchira", "LIBERTADORES", "$SHIELD_BASE/1133.png")
    )

    val ALL: List<CatalogTeam> = UCL + UEL + LIBERTADORES
}
