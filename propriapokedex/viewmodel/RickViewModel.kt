package com.theoandrade.propriapokedex.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.theoandrade.propriapokedex.database.RickDatabase
import com.theoandrade.propriapokedex.model.Rick
import com.theoandrade.propriapokedex.repository.RickRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SyncState {

    object Idle : SyncState

    object Loading : SyncState

    object Success : SyncState

    data class Error(
        val message: String
    ) : SyncState
}

class RickViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = RickRepository(
        dao = RickDatabase
            .getInstance(application)
            .rickDao()
    )

    /*
     * A UI observa SOMENTE o Room.
     */
    val ricks: StateFlow<List<Rick>> =
        repository
            .observeRicks()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    var syncState: SyncState by mutableStateOf(
        SyncState.Idle
    )
        private set

    init {
        syncCharacters()
    }

    /*
     * Sincroniza API -> Repository -> Room.
     */
    fun syncCharacters() {

        viewModelScope.launch {

            syncState = SyncState.Loading

            syncState = try {

                repository.syncCharacters()

                SyncState.Success

            } catch (e: Exception) {

                SyncState.Error(
                    "Não foi possível sincronizar. Verifique sua conexão com a internet."
                )
            }
        }
    }
}