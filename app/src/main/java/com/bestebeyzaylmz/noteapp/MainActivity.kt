package com.bestebeyzaylmz.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.bestebeyzaylmz.noteapp.ui.navigation.MainNavigation
import com.bestebeyzaylmz.noteapp.ui.theme.NoteAppDarkTheme
import com.bestebeyzaylmz.noteapp.ui.theme.NoteAppLightTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint //activity inject edilebilir hale getirildi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDark by remember { mutableStateOf(false) }

            MainContent(isDarkTheme = isDark) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainNavigation(isDarkTheme = isDark) {
                        isDark = !isDark //tema değiştire tıklayığında dark ise light ligh ise dark görünmesi için
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent(isDarkTheme: Boolean, onToggleTheme: @Composable () -> Unit) {
    //MainContent içerisindeki diğer bileşenler
    if (isDarkTheme) NoteAppDarkTheme { onToggleTheme.invoke() }
    else NoteAppLightTheme { onToggleTheme.invoke() }
}