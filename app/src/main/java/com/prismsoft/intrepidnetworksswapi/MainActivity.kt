package com.prismsoft.intrepidnetworksswapi

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prismsoft.intrepidnetworksswapi.api.StarWarsApi
import com.prismsoft.intrepidnetworksswapi.dto.Episode
import com.prismsoft.intrepidnetworksswapi.dto.StarWarsApiResponse
import com.prismsoft.intrepidnetworksswapi.ui.ClickListener
import com.prismsoft.intrepidnetworksswapi.ui.EpisodeDetail
import com.prismsoft.intrepidnetworksswapi.ui.LineItem
import com.prismsoft.intrepidnetworksswapi.ui.theme.IntrepidNetworksSwapiTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.direct
import org.kodein.di.generic.instance
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime


// include “episode_id" and “title” in each list item
// api: https://swapi.dev/api/films/ [GET]

class MainActivity : ComponentActivity(), KodeinAware, ClickListener {

    override val kodein: Kodein by kodein()
    private val viewModel: MainViewModel by viewModels()
    private val swApi: StarWarsApi by instance()
    private val episodes = mutableListOf<Episode>()
    private lateinit var selectedEpisode: Episode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntrepidNetworksSwapiTheme {
                MainContent()
            }
        }
        viewModel.api = swApi
        viewModel.state.observe(this){ state ->
            when(state){
                is MainViewModel.MainState.Error -> showError(state.navController)
                is MainViewModel.MainState.DataLoaded -> navigateToList(state.episodes, state.navController)
                else -> Unit
            }
        }
    }

    data class WrapperEp(val episodes: List<Episode>): Serializable

    private fun navigateToList(eps: List<Episode>, navController: NavController) {
        navController.currentBackStackEntry?.arguments?.putSerializable("episodes", WrapperEp(eps))
        episodes.clear()
        episodes.addAll(eps)
        navController.navigate("list")
    }

    override fun onItemClicked(ep: Episode, navController: NavController?) {
        selectedEpisode = ep
        Log.i("MainActivity", "item clicked: ${ep.title}")
        navController?.let { ctrl ->
            Log.i("MainActivity", "navigating to episodeDetail")
            ctrl.navigate("episodeDetail")
            ctrl.currentBackStackEntry?.arguments?.putSerializable("episode", ep)
        }

    }

    fun showError(navController: NavController){
        navController.navigate("error")
    }

    @Composable
    fun MainContent(){
        Log.w("MainActivity","MainContent set")
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "splash" ){
            viewModel.getEpisodes(navController)
            composable("splash"){
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }
            composable("list"){ entry ->
                val eps = (entry.arguments?.getSerializable("episodes") as? WrapperEp) ?: WrapperEp(episodes)
                if(eps.episodes.isEmpty()){
                    Row {
                       Text(text = "The returned list was empty")
                    }
                } else {
                    Column {
                        eps.episodes.forEach { ep ->
                            Row {
                                LineItem(ep = ep, navController, listener = this@MainActivity)
                            }
                        }
                    }
                }
            }
            composable("episodeDetail"){ entry ->
                val ep = (entry.arguments?.getSerializable("episode") as? Episode) ?: selectedEpisode
                Row{
                    EpisodeDetail(ep = ep, navController)
                }
            }
            composable("error"){
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
            characters = listOf(),
            planets = listOf(),
            starships = listOf(),
            vehicles = listOf(),
            species = listOf(),
            created = LocalDateTime.now(),
            edited = LocalDateTime.now(),
            url = ""
        )
    )
)


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    IntrepidNetworksSwapiTheme {
        EpisodeDetail(ep = dummyResponse.results[0])
//        LineItem(ep = dummyResponse.results[0]) { _, _ -> }
    }
}

inline fun <reified VM : ViewModel, T> T.viewModel(): Lazy<VM> where T : KodeinAware, T : ComponentActivity {
    return lazy { ViewModelProvider(this, direct.instance()).get(VM::class.java) }
}