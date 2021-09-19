package com.example.lio.takenoteapp.repositories

import android.app.Application
import com.example.lio.takenoteapp.data.local.NoteDao
import com.example.lio.takenoteapp.data.local.entities.Note
import com.example.lio.takenoteapp.data.remote.NoteApi
import com.example.lio.takenoteapp.data.remote.request.AccountRequest
import com.example.lio.takenoteapp.other.Resource
import com.example.lio.takenoteapp.other.checkForInternetConnection
import com.example.lio.takenoteapp.other.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val noteApi: NoteApi,
    private val context: Application
) {
    suspend fun register(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val response = noteApi.register(AccountRequest(email, password))
            if (response.isSuccessful && response.body()!!.successful) {
                Resource.success(response.body()?.message)
            } else {
                Resource.error(response.body()?.message ?: response.message(), null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't connect to the servers. Check your internet connection", null)
        }
    }

    suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val response = noteApi.login(AccountRequest(email, password))
            if (response.isSuccessful && response.body()!!.successful) {
                Resource.success(response.body()?.message)
            } else {
                Resource.error(response.body()?.message ?: response.message(), null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't connect to the servers. Check your internet connection", null)
        }
    }

    fun getAllNotes(): Flow<Resource<List<Note>>> {
        return networkBoundResource(
            query = {
                noteDao.getAllNotes()
            },
            fetch = {
                noteApi.getNotes()
            },
            saveFetchResult = { response ->
                response.body()?.let{
                    //insert notes in database
                }
            },
            shouldFetch = {
                checkForInternetConnection(context)
            }
        )
    }
}