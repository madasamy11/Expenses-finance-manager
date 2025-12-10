package com.expense.financemanager.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.expense.financemanager.data.local.dao.CategoryTotal
import com.expense.financemanager.domain.model.Category
import java.text.NumberFormat
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CategoryTotalsChart(
    categoryTotals: List<CategoryTotal>,
    categories: List<Category>,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale.getDefault())
    }
    
    val total = categoryTotals.sumOf { it.total }
    val chartData = categoryTotals.map { categoryTotal ->
        val category = categories.find { it.id == categoryTotal.categoryId }
        ChartData(
            category = category,
            amount = categoryTotal.total,
            percentage = if (total > 0) (categoryTotal.total / total * 100).toFloat() else 0f
        )
    }
    
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Category Breakdown",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            if (chartData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No data available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // Simple pie chart
                PieChart(
                    data = chartData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
                
                // Legend
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    chartData.forEach { data ->
                        LegendItem(
                            color = Color(data.category?.color ?: 0xFF888888),
                            label = data.category?.name ?: "Unknown",
                            amount = currencyFormatter.format(data.amount),
                            percentage = String.format("%.1f%%", data.percentage)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PieChart(
    data: List<ChartData>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val canvasSize = size.minDimension
        val radius = canvasSize / 2.5f
        val centerX = size.width / 2
        val centerY = size.height / 2
        
        var startAngle = -90f
        
        data.forEach { chartData ->
            val sweepAngle = (chartData.percentage / 100f) * 360f
            val color = Color(chartData.category?.color ?: 0xFF888888)
            
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = Offset(centerX - radius, centerY - radius),
                size = Size(radius * 2, radius * 2)
            )
            
            startAngle += sweepAngle
        }
        
        // Draw white circle in center for donut effect
        drawCircle(
            color = Color.White,
            radius = radius * 0.6f,
            center = Offset(centerX, centerY)
        )
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    amount: String,
    percentage: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .padding(2.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(color = color)
                }
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = amount,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = percentage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private data class ChartData(
    val category: Category?,
    val amount: Double,
    val percentage: Float
)
