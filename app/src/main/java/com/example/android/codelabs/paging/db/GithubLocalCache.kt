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

package com.example.android.codelabs.paging.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.android.codelabs.paging.model.Repo
import java.util.concurrent.Executor

/**
     * Classe que lida com a fonte de dados local do DAO. Isso garante que os métodos sejam acionados no executor
     * correto.
 */
class GithubLocalCache(
    private val repoDao: RepoDao,
    private val ioExecutor: Executor
) {

    /**
     * Insira uma lista de repositórios no banco de dados, em um encadeamento em segundo plano.
     */
    fun insert(repos: List<Repo>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("GithubLocalCache", "inserting ${repos.size} repos")
            repoDao.insert(repos)
            insertFinished()
        }
    }

    /**
     * Solicite um LiveData <List <Repo>> do Dao, com base em um nome de repo. Se o nome contiver várias
     * palavras separadas por espaços, emularemos o comportamento da API do GitHub e permitiremos qualquer
     * caractere entre as palavras.
     * @param name repository name
     */
    fun reposByName(name: String): DataSource.Factory<Int, Repo> {
        // appending '%' so we can allow other characters to be before and after the query string
        val query = "%${name.replace(' ', '%')}%"
        return repoDao.reposByName(query)
    }
}
