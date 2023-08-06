package com.akira.pisowifitimer.feature_history.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val timeInfos = viewModel.timeInfos.value
    val totalAmount = viewModel.totalAmount.value
    val startDate = viewModel.startDate.value
    val endDate = viewModel.endDate.value

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Text(
                text = "Total Amount is ₱$totalAmount", style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Start Date is $startDate", style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "End Date is $endDate", style = MaterialTheme.typography.bodySmall
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(timeInfos.size) {
                    HistoryItem(
                        timeInfo = timeInfos[it],
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}