/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (c) 2024-2026. The LibreFit Contributors
 *
 * LibreFit is subject to additional terms covering author attribution and trademark usage;
 * see the ADDITIONAL_TERMS.md and TRADEMARK_POLICY.md files in the project root.
 */

package org.librefit.helpers

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import dagger.hilt.android.qualifiers.ApplicationContext
import org.librefit.R
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages short, low-latency audio playback using [SoundPool].
 */
@Singleton
class SoundPlayer @Inject constructor(
    @ApplicationContext context: Context
) : AutoCloseable {

    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(1)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
        .build()

    private val soundId: Int = soundPool.load(context, R.raw.alert_notification, 1)

    /**
     * Plays the alert sound.
     */
    fun playAlert() {
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    override fun close() {
        soundPool.release()
    }
}
