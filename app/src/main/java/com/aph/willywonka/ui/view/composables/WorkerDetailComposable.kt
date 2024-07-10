package com.aph.willywonka.ui.view.composables


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Down
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Up
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.aph.willywonka.R
import com.aph.willywonka.data.model.WorkerBO
import com.aph.willywonka.ui.materialDesing.AppTheme
import com.aph.willywonka.ui.viewmodel.WorkerViewModel
import com.aph.willywonka.utils.AphToolbox
import kotlinx.coroutines.launch


@Composable
fun WorkerDetailsComposable(
    viewModel: WorkerViewModel,
    modifier: Modifier = Modifier,
    onBackPress: () -> Unit = {},
) {
    val refreshScope = rememberCoroutineScope()

    fun reload() = refreshScope.launch {
        val workerId = viewModel.worker.value?.id
        workerId?.let {id ->
            viewModel.requestWorker(id)
        }
    }

    val worker by remember { viewModel.worker }
    val isLoading by remember { viewModel.isLoading }

    WorkerDetails(
        isLoading = isLoading,
        worker = worker,
        reloadAction = { reload() },
        onBackPress = onBackPress,
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun WorkerDetails(
    isLoading: Boolean,
    worker: WorkerBO?,
    reloadAction: () -> Unit,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier
) {

    val state = rememberPullRefreshState(
        refreshing =  isLoading,
        onRefresh = { reloadAction() }
    )

    Box(modifier.pullRefresh(state)) {
        Column(
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            WorkerDetailHeader(worker, onBackPress)
            WorkerDetailBody(worker)
        }
        PullRefreshIndicator(
            isLoading,
            state,
            modifier.align(alignment = Alignment.TopCenter)
        )
    }
}

@Composable
fun WorkerDetailHeader(
    worker: WorkerBO?,
    onBackPress: () -> Unit
) {
    ConstraintLayout  {
        val (backButton, textName, textProfession, imageGender, imageAvatar, imageDecoration , auxView) = createRefs()

        val startGuideline = createGuidelineFromStart(0.3f)
        val endGuideline = createGuidelineFromEnd(0.3f)
        val startDecorationImageGuideline = createGuidelineFromStart(-0.2f)
        val endDecorationImageGuideline = createGuidelineFromEnd(-0.2f)
        Box(
            modifier = Modifier
                .constrainAs(backButton) {
                    top.linkTo(parent.top, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    height = Dimension.value(48.dp)
                    width = Dimension.value(48.dp)
                }
                .shadow(0.dp)
                .clickable { onBackPress() }
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "",
                modifier = Modifier
                    .background(
                        Color.Transparent
                    )
                    .align(Alignment.Center) ,
            )
        }

        Text(
            text = worker?.let{"${it.first_name} ${it.last_name}"} ?: "",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(textName) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                height = Dimension.wrapContent
            }
        )

        Image(
            painter = painterResource(AphToolbox.GetGenderIcon(worker?.gender)),
            contentDescription = stringResource(id = R.string.workerItemList_gender_content_description),
            modifier = Modifier.constrainAs(imageGender) {
                top.linkTo(textName.top)
                bottom.linkTo(textName.bottom)
                start.linkTo(textName.end)
                height = Dimension.wrapContent
            }
        )

        Text(
            text = worker?.profession ?: "",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.constrainAs(textProfession) {
                top.linkTo(textName.bottom)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                height = Dimension.wrapContent
            }
        )

        AsyncImage(
            model = worker?.image,
            placeholder = painterResource(R.drawable.ic_launcher_background),
            contentDescription = stringResource(id = R.string.workerItemList_avatar_content_description),
            modifier = Modifier
                .constrainAs(imageAvatar) {
                    width = Dimension.fillToConstraints
                    top.linkTo(textProfession.bottom, margin = 8.dp)
                    start.linkTo(startGuideline, margin = 0.dp)
                    end.linkTo(endGuideline, margin = 0.dp)
                }
                .aspectRatio(1f, true)
                .shadow(
                    6.dp,
                    shape = CircleShape,
                    true
                )
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop,
        )


        Box(
            modifier = Modifier.constrainAs(auxView) {
                top.linkTo(imageAvatar.top)
                bottom.linkTo(imageAvatar.bottom)
                height = Dimension.value(0.dp)
            }
        )

        Box(
            modifier = Modifier
                .constrainAs(imageDecoration) {
                    bottom.linkTo(auxView.top)
                    start.linkTo(startDecorationImageGuideline)
                    end.linkTo(endDecorationImageGuideline)
                    width = Dimension.matchParent
                }
                .zIndex(-10f)
                .aspectRatio(1f, true)
                .shadow(
                    6.dp,
                    shape = CircleShape,
                    true
                )
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
        )
        /*
        Image(
            painter = painterResource(R.drawable.bg_corner),
            contentDescription = null,
            modifier = Modifier.constrainAs(imageDecoration) {
                bottom.linkTo(imageAvatar.top)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
            }
        )

         */
    }
}

@Composable
fun WorkerDetailBody(worker: WorkerBO?) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        WorkerDetailAboutMeSection(worker)
        WorkerDetailDescriptionSection(worker)
        WorkerDetailQuotaSection(worker)
        WorkerDetailPreferencesSection(worker)
    }
}

//#region Worker Detail Body elements
@Composable
private fun WorkerDetailBodySection(
    title: String,
    modifier: Modifier = Modifier,
    iconDrawable: Int = R.mipmap.ic_info,
    content: @Composable () -> Unit
) {
    val contentVisible = remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if(contentVisible.value) 180f else 0f,
        label = "rotationState",
    )
    Card (
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, colorResource(id = R.color.accent_color_borders)),
        modifier = modifier
            .padding(
                horizontal = 20.dp,
                vertical = 10.dp
            )
            .shadow(
                shape = RoundedCornerShape(12.dp),
                elevation = 4.dp
            )
    ) {
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
        ) {
            val (titleLabel, icon, button) = createRefs()

            val startGuideline = createGuidelineFromStart(0.15f)
            val endGuideline = createGuidelineFromEnd(0.15f)

            Text(
                text = title,
                fontSize = 12.sp,
                modifier = Modifier
                    .constrainAs(titleLabel) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(startGuideline)
                        end.linkTo(button.start)
                        height = Dimension.wrapContent
                        width = Dimension.fillToConstraints
                    }
                    .padding(5.dp)
            )

            Image(
                painter = painterResource(iconDrawable),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start, margin = 8.dp)
                        end.linkTo(startGuideline)
                        width = Dimension.fillToConstraints
                    }
                    .aspectRatio(1f, false)
                    .padding(5.dp)
            )

            IconButton(
                onClick = { contentVisible.value = !contentVisible.value },
                modifier = Modifier
                    .constrainAs(button) {
                        top.linkTo(parent.top)
                        start.linkTo(endGuideline)
                        end.linkTo(parent.end, margin = 8.dp)
                        width = Dimension.fillToConstraints
                    }
                    .aspectRatio(1f, false)
                    .padding(5.dp)
                    .rotate(rotationState)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Drop-down Arrow"
                )
            }


        }

//        AnimatedVisibility(
//            visible = contentVisible.value,
//            enter = slideInVertically(initialOffsetY = { fullHeight -> -fullHeight }),
//            exit = slideOutVertically(targetOffsetY = { fullHeight -> -fullHeight }),
//            modifier = Modifier.zIndex(-1f)
//
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                content()
//            }
//        }
        AnimatedContent(
            targetState = contentVisible.value,
            transitionSpec = {
                 slideIntoContainer(
                     animationSpec = tween(durationMillis = 300, easing = EaseIn),
                     towards = Down
                 ).togetherWith(
                     slideOutOfContainer(
                         animationSpec = tween(durationMillis = 300, easing = EaseOut),
                         towards = Up
                     )
                 )
            }, label = ""
        ) { targetState ->
            if (targetState) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Divider()
                    content()
                }
            }
        }
    }
}

@Composable
private fun WorkerDetailAboutMeSection(
    worker: WorkerBO?,
) {
    WorkerDetailBodySection(
        title = stringResource(id = R.string.about_me),
        iconDrawable = R.mipmap.ic_aboutme
    ) {
        Column (modifier = Modifier
            .padding(16.dp)
            .background(Color.Transparent)
            .fillMaxWidth()
        ) {
            Text(text = worker?.email ?: "")
            Text(text = "From: ${worker?.country}")
            Text(text = "${worker?.age ?: "?"} y/o")
            Text(text = "${worker?.height ?: "?"} cm")
        }
    }
}

@Composable
private fun WorkerDetailDescriptionSection(
    worker: WorkerBO?,
) {
    WorkerDetailBodySection(
        title = stringResource(id = R.string.description),
        iconDrawable = R.mipmap.ic_description
    ) {
        Column (modifier = Modifier
            .padding(16.dp)
            .background(Color.Transparent)
            .fillMaxWidth()
        ) {
            Text(text = worker?.description ?: "")
        }
    }
}

@Composable
private fun WorkerDetailQuotaSection(
    worker: WorkerBO?,
) {
    WorkerDetailBodySection(
        title = stringResource(id = R.string.quota),
        iconDrawable = R.mipmap.ic_info
    ) {
        Column (modifier = Modifier
            .padding(16.dp)
            .background(Color.Transparent)
            .fillMaxWidth()
        ) {
            Text(text = worker?.quota ?: "")
        }
    }
}

@Composable
private fun WorkerDetailPreferencesSection(
    worker: WorkerBO?,
) {
    WorkerDetailBodySection(
        title = stringResource(id = R.string.quota),
        iconDrawable = R.mipmap.ic_info
    ) {
        Column (modifier = Modifier
            .padding(16.dp)
            .background(Color.Transparent)
            .fillMaxWidth()
        ) {
            Text(text = "Food: ${worker?.favorite?.food ?: "???"}")
            Text(text = "Color: ${worker?.favorite?.color ?: "???"}")
            Text(text = "Song:")
            Text(
                text = worker?.favorite?.song ?: "???",
                fontWeight = FontWeight.Light,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .padding(
                        bottom = 16.dp,
                        top = 8.dp
                    )
                    .align(Alignment.CenterHorizontally)
            )
            Text(text = stringResource(id = R.string.random))
            Text(
                text = worker?.favorite?.random_string ?: "???",
                modifier = Modifier.padding(
                    bottom = 16.dp,
                    top = 8.dp
                ),
                fontSize = 10.sp
            )
        }
    }
}


//#endregion

@Preview(showBackground = true)
@Composable
fun WorkerDetailBodySectionPreview() {
    val worker = AphToolbox.DummyWorker(0)
    AppTheme {
        WorkerDetailBody(worker)
    }
}
@Preview(showBackground = true)
@Composable
fun WorkerDetailPreview() {
    AppTheme {
        WorkerDetails(
            true,
            AphToolbox.DummyWorker(),
            {},
            {},
            Modifier
        )
    }
}
@Preview(showBackground = true)
@Composable
fun WorkerDetailHeaderPreview() {
    AppTheme {
        WorkerDetailHeader(
            AphToolbox.DummyWorker(),
        ) {}
    }
}
