package org.librefit

import org.librefit.data.ExerciseDeserializer
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import org.librefit.data.Exercise
import org.librefit.nav.NavigationHost
import org.librefit.ui.theme.LibreFitTheme
import org.librefit.util.DataStoreManager
import java.io.BufferedReader
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private lateinit var userPreferences: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        userPreferences = DataStoreManager(this)

        var list = emptyList<Exercise>()

        lifecycleScope.launch {
            list = loadExercises(resources.openRawResource(R.raw.exercises))
        }

        setContent {
            LibreFitTheme(userPreferences){
                NavigationHost( list = list , userPreferences = userPreferences )
            }
        }
    }
}

private fun loadExercises(inputStream: InputStream) : List<Exercise> {
    val gson = GsonBuilder()
        .registerTypeAdapter(Exercise::class.java, ExerciseDeserializer())
        .create()
    val jsonString = inputStream.bufferedReader().use(BufferedReader::readText)
    val listType = object : TypeToken<List<Exercise>>() {}.type

    return gson.fromJson(jsonString, listType)
}

