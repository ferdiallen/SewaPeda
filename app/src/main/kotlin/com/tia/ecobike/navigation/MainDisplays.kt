package com.tia.ecobike.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tia.ecobike.bottomnav.BottomBarScreenHolder
import com.tia.ecobike.bottomnav.NavGraphScaffoldBottom
import com.tia.ecobike.ui.theme.Greenify
import com.tia.ecobike.ui.theme.backgroundLights

@Composable
fun MainDisplays() {
    val navBottomHostController = rememberNavController()
    Scaffold(bottomBar = {
        BottomBar(nav = navBottomHostController)
    }) {
        NavGraphScaffoldBottom(nav = navBottomHostController)
    }
}

@Composable
fun BottomBar(nav: NavHostController) {
    val screens = listOf(
        BottomBarScreenHolder.Mains,
        BottomBarScreenHolder.Cart,
        BottomBarScreenHolder.Profile
    )
    val stackEntry by nav.currentBackStackEntryAsState()
    val current = stackEntry?.destination
    BottomNavigation(
        elevation = 12.dp, backgroundColor = Color.White, modifier = Modifier.clip(
            RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
        )
    ) {
        screens.forEach { screen ->
            AddItem(screen = screen, current = current, nav = nav)
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreenHolder,
    current: NavDestination?,
    nav: NavHostController
) {
    BottomNavigationItem(selected = current?.hierarchy?.any {
        it.route == screen.route
    } == true, onClick = { nav.navigate(screen.route) },
        label = { Text(text = screen.title) }, icon = {
            Icon(imageVector = screen.icon, contentDescription = "bottom nav icon")
        }, unselectedContentColor = Color.Gray, selectedContentColor = Greenify
    )
}

@Composable
fun HomeScreens() {
    val isdark = isSystemInDarkTheme()

    var search by rememberSaveable {
        mutableStateOf("")
    }

    val focusmgr = LocalFocusManager.current
    fun isDark(): Color {
        return when (isdark) {
            true -> {
                Color.White
            }
            false -> {
                Color.Black
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(if (isdark) Color.Black else backgroundLights)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clickable(enabled = false, onClick = {}),
            backgroundColor = Greenify, shape = RoundedCornerShape(
                bottomStart = 64.dp
            )
        ) {}
        Column(Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(37.dp))
            Image(
                painter = painterResource(id = com.tia.ecobike.R.drawable.mainlogo),
                modifier = Modifier
                    .width(215.dp)
                    .height(79.dp),
                contentDescription = "for main menu"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Hello,What are you looking for ?",
                color = Color.White,
                modifier = Modifier.padding(start = 24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9F)
                    .clip(RoundedCornerShape(12.dp))
                    .align(CenterHorizontally),
                color = MaterialTheme.colors.primary,
                elevation = 14.dp
            ) {
                TextField(
                    value = search,
                    onValueChange = {
                        search = it
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    maxLines = 1,
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            "search field",
                            tint = Greenify
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Greenify,
                        backgroundColor = Color.White, disabledLabelColor = Color.Gray
                    ), label = {
                        Text(text = "Search bike")
                    }, keyboardActions = KeyboardActions(onDone = {
                        focusmgr.clearFocus()
                    })
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp)
            ) {
                Text(text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    ) {
                        append("Hot ")
                    }
                    withStyle(style = SpanStyle(Color.White, fontSize = 17.sp)) {
                        append("Deals")
                    }
                }, modifier = Modifier.weight(3F))
                Text(
                    text = "View all",
                    color = Color.White,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1F)
                        .padding(end = 24.dp)
                        .clickable {

                        }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 24.dp, end = 12.dp)
            ) {
                items(5) {
                    Surface(
                        elevation = 14.dp, shape = RoundedCornerShape(32.dp)
                    ) {
                        RowsOfHotDeals()
                    }
                }
            }
            Spacer(modifier = Modifier.height(36.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp)
            ) {
                Text(text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            isDark(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    ) {
                        append("Top ")
                    }
                    withStyle(style = SpanStyle(isDark(), fontSize = 17.sp)) {
                        append("Locations")
                    }
                }, modifier = Modifier.weight(3F))
                Text(
                    text = "View all",
                    color = Color.Black,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1F)
                        .padding(end = 24.dp)
                        .clickable {

                        }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 24.dp, end = 12.dp)
            ) {
                items(5) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(12.dp),
                        elevation = 14.dp
                    ) {
                        RowsOfLocationsTop()
                    }
                }
            }
        }
    }
}

@Composable
fun RowsOfHotDeals() {
    Card(
        modifier = Modifier
            .size(202.dp, 231.dp)
            .clickable {}
    ) {

    }
}

@Composable
fun RowsOfLocationsTop() {
    Card(modifier = Modifier
        .size(93.dp, 131.dp)
        .clickable { }) {

    }
}

@Preview(showBackground = true)
@Composable
fun prevs() {
    MainDisplays()
}