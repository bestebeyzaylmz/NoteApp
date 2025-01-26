package com.bestebeyzaylmz.noteapp.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bestebeyzaylmz.noteapp.ui.component.NoteListView
import com.bestebeyzaylmz.noteapp.ui.navigation.AppScreen
import com.bestebeyzaylmz.noteapp.ui.viewmodel.NoteViewModel
import com.bestebeyzaylmz.noteapp.ui.widget.DeleteAlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteList(
    viewModel: NoteViewModel,
    navController: NavController,
    isDark: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val noteList = viewModel.noteListState.collectAsState().value

    //dropdownMenu
    var showMenu by remember { mutableStateOf(false) }

    //searchbar
    var isSearchState by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    //alertDialog
    var showDeleteAllAlertDialog by remember { mutableStateOf(false) }
    if (showDeleteAllAlertDialog) {
        DeleteAlertDialog(
            onDismissRequest = { showDeleteAllAlertDialog = false },
            onConfirmation = { viewModel.removeAllNotes() },
            dialogTitle = "Tümünü Sil",
            dialogText = "Tüm notları silmek istediğinize emin misiniz?"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchState) {
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = searchText,
                            onValueChange = {
                                searchText = it
                                viewModel.searchNote(it)
                            },
                            textStyle = TextStyle(
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            label = {
                                Text(text = "Search")
                            },
                            colors = TextFieldColors(
                                focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                                unfocusedSupportingTextColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = Color.Transparent,
                                disabledLabelColor = Color.Transparent,
                                disabledLeadingIconColor = Color.Transparent,
                                disabledPlaceholderColor = Color.Transparent,
                                disabledPrefixColor = Color.Transparent,
                                disabledSuffixColor = Color.Transparent,
                                disabledSupportingTextColor = Color.Transparent,
                                disabledTextColor = Color.Transparent,
                                disabledTrailingIconColor = Color.Transparent,
                                errorContainerColor = Color.Transparent,
                                errorCursorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                                errorLabelColor = Color.Transparent,
                                errorLeadingIconColor = Color.Transparent,
                                errorPlaceholderColor = Color.Transparent,
                                errorPrefixColor = Color.Transparent,
                                errorSuffixColor = Color.Transparent,
                                errorSupportingTextColor = Color.Transparent,
                                errorTextColor = Color.Transparent,
                                errorTrailingIconColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                focusedLeadingIconColor = Color.Transparent,
                                focusedPlaceholderColor = Color.Transparent,
                                focusedPrefixColor = Color.Transparent,
                                focusedSuffixColor = Color.Transparent,
                                focusedSupportingTextColor = Color.Transparent,
                                focusedTextColor = Color.Transparent,
                                focusedTrailingIconColor = Color.Transparent,
                                textSelectionColors = LocalTextSelectionColors.current,
                                unfocusedContainerColor = Color.Transparent,
                                unfocusedLabelColor = Color.Transparent,
                                unfocusedLeadingIconColor = Color.Transparent,
                                unfocusedPlaceholderColor = Color.Transparent,
                                unfocusedPrefixColor = Color.Transparent,
                                unfocusedSuffixColor = Color.Transparent,
                                unfocusedTextColor = Color.Transparent,
                                unfocusedTrailingIconColor = Color.Transparent
                            ),
                            maxLines = 1,
                            singleLine = true
                        )
                    } else {
                        Text(
                            text = AppScreen.NOTE_LIST.topBarName,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    if (isSearchState) {
                        IconButton(onClick = {
                            isSearchState = false
                            viewModel.getAllNotes()
                            searchText = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                contentDescription = "Search close icon"
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            isSearchState = true
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                contentDescription = "Search icon"
                            )
                        }
                        IconButton(onClick = {
                            showMenu = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                contentDescription = "More vertical icon"
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = {
                            showMenu = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Tümünü sil",
                                    fontSize = 15.sp
                                )
                            },
                            onClick = {
                                if (noteList.isNotEmpty()) showDeleteAllAlertDialog = true
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Tema değiştir",
                                    fontSize = 15.sp
                                )
                            },
                            onClick = {
                                onThemeChange.invoke(isDark)
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(AppScreen.ADD_NOTE.name)
                },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                text = {
                    Text(text = "Add Note")
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add note icon"
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            NoteListView(noteList = noteList, viewModel = viewModel)
        }
    }
}