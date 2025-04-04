package com.example.uselessinformationaboutyourself.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.uselessinformationaboutyourself.viewModels.UserViewModel

@Composable
fun EditScreen(viewModel: UserViewModel, modifier: Modifier, onSave: () -> Unit = {}) {
    val user by viewModel.user.collectAsState()

    // Initialise les états avec les valeurs récupérées
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
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
        TextField(value = birthDate, onValueChange = { birthDate = it }, label = { Text("Date de naissance") })
        TextField(value = height, onValueChange = { height = it }, label = { Text("Taille (cm)") })

        Button(onClick = {
            viewModel.saveUser(name, birthDate, height.toIntOrNull() ?: 0)
            onSave()
            currentFocus.clearFocus()
        }) {
            Text("Enregistrer")
        }
    }
}