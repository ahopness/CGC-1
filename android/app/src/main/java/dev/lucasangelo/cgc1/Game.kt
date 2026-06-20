package dev.lucasangelo.cgc1

import androidx.compose.ui.graphics.Color

data class Game(
    val name :String,
    val codename :String,
    val version :String,
    val description :String,
    val logo :Int,
    val icon :Int,
    val background :Int,
    val color_theme :Color,
    val playstore_link :String = ""
)

val gameList = listOf(
    Game(
        "Blood Loss",
        "nietzsche",
        "v2.3rev0",
        "Control, oppress and consume the blood of your enemies to win this arena.\n" +
                "\n" +
                "Inside a depression in the middle of a desert a blind soldier holds a special power: blood manipulation. Control, oppress and consume the blood of your enemies to win this arena amidst the world comes to an end.\n" +
                "Featuring music by OKMO.\n" +
                "\n" +
                "\"It's in our heartbeats.\"",
        R.drawable.logo_bloodloss,
        R.drawable.icon_bloodloss,
        R.drawable.screenshot_bloodloss,
        Color.Yellow.darken(),
    ),
    Game(
        "Tidal Hell",
        "wargames",
        "v2.3rev0",
        "A zero-player game where you make weapons and watch solders fight.\n" +
                "\n" +
                "An anti-war zero-player simulation game where you engineer weapons and send off your pawns to the battlefield and watch them fight. You gain more resources each time your team wins a round. Good luck, you'll need it.\n" +
                "Featuring music by Jarren Crist.\n" +
                "\n" +
                "\"Shoot first, apoligize later.\"",
        R.drawable.logo_tidalhell,
        R.drawable.icon_tidalhell,
        R.drawable.screenshot_tidalhell,
        Color.Green.darken(),
    ),
    Game(
        "Castration",
        "caledonia",
        "v2.2rev0",
        "Complex enemy AI inside a reality bending prison.\n" +
                "\n" +
                "With controls that are out of the ordinary, the enemies also demonstrate complex behaviors that are only understood if you pay close attention to your surroundings. You can't make out if you're awake or still dreaming, reality is twisted around this far away prison with eyes around every corner and your biggest hope is to make it out in one peace.\n" +
                "\n" +
                "\"There are only dormitories.\"",
        R.drawable.logo_castration,
        R.drawable.icon_castration,
        R.drawable.screenshot_castration,
        Color.Red.darken(),
    ),
    Game(
        "Full Metal Syncope",
        "amianto",
        "v1.3rev0",
        "Explore this surreal world, meet, greet & scream at it's residents.\n" +
                "\n" +
                "Floating islands inside a virtual landscape, post-post-modern writing & retro visuals are all part of this part visual novel, part fps game. Dive in, walk, talk, scream. Gather points, but for what purpose? no one is really sure. Don't forget to beware, the horse is the white of the eye and the dark within. Eyes wide open.\n" +
                "Featuring music by Jarren Crist.\n" +
                "\n" +
                "\"People are graveyards.\"",
        R.drawable.logo_fullmetalsyncope,
        R.drawable.icon_fullmetalsyncope,
        R.drawable.screenshot_fullmetalsyncope,
        Color.Magenta.darken(),
    ),
    Game(
        "GIFT",
        "pier",
        "",
        "You are on the post-death, search it, find memories, find yourself.\n" +
                "\n" +
                "GIFT is about death and rebirth. Explore these liminal monochromatic fragments of memories, breath in that doubts that create fear, search it, find memories, find yourself. It's in our hands, everything is.",
        R.drawable.logo_gift,
        R.drawable.icon_gift,
        R.drawable.screenshot_gift,
        Color.DarkGray,
//        "https://play.google.com/store/apps/details?id=com.ahopness.gift"
        "market://details?id=com.ahopness.gift"
    ),
    Game(
        "nDV: NeuroDive",
        "yosemite",
        "v1.0rev0",
        "Reverse hashing reconstruction software for mass storage segments.\n" +
                "\n" +
                "Recover and decrypt, sniff and scratch, this is - or was - a memoir. Most corrupted data here is encrypted, use reverse hashing techniques to reconstruct the echoes of this single constant in this spiritual successor to GIFT.\n" +
                "\n" +
                "\"The natural order of everything is chaos.\"",
        R.drawable.logo_ndvneurodive,
        R.drawable.icon_ndvneurodive,
        R.drawable.screenshot_ndvneurodive,
        Color.Black,
    )
)

inline fun Color.darken(darkenBy: Float = 0.75f): Color {
    val multiplier = 1f - darkenBy
    return copy(
        red = (red * multiplier).coerceIn(0f, 1f),
        green = (green * multiplier).coerceIn(0f, 1f),
        blue = (blue * multiplier).coerceIn(0f, 1f),
        alpha = alpha
    )
}