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

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.librefit.data.ChartData
import org.librefit.db.entity.Measurement
import org.librefit.db.repository.MeasurementRepository
import org.librefit.enums.chart.MeasurementChart
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MeasurementScreenViewModel @Inject constructor(
    private val measurementRepository: MeasurementRepository
) : ViewModel() {

    val measurements = mutableStateListOf<Measurement>()

    val shortDate: DateTimeFormatter? =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(
            Locale.getDefault()
        )

    fun getListChartData(): List<ChartData> {
        return measurements
            .filter {
                when (measurementChart.value) {
                    MeasurementChart.BODY_WEIGHT -> it.bodyWeight != 0f
                    MeasurementChart.FAT_MASS -> it.bodyFatPercentage != 0f
                    MeasurementChart.LEAN_MASS -> it.muscleMassPercentage != 0f
                }
            }
            .map {
                ChartData(
                    yValue = when (measurementChart.value) {
                        MeasurementChart.BODY_WEIGHT -> it.bodyWeight
                        MeasurementChart.FAT_MASS -> it.bodyFatPercentage
                        MeasurementChart.LEAN_MASS -> it.muscleMassPercentage
                    },
                    xValue = it.date.format(shortDate)
                )
            }
    }

    fun upsertMeasurementToDB(measurement: Measurement) {
        viewModelScope.launch(Dispatchers.IO) {
            measurementRepository.upsertMeasurement(measurement)
        }
    }

    fun deleteMeasurementById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            measurementRepository.deleteById(id)
        }
    }

    fun getMeasurementsFromDB() {
        viewModelScope.launch {
            measurementRepository.getAllMeasurements()
                .distinctUntilChanged()
                .collect {
                    measurements.clear()
                    measurements.addAll(it)
                }
        }
    }


    private var measurementChart = mutableStateOf(MeasurementChart.BODY_WEIGHT)

    fun updateMeasurementChart(newMeasurementChart: MeasurementChart) {
        measurementChart.value = newMeasurementChart
    }

    fun getMeasurementChart(): MeasurementChart {
        return measurementChart.value
    }

}