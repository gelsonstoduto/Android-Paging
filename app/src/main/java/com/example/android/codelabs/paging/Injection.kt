/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.codelabs.paging

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.android.codelabs.paging.api.GithubService
import com.example.android.codelabs.paging.data.GithubRepository
import com.example.android.codelabs.paging.db.GithubLocalCache
import com.example.android.codelabs.paging.db.RepoDatabase
import com.example.android.codelabs.paging.ui.ViewModelFactory
import java.util.concurrent.Executors

/**
 * Classe que lida com a criação de objetos.
 * Assim, os objetos podem ser passados como parâmetros nos construtores e depois substituídos
 * por testes, quando necessário.
 */
object Injection {

    /**
     * Cria uma instância do [GithubLocalCache] com base no banco de dados DAO.
     */
    private fun provideCache(context: Context): GithubLocalCache {
        val database = RepoDatabase.getInstance(context)
        return GithubLocalCache(database.reposDao(), Executors.newSingleThreadExecutor())
    }

    /**
     * Cria uma instância do [GithubRepository] com base no [GithubService] e em um [GithubLocalCache]
     */
    private fun provideGithubRepository(context: Context): GithubRepository {
        return GithubRepository(GithubService.create(), provideCache(context))
    }

    /**
     * Fornece o [ViewModelProvider.Factory] que é usado para obter uma referência aos objetos [ViewModel].
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideGithubRepository(context))
    }
}
