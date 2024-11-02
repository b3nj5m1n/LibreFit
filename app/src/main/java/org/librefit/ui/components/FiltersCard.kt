/*
 * Copyright (c) 2024 LibreFit
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

package org.librefit.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.librefit.R
import org.librefit.data.Category
import org.librefit.data.Equipment
import org.librefit.data.Force
import org.librefit.data.Level
import org.librefit.data.Mechanic
import org.librefit.data.Muscle
import org.librefit.data.SharedViewModel
import org.librefit.util.exerciseEnumToStringId
import kotlin.enums.EnumEntries

@Composable
fun FiltersCard(
    isFilterExpanded: MutableState<Boolean>,
    viewModel: SharedViewModel
) {
    var iconRotation by rememberSaveable { mutableFloatStateOf(0f) }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_filter),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.label_filters),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            IconButton(
                onClick = {
                    isFilterExpanded.value = !isFilterExpanded.value
                    iconRotation = if (isFilterExpanded.value) 180f else 0f
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = Icons.Default.ArrowDropDown.name,
                    modifier = Modifier.rotate(iconRotation)
                )
            }
        }


        val titles = listOf(
            R.string.label_force,
            R.string.label_level,
            R.string.label_mechanic,
            R.string.label_equipment,
            R.string.label_muscles,
            R.string.label_category
        )

        val options = listOf(
            Force.entries,
            Level.entries,
            Mechanic.entries,
            Equipment.entries,
            Muscle.entries,
            Category.entries
        )


        //Animation to display the filters
        AnimatedVisibility(visible = isFilterExpanded.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ItemFilter(stringResource(titles[0]), options[0], viewModel)
                ItemFilter(stringResource(titles[1]), options[1], viewModel)
                ItemFilter(stringResource(titles[2]), options[2], viewModel)
                ItemFilter(stringResource(titles[3]), options[3], viewModel)
                ItemFilter(stringResource(titles[4]), options[4], viewModel)
                ItemFilter(stringResource(titles[5]), options[5], viewModel)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ItemFilter(
    title: String,
    options: EnumEntries<out Enum<*>>,
    viewModel: SharedViewModel
) {
    Spacer(Modifier.height(10.dp))

    Text(title)

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        options.forEach { enum ->

            FilterChip(
                selected = viewModel.isEnumInFilter(enum),
                onClick = {
                    if (viewModel.isEnumInFilter(enum)) {
                        viewModel.removeEnumFromFilter(enum)
                    } else {
                        viewModel.addEnumToFilter(enum)
                    }
                },
                label = {
                    Text(
                        stringResource(exerciseEnumToStringId(enum)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = if (viewModel.isEnumInFilter(enum)) {
                    {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                }
            )
        }
    }
}

@Preview
@Composable
fun FiltersCardPreview() {
    FiltersCard(remember { mutableStateOf(true) }, viewModel())
}