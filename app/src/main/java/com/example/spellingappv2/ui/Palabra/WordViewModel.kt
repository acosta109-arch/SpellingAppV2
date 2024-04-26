package com.example.spellingappv2.ui.Palabra

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellingappv2.data.repositorios.PalabraRepository
import com.example.spellingappv2.model.Palabra
import com.example.spellingappv2.model.SpellListState
import com.example.spellingappv2.model.Usuario
import com.example.spellingappv2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordViewModel @Inject constructor(
    val palabraRepository: PalabraRepository
): ViewModel() {
    var palabra by mutableStateOf(Palabra(0,"","",""))
    var word by mutableStateOf("")
    var description by mutableStateOf("")
    var imageUrl by mutableStateOf("")

    var listado = palabraRepository.getList()
        private set



    /*var palabrasListado = palabraRepository.getList()
        private set*/

    private var _state = mutableStateOf(SpellListState())
    val state: State<SpellListState> = _state

    init {
        palabraRepository.getList().onEach {
            result ->
            when(result){
                is Resource.Loading<*> -> {
                    _state.value = SpellListState(isLoading = true)
                }
                is Resource.Success<*> -> {
                    _state.value = SpellListState( palabras = result.data as List<Palabra>)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun Guardar(){
        viewModelScope.launch {
            palabraRepository.insertar(
                Palabra(
                    palabra = word,
                    descripcion = description,
                    imagenUrl = imageUrl
                )
            )
        }
    }

    fun GetPalabra (Id : Int = 0) : Palabra{
        viewModelScope.launch {
            palabraRepository.buscar(Id).collect { response ->
                palabra = response
            }
        }
        return palabra
    }
}