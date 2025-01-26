package com.bestebeyzaylmz.noteapp.ui.pages

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bestebeyzaylmz.noteapp.data.database.NoteEntity
import com.bestebeyzaylmz.noteapp.ui.navigation.AppScreen
import com.bestebeyzaylmz.noteapp.ui.viewmodel.NoteViewModel
import com.bestebeyzaylmz.noteapp.ui.widget.DeleteAlertDialog
import com.bestebeyzaylmz.noteapp.ui.widget.NoteInputText
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NoteAdd(
    viewModel: NoteViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var noteTitle by remember { mutableStateOf("") }
    var noteSubTitle by remember { mutableStateOf("") }
    var noteDescription by remember { mutableStateOf("") }
    var noteImageUri by remember { mutableStateOf<String?>(null) }

    //Image için alert dialog
    var showImageAlertDialog by remember { mutableStateOf(false) }
    if (showImageAlertDialog) {
        DeleteAlertDialog(
            onDismissRequest = { showImageAlertDialog = false },
            onConfirmation = { noteImageUri = null },
            dialogTitle = "Sil",
            dialogText = "Nota eklenen görsel silinsin mi?"
        )
    }

    //Permission(galeriden resim seçmek için)
    val mediaPermissionState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            listOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(READ_MEDIA_IMAGES)
        } else listOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
    )

    val hasMediaPermission = mediaPermissionState.allPermissionsGranted
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) noteImageUri = data.data.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = AppScreen.ADD_NOTE.topBarName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Add note back icon"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (noteTitle.isNotEmpty() && noteSubTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                                viewModel.addNote(
                                    NoteEntity(
                                        NoteTitle = noteTitle,
                                        NoteSubtitle = noteSubTitle,
                                        NoteDescription = noteDescription,
                                        NoteImagePath = noteImageUri
                                    )
                                )
                                noteTitle = ""
                                noteSubTitle = ""
                                noteDescription = ""
                                noteImageUri = null
                                Toast.makeText(context, "Note added!", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Add note save icon"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        //ekranı oluşturucak componentler
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            NoteInputText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                text = noteTitle,
                label = "Title",
                onTextChanged = {
                    if (it.all { char -> char.isLetter() || char.isWhitespace() } && it.length <= 10) {
                        noteTitle = it //büyük harf, boşluk ve 10 karakterden fazla olmamalı
                    }
                    if (it.length > 10) {
                        focusManager.moveFocus(FocusDirection.Down) //10. karakterden 11 e geçmek istediğinde karakter sayısı dolduğu için subtitle a yazmaya devam eder
                    }
                },
                maxChar = 10
            )
            NoteInputText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                text = noteSubTitle,
                label = "Subtitle",
                onTextChanged = {
                    if (it.all { char -> char.isLetter() || char.isWhitespace() } && it.length <= 30) {
                        noteSubTitle = it //büyük harf, boşluk ve 10 karakterden fazla olmamalı
                    }
                    if (it.length > 30) {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                },
                maxChar = 30
            )
            NoteInputText(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                text = noteDescription,
                label = "Note description",
                maxLine = 20,
                onTextChanged = {
                    if (it.all { char -> char.isLetter() || char.isWhitespace() } && it.length <= 500) {
                        noteDescription = it //büyük harf, boşluk ve 10 karakterden fazla olmamalı
                    }
                },
                maxChar = 500
            )

            if (noteImageUri == null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, //her iki ucta yer alması için
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBox,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Image icon"
                        )
                        Text(
                            text = "./root/noteimage.jpg",
                            modifier = Modifier.padding(horizontal = 10.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    IconButton(onClick = {
                        if (hasMediaPermission) {
                            val intent = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            ) //görsel seçme
                            galleryLauncher.launch(intent)
                        } else {
                            mediaPermissionState.launchMultiplePermissionRequest()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .clip(
                                    CircleShape
                                )
                                .padding(8.dp),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Image upload icon"
                        )
                    }
                }
            } else {
                AsyncImage(
                    model = Uri.parse(noteImageUri!!),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .clickable { showImageAlertDialog = true },
                    contentDescription = "Uploaded image!!"
                )
            }
        }
    }
}