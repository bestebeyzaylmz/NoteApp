package com.bestebeyzaylmz.noteapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bestebeyzaylmz.noteapp.ui.pages.NoteAdd
import com.bestebeyzaylmz.noteapp.ui.pages.NoteList
import com.bestebeyzaylmz.noteapp.ui.viewmodel.NoteViewModel

@Composable
fun MainNavigation(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val controller = rememberNavController()
    val noteViewModel: NoteViewModel = hiltViewModel()

    NavHost(navController = controller, startDestination = AppScreen.NOTE_LIST.name) {
        composable(AppScreen.NOTE_LIST.name) {
            NoteList(
                viewModel = noteViewModel,
                navController = controller,
                isDark = isDarkTheme,
                onThemeChange = onThemeChange
            )
        }
        composable(AppScreen.ADD_NOTE.name) {
            NoteAdd(
                viewModel = noteViewModel,
                navController = controller
            )
        }
    }
}