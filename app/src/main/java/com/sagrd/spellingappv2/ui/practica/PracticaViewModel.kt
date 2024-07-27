package com.sagrd.spellingappv2.ui.practica

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sagrd.spellingappv2.data.repository.PracticaRepository
import com.sagrd.spellingappv2.model.Palabra
import com.sagrd.spellingappv2.model.Practica
import com.sagrd.spellingappv2.model.SpellListState
import com.sagrd.spellingappv2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PracticaViewModel @Inject constructor(
    val practicaRepository: PracticaRepository
): ViewModel() {

    var usuarioId by mutableStateOf(0)
    var fraseId by mutableStateOf(0)
    var vecesPracticado by mutableStateOf(0)

    private var _state = mutableStateOf(SpellListState())
    val state: State<SpellListState> = _state

    var listado = practicaRepository.getListStream()
        private set

    init {
        practicaRepository.getListStream().onEach {
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

    fun Guardar(usuarioid : Int){
        viewModelScope.launch {
            practicaRepository.upsert(
               Practica(
                   usuarioId = usuarioId,
                   fecha = Date().toString()
               )
            )
        }
    }
}