package com.akira.pisowifitimer.feature_timer.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.akira.pisowifitimer.core.util.Screen
import com.akira.pisowifitimer.feature_timer.presentation.TimerWorker.Companion.AMOUNT
import com.akira.pisowifitimer.feature_timer.presentation.TimerWorker.Companion.DATE
import com.akira.pisowifitimer.feature_timer.presentation.TimerWorker.Companion.HOUR
import com.akira.pisowifitimer.feature_timer.presentation.TimerWorker.Companion.MINUTE
import com.akira.pisowifitimer.feature_timer.presentation.TimerWorker.Companion.SECOND
import com.akira.pisowifitimer.feature_timer.presentation.TimerWorker.Companion.TIME
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun TimerBottomSheet(
    navController: NavController,
    workManager: WorkManager,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (title, alarm, history, stop, sliders) = createRefs()
        var hour by remember { mutableStateOf(0f) }
        var minute by remember { mutableStateOf(0f) }
        var second by remember { mutableStateOf(0f) }
        var amount by remember { mutableStateOf(0f) }

        Text(
            "Time Picker", Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
            style = MaterialTheme.typography.titleLarge
        )

        IconButton(onClick = { }, modifier = Modifier.constrainAs(alarm) {
            top.linkTo(title.top)
            bottom.linkTo(title.bottom)
            start.linkTo(title.end)
            end.linkTo(history.start)
        }) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Alarm",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
        }

        IconButton(
            onClick = { navController.navigate(Screen.HistoryScreen.route) },
            modifier = Modifier.constrainAs(history) {
                top.linkTo(title.top)
                bottom.linkTo(title.bottom)
                start.linkTo(alarm.end)
                end.linkTo(stop.start)
            }) {
            Icon(
                Icons.Filled.List,
                contentDescription = "History",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
        }

        IconButton(onClick = { }, modifier = Modifier.constrainAs(stop) {
            top.linkTo(title.top)
            bottom.linkTo(title.bottom)
            start.linkTo(history.end)
            end.linkTo(parent.end)
        }) {
            Icon(
                Icons.Filled.Clear,
                contentDescription = "Stop",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
        }

        Column(modifier = Modifier
            .padding(top = 10.dp)
            .constrainAs(sliders) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(title.bottom)
            }) {
            Text(
                text = "Hour is ${hour.toInt()}", style = MaterialTheme.typography.bodySmall
            )
            Slider(
                value = hour,
                onValueChange = { value -> hour = value },
                valueRange = 0f..23f,
                steps = 23
            )

            Text(
                text = "Minute is ${minute.toInt()}", style = MaterialTheme.typography.bodySmall
            )
            Slider(
                value = minute,
                onValueChange = { value -> minute = value },
                valueRange = 0f..59f,
                steps = 59
            )

            Text(
                text = "Second is ${second.toInt()}", style = MaterialTheme.typography.bodySmall
            )
            Slider(
                value = second,
                onValueChange = { value -> second = value },
                valueRange = 0f..59f,
                steps = 59
            )

            Text(
                text = "Amount is ₱${amount.toInt()}", style = MaterialTheme.typography.bodySmall
            )
            Slider(
                value = amount,
                onValueChange = { value -> amount = value },
                valueRange = 0f..100f,
            )

            Text(
                text = "No running task", style = MaterialTheme.typography.bodySmall
            )

            Button(
                onClick = {
                    val dateFormat = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())
                    val currentDate: String = dateFormat.format(Date())
                    val formattedTime =
                        "${hour.toInt()} hours, ${minute.toInt()} minutes, ${second.toInt()} seconds"

                    val data: Data =
                        Data.Builder()
                            .putInt(HOUR, hour.toInt())
                            .putInt(MINUTE, minute.toInt())
                            .putInt(SECOND, second.toInt())
                            .putString(AMOUNT, amount.toInt().toString())
                            .putString(TIME, formattedTime)
                            .putString(DATE, currentDate).build()

                    val request =
                        OneTimeWorkRequest.Builder(TimerWorker::class.java).setInputData(data)
                            .build()

                    workManager.enqueueUniqueWork(
                        "TIMER_WORK_TAG", ExistingWorkPolicy.REPLACE, request
                    )
                }, modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Start")
            }
        }
    }
}