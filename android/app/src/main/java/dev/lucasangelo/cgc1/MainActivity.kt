package dev.lucasangelo.cgc1

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CGC1Theme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth })
                    },
                    exitTransition = {
                        slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth })
                    },
                ) {
                    composable ("home") {
                        HomeScreen(
                            onGameStartRequest = { gameId ->
                            navController.navigate("game/$gameId")
                        } )
                    }
                    composable("game/{gameId}",
                        arguments = listOf(navArgument("gameId") { type = NavType.IntType })
                        ) { backStackEntry ->
                        GameScreen(gameList[backStackEntry.arguments?.getInt("gameId")!!])
                    }
                }
            }
        }
    }
}
