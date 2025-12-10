package com.expense.financemanager.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expense.financemanager.domain.model.Period

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodSelector(
    selectedPeriod: Period,
    onPeriodSelected: (Period) -> Unit,
    onCustomDateRangeSelected: (Long, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Period",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PeriodChip(
                    text = "Day",
                    selected = selectedPeriod == Period.DAY,
                    onClick = { onPeriodSelected(Period.DAY) },
                    modifier = Modifier.weight(1f)
                )
                PeriodChip(
                    text = "Week",
                    selected = selectedPeriod == Period.WEEK,
                    onClick = { onPeriodSelected(Period.WEEK) },
                    modifier = Modifier.weight(1f)
                )
                PeriodChip(
                    text = "Month",
                    selected = selectedPeriod == Period.MONTH,
                    onClick = { onPeriodSelected(Period.MONTH) },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PeriodChip(
                    text = "Year",
                    selected = selectedPeriod == Period.YEAR,
                    onClick = { onPeriodSelected(Period.YEAR) },
                    modifier = Modifier.weight(1f)
                )
                PeriodChip(
                    text = "Custom",
                    selected = selectedPeriod == Period.CUSTOM,
                    onClick = { showDatePicker = true },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PeriodChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { 
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium
            )
        },
        modifier = modifier
    )
}
