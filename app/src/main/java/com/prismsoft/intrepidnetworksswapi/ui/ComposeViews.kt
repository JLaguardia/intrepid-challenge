package com.prismsoft.intrepidnetworksswapi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import com.prismsoft.intrepidnetworksswapi.R
import com.prismsoft.intrepidnetworksswapi.SortEnum
import com.prismsoft.intrepidnetworksswapi.dto.Episode

val margins = 8.dp

@Composable
fun LineItem(ep: Episode, listener: (Episode) -> Unit) {
    val displayText = "Episode ${ep.episodeNo}: ${ep.title}"
    ConstraintLayout(
        modifier = Modifier
            .clickable {
                listener(ep)
            }
            .background(Color(0xFF050505))
            .fillMaxWidth()
            .height(60.dp)
    ) {
        val (title, img) = createRefs()
        Text(
            text = displayText,
            color = Color(0xFFCBCB00),
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
            contentDescription = "click for more details",
            modifier = Modifier
                .constrainAs(img) {
                    end.linkTo(
                        anchor = parent.end,
                    )
                    top.linkTo(anchor = title.top)
                    bottom.linkTo(anchor = title.bottom)
                }
        )
    }
}

@Composable
fun EpisodeDetail(ep: Episode, onClick: () -> Unit) {
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
                .clickable { onClick() }
                .constrainAs(backBtn) {
                    top.linkTo(
                        anchor = parent.top,
                        margin = margins / 2
                    )
                    start.linkTo(
                        anchor = parent.start,
                        margin = margins / 2
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

@Composable
fun SwSplashScreen() {
    ConstraintLayout {
        val (progress, text) = createRefs()
        CircularProgressIndicator(modifier = Modifier
            .fillMaxSize()
            .constrainAs(progress) {
                top.linkTo(parent.top)
                constrainCenter(vertical = false)
            })
        Text(
            modifier = Modifier.constrainAs(text) {
                constrainCenter(vertical = false)
                top.linkTo(
                    anchor = parent.top,
                    margin = 150.dp
                )
            },
            text = "Loading... all the way from Mustafar."
        )
    }
}

@Composable
fun EpisodeList(
    episodes: List<Episode>,
    onItemClick: (Episode) -> Unit,
    onSortClick: (SortEnum) -> Unit
) {
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
                LineItem(ep = ep) { episode -> onItemClick(episode) }
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
            onClick = { onSortClick(SortEnum.EPISODE_NUM) }) {
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
            onClick = { onSortClick(SortEnum.TITLE_DESC) }) {
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
            onClick = { onSortClick(SortEnum.TITLE_ASC) }) {
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
            onClick = { onSortClick(SortEnum.RELEASE_DATE) }) {
            Text(text = "Sort By Release")
        }
    }
}

fun ConstrainScope.constrainCenter(
    target: ConstrainedLayoutReference = parent,
    horizontal: Boolean = true,
    vertical: Boolean = true,
    margins: Dp = 0.dp
) {
    if (vertical) {
        top.linkTo(anchor = target.top, margin = margins)
        bottom.linkTo(target.bottom, margin = margins)
    }
    if (horizontal) {
        start.linkTo(target.start, margin = margins)
        end.linkTo(target.end, margin = margins)
    }
}