package com.example.new_campus_teamup.screens.profilescreens

import androidx.compose.ui.unit.sp

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.ui.theme.IconColor
import com.example.new_campus_teamup.viewmodels.UserProfileViewModel


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SkillsCard(userProfileViewModel: UserProfileViewModel) {

    var pressed by remember { mutableStateOf(false) }

    val userSkillsList = userProfileViewModel.skills.collectAsState()
    var showEditSkillDialog  = remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if(pressed) 0.97f else 1f
    )

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showEditSkillDialog.value) {
        ModalBottomSheet(
            onDismissRequest = {
                showEditSkillDialog.value = false
            },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            AddSkillsBottomSheet(
                userSkillsList = userSkillsList,
                onDismiss = {
                    showEditSkillDialog.value = false
                },
                userProfileViewModel = userProfileViewModel
            )
        }
    }

    Card(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false

                    }
                )
            }
            .fillMaxWidth(0.9f)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFbce3f6)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {


            DetailsCardHeading(true, onEditButtonClick = {
                showEditSkillDialog.value = true
            } , "Skills")


            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                userSkillsList.value.forEach { skill ->
                    SkillChip(skill, false)//false means don't need to show remove icon
                }
            }
        }
    }
}

@Composable
fun SkillChip(skill: String,showRemoveIcon : Boolean ,  onSkillRemove : (String) -> Unit = {}) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = skill,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
            if(showRemoveIcon){
                Icon(painter = painterResource(R.drawable.rejectbtn), contentDescription = null , modifier = Modifier.padding(end = 6.dp)
                    .size(16.dp)
                    .clickable {
                        onSkillRemove(skill)
                    }, tint = Color.Black)
            }

        }

    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddSkillsBottomSheet(
    userSkillsList: State<List<String>>,
    onDismiss: () -> Unit,
    userProfileViewModel: UserProfileViewModel
) {
    // Local state for the editing session
    val selectedSkills = remember { mutableStateListOf<String>().apply { addAll(userSkillsList.value) } }
    var searchQuery by remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }

    // Mock suggestions - In a real app, this might come from a ViewModel/Repo
    val allSuggestions = remember {
        listOf(
            "Flutter", "Java", "AWS", "Python", "Figma", "TypeScript",
            "React", "Node.js", "UI Design", "Kotlin", "Swift", "C++",
            "Go", "Rust", "Docker", "Kubernetes", "GraphQL"
        )
    }

    // Filter suggestions: (All - Selected) AND (Matches Search)
    val suggestedSkills = remember(searchQuery, selectedSkills.toList()) {
        allSuggestions.filter { candidate ->
            !selectedSkills.contains(candidate) &&
                    candidate.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp), // Add padding for bottom sheet
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Header ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Add Skills",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // --- Search Bar ---
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text("Search for skills", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF3F4F6),
                unfocusedContainerColor = Color(0xFFF3F4F6),
                disabledContainerColor = Color(0xFFF3F4F6),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            singleLine = true
        )

        // --- Selected Skills ---
        if (selectedSkills.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SELECTED SKILLS",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Surface(
                    color = Color(0xFFE0E7FF),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "${selectedSkills.size} Selected",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF3730A3),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedSkills.forEach { skill ->
                    SelectedSkillChip(skill = skill, onRemove = { selectedSkills.remove(skill) })
                }
            }
        }

        // --- Suggested Skills ---
        Text(
            text = "SUGGESTED SKILLS",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestedSkills.forEach { skill ->
                SuggestedSkillChip(skill = skill, onAdd = { selectedSkills.add(skill) })
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        // --- Save Button ---
        androidx.compose.material3.Button(
            onClick = {
                isLoading.value = true
                userProfileViewModel.saveSkills(selectedSkills, onSuccess = {
                    isLoading.value = false
                    onDismiss()
                })
            },
            modifier = Modifier
                .fillMaxWidth()
                .size(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2563EB) // Blue color
            ),
            shape = RoundedCornerShape(25.dp)
        ) {
            if (isLoading.value) {
                Text("Saving...", color = Color.White)
            } else {
                Text("Save Skills", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun SelectedSkillChip(skill: String, onRemove: () -> Unit) {
    Surface(
        color = Color(0xFF2563EB), // Blue
        shape = RoundedCornerShape(50),
        modifier = Modifier.clickable { onRemove() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = skill,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                tint = Color.White,
                modifier = Modifier
                    .size(16.dp)
                    .graphicsLayer { alpha = 0.8f }
            )
        }
    }
}

@Composable
fun SuggestedSkillChip(skill: String, onAdd: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(50),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
        color = Color.White,
        modifier = Modifier.clickable { onAdd() }
    ) {
        Text(
            text = skill,
            color = Color.Black, // Dark gray text
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}


