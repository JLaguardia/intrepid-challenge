package com.prismsoft.intrepidnetworksswapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.prismsoft.intrepidnetworksswapi.dto.Episode
import com.prismsoft.intrepidnetworksswapi.dto.StarWarsApiResponse
import com.prismsoft.intrepidnetworksswapi.ui.*
import com.prismsoft.intrepidnetworksswapi.ui.theme.IntrepidNetworksSwapiTheme
import org.kodein.di.*
import org.kodein.di.android.closestDI
import java.time.LocalDate
import java.time.LocalDateTime

// api: https://swapi.dev/api/films/ [GET]
class MainActivity : ComponentActivity(), DIAware {

    override val di: DI by closestDI()
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntrepidNetworksSwapiTheme {
                MainContent()
            }
        }
        viewModel.state.observe(this) { state ->
            when (state) {
                is MainViewModel.MainState.Error -> showError(state.navController)
                is MainViewModel.MainState.DataFetched -> navigateToList(state.navController)
                else -> Unit
            }
        }
    }

    private fun navigateToList(navController: NavController) {
        navController.navigate("list")
    }

    private fun showError(navController: NavController) {
        navController.navigate("error")
    }

    @Composable
    fun MainContent() {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxHeight()
        ) {
            viewModel.getEpisodes(navController)
            composable("splash") { SwSplashScreen() }
            composable(route = "list") {
                val episodes by remember { viewModel.getAllEpisodes().asFlow() }
                    .collectAsState(initial = emptyList())
                EpisodeList(
                    episodes = episodes,
                    listener = object : EpisodeListListener {
                        override fun onItemClick(episode: Episode) {
                            navController.navigate("episodeDetail/${episode.episodeNo}")
                        }

                        override fun onSortClick(sortType: SortEnum) {
                            viewModel.setSortType(sortType)
                        }

                        override fun onRefreshClick() {
                            viewModel.getEpisodes(navController)
                        }

                    }
                )
            }
            composable(
                route = "episodeDetail/{epId}",
                arguments = listOf(navArgument("epId") { type = NavType.StringType })
            ) { entry ->
                entry.arguments?.getString("epId")?.let { epId ->
                    val episode by remember { viewModel.getEpisodeByIdFlow(epId = epId.toInt()) }
                        .collectAsState(initial = null)
                    episode?.let {
                        EpisodeDetail(ep = it) { navController.popBackStack() }
                    }
                }
            }
            composable("error") {
                Column {
                    Row {
                        Text(
                            text = "There was an error!",
                            color = MaterialTheme.colors.error
                        )
                    }
                    Row {
                        Button(
                            onClick = { navController.navigate("splash") }) {
                            Text(text = "Try again")
                        }
                    }
                }
            }
        }
    }
}

val dummyResponse = StarWarsApiResponse(
    results = listOf(
        Episode(
            title = "A New Hope",
            episodeNo = 4,
            crawlText = "whatever",
            director = "some guy",
            producer = "sumeguy2",
            releaseDate = LocalDate.now(),
            created = LocalDateTime.now(),
            edited = LocalDateTime.now(),
            url = ""
        )
    )
)

//@Preview(showBackground = true)
//@Composable
//fun SplashScreenPreview() {
//    IntrepidNetworksSwapiTheme {
//        SwSplashScreen()
//    }
//}

@Preview(showBackground = true)
@Composable
fun LineItemPreview() {
    IntrepidNetworksSwapiTheme {
        EpisodeList(episodes = emptyList(), object : EpisodeListListener{
            override fun onItemClick(episode: Episode) {}

            override fun onSortClick(sortType: SortEnum) {}

            override fun onRefreshClick() {}
        })
//        LineItem(ep = dummyResponse.results[0]) { }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    IntrepidNetworksSwapiTheme {
        EpisodeDetail(ep = dummyResponse.results[0]) { }
    }
}

inline fun <reified VM : ViewModel, T> T.viewModel(): Lazy<VM> where T : DIAware, T : ComponentActivity {
    return lazy { ViewModelProvider(this, direct.instance()).get(VM::class.java) }
}