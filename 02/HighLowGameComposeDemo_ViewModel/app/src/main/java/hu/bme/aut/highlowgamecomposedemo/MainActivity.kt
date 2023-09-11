package hu.bme.aut.highlowgamecomposedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hu.bme.aut.highlowgamecomposedemo.screen.about.AboutScreen
import hu.bme.aut.highlowgamecomposedemo.screen.game.GameScreen
import hu.bme.aut.highlowgamecomposedemo.screen.help.HelpScreen
import hu.bme.aut.highlowgamecomposedemo.screen.mainmenu.MainMenuScreen
import hu.bme.aut.highlowgamecomposedemo.ui.theme.HighLowGameComposeDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HighLowGameComposeDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyAppNavHost()
                }
            }
        }
    }
}

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "mainmenuscreen"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable("mainmenuscreen") {
            MainMenuScreen(
                onNavigateToGame = { navController.navigate("gamescreen?upperBound=20") },
                onNavigateToHelp = { navController.navigate("helpscreen/this is the help text") },
                navController
            )
        }
        composable("gamescreen?upperBound={upperBound}",
            arguments = listOf(navArgument("upperBound") {
                defaultValue = 0
                type = NavType.IntType }
            )
        ) {

            GameScreen()
        }


        composable("aboutscreen") {
            AboutScreen()
        }
        composable(
            "helpscreen/{helptext}",
            arguments = listOf(navArgument("helptext") { type = NavType.StringType })
        ) { navBackStackEntry ->
            /* Extracting the helptext from the route */
            val text = navBackStackEntry.arguments?.getString("helptext")

            text?.let {
                HelpScreen(helpText = text)
            }
        }
    }
}


