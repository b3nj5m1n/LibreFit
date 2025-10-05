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

package org.librefit.di

import android.app.Activity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.librefit.activities.MainActivity
import org.librefit.di.qualifiers.MainActivityClass

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @MainActivityClass
    fun provideMainActivityClass(): Class<out Activity> {
        return MainActivity::class.java
    }
}