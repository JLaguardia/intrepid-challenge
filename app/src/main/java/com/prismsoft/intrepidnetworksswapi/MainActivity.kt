package com.prismsoft.intrepidnetworksswapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
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
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.direct
import org.kodein.di.generic.instance
import java.time.LocalDate
import java.time.LocalDateTime

// api: https://swapi.dev/api/films/ [GET]

class MainActivity : ComponentActivity(), KodeinAware {

    override val kodein: Kodein by kodein()
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
            composable("splash") {
                SwSplashScreen()
            }
            composable(route = "list") {
                val episodes by remember { viewModel.getAllEpisodes().asFlow() }.collectAsState(
                    initial = emptyList()
                )

                if (episodes.isEmpty()) {
                    Row { Text(text = "The returned list was empty") }
                } else {
                    ConstraintLayout {
                        val (itemList, sortEp, sortTitleAZ, sortTitleZA, sortDate) = createRefs()

                        LazyColumn(
                            modifier = Modifier.constrainAs(itemList) {
                                constrainCenter(
                                    vertical = false,
                                    margins = margins
                                )
                            }
                        ) {
                            items(episodes) { ep ->
                                LineItem(ep = ep) { episode ->
                                    navController.navigate("episodeDetail/${episode.episodeNo}")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        Button(
                            modifier = Modifier
                                .constrainAs(sortEp) {
                                    start.linkTo(
                                        anchor = parent.start,
                                        margin = margins
                                    )
                                    top.linkTo(
                                        anchor = itemList.bottom,
                                        margin = 30.dp
                                    )
                                },
                            onClick = { viewModel.setSortType(SortEnum.EPISODE_NUM) }) {
                            Text(text = "Sort By Episode #")
                        }
                        Button(
                            modifier = Modifier
                                .constrainAs(sortTitleZA) {
                                    end.linkTo(
                                        anchor = parent.end,
                                        margin = margins
                                    )
                                    top.linkTo(
                                        anchor = sortTitleAZ.bottom,
                                        margin = 30.dp
                                    )
                                },
                            onClick = { viewModel.setSortType(SortEnum.TITLE_DESC) }) {
                            Text(text = "Sort By Title Z-A")
                        }
                        Button(
                            modifier = Modifier
                                .constrainAs(sortTitleAZ) {
                                    end.linkTo(
                                        anchor = parent.end,
                                        margin = margins
                                    )
                                    top.linkTo(
                                        anchor = itemList.bottom,
                                        margin = 30.dp
                                    )
                                },
                            onClick = { viewModel.setSortType(SortEnum.TITLE_ASC) }) {
                            Text(text = "Sort By Title A-Z")
                        }
                        Button(
                            modifier = Modifier
                                .constrainAs(sortDate) {
                                    start.linkTo(
                                        anchor = parent.start,
                                        margin = margins
                                    )
                                    top.linkTo(
                                        anchor = sortEp.bottom,
                                        margin = 30.dp
                                    )
                                },
                            onClick = { viewModel.setSortType(SortEnum.RELEASE_DATE) }) {
                            Text(text = "Sort By Release")
                        }
                    }
                }
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
                        Text(text = "There was an error!", color = Color.Red)
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

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    IntrepidNetworksSwapiTheme {
        SwSplashScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun LineItemPreview() {
    IntrepidNetworksSwapiTheme {
        LineItem(ep = dummyResponse.results[0]) {  }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    IntrepidNetworksSwapiTheme {
        EpisodeDetail(ep = dummyResponse.results[0]) { }
    }
}

inline fun <reified VM : ViewModel, T> T.viewModel(): Lazy<VM> where T : KodeinAware, T : ComponentActivity {
    return lazy { ViewModelProvider(this, direct.instance()).get(VM::class.java) }
}