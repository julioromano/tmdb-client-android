package net.marcoromano.tmdb.movie

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.marcoromano.tmdb.movie.widgets.Movie

@Composable
internal fun MovieScreen(
  navBack: () -> Unit,
) {
  val vm = hiltViewModel<MovieViewModel>()
  val state by vm.state.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    if (state.movie == null) vm.load()
  }
  MovieScreen(
    state = state,
    navBack = navBack,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieScreen(
  state: MovieState,
  navBack: () -> Unit,
) {
  val behavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  Scaffold(
    modifier = Modifier.nestedScroll(behavior.nestedScrollConnection),
    topBar = {
      LargeTopAppBar(
        title = { Text(text = state.movie?.title ?: stringResource(R.string.movie)) },
        navigationIcon = {
          IconButton(onClick = navBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
          }
        },
        scrollBehavior = behavior,
      )
    },
  ) { paddingValues ->
    if (state.isLoading) {
      Box(
        modifier = Modifier
          .padding(paddingValues)
          .fillMaxSize(),
        contentAlignment = Alignment.Center,
      ) {
        CircularProgressIndicator(
          modifier = Modifier.fillMaxSize(fraction = 0.5f),
        )
      }
    } else {
      LazyColumn(
        modifier = Modifier.padding(paddingValues),
      ) {
        state.movie?.let {
          item {
            Movie(
              movie = it,
            )
          }
        }
      }
    }
  }
}

@Preview(name = "Day mode")
@Preview(name = "Night mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
  MovieScreen(
    state = MovieState(
      movie = demoMovie(),
    ),
    navBack = {},
  )
}
