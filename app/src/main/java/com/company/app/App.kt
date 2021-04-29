package com.company.app

import android.app.Application
import android.util.Log
import com.company.app.repository.Repository
import com.company.app.ui.map.Slope
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.io.BufferedReader
import java.io.IOException
import java.util.*

class App: Application() {
    private val slopeDirectory = "slopes"
    private val _slopes: MutableList<Slope> = LinkedList()
    val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob())
    val repository by lazy { Repository(this) }
    lateinit var slopes: List<Slope>

    override fun onCreate() {
        super.onCreate()

        coroutineScope.run {
            val gsonConverter = Gson()
            val assets = applicationContext?.assets
            val assetFiles = assets?.list(slopeDirectory)
            assetFiles?.let { fileNames ->
                for (filePath in fileNames) {
                    try {
                        val file = assets.open("$slopeDirectory/$filePath")
                        val content = file.bufferedReader().use(BufferedReader::readText)
                        val slope = gsonConverter.fromJson(content, Slope::class.java)
                        _slopes.add(slope)
                    } catch (ioe: IOException) {
                        ioe.printStackTrace()
                    }
                }
            }
            slopes = _slopes
        }
    }
}