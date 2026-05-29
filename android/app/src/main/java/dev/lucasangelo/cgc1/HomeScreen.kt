package dev.lucasangelo.cgc1

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun HomeScreenPreview() {
    CGC1Theme {
        HomeScreen(onGameStartRequest = {})
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onGameStartRequest : (Int) -> Unit) {
    val pagerState : PagerState = rememberPagerState ( pageCount = { gameList.size } )
    val currentGame : Game = gameList[pagerState.currentPage]

    var showAboutSheet by remember { mutableStateOf( false ) }

    Row {
        HomeNavigationRail({
            showAboutSheet = true
        })

        Box( modifier = Modifier.fillMaxSize() ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
            ) { page ->
                GameInfoPanel(gameList[page])
            }

            GameInfoBar(
                game = currentGame,
                onGameStartRequest = onGameStartRequest,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .safeDrawingPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            PagerDotIndicator(
                pageCount = gameList.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .safeDrawingPadding()
                    .padding(vertical = 32.dp),
            )
        }
    }

    val modalBottomSheetState = rememberModalBottomSheetState()
    if (showAboutSheet) {
        AboutBottomSheet(
            sheetState = modalBottomSheetState,
            onDismissRequest = { showAboutSheet = false }
        )
    }
}

@Composable
fun HomeNavigationRail(onAboutItemClick: () -> Unit) {
    val context = LocalContext.current
    NavigationRail {
        NavigationRailItem(
            selected = false,
            label = { Text("About") },
            icon = { Icon(
                imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                contentDescription = "About"
            ) },
            onClick = onAboutItemClick
        )
        NavigationRailItem(
            selected = false,
            label = { Text(
                text = "Open\nWebsite",
                textAlign = TextAlign.Center
            ) },
            icon = { Icon(
                imageVector = Icons.Outlined.Link,
                contentDescription = "Open Website"
            ) },
            onClick = {
                context.startActivity(Intent(
                    Intent.ACTION_VIEW,
                    "https://lucasangelo.dev".toUri()
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        NavigationRailItem(
            selected = false,
            label = { Text(
                text = "Rate\nCollection",
                textAlign = TextAlign.Center
            ) },
            icon = { Icon(
                imageVector = Icons.Outlined.StarRate,
                contentDescription = "Rate Collection"
            ) },
            onClick = {
                context.startActivity(Intent(
                    Intent.ACTION_VIEW,
                    "market://details?id=dev.lucasangelo.cgc1".toUri()
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        )
    }
}

@Composable
fun GameInfoPanel(game: Game) {
    Box (modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = game.background,
            transitionSpec = { fadeIn(tween(600)) togetherWith fadeOut(tween(600)) }
        ) { bgRes ->
            Image(
                painter = painterResource(bgRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 8.dp)
        ) {
            Box(modifier = Modifier.weight(.5f)) {
                Image(
                    painter = painterResource(game.icon),
                    contentDescription = "Icon for ${game.name}",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(modifier = Modifier.weight(1f)) {
                Image(
                    painter = painterResource(game.logo),
                    contentDescription = game.name,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(modifier = Modifier.weight(4f)) {
                Text(
                    text = game.description,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
@Composable
fun GameInfoBar(game: Game, onGameStartRequest: (Int) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        if (game.version.isNotBlank()) {
            Text(
                text = game.version,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.icon_extra),
                contentDescription = "Extra game",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .height(42.dp)
                    .rotate(20f)

            )
        }

        AnimatedContent(
            targetState = game.color_theme,
            transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) }
        ) { colorTheme ->
            Button(
                onClick = {
                    if(!game.playstore_link.isNotBlank()) {
                        onGameStartRequest(gameList.indexOf(game))
                    } else {
                        context.startActivity(Intent(
                            Intent.ACTION_VIEW,
                            game.playstore_link.toUri()
                        ).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        })
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    colorTheme,
                    Color.White,
                    Color.Black,
                    Color.Gray
                )
            ) {
                Text(
                    text = if (game.playstore_link.isNotBlank()) "Open in Playstore" else "Dive In",
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}
@Composable
fun PagerDotIndicator(pageCount: Int, currentPage: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(pageCount) { i ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (i == currentPage) Color.White
                            else Color.White.copy(alpha = 0.5f)
                    )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutBottomSheet(sheetState: SheetState, onDismissRequest: () -> Unit) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo_cgc_alt_horizontal),
                contentDescription = "Lucas' Computer Game Collection Vol 1.",
                contentScale = ContentScale.Inside,
                alignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
            )

            Text(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                text = "ABOUT"
            )

            Text(
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.bodySmall,
                text =
                    "Imagine you're burried alive, what things would be floating around in your head while you are waiting for your demise?\n" +
                    "\n" +
                    "This is a collection of 5+1 games made between 2024 and 2026 , all of these titles contain experimental takes on different genres, from action to simulation, from horror to puzzle, dive into these wierd games, fall in love or hate every detail of the worlds and characters, no in-betweens, twee and brutal are the only available feelings here.\n" +
                    "\n" +
                    "These titles are more akin to art pieces rathen than actual games, fun isn't garanteed. Everything is design with an intent in mind, but it's ok if you don't get it at first, neither do i most of the time, just try to feel it.\n" +
                    "\n" +
                    "Something you wanna talk about? Hit me up at 'dev.lucas.angelo@gmail.com'.\n" +
                    "Looking for more? Go check 'https://lucasangelo.dev' out."
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                text = "WARNING"
            )

            Text(
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.bodySmall,
                text = "None of theses games have saving and loading, you're progress will be lost once you exit each game, this should be fine since none of the title are particularly long."
            )
        }
    }
}
