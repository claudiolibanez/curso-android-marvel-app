package com.example.core.data.repository

import com.example.core.domain.model.CharacterPaging
import com.example.core.domain.model.Comic
import com.example.core.domain.model.Event

interface CharactersRemoteDataSource {

    suspend fun fetchCharacters(queries: Map<String, String>): CharacterPaging

    suspend fun getComics(characterId: Int): List<Comic>

    suspend fun  getEvents(characterId: Int): List<Event>
}