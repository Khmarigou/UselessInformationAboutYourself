package com.example.uselessinformationaboutyourself.views.infoViews

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.uselessinformationaboutyourself.ui.components.TextTitle
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

@Composable
fun HeartDetails(
    birthDateString : String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        val BPM = 70 // Example heart rate
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val inputDate = LocalDate.parse(birthDateString, formatter)
        var currentTime by remember { mutableStateOf(LocalDateTime.now()) }
        val totalSecond = currentTime.second + currentTime.minute * 60 + currentTime.hour * 3600
        val totalBeatsToday = totalSecond * BPM / 60
        val totalBeatsLife = ChronoUnit.SECONDS.between(inputDate.atStartOfDay(), currentTime) * BPM / 60


        // Met à jour chaque seconde
        LaunchedEffect(Unit) {
            while (true) {
                delay(1000)  // Attend 1 seconde
                currentTime = LocalDateTime.now()
            }
        }

        TextTitle("Votre rythme cardiaque")
        Text("• Votre coeur a battu ${"%,d".format(totalBeatsToday).replace(',', ' ')} fois aujourd'hui")
        Text("• Votre coeur a battu ${"%,d".format(totalBeatsLife).replace(',', ' ')} fois depuis votre naissance")

    }
}

fun getCurrentTime(): String {
    val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Date())
}