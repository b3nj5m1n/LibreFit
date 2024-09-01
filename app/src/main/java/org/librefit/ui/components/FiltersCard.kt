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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.librefit.R
import org.librefit.data.SharedViewModel
import org.librefit.util.stringToEnum

@Composable
fun FiltersCard(
    isFilterExpanded: MutableState<Boolean>,
    viewModel: SharedViewModel
) {
    var iconRotation by remember { mutableFloatStateOf(0f) }

    OutlinedCard (
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
    ){
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ){
            Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)){
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_filter),
                    contentDescription = ""
                )
                Text(text = "Filters", style = MaterialTheme.typography.headlineSmall)
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

        val titles = listOf("Force","Level","Mechanic","Equipment","Primary Muscles","Secondary Muscles","Category")
        val options = listOf(
            listOf("Static", "Pull", "Push"),
            listOf("Beginner", "Intermediate", "Expert"),
            listOf("Isolation", "Compound"),
            listOf("Medicine ball","Dumbbell","Body only","Bands","Kettlebells",
                "Foam roll","Cable","Machine","Barbell","Exercise ball","E-z curl bar","Other"
            ),
            listOf("Abdominals","Abductors","Adductors","Biceps","Calves","Chest",
                "Forearms","Glutes","Hamstrings","Lats","Lower back","Middle back",
                "Neck","Quadriceps","Shoulders","Traps","Triceps"
            ),
            listOf("Abdominals","Abductors","Adductors","Biceps","Calves","Chest",
                "Forearms","Glutes","Hamstrings","Lats","Lower back","Middle back",
                "Neck","Quadriceps","Shoulders","Traps","Triceps"
            ),
            listOf("Powerlifting","Strength","Stretching","Cardio","Olympic weightlifting","Strongman","Plyometrics")
        )


        //Animation to display the filters
        AnimatedVisibility(visible = isFilterExpanded.value) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (i in titles.indices){
                    ItemFilter(title = titles[i], options = options[i], viewModel = viewModel)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemFilter(
    title: String,
    options: List<String>,
    viewModel: SharedViewModel
) {

    Text(title)

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        options.forEach { option ->
            val enum = stringToEnum(option)

            FilterChip(
                selected = viewModel.isEnumInList(enum!!),
                onClick = {
                    if (viewModel.isEnumInList(enum)){
                        viewModel.removeEnum(enum)
                    } else {
                        viewModel.addEnum(enum)
                    }
                },
                label = { Text(text = option, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                leadingIcon = if (viewModel.isEnumInList(enum)) {
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
fun FiltersCardPreview(){
    FiltersCard(remember { mutableStateOf(true) }, viewModel())
}