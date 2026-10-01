package com.theoandrade.propriapokedex.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.theoandrade.propriapokedex.model.Rick
import com.theoandrade.propriapokedex.viewmodel.RickViewModel
import com.theoandrade.propriapokedex.viewmodel.SyncState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RickScreen(
    viewModel: RickViewModel = viewModel()
) {

    val ricks by viewModel.ricks.collectAsStateWithLifecycle()

    val syncState = viewModel.syncState

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Multiverse Radar"
                    )
                },

                actions = {

                    IconButton(
                        onClick = {
                            viewModel.syncCharacters()
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sincronizar"
                        )
                    }
                }
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            when (syncState) {

                is SyncState.Loading -> {

                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                is SyncState.Error -> {

                    Text(
                        text = syncState.message,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                is SyncState.Success -> {

                    Text(
                        text = "Sincronização concluída!",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                top = 12.dp,
                                end = 16.dp
                            )
                    )
                }

                is SyncState.Idle -> {
                    // Nada
                }
            }

            if (
                syncState is SyncState.Loading &&
                ricks.isEmpty()
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    CircularProgressIndicator()

                    Text(
                        text = "Carregando personagens...",
                        modifier = Modifier.padding(
                            top = 12.dp
                        )
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),

                    contentPadding = PaddingValues(
                        16.dp
                    ),

                    verticalArrangement = Arrangement.spacedBy(
                        12.dp
                    )
                ) {

                    items(
                        items = ricks,
                        key = { it.id }
                    ) { rick ->

                        RickCard(rick)
                    }
                }
            }
        }
    }
}

@Composable
fun RickCard(
    rick: Rick
) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        elevation = CardDefaults
            .cardElevation(
                defaultElevation = 4.dp
            )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            AsyncImage(
                model = rick.imageUrl,
                contentDescription = rick.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            Text(
                text = rick.name,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Espécie: ${rick.species}"
            )

            Text(
                text = "Status: ${rick.status}"
            )
        }
    }
}