package com.aph.willywonka.ui.view.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import coil.compose.AsyncImage
import com.aph.willywonka.R
import com.aph.willywonka.data.model.WorkerBO
import com.aph.willywonka.ui.materialDesing.AppTheme
import com.aph.willywonka.utils.AphToolbox.Companion.DummyWorker
import com.aph.willywonka.utils.AphToolbox.Companion.GetGenderIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerItemListComposable(
    worker: WorkerBO,
    modifier: Modifier = Modifier,
    onClickAction: (WorkerBO) -> Unit,
) {
    Card(
        onClick = { onClickAction(worker) },
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .background(color = Color.White)
            .padding(8.dp),
        border = BorderStroke(1.dp, colorResource(id = R.color.accent_color_borders)),
        shape = RoundedCornerShape(
            topStart = CornerSize(percent = 50),
            topEnd = CornerSize(10.dp),
            bottomStart = CornerSize(percent = 50),
            bottomEnd = CornerSize(10.dp),
        )
    )
    {
        Row(
            modifier = modifier
                .background(color = Color.White)
                .padding(8.dp)
        ) {
            Box(  modifier = Modifier.weight(1f).align(Alignment.CenterVertically)) {
                AsyncImage(
                    model = worker.image,
                    placeholder = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = stringResource(id = R.string.workerItemList_avatar_content_description),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(1f)
                        .clip(shape = CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }
            Column(
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp),
            ) {
                Row {
                    Text(
                        text = "${worker.first_name} ${worker.last_name}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                    )
                    Image(
                        painter = painterResource(GetGenderIcon(worker.gender)),
                        contentDescription = stringResource(id = R.string.workerItemList_gender_content_description),
                        modifier = Modifier.height(21.dp),
                    )
                }
                Text(
                    text = "${worker.profession}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = "${worker.email}",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.ExtraLight,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }

}

//#region Previews
@Preview(showBackground = true)
@Composable
fun WorkerItemPreview() {
    AppTheme {
        WorkerItemListComposable(DummyWorker()){}
    }
}

@Preview(showBackground = true)
@Composable
fun WorkerItemListPreview() {
    AppTheme {
        LazyColumn(content = {
            items(3) {
                WorkerItemListComposable(DummyWorker()){}
            }
        })
    }
}
//#endregion