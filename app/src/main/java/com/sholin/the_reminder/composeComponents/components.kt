package com.sholin.the_reminder.composeComponents

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import com.sholin.the_reminder.R
import com.sholin.the_reminder.Reminder
import com.sholin.the_reminder.Utils
import com.sholin.the_reminder.ui.theme.ComposeTypography
import com.sholin.the_reminder.ui.theme.Typography

@Composable
fun CustomButton(){
        Button(onClick = {
            //action
        },
            modifier = Modifier.size(250.dp,50.dp),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.purple_200)),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(0.dp,colorResource(R.color.purple_200))
        ) {
            Text(text = "Please Click",color = colorResource(R.color.black), fontSize = 20.sp, textAlign = TextAlign.Center)
        }
    }

@Composable
fun CustomTextField(){

var textValue by remember { mutableStateOf<String?>(null) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .size(250.dp, 50.dp)
            .background(color = colorResource(R.color.white), shape = RoundedCornerShape(12.dp)),
        value = textValue?:"",
        onValueChange = {
            textValue = it
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(Color.Black, Color.Black),
        textStyle = Typography.labelSmall,
        singleLine = true,
        label = {Text(text = "Add Header")}
    )
}


@Composable
fun IconButtonExample() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            text = "Reminder",
            color = colorResource(R.color.black),
            style = ComposeTypography.header
        )

        IconButton(onClick = { /* Handle click */ }) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Add",
                tint = colorResource(R.color.purple_200),
            )
        }

        IconButton(onClick = { /* Handle click */ }) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear",
                tint = colorResource(R.color.purple_200),
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalInput(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun SingleReminderItem(reminder: Reminder,onCheckedChange: (Boolean) -> Unit){
//    val randomColor = remember {
//        Color(
//            red = (0..255).random(),
//            green = (0..255).random(),
//            blue = (0..255).random()
//        )
//    }
    val color = colorResource(R.color.purple_200)


    Column(
        modifier = Modifier
//            .size(150.dp, 120.dp)
            .background(shape = RoundedCornerShape(10.dp), color = color)
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.element_spacing))
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = reminder.header,
            color = colorResource(R.color.black),
            style = ComposeTypography.bodyMedium
        )
        HorizontalDivider(
            modifier = Modifier
                .height(10.dp),
            thickness = 10.dp,
            color = color
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = reminder.description,
            color = colorResource(R.color.black),
            style = ComposeTypography.defaultType.labelSmall
        )
        HorizontalDivider(
            modifier = Modifier
                .height(10.dp),
            thickness = 10.dp,
            color = color
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = reminder.date,
            color = colorResource(R.color.black),
            style = ComposeTypography.defaultType.bodyLarge.copy(fontSize = 22.sp, fontWeight = FontWeight.Bold)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = reminder.time,
            color = colorResource(R.color.black),
            style = ComposeTypography.defaultType.bodyLarge.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
                text = "Enable Reminder",
                style = ComposeTypography.bodyMedium)
            Switch(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                checked = reminder.alarm == true, onCheckedChange = {
                    onCheckedChange(it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(R.color.purple_500),
                    uncheckedThumbColor = colorResource(R.color.purple_200))
            )
        }

        

    }
}



@Preview()
@Composable
fun SingleItemPreview(){
    SingleReminderItem(Reminder(header = "Test", description = "Test am on here on the other hand ", date = "15/12/2026", time = "02:00"), onCheckedChange = {true})
}

@Preview(showBackground = true)
@Composable
fun PreviewDatePicker() {
    DatePickerModalInput(
        onDateSelected ={} ,
        onDismiss = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewIconButton() {
    IconButtonExample()
}



@Preview(showBackground = true)
@Composable
fun TextFiledPreview(){
    CustomTextField()
}


@Preview(showBackground = true)
@Composable
fun AvailableButtonsPreview(){
    CustomButton()
}