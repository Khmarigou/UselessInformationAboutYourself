package com.example.uselessinformationaboutyourself

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.uselessinformationaboutyourself.ui.theme.UselessInformationAboutYourselfTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            UselessInformationAboutYourselfTheme {
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val colors = MaterialTheme.colorScheme
                val context = LocalContext.current
                var name by remember { mutableStateOf(getName(context)) }

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        Column (
                            modifier = Modifier
                                .background(colors.primary)
                                .fillMaxHeight()
                                .fillMaxWidth(.7f)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Spacer(modifier = Modifier.statusBarsPadding())
                            TextField(
                                value = name,
                                onValueChange = {
                                    name = it
                                    saveName(context, it)
                                },
                                label = { Text("Name") },
                                singleLine = true
                            )
                            DatePickerFieldToModal(context = context)
                        }
                    },
                    scrimColor = Color.Gray,
                ) {

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Useless Infos") },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }) {
                                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                                    }
                                }
                            )
                                 },
                        content = { innerPadding ->
                            Text("Contenu principal", modifier = Modifier.padding(innerPadding))
                        }
                    )
                }
            }
        }
    }
}

private fun getName(context: Context): String {
    val sharedPreferences = context.getSharedPreferences("user_prefs", MODE_PRIVATE)
    return sharedPreferences.getString("name", "") ?: ""
}

private fun saveName(context: Context, name: String) {
    val sharedPreferences = context.getSharedPreferences("user_prefs", MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString("name", name)
        apply()
    }
}

private fun getBirthDate(context: Context): Long? {
    val sharedPreferences = context.getSharedPreferences("user_prefs", MODE_PRIVATE)
    return sharedPreferences.getLong("birth_date", 0L).takeIf { it != 0L }
}

private fun saveBirthDate(context: Context, date: Long?) {
    val sharedPreferences = context.getSharedPreferences("user_prefs", MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putLong("birth_date", date?: 0L)
        apply()
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Composable
fun DatePickerFieldToModal(context: Context, modifier: Modifier = Modifier) {
    val selectedDate by remember { mutableStateOf(getBirthDate(context)) }
    var showModal by remember { mutableStateOf(false) }

    TextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        label = { Text("Birth Date") },
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Birth Date")
        },

        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { saveBirthDate(context, it) },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
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