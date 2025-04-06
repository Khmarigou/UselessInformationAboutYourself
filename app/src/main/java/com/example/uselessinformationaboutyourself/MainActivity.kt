package com.example.uselessinformationaboutyourself

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.uselessinformationaboutyourself.ui.theme.UselessInformationAboutYourselfTheme
import com.example.uselessinformationaboutyourself.viewModels.UserViewModel
import com.example.uselessinformationaboutyourself.views.EditScreen
import com.example.uselessinformationaboutyourself.views.ViewScreen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = UserViewModel(application)
        enableEdgeToEdge()
        setContent {

            UselessInformationAboutYourselfTheme {
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val colors = MaterialTheme.colorScheme
                val currentFocus = LocalFocusManager.current

                LaunchedEffect(drawerState) {
                    snapshotFlow { drawerState.currentValue }
                        .collectLatest { state ->
                            if (state == DrawerValue.Closed) {
                                currentFocus.clearFocus()
                            }
                        }
                }

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            drawerContentColor = colors.onPrimaryContainer,
                            drawerContainerColor = colors.primaryContainer,
                            modifier = Modifier
                                .background(colors.primaryContainer)
                                .fillMaxHeight()
                                .fillMaxWidth(.7f)
                                .pointerInput(Unit) {
                                    awaitEachGesture {
                                        awaitFirstDown(pass = PointerEventPass.Initial)
                                        val up = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                        if (up != null) {
                                            currentFocus.clearFocus()
                                        }
                                    }
                                }
                        ) {
                            EditScreen(viewModel,
                                onSave = {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                }
                            )
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
                            ViewScreen(viewModel, modifier = Modifier.padding(innerPadding))
                        }
                    )
                }
            }
        }
    }
}