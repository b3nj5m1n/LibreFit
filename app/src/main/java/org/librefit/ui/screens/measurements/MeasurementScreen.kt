/*
 * Copyright (c) 2025. LibreFit
 *
 * This file is part of LibreFit
 *
 * LibreFit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LibreFit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LibreFit.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.librefit.ui.screens.measurements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.librefit.R
import org.librefit.data.ChartData
import org.librefit.db.entity.Measurement
import org.librefit.ui.components.CustomButton
import org.librefit.ui.components.CustomScaffold
import org.librefit.ui.components.HeadlineText
import org.librefit.ui.components.animations.EmptyLottie
import org.librefit.ui.components.bottomMargin
import org.librefit.ui.components.charts.CustomCartesianChart
import org.librefit.ui.theme.LibreFitTheme
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasurementScreen(
    navigateBack: () -> Unit
) {
    val viewModel: MeasurementScreenViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.getMeasurementsFromDB()
    }


    val date = rememberSaveable { mutableStateOf(LocalDateTime.now()) }

    val datePickerState = rememberDatePickerState()
    val showDatePickerDialog = remember { mutableStateOf(false) }

    if (showDatePickerDialog.value == true) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        date.value = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(
                                datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                            ),
                            ZoneId.systemDefault()
                        )
                        showDatePickerDialog.value = false
                    }
                ) {
                    Text(stringResource(R.string.ok_dialog))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog.value = false }) {
                    Text(stringResource(R.string.cancel_dialog))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    MeasurementScreenContent(
        navigateBack = navigateBack,
        measurements = viewModel.measurements,
        listChartData = viewModel.getListChartData(),
        addMeasurement = viewModel::addMeasurementToDB,
        date = date,
        showDatePickerDialog = showDatePickerDialog
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MeasurementScreenContent(
    navigateBack: () -> Unit,
    measurements: List<Measurement>,
    listChartData: List<ChartData>,
    addMeasurement: (Measurement) -> Unit,
    date: MutableState<LocalDateTime>,
    showDatePickerDialog: MutableState<Boolean>,
) {
    val fullDate: DateTimeFormatter? = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.FULL)
        .withLocale(Locale.getDefault())

    val shortDate: DateTimeFormatter? = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.SHORT)
        .withLocale(Locale.getDefault())


    val notes = rememberSaveable { mutableStateOf("") }
    val bodyWeight = rememberSaveable { mutableStateOf("") }
    val fatMass = rememberSaveable { mutableStateOf("") }
    val leanMass = rememberSaveable { mutableStateOf("") }



    CustomScaffold(
        title = AnnotatedString(stringResource(R.string.measurements)),
        navigateBack = navigateBack
    ) { innerPadding ->
        // This box is used to constrain width in landscape mode
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .widthIn(max = 600.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    CustomCartesianChart(listChartData = listChartData)
                }


                // Add new measurement button
                item {
                    OutlinedCard {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "New measurement",
                                style = MaterialTheme.typography.headlineSmall
                            )

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = notes.value,
                                label = { Text(stringResource(R.string.notes)) },
                                onValueChange = { notes.value = it }
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                                OutlinedTextField(
                                    modifier = Modifier.weight(0.5f),
                                    value = bodyWeight.value,
                                    label = { Text(text = "Body weight *") },
                                    suffix = { Text("kg") },
                                    isError = bodyWeight.value.isBlank(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    onValueChange = {
                                        bodyWeight.value = processFloatValue(it, 20f, 200f)
                                    }
                                )
                                OutlinedTextField(
                                    modifier = Modifier.weight(0.6f),
                                    value = fatMass.value,
                                    trailingIcon = {
                                        IconButton(
                                            onClick = { fatMass.value = "" }
                                        ) {
                                            Icon(
                                                ImageVector.vectorResource(R.drawable.ic_cancel),
                                                null
                                            )
                                        }
                                    },
                                    label = { Text("Fat mass", overflow = TextOverflow.Ellipsis) },
                                    suffix = { Text("%") },
                                    singleLine = true,
                                    isError = leanMass.value.ifEmpty { "0" }.toFloat()
                                            + fatMass.value.ifEmpty { "0" }.toFloat() > 100,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    onValueChange = {
                                        fatMass.value = processFloatValue(it, 0f, 80f)
                                    }
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                                OutlinedTextField(
                                    modifier = Modifier.weight(0.5f),
                                    value = date.value.format(shortDate),
                                    onValueChange = {},
                                    label = { Text(stringResource(R.string.label_when)) },
                                    readOnly = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            showDatePickerDialog.value = true
                                        }) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(R.drawable.ic_date_range),
                                                contentDescription = stringResource(R.string.select_date)
                                            )
                                        }
                                    }
                                )
                                OutlinedTextField(
                                    modifier = Modifier.weight(0.6f),
                                    value = leanMass.value,
                                    trailingIcon = {
                                        IconButton(
                                            onClick = { leanMass.value = "" }
                                        ) {
                                            Icon(
                                                ImageVector.vectorResource(R.drawable.ic_cancel),
                                                null
                                            )
                                        }
                                    },
                                    label = { Text("Lean mass") },
                                    suffix = { Text("%") },
                                    isError = leanMass.value.ifEmpty { "0" }.toFloat()
                                            + fatMass.value.ifEmpty { "0" }.toFloat() < 100,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    onValueChange = {
                                        leanMass.value = processFloatValue(it, 20f, 80f)
                                    }
                                )
                            }

                            CustomButton(
                                text = stringResource(R.string.add),
                                icon = ImageVector.vectorResource(R.drawable.ic_add),
                                enabled = bodyWeight.value.isNotBlank() &&
                                        (leanMass.value.toFloat() + fatMass.value.toFloat() > 100)
                            ) {
                                addMeasurement(
                                    Measurement(
                                        bodyWeight = bodyWeight.value.toFloat(),
                                        bodyFatPercentage = fatMass.value.ifEmpty { "0" }.toFloat(),
                                        muscleMassPercentage = leanMass.value.ifEmpty { "0" }
                                            .toFloat(),
                                        date = date.value,
                                        notes = notes.value
                                    )
                                )
                            }
                        }
                    }
                }


                item { HeadlineText("Past measurements") }

                if (measurements.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            EmptyLottie()
                            Text(
                                text = stringResource(R.string.nothing_to_show),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                items(measurements, key = { it.id }) {
                    ElevatedCard {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("${it.date.format(fullDate)}")
                            }
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_info),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
                bottomMargin()
            }
        }
    }
}

private fun processFloatValue(string: String, min: Float, max: Float): String {
    if (string.isBlank()) return ""

    val stringValue = string
        .replace(",", ".")
        .filter { it.isDigit() || it == '.' }
        .takeLast(5)

    val firstDotIndex = stringValue.indexOf(".")

    var value: String

    if (firstDotIndex == -1) {
        value = stringValue
    } else {
        val beforeFirstDot = stringValue.substring(
            0, firstDotIndex + 1
        )

        val afterFirstDot = stringValue
            .substring(firstDotIndex + 1)
            .replace(".", "")

        value = beforeFirstDot + afterFirstDot
    }

    if (value == ".") value = "0"

    return value.toFloat().coerceIn(min, max).toString()
}

@Preview
@Composable
private fun MeasurementScreenPreview() {
    val shortDate: DateTimeFormatter? = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.SHORT)
        .withLocale(Locale.getDefault())

    // Used to generate random dates
    val from = LocalDateTime.of(2025, 1, 1, 0, 0)
    val to = LocalDateTime.of(2025, 12, 31, 23, 59)

    val fromEpochSecond = from.toEpochSecond(ZoneOffset.UTC)
    val toEpochSecond = to.toEpochSecond(ZoneOffset.UTC)


    val measurements = (0 until 10)
        .map {
            Measurement(
                id = it.toLong(),
                bodyWeight = Random.nextFloat(),
                date = LocalDateTime.ofEpochSecond(
                    Random.nextLong(fromEpochSecond, toEpochSecond),
                    0,
                    ZoneOffset.UTC
                )
            )
        }
        .sortedByDescending { it.date }

    LibreFitTheme(false, true) {
        MeasurementScreenContent(
            navigateBack = {},
            measurements = measurements,
            listChartData = measurements.map {
                ChartData(yValue = it.bodyWeight, xValue = it.date.format(shortDate))
            },
            addMeasurement = {},
            showDatePickerDialog = remember { mutableStateOf(false) },
            date = remember { mutableStateOf(LocalDateTime.now()) }
        )
    }
}