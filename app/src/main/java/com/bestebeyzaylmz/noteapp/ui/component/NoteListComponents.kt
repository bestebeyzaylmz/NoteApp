package com.bestebeyzaylmz.noteapp.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bestebeyzaylmz.noteapp.data.database.NoteEntity
import com.bestebeyzaylmz.noteapp.ui.pages.NoteUpdate
import com.bestebeyzaylmz.noteapp.ui.viewmodel.NoteViewModel
import com.bestebeyzaylmz.noteapp.ui.widget.DeleteAlertDialog

@Composable
fun NoteListView(
    noteList: List<NoteEntity>,
    viewModel: NoteViewModel
) {
    //ikili recyclerview
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        reverseLayout = false //en son kaydedilen en üstte olması için
    ) {
        items(noteList) { note ->
            NoteRow(
                noteEntity = note,
                viewModel = viewModel
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteRow(
    modifier: Modifier = Modifier,
    noteEntity: NoteEntity,
    viewModel: NoteViewModel
) {
    //delete alert dialog
    var showAlertDiaglog by remember { mutableStateOf(false) }
    if (showAlertDiaglog) {
        DeleteAlertDialog(
            onDismissRequest = { showAlertDiaglog = false },
            onConfirmation = { viewModel.removeNote(noteEntity) },
            dialogTitle = "Sil",
            dialogText = "${noteEntity.NoteTitle} notunu silmek istediğinize emin misiniz?"
        )
    }

    //update bottom sheet
    var showBottonSheet by remember { mutableStateOf(false) }
    if (showBottonSheet) {
        NoteUpdate(
            noteEntity = noteEntity,
            viewModel = viewModel
        ) {
            showBottonSheet = false
        }
    }

    Card(
        modifier = modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(15.dp))
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {},
                    onLongClick = {
                        showBottonSheet = true
                    }
                )
                .padding(vertical = 8.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            //aynı satırda iki kolon açacak
            Column(
                modifier = Modifier.weight(0.8F),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = noteEntity.NoteTitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Text(
                    text = noteEntity.NoteSubtitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = noteEntity.NoteEntryDate,
                    modifier = Modifier.padding(horizontal = 1.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.LightGray
                )
            }
            Column(
                modifier = Modifier.weight(0.2F),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = {
                    showAlertDiaglog = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete note icon"
                    )
                }
            }
        }
    }
}

