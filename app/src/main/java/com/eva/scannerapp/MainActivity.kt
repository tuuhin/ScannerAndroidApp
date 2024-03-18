package com.eva.scannerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.eva.scannerapp.presentation.navigation.routes.NavGraphs
import com.eva.scannerapp.presentation.util.LocalSnackBarStateProvider
import com.eva.scannerapp.ui.theme.ScannerAppTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import dagger.hilt.android.AndroidEntryPoint

@OptIn(
	ExperimentalMaterialNavigationApi::class,
	ExperimentalAnimationApi::class
)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


	override fun onCreate(savedInstanceState: Bundle?) {
		//splash api
		installSplashScreen()
		//enable edge to edge
		WindowCompat.setDecorFitsSystemWindows(window, false)

		super.onCreate(savedInstanceState)

		setContent {
			ScannerAppTheme {
				val snackBarProvider = remember { SnackbarHostState() }
				// A surface container using the 'background' color from the theme
				CompositionLocalProvider(
					LocalSnackBarStateProvider provides snackBarProvider
				) {
					Surface(color = MaterialTheme.colorScheme.background) {
						val engine = rememberAnimatedNavHostEngine()
						DestinationsNavHost(navGraph = NavGraphs.root, engine = engine)
					}
				}
			}
		}
	}
}
