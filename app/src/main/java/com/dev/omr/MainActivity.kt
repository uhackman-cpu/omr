package com.dev.omr

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dev.omr.data.OmrConfig
import com.dev.omr.data.SessionHolder
import com.dev.omr.ui.check.CheckScreen
import com.dev.omr.ui.copy.CopyScreen
import com.dev.omr.ui.home.HomeScreen
import com.dev.omr.ui.omr.OmrFillScreen
import com.dev.omr.ui.summary.SummaryScreen
import com.dev.omr.ui.theme.OMRPracticeTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OMRPracticeTheme {
                AppNav()
            }
        }
    }
}

@Composable
fun AppNav() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "home") {
        composable("home") {
            HomeScreen(onStart = { cfg: OmrConfig ->
                SessionHolder.current = null
                PendingConfig.config = cfg
                nav.navigate("fill")
            })
        }
        composable("fill") {
            val cfg = PendingConfig.config ?: return@composable
            OmrFillScreen(config = cfg, onSubmit = { _ ->
                nav.navigate("copy") { popUpTo("home") { inclusive = false } }
            })
        }
        composable("copy") {
            val s = SessionHolder.current ?: return@composable
            CopyScreen(session = s, onCheck = { nav.navigate("check") })
        }
        composable("check") {
            val s = SessionHolder.current ?: return@composable
            CheckScreen(session = s, onDone = { nav.navigate("summary") })
        }
        composable("summary") {
            val s = SessionHolder.current ?: return@composable
            SummaryScreen(
                session = s,
                onNewOmr = { nav.navigate("home") { popUpTo("home") { inclusive = true } } },
                onReview = { nav.navigate("check") }
            )
        }
    }
}

object PendingConfig { var config: OmrConfig? = null }
