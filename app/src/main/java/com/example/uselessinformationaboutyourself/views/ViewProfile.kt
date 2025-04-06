package com.example.uselessinformationaboutyourself.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uselessinformationaboutyourself.viewModels.UserViewModel
import com.example.uselessinformationaboutyourself.views.infoViews.AgeDisplay
import com.example.uselessinformationaboutyourself.views.infoViews.HeartDetails
import com.example.uselessinformationaboutyourself.views.infoViews.HeightComparaisons

@Composable
fun ViewScreen(viewModel: UserViewModel, modifier: Modifier = Modifier) {
    val userState = viewModel.user.collectAsState()
    val user = userState.value
    val colors = MaterialTheme.colorScheme

    user?.let { user ->
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val componentModifier = Modifier
                .background(color = colors.secondaryContainer, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
            AgeDisplay(
                user.birthDate,
                modifier = componentModifier
            )
            HeightComparaisons(
                user.height,
                modifier = componentModifier
            )
            HeartDetails(
                user.birthDate,
                modifier = componentModifier
            )
        }
    } ?: Text("Aucune donnée enregistrée", modifier = Modifier.padding(16.dp))
}