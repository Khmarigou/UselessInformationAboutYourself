package com.example.uselessinformationaboutyourself.views

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.uselessinformationaboutyourself.viewModels.UserViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EditScreen(viewModel: UserViewModel, modifier: Modifier, onSave: () -> Unit = {}) {
    val user by viewModel.user.collectAsState()

    // Initialise les états avec les valeurs récupérées
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val currentFocus = LocalFocusManager.current

    // Quand user est dispo (chargé depuis la DB), on met à jour les champs si vides
    LaunchedEffect(user) {
        user?.let {
            if (name.isBlank()) name = it.name
            if (birthDate.isBlank()) birthDate = it.birthDate
            if (height.isBlank()) height = it.height.toString()
        }
    }

    Column (modifier = modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Votre profil")
        TextField(value = name, onValueChange = { name = it }, label = { Text("Nom") })
        DatePickerField(
            birthDate = birthDate,
            onClick = { showDatePicker = true }
        )
        TextField(value = height, onValueChange = { height = it }, label = { Text("Taille (cm)") })

        Button(onClick = {
            viewModel.saveUser(name, birthDate, height.toIntOrNull() ?: 0)
            onSave()
            currentFocus.clearFocus()
        }) {
            Text("Enregistrer")
        }
        if (showDatePicker) {
            DatePickerModal(
                initialDate = convertDateToMillisOrNull(birthDate),
                onDateSelected = {
                    it?.let { birthDate = convertMillisToDate(it) }
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

@Composable
fun DatePickerField(
    birthDate: String,
    onClick: () -> Unit
) {
    TextField(
        value = birthDate,
        onValueChange = {},
        label = { Text("Date de naissance") },
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Date de naissance")
        },
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val up = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (up != null) onClick()
                }
            }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    initialDate: Long? = null,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate,
        initialDisplayMode = DisplayMode.Input
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@SuppressLint("SimpleDateFormat")
fun convertMillisToDate(millis: Long): String {
    val format = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return format.format(Date(millis))
}

@SuppressLint("SimpleDateFormat")
fun convertDateToMillisOrNull(date: String): Long? {
    return try {
        val format = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        format.parse(date)?.time
    } catch (_: Exception) {
        null
    }
}