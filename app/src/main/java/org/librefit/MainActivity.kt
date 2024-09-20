package org.librefit

import org.librefit.data.ExerciseDeserializer
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import org.librefit.data.ExerciseDC
import org.librefit.data.SharedViewModel
import org.librefit.nav.NavigationHost
import org.librefit.ui.theme.LibreFitTheme
import org.librefit.data.DataStoreManager
import java.io.BufferedReader
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private lateinit var userPreferences: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        userPreferences = DataStoreManager(this)

        var list = emptyList<ExerciseDC>()

        lifecycleScope.launch {
            list = loadExercises(resources.openRawResource(R.raw.exercises))
        }

        setContent {
            LibreFitTheme(userPreferences){
                NavigationHost(
                    list = list,
                    userPreferences = userPreferences
                )
            }
        }
    }
}

private fun loadExercises(inputStream: InputStream) : List<ExerciseDC> {
    val gson = GsonBuilder()
        .registerTypeAdapter(ExerciseDC::class.java, ExerciseDeserializer())
        .create()
    val jsonString = inputStream.bufferedReader().use(BufferedReader::readText)
    val listType = object : TypeToken<List<ExerciseDC>>() {}.type

    return gson.fromJson(jsonString, listType)
}

