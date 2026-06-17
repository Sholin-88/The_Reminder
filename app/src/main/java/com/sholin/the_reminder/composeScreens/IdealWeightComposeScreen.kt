package com.sholin.the_reminder.composeScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.sholin.the_reminder.IdealWeightViewModel
import com.sholin.the_reminder.R
import com.sholin.the_reminder.ui.theme.ComposeTypography
import com.sholin.the_reminder.ui.theme.Typography

@Composable
fun FindIdealWeight(viewModel: IdealWeightViewModel, innerPaddingValues: PaddingValues) {

    val height by viewModel.height.collectAsState("")
    val gender by viewModel.gender.collectAsState("")
    val idealWeight by viewModel.idealWeight.collectAsState("")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = innerPaddingValues.calculateTopPadding())
            .background(color = colorResource(R.color.white)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            text = "Find Ideal Weight",
            style = ComposeTypography.header,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().height(70.dp).padding(horizontal = 10.dp),
                value = height,
                onValueChange =  { viewModel.setHeight(it) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black),
                textStyle = Typography.labelMedium,
                singleLine = true,
                label = { Text("Height (cm)")  }
            )
            HorizontalDivider(modifier = Modifier.height(10.dp), thickness = 0.dp, color = Color.Transparent)

            Row {
                RadioButton(
                    selected = gender == "Male",
                    onClick = { viewModel.setGender("Male") }
                )
                Text("Male")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = gender == "Female",
                    onClick = { viewModel.setGender("Female") }
                )
                Text("Female")
            }

            Button(onClick = {
              viewModel.calculateIdealWeight()
            }) {
                Text("Calculate")
            }

            if (idealWeight.isNotEmpty()) {
                Text(
                    idealWeight, style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black,)
            }
        }

    }
}
