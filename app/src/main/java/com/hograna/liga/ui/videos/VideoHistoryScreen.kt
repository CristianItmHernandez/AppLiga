package com.hograna.liga.ui.videos

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.hograna.liga.data.local.entity.VideoHighlightEntity
import com.hograna.liga.ui.theme.Background
import com.hograna.liga.ui.theme.CardBackground
import com.hograna.liga.ui.theme.ErrorRed
import com.hograna.liga.ui.theme.GreenNeon
import com.hograna.liga.ui.theme.Surface
import com.hograna.liga.ui.theme.SurfaceVariant
import com.hograna.liga.ui.theme.TextDisabled
import com.hograna.liga.ui.theme.TextPrimary
import com.hograna.liga.ui.theme.TextSecondary

private enum class VideoSource(val label: String, val storedValue: String) {
    DRIVE("Google Drive", "GOOGLE_DRIVE"),
    LOCAL("Teléfono", "LOCAL_URI")
}

@Composable
fun VideoHistoryScreen(viewModel: VideoViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var selectedVideo by remember { mutableStateOf<VideoHighlightEntity?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var player by remember { mutableStateOf<ExoPlayer?>(null) }
    var playbackError by remember { mutableStateOf<String?>(null) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    if (player == null) player = ExoPlayer.Builder(context).build()
                }
                Lifecycle.Event.ON_STOP -> {
                    player?.release()
                    player = null
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            player = ExoPlayer.Builder(context).build()
        }
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player?.release()
            player = null
        }
    }

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                playbackError = error.message ?: "No se pudo reproducir el vídeo"
            }
        }
        player?.addListener(listener)
        onDispose { player?.removeListener(listener) }
    }

    LaunchedEffect(player, selectedVideo) {
        val currentPlayer = player ?: return@LaunchedEffect
        val video = selectedVideo ?: return@LaunchedEffect
        playbackError = null
        currentPlayer.setMediaItem(MediaItem.fromUri(video.videoUrlOrUri))
        currentPlayer.prepare()
        currentPlayer.playWhenReady = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Historial de vídeos", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = GreenNeon)
            ) {
                Text("Agregar", color = Background, fontWeight = FontWeight.Bold)
            }
        }

        selectedVideo?.let { video ->
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "${video.homeTeamName} ${video.scoreSummary} ${video.awayTeamName}",
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            if (video.scorersList.isNotBlank()) {
                Text("Goleadores: ${video.scorersList}", color = TextSecondary, fontSize = 12.sp)
            }
            if (player != null) {
                AndroidView(
                    factory = { viewContext ->
                        PlayerView(viewContext).apply {
                            useController = true
                            this.player = player
                        }
                    },
                    update = { it.player = player },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(top = 8.dp)
                )
            }
        }

        playbackError?.let {
            Text(it, color = ErrorRed, fontSize = 12.sp, modifier = Modifier.padding(top = 6.dp))
        }

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GreenNeon)
            }
        } else if (uiState.videos.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "Todavía no hay vídeos guardados.\nAgrega un enlace público de Drive o un vídeo del teléfono.",
                    color = TextDisabled,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.videos, key = { it.id }) { video ->
                    VideoHighlightCard(
                        video = video,
                        isSelected = selectedVideo?.id == video.id,
                        onPlay = { selectedVideo = video },
                        onDelete = { viewModel.deleteVideo(video) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddVideoDialog(
            onDismiss = { showAddDialog = false },
            onSave = {
                viewModel.addVideo(it)
                showAddDialog = false
            }
        )
    }

    uiState.error?.let { message ->
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Snackbar(
                action = {
                    TextButton(onClick = viewModel::clearError) {
                        Text("OK", color = GreenNeon)
                    }
                },
                containerColor = SurfaceVariant
            ) {
                Text(message, color = TextPrimary)
            }
        }
    }
}

@Composable
private fun VideoHighlightCard(
    video: VideoHighlightEntity,
    isSelected: Boolean,
    onPlay: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${video.homeTeamName} ${video.scoreSummary} ${video.awayTeamName}",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                if (video.scorersList.isNotBlank()) {
                    Text(
                        "Goleadores: ${video.scorersList}",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
                Text(
                    if (video.videoSourceType == VideoSource.DRIVE.storedValue) "Google Drive" else "Vídeo local",
                    color = TextDisabled,
                    fontSize = 11.sp
                )
            }
            IconButton(onClick = onPlay) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = if (isSelected) "Reproducir seleccionado" else "Reproducir",
                    tint = GreenNeon
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = ErrorRed)
            }
        }
    }
}

@Composable
private fun AddVideoDialog(
    onDismiss: () -> Unit,
    onSave: (VideoHighlightEntity) -> Unit
) {
    val context = LocalContext.current
    var homeTeam by remember { mutableStateOf("") }
    var awayTeam by remember { mutableStateOf("") }
    var score by remember { mutableStateOf("") }
    var scorers by remember { mutableStateOf("") }
    var driveUrl by remember { mutableStateOf("") }
    var source by remember { mutableStateOf(VideoSource.DRIVE) }
    var localVideoUri by remember { mutableStateOf<Uri?>(null) }
    var formError by remember { mutableStateOf<String?>(null) }

    val videoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                localVideoUri = it
                formError = null
            } catch (error: SecurityException) {
                formError = error.message ?: "No se pudo conservar el acceso al vídeo"
            }
        }
    }

    val driveStream = driveStreamUrl(driveUrl)
    val valid = homeTeam.isNotBlank() &&
        awayTeam.isNotBlank() &&
        score.isNotBlank() &&
        when (source) {
            VideoSource.DRIVE -> driveStream != null
            VideoSource.LOCAL -> localVideoUri != null
        }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface,
        title = { Text("Agregar vídeo de gol", color = TextPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 520.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                VideoTextField("Equipo local", homeTeam) { homeTeam = it }
                VideoTextField("Equipo visitante", awayTeam) { awayTeam = it }
                VideoTextField("Marcador final", score) { score = it }
                VideoTextField("Goleadores", scorers) { scorers = it }

                VideoSource.entries.forEach { option ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = source == option, onClick = { source = option })
                        TextButton(onClick = { source = option }) {
                            Text(option.label, color = TextPrimary)
                        }
                    }
                }

                if (source == VideoSource.DRIVE) {
                    VideoTextField("Enlace compartido de Drive", driveUrl) { driveUrl = it }
                    if (driveUrl.isNotBlank() && driveStream == null) {
                        Text(
                            "Usa un enlace de archivo de drive.google.com accesible para quien reproduzca.",
                            color = ErrorRed,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    TextButton(
                        onClick = {
                            videoPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                            )
                        }
                    ) {
                        Text(
                            localVideoUri?.lastPathSegment ?: "Seleccionar vídeo",
                            color = GreenNeon
                        )
                    }
                }

                formError?.let {
                    Text(it, color = ErrorRed, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                enabled = valid,
                onClick = {
                    val url = when (source) {
                        VideoSource.DRIVE -> driveStream ?: return@Button
                        VideoSource.LOCAL -> localVideoUri?.toString() ?: return@Button
                    }
                    onSave(
                        VideoHighlightEntity(
                            homeTeamName = homeTeam.trim(),
                            awayTeamName = awayTeam.trim(),
                            scoreSummary = score.trim(),
                            scorersList = scorers.trim(),
                            videoSourceType = source.storedValue,
                            videoUrlOrUri = url
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenNeon)
            ) {
                Text("Guardar", color = Background, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = TextSecondary) }
        }
    )
}

@Composable
private fun VideoTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = TextDisabled) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GreenNeon,
            unfocusedBorderColor = TextDisabled,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
        )
    )
}

private fun driveStreamUrl(raw: String): String? {
    val uri = Uri.parse(raw.trim())
    val host = uri.host?.lowercase()
    if (uri.scheme?.lowercase() != "https" || host !in setOf("drive.google.com", "docs.google.com")) {
        return null
    }
    val idFromPath = Regex("/file/d/([A-Za-z0-9_-]+)").find(uri.path.orEmpty())
        ?.groupValues
        ?.getOrNull(1)
    val fileId = uri.getQueryParameter("id") ?: idFromPath ?: return null
    if (!fileId.matches(Regex("[A-Za-z0-9_-]+"))) return null
    return "https://drive.google.com/uc?export=download&id=$fileId"
}
