package com.example.uselessinformationaboutyourself.views.infoViews

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.uselessinformationaboutyourself.ui.components.TextTitle

@Composable
fun HeightComparaisons(height: Int, modifier: Modifier = Modifier) {
    val heightInM = height / 100.0
    val heightEiffelTower = 330 // in meters
    val heightStatueOfLiberty = 93 // in meters
    val heightEmpireStateBuilding = 443 // in meters
    val heightBurjKhalifa = 828 // in meters

    Column (
        modifier = modifier
    ) {
        TextTitle("Votre taille")
        Text("• On peut vous placer ${"%.2f".format(heightEiffelTower / heightInM)} fois dans la Tour Eiffel")
        Text("• On peut vous placer ${"%.2f".format(heightStatueOfLiberty / heightInM)} fois dans la Statue de la Liberté")
        Text("• On peut vous placer ${"%.2f".format(heightEmpireStateBuilding / heightInM)} fois dans l'Empire State Building")
        Text("• On peut vous placer ${"%.2f".format(heightBurjKhalifa / heightInM)} fois dans le Burj Khalifa")
    }
}
