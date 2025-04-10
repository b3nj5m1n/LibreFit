/*
 * Copyright (c) 2024-2025. LibreFit
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

package org.librefit.ui.screens.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.librefit.R
import org.librefit.ui.components.CustomButton
import org.librefit.ui.components.CustomScaffold
import org.librefit.ui.components.MarkdownText
import org.librefit.ui.components.bottomMargin
import org.librefit.ui.components.dialogs.UrlActionDialog
import org.librefit.ui.theme.LibreFitTheme

@Composable
fun LicenseScreen(navigateBack: () -> Unit) {
    val licenseText = rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        licenseText.value = context.resources.openRawResource(R.raw.license)
            .bufferedReader()
            .use { it.readText() }
    }


    val url = remember { mutableStateOf("") }

    UrlActionDialog(url)

    CustomScaffold(
        title = AnnotatedString(stringResource(id = R.string.license)),
        navigateBack = navigateBack,
    ) { innerPadding ->
        // This box is used to constrain width in landscape mode
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .padding(start = 10.dp, end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                item {
                    CustomButton(
                        text = stringResource(R.string.view_online_version),
                        icon = Icons.AutoMirrored.Default.ExitToApp,
                        onClick = {
                            url.value = context.getString(R.string.url_gpl3)
                        }
                    )
                }

                item {
                    MarkdownText(licenseText.value)
                }

                bottomMargin()
            }
        }
    }
}

@Preview
@Composable
private fun LicenseScreenPreview() {
    LibreFitTheme(false, true) {
        LicenseScreen { }
    }
}