package com.example.campus_teamup.helper


import androidx.compose.foundation.background

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.campus_teamup.myThemes.TextFieldStyle

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CustomDropdown(
    label: String = "Select",
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    backgroundColor: androidx.compose.ui.graphics.Color,
    textColor: androidx.compose.ui.graphics.Color,
    leadingIcon: Int
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            value = selectedOption,
            shape = TextFieldStyle.defaultShape,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            leadingIcon = {
                leadingIcon?.let { iconRes ->
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = textColor
                    )
                }
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                unfocusedIndicatorColor = backgroundColor,
                focusedIndicatorColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                focusedContainerColor = backgroundColor
            )
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.background(backgroundColor)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option, color = textColor) },
                    onClick = {
                        onOptionSelected(option)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

