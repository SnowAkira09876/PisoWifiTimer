package com.akira.pisowifitimer.feature_history.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.akira.pisowifitimer.feature_timer.domain.model.TimeInfo

@Composable
fun HistoryItem(
    timeInfo: TimeInfo,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            "₱${timeInfo.amount}",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            "${timeInfo.date}",
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            "${timeInfo.time}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}