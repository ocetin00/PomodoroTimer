package com.oguzhancetin.pomodoro.presentation.screen.task

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.oguzhancetin.pomodoro.domain.model.Category
import java.util.UUID

//previewCategoryAlertDialog
@Composable
@Preview
fun CategoryDialogPreview() {
    CategoryDialog(
        category = Category(
            id = UUID.randomUUID(),
            name = "Category 1",
        ),
        onUpsertCategory = {},
        onDismisRequest = {},
        onDeleteClick = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDialog(
    modifier: Modifier = Modifier,
    category: Category?,
    onUpsertCategory: (Category) -> Unit,
    onDeleteClick: (Category) -> Unit,
    onDismisRequest: (Boolean) -> Unit,
) {

    val txtFieldError = remember { mutableStateOf("") }
    val txtField = remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = { onDismisRequest(false) },
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = category?.let { "Update Category" } ?: "Add Category",
                        )
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "",
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { onDismisRequest(false) }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(
                                    width = 2.dp,
                                    color = if (txtFieldError.value.isEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
                                ),
                                shape = RoundedCornerShape(50)
                            ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        leadingIcon = {
                            /**
                             *    Icon(
                             *                                 imageVector = Icons.Filled.T,
                             *                                 contentDescription = "",
                             *                                 tint = MaterialTheme.colorScheme.primary,
                             *                                 modifier = Modifier
                             *                                     .width(20.dp)
                             *                                     .height(20.dp)
                             *                             )
                             */
                        },
                        placeholder = { Text(text = category?.name ?: "Enter Category Title") },
                        value = txtField.value,
                        onValueChange = {
                            txtField.value = it.take(10)
                        })

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        Button(
                            onClick = {
                                if (txtField.value.isEmpty()) {
                                    txtFieldError.value = "Field can not be empty"
                                    return@Button
                                }
                                onUpsertCategory(
                                    Category(
                                        id = category?.id ?: UUID.randomUUID(),
                                        name = txtField.value
                                    )
                                )

                            },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Done")
                        }
                    }
                    category?.let {
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Delete Category",
                                color = Color.Red,
                                modifier = Modifier.clickable {
                                    onDeleteClick(category)
                                }
                            )

                        }
                    }
                }
            }
        }
    }
}