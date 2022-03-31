package com.prismsoft.intrepidnetworksswapi.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.prismsoft.intrepidnetworksswapi.R
import com.prismsoft.intrepidnetworksswapi.dto.Episode

fun interface ClickListener {
    fun onItemClicked(ep: Episode, navController: NavController?)
}

val margins = 8.dp

@Composable
fun LineItem(ep: Episode, navController: NavController? = null, listener: ClickListener) {
    val displayText = "Episode ${ep.episodeNo}: ${ep.title}"
    ConstraintLayout(
        modifier = Modifier.clickable {
            listener.onItemClicked(ep, navController)
        }
    ) {
        val (title, img) = createRefs()
        Text(
            text = displayText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(
                    anchor = parent.top,
                    margin = margins
                )
                start.linkTo(
                    anchor = parent.start,
                    margin = margins
                )
                end.linkTo(anchor = img.start)
                bottom.linkTo(
                    anchor = parent.bottom,
                    margin = margins
                )
            }
        )

        Image(
            painter = painterResource(
                id = R.drawable.ic_baseline_chevron_right_24,
            ),
            contentDescription = "expand for more details",
            colorFilter = ColorFilter.tint(Color.Black),
            modifier = Modifier
                .constrainAs(img) {
                    start.linkTo(
                        anchor = title.end,
                    )
                    top.linkTo(anchor = title.top)
                    bottom.linkTo(anchor = title.bottom)
                }
        )
    }
}

@Composable
fun EpisodeDetail(ep: Episode, navController: NavController? = null) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (backBtn, title, director, producer) = createRefs()
        //backBtn
        Image(
            painter = painterResource(
                id = R.drawable.ic_baseline_chevron_left_24
            ),
            contentDescription = "go back",
            colorFilter = ColorFilter.tint(Color.Black),
            modifier = Modifier
                .scale(1.5f)
                .clickable { navController?.popBackStack() }
                .constrainAs(backBtn) {
                    top.linkTo(
                        anchor = parent.top,
                        margin = margins/2
                    )
                    start.linkTo(
                        anchor = parent.start,
                        margin = margins/2
                    )
                }
        )

        Text(
            text = "Episode ${ep.episodeNo}: ${ep.title}",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 22.sp,
            modifier = Modifier.constrainAs(title) {
                constrainCenter(
                    vertical = false
                )
                top.linkTo(
                    anchor = backBtn.bottom,
                    margin = margins
                )
            }
        )

        val sameDirectorProducer = ep.director == ep.producer
        val directedText = "Original Release date: ${ep.formattedReleaseDate()}\n${
            if (sameDirectorProducer) {
                "Directed and produced by ${ep.director}"
            } else {
                "Directed by ${ep.director}"
            }
        }"

        Text(
            text = directedText,
            fontSize = 18.sp,
            modifier = Modifier
                .constrainAs(director) {
                    constrainCenter(
                        target = title,
                        vertical = false
                    )
                    top.linkTo(
                        anchor = title.bottom,
                        margin = margins * 2
                    )
                }
                .padding(12.dp)
        )

        if (!sameDirectorProducer) {
            Text(
                text = "Produced by ${ep.producer}",
                fontSize = 18.sp,
                modifier = Modifier.constrainAs(producer) {
                    constrainCenter(
                        target = title,
                        vertical = false
                    )
                    top.linkTo(
                        anchor = director.bottom,
                        margin = margins
                    )
                }
            )
        }

    }
}

fun ConstrainScope.constrainCenter(
    target: ConstrainedLayoutReference = parent,
    horizontal: Boolean = true,
    vertical: Boolean = true
) {
    if (vertical) {
        top.linkTo(target.top)
        bottom.linkTo(target.bottom)
    }
    if (horizontal) {
        start.linkTo(target.start)
        end.linkTo(target.end)
    }
}