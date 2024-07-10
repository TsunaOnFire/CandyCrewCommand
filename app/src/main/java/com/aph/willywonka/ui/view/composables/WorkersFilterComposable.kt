package com.aph.willywonka.ui.view.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonDefaults.elevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aph.willywonka.R
import com.aph.willywonka.ui.materialDesing.AppTheme

data class WorkerGenderOptionVO(
    val key: String? = null,
    @StringRes
    val genderText: Int = R.string.gender_both,
    @DrawableRes
    val image: Int = R.mipmap.ic_both
)

val genderList = listOf(
    WorkerGenderOptionVO(
        key = "M",
        genderText = R.string.gender_male,
        image = R.mipmap.ic_male
    ),
    WorkerGenderOptionVO(
        key = null,
        genderText = R.string.gender_both,
        image = R.mipmap.ic_both
    ),
    WorkerGenderOptionVO(
        key = "F",
        genderText = R.string.gender_female,
        image = R.mipmap.ic_female
    )
)

@Composable
fun WorkerFilterComposable(
    modifier: Modifier = Modifier,
    selectedGenderKeyValue: String? = null,
    inputProfessionValue: String? = "",
    onSearchClicked: (inputProfession: String?, selectedGenderKey: String?) -> Unit,
) {

    var selectedGenderKey by remember { mutableStateOf<String?>(selectedGenderKeyValue) }
    var inputProfession by remember { mutableStateOf(inputProfessionValue) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        ProfessionFilter(
            value = inputProfession
        ) { inputProfession = it }

        GenderFilter(
            genderValueSelected = selectedGenderKeyValue
        ) {
            selectedGenderKey = it
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                keyboardController?.hide()
                onSearchClicked(inputProfession, selectedGenderKey)
            },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ),
        ){
            Text(
                text = stringResource(id = R.string.filter_button_search).uppercase(),
                color = MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun ProfessionFilter(value: String?, onValueChangedAction: (String) -> Unit) {
    Column {
        Text(
            text = stringResource(id = R.string.filter_profession),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(Alignment.CenterHorizontally),
        )
        TextField(
            value = value ?: "",
            onValueChange = { onValueChangedAction(it) },
            label = { Text(text = stringResource(id = R.string.profession)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun GenderFilter(genderValueSelected:String? = genderList[1].key ,onValueChangedAction: (String?) -> Unit) {

    var selectedOption by remember { mutableStateOf(genderList.find { it.key == genderValueSelected } ?: genderList[1]) }

    Column {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.filter_gender),
            fontWeight = FontWeight.Bold,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            genderList.forEach { workerGenderOption ->
                RadioButtonGender(workerGenderOption,
                    isSelected = workerGenderOption == selectedOption,
                    onClickEvent = {
                        selectedOption = it
                        onValueChangedAction(selectedOption.key)
                    },
                    modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun RadioButtonGender(
    gender: WorkerGenderOptionVO,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClickEvent: (WorkerGenderOptionVO) -> Unit
) {
    val shape = RoundedCornerShape(15.dp)

    val keyboardController = LocalSoftwareKeyboardController.current

    Button(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .widthIn(max = 100.dp)
            .clip(shape)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        onClick = {
            keyboardController?.hide()
            onClickEvent(gender)
        },
        shape = shape,
        colors = ButtonDefaults.outlinedButtonColors(),
        border = BorderStroke(
            width = if (isSelected) 3.dp else 1.dp,
            color =
            if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.secondary,
        ),
        elevation = elevation(
            pressedElevation = 0.dp,
            defaultElevation = if(isSelected) 0.dp else 4.dp
        )

    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(gender.image),
                contentDescription = "",
                modifier = Modifier.aspectRatio(1f),
            )
            Text(
                text = stringResource(gender.genderText),
                modifier = Modifier
                    .padding(
                        vertical = 8.dp,
                    )
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

//#region Previews
@Preview(showBackground = true)
@Composable
private fun ProfessionFilterPreview() {
    AppTheme {
        ProfessionFilter("", {})
    }
}

@Preview(showBackground = true)
@Composable
private fun RadioButtonGenderPreview() {
    AppTheme {
        RadioButtonGender(
            WorkerGenderOptionVO(
                key = null,
                genderText = R.string.gender_both,
                image = R.mipmap.ic_both
            ),
            isSelected = true,
            onClickEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GenderFilterPreview() {
    AppTheme {
        GenderFilter{}
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkerFilterPreview() {
    AppTheme {
        WorkerFilterComposable{ _, _ -> }
    }
}
//#endregion