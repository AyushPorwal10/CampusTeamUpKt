package com.example.new_campus_teamup.helper


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.ui.theme.BorderColor



@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CustomDropdown(
    canEdit: Boolean,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    backgroundColor: Color,
    textColor: Color,
    leadingIcon: Int
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded  && canEdit},
        modifier = Modifier.border(1.dp , BorderColor ,TextFieldStyle.defaultShape )
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
                Icon(
                    painter = painterResource(id = leadingIcon),
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(22.dp)
                )
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

