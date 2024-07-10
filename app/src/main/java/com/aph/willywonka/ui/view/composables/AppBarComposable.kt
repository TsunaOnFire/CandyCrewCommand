package com.aph.willywonka.ui.view.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.layoutId
import com.aph.willywonka.R
import com.aph.willywonka.ui.materialDesing.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppBarComposable(progress: Float, modifier: Modifier = Modifier, onMenuClickAction: () -> Unit) {

    Row(modifier = modifier.heightIn(32.dp,64.dp)) {
        Image(
            painter = painterResource(id = R.mipmap.ic_app),
            contentDescription = "",
            modifier = Modifier
                .aspectRatio(1f),
        )
        Text(text = stringResource(id = R.string.app_name).uppercase(),
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        IconButton(onClick = { onMenuClickAction() },
            modifier = Modifier.aspectRatio(1f)
        ) {
            ButtonHamburgerCross(progress)
        }
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun ButtonHamburgerCross(progress: Float, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.hamburger_cross_scene)
            .readBytes()
            .decodeToString()
    }

    MotionLayout(
        motionScene = MotionScene(content = motionScene),
        progress = progress,
        modifier = Modifier
            .size(32.dp)
    ) {
        Box(
            Modifier
                .layoutId("box_top")
                .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
        )
        Box(
            Modifier
                .layoutId("box_mid")
                .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
        )
        Box(
            Modifier
                .layoutId("box_bot")
                .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
        )
    }
}

//#region Previews
@Preview(showBackground = true)
@Composable
fun HamburgerButtonPreview() {
    AppTheme {
        ButtonHamburgerCross(0f)
    }
}
@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
    val coroutineScope = rememberCoroutineScope()

    val progress = remember { Animatable(0f) }
    AppTheme {
        AppBarComposable(progress.value){
            coroutineScope.launch {
                when (progress.value) {
                    0f -> progress.animateTo(1f);
                    1f -> progress.animateTo(0f);
                    else -> {}
                }
            }
        }
    }
}
//#endregion