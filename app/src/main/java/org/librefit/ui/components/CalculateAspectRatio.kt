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
 *
 * LibreFit is subject to additional terms covering author attribution and
 * trademark usage, as found in the accompanying ADDITIONAL_TERMS.md file.
 */

package org.librefit.ui.components

import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalResources

/**
 * A Composable function that reads a drawable's dimensions and returns its aspect ratio without loading the full bitmap into memory.
 * This project uses it to ensure correct sizes of async images in lazy columns in order to correctly set visible items on first composition.
 *
 * @param id The resource ID of the drawable.
 * @return The aspect ratio (width / height) of the drawable.
 */
@Composable
fun rememberDrawableAspectRatio(@DrawableRes id: Int): Float {
    val resources = LocalResources.current

    return remember(id) {
        // Create options and set inJustDecodeBounds to true which prevents memory allocation for the pixels.
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        // It will not return a bitmap, but it will populate the outWidth and outHeight properties in 'options'.
        BitmapFactory.decodeResource(resources, id, options)

        val width = options.outWidth
        val height = options.outHeight

        // Calculate and return the aspect ratio. Add a check to prevent division by zero.
        if (height > 0) {
            width.toFloat() / height.toFloat()
        } else {
            1f // Default to a square aspect ratio as a fallback
        }
    }
}