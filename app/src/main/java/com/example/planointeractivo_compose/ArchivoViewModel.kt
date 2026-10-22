package com.example.planointeractivo_compose

import android.app.Application
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray

class ArchivoViewModel(application: Application) : AndroidViewModel(application) {

    // Lista de marcadores leídos desde el JSON
    private val _marcadores = MutableStateFlow<List<Marcador>>(emptyList())
    val marcadores: StateFlow<List<Marcador>> = _marcadores.asStateFlow()

    // Marcador seleccionado tras el toque
    private val _marcadorSeleccionado = MutableStateFlow<Marcador?>(null)
    val marcadorSeleccionado: StateFlow<Marcador?> = _marcadorSeleccionado.asStateFlow()

    init {
        cargarMarcadores()
    }

    private fun cargarMarcadores() {
        val lista = mutableListOf<Marcador>()
        try {
            val context = getApplication<Application>().applicationContext
            val jsonString = context.assets.open("marcadores.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val x = obj.getDouble("x").toFloat()
                val y = obj.getDouble("y").toFloat()
                val radio = obj.getDouble("radio").toFloat()
                val titulo = obj.getString("titulo")
                val descripcion = obj.getString("descripcion")

                lista.add(Marcador(Offset(x, y), radio, titulo, descripcion))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        _marcadores.value = lista
    }

    fun seleccionarMarcadorSiAplica(tapOffset: Offset) {
        val encontrado = _marcadores.value.find { marcador ->
            (tapOffset - marcador.offset).getDistance() <= marcador.radio
        }
        if (encontrado != null) {
            _marcadorSeleccionado.value = encontrado
        }
    }

    fun cerrarDialogo() {
        _marcadorSeleccionado.value = null
    }
}