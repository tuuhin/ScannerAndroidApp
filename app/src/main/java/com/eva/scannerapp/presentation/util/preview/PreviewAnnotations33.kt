package com.eva.scannerapp.presentation.util.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Retention(AnnotationRetention.SOURCE)
@Target(
	AnnotationTarget.FUNCTION
)
@Preview(
	apiLevel = 33,
	uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
	apiLevel = 33,
	uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
annotation class PreviewLightDarkApi33


@Preview(
	apiLevel = 33
)
annotation class PreviewApi33



