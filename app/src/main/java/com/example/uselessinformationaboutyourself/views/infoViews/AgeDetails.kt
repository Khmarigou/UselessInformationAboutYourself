package com.example.uselessinformationaboutyourself.views.infoViews

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun AgeDisplay(birthDateString: String) {
    //TODO: Add hours/minutes/seconds
    val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    val inputDate = LocalDate.parse(birthDateString, formatter)
    val today = LocalDate.now()

    val daysDifference = ChronoUnit.DAYS.between(inputDate, today)
    val weeksDifference = ChronoUnit.WEEKS.between(inputDate, today)
    val monthsDifference = ChronoUnit.MONTHS.between(inputDate, today)
    val yearsDifference = ChronoUnit.YEARS.between(inputDate, today)

    Text("Vous avez $yearsDifference ans")
    Text("ou $monthsDifference mois")
    Text("ou $weeksDifference semaines")
    Text("ou $daysDifference jours")
}