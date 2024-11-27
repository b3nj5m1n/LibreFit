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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.librefit.R


/**
 * A scaffold used by multiple screen to avoid boilerplate and to ensure a consistent design.
 * @param title Title to be displayed in the [TopAppBar]
 * @param navigateBack This is performed by clicking the navigation icon in the [TopAppBar] (it should be a back stack)
 * @param action This is performed by clicking the action in the [TopAppBar]
 * @param actionEnabled Controls the enabled state of the [action]
 * @param actionIcon Icon displayed in the [FloatingActionButton].
 * @param elevatedActionIcon If true, it elevates the [actionIcon] colors
 * @param actionDescription It should be passed to describe the [action]
 * @param fabAction This is performed by clicking the [FloatingActionButton]
 * @param fabIcon Icon displayed in the [FloatingActionButton]
 * @param fabDescription It should be passed to describe the [fabAction]
 * @param content content of the screen. The lambda receives a [PaddingValues] that should be
 * applied to the content root via [Modifier.padding] and [Modifier.consumeWindowInsets] to
 * properly offset top and bottom bars. If using [Modifier.verticalScroll], apply this modifier to
 * the child of the scroll, and not on the scroll itself.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffold(
    title: String,
    navigateBack: () -> Unit = {},
    action: () -> Unit = {},
    actionEnabled: Boolean = true,
    actionIcon: ImageVector? = null,
    elevatedActionIcon: Boolean = false,
    actionDescription: String? = null,
    fabAction: () -> Unit = {},
    fabIcon: ImageVector? = null,
    fabDescription: String? = null,
    content: @Composable ((PaddingValues) -> Unit),
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = title)
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.label_navigate_back)
                        )
                    }
                },
                actions = {
                    if (actionIcon != null) {
                        IconButton(
                            onClick = action,
                            enabled = actionEnabled,
                            colors = if (elevatedActionIcon) IconButtonDefaults.filledIconButtonColors() else IconButtonDefaults.iconButtonColors()
                        ) {
                            Icon(
                                imageVector = actionIcon,
                                contentDescription = actionDescription
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            if (fabIcon != null) {
                FloatingActionButton(
                    onClick = fabAction
                ) {
                    Icon(
                        imageVector = fabIcon,
                        contentDescription = fabDescription
                    )
                }
            }
        }
    ) {
        content(it)
        Spacer(Modifier.height(100.dp))
    }
}