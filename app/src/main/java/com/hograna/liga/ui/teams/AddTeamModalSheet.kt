package com.hograna.liga.ui.teams

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.hograna.liga.R
import com.hograna.liga.data.catalog.CatalogTeam
import com.hograna.liga.data.catalog.PredefinedTeamsCatalog
import com.hograna.liga.ui.theme.Background
import com.hograna.liga.ui.theme.CardBackground
import com.hograna.liga.ui.theme.GreenNeon
import com.hograna.liga.ui.theme.Surface
import com.hograna.liga.ui.theme.SurfaceVariant
import com.hograna.liga.ui.theme.TextDisabled
import com.hograna.liga.ui.theme.TextPrimary
import com.hograna.liga.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTeamModalSheet(
    onDismiss: () -> Unit,
    viewModel: AddTeamViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("UCL", "Europa League", "Libertadores", "Personalizado")

    var searchQuery by remember { mutableStateOf("") }
    var captainName by remember { mutableStateOf("") }
    var customTeamName by remember { mutableStateOf("") }
    var customCrestUri by remember { mutableStateOf<Uri?>(null) }
    var selectedCatalogTeam by remember { mutableStateOf<CatalogTeam?>(null) }
    var pickerError by remember { mutableStateOf<String?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                customCrestUri = it
                pickerError = null
            } catch (error: SecurityException) {
                pickerError = error.message ?: "No se pudo conservar el acceso al escudo"
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Surface
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = stringResource(R.string.add_team),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))

            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Background,
                contentColor = GreenNeon,
                edgePadding = 0.dp
            ) {
                tabs.forEachIndexed { index, label ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            searchQuery = ""
                            selectedCatalogTeam = null
                        },
                        text = {
                            Text(
                                label,
                                color = if (selectedTab == index) GreenNeon else TextDisabled
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (selectedTab < 3) {
                // Catalog view
                val catalogList = when (selectedTab) {
                    0 -> PredefinedTeamsCatalog.UCL
                    1 -> PredefinedTeamsCatalog.UEL
                    else -> PredefinedTeamsCatalog.LIBERTADORES
                }
                val filtered = catalogList.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(stringResource(R.string.search_teams), color = TextDisabled) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = textFieldColors()
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(modifier = Modifier.height(280.dp)) {
                    items(filtered, key = { it.name }) { catalogTeam ->
                        val alreadyAdded = uiState.registeredTeamNames.contains(catalogTeam.name)
                        CatalogTeamRow(
                            team = catalogTeam,
                            isRegistered = alreadyAdded,
                            isSelected = selectedCatalogTeam?.name == catalogTeam.name,
                            onClick = {
                                if (!alreadyAdded) selectedCatalogTeam = catalogTeam
                            }
                        )
                    }
                }
            } else {
                // Custom team view
                OutlinedTextField(
                    value = customTeamName,
                    onValueChange = { customTeamName = it },
                    label = { Text(stringResource(R.string.team_name), color = TextDisabled) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = textFieldColors()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceVariant)
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        .padding(12.dp)
                ) {
                    if (customCrestUri != null) {
                        AsyncImage(
                            model = customCrestUri,
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Background),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = TextSecondary)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        if (customCrestUri != null) "Escudo seleccionado ✓" else "Seleccionar escudo del equipo",
                        color = if (customCrestUri != null) GreenNeon else TextSecondary
                    )
                }
            }

            (pickerError ?: uiState.error)?.let { error ->
                Text(
                    error,
                    color = Color(0xFFE74C3C),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Captain name — always required
            OutlinedTextField(
                value = captainName,
                onValueChange = { captainName = it },
                label = { Text(stringResource(R.string.captain_name), color = TextDisabled) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                colors = textFieldColors()
            )

            Spacer(modifier = Modifier.height(16.dp))

            val isValid = captainName.isNotBlank() && when {
                selectedTab < 3 -> selectedCatalogTeam != null
                else -> customTeamName.isNotBlank() && customCrestUri != null
            }

            Button(
                onClick = {
                    if (!isValid) return@Button
                    scope.launch {
                        val result = if (selectedTab < 3) {
                            val ct = selectedCatalogTeam ?: return@launch
                            viewModel.addTeamFromCatalog(ct, captainName)
                        } else {
                            viewModel.addCustomTeam(
                                name = customTeamName.trim(),
                                captainName = captainName.trim(),
                                crestUri = customCrestUri?.toString().orEmpty()
                            )
                        }
                        if (result.isFailure) {
                            pickerError = result.exceptionOrNull()?.message ?: "No se pudo registrar el equipo"
                            return@launch
                        }
                        sheetState.hide()
                        onDismiss()
                    }
                },
                enabled = isValid && !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = GreenNeon)
            ) {
                Text(
                    stringResource(R.string.confirm),
                    color = Background,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CatalogTeamRow(
    team: CatalogTeam,
    isRegistered: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                when {
                    isSelected -> GreenNeon.copy(alpha = 0.15f)
                    isRegistered -> SurfaceVariant.copy(alpha = 0.4f)
                    else -> Color.Transparent
                }
            )
            .clickable(enabled = !isRegistered, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = team.crestUrl,
            contentDescription = team.name,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = team.name,
            color = if (isRegistered) TextDisabled else TextPrimary,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )
        if (isRegistered) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = GreenNeon,
                modifier = Modifier.size(18.dp)
            )
        } else if (isSelected) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = GreenNeon,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GreenNeon,
    unfocusedBorderColor = TextDisabled,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    cursorColor = GreenNeon,
    focusedLabelColor = GreenNeon
)
