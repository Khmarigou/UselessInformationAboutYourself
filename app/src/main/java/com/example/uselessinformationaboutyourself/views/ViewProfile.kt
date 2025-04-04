package com.example.uselessinformationaboutyourself.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uselessinformationaboutyourself.viewModels.UserViewModel
import com.example.uselessinformationaboutyourself.views.infoViews.AgeDisplay

@Composable
fun ViewScreen(viewModel: UserViewModel, modifier: Modifier = Modifier) {
    val userState = viewModel.user.collectAsState()
    val user = userState.value

    user?.let { user ->
        Column(modifier = modifier.padding(16.dp)) {
            AgeDisplay(user.birthDate)
        }
    } ?: Text("Aucune donnée enregistrée", modifier = Modifier.padding(16.dp))
}