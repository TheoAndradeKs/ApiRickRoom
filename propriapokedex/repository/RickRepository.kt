package com.theoandrade.propriapokedex.repository

import com.theoandrade.propriapokedex.database.RickDao
import com.theoandrade.propriapokedex.database.RickEntity
import com.theoandrade.propriapokedex.model.Rick
import com.theoandrade.propriapokedex.network.OpenRickService
import com.theoandrade.propriapokedex.network.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RickRepository(
    private val dao: RickDao,
    private val api: OpenRickService = RetrofitClient.api
) {

    /*
     * A UI observa somente o banco local.
     */
    fun observeRicks(): Flow<List<Rick>> =
        dao.observeAll().map { entidades ->
            entidades.map { it.toDomain() }
        }

    /*
     * Busca os personagens na API
     * e salva no Room.
     *
     * A UI não recebe os dados diretamente daqui.
     */
    suspend fun syncCharacters() {

        val resposta = api.getCharacters()

        val personagens = resposta.results.map { personagem ->

            RickEntity(
                id = personagem.id,
                name = personagem.name,
                species = personagem.species,
                status = personagem.status,
                imageUrl = personagem.image
            )
        }

        dao.insertAll(personagens)
    }
}

/*
 * Conversão Entity -> Model
 */
private fun RickEntity.toDomain(): Rick {

    return Rick(
        id = id,
        name = name,
        species = species,
        status = status,
        imageUrl = imageUrl
    )
}