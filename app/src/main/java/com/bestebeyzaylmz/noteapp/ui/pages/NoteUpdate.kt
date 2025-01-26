package com.bestebeyzaylmz.noteapp.ui.pages

import android.Manifest
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bestebeyzaylmz.noteapp.data.database.NoteEntity
import com.bestebeyzaylmz.noteapp.ui.viewmodel.NoteViewModel
import com.bestebeyzaylmz.noteapp.ui.widget.NoteInputText
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteUpdate(
    noteEntity: NoteEntity,
    viewModel: NoteViewModel,
    onDismissBottomSheet: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { onDismissBottomSheet.invoke() },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        UpdateBottomSheetContent(
            noteEntity = noteEntity,
            onNoteUpdate = { updatedNote ->
                viewModel.updateNote(updatedNote)
                onDismissBottomSheet.invoke()
            })
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UpdateBottomSheetContent(noteEntity: NoteEntity, onNoteUpdate: (NoteEntity) -> Unit) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var newTitle by remember { mutableStateOf(noteEntity.NoteTitle) }
    var newSubTitle by remember { mutableStateOf(noteEntity.NoteSubtitle) }
    var newDescription by remember { mutableStateOf(noteEntity.NoteDescription) }
    var newImageUri by remember { mutableStateOf(noteEntity.NoteImagePath) }

    //Permission(galeriden resim seçmek için)
    val mediaPermissionState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            listOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    )

    val hasMediaPermission = mediaPermissionState.allPermissionsGranted
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) newImageUri = data.data.toString()
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        NoteInputText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 10.dp),
            text = newTitle,
            label = "Title",
            onTextChanged = {
                if (it.all { char -> char.isLetter() || char.isWhitespace() } && it.length <= 10) {
                    newTitle = it //büyük harf, boşluk ve 10 karakterden fazla olmamalı
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
            text = newSubTitle,
            label = "Subtitle",
            onTextChanged = {
                if (it.all { char -> char.isLetter() || char.isWhitespace() } && it.length <= 30) {
                    newSubTitle = it //büyük harf, boşluk ve 10 karakterden fazla olmamalı
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
            text = newDescription,
            label = "Note description",
            maxLine = 20,
            onTextChanged = {
                if (it.all { char -> char.isLetter() || char.isWhitespace() } && it.length <= 500) {
                    newDescription = it //büyük harf, boşluk ve 10 karakterden fazla olmamalı
                }
            },
            maxChar = 500
        )

        if (newImageUri == null) {
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
                model = Uri.parse(newImageUri!!),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .clickable { newImageUri = null },
                contentDescription = "Uploaded image!!"
            )
        }

        IconButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(60.dp)
                .clip(CircleShape)
                .background(Color.Black)
                .align(Alignment.End),
            onClick = {
                if (newTitle.isNotEmpty() || newSubTitle.isNotEmpty() && newDescription.isNotEmpty()) {
                    val newUpdatedNote = NoteEntity(
                        NoteId = noteEntity.NoteId,
                        NoteTitle = newTitle,
                        NoteSubtitle = newSubTitle,
                        NoteDescription = newDescription,
                        NoteImagePath = newImageUri
                    )
                    onNoteUpdate.invoke(newUpdatedNote)
                    Toast.makeText(context, "Note updated successfully", Toast.LENGTH_LONG).show()
                }
            }) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Update note save icon",
                tint = Color.White
            )
        }
    }
}
