package com.eva.scannerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.eva.scannerapp.ui.theme.ScannerAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			ScannerAppTheme {
				// A surface container using the 'background' color from the theme
				Surface(color = MaterialTheme.colorScheme.background) {
				}
			}
		}
	}
}
