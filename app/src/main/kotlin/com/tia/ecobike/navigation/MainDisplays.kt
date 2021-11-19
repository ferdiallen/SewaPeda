package com.tia.ecobike.navigation

import androidx.compose.animation.core.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tia.ecobike.bottomnav.BottomBarScreenHolder
import com.tia.ecobike.bottomnav.NavGraphScaffoldBottom
import com.tia.ecobike.darklightcontroller.IsDarkOrLight
import com.tia.ecobike.darklightcontroller.UiController
import com.tia.ecobike.navigators.NavigatorQueue
import com.tia.ecobike.ui.theme.Greenify
import com.tia.ecobike.ui.theme.backgroundLights

@ExperimentalMaterialApi
@Composable
fun MainDisplays(mainNav: NavHostController) {
    val navBottomHostController = rememberNavController()
    Scaffold(bottomBar = {
        BottomBar(nav = navBottomHostController)
    }) { paddingContent ->
        Box(Modifier.padding(bottom = paddingContent.calculateBottomPadding())) {
            NavGraphScaffoldBottom(nav = navBottomHostController, mainNav)
        }
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
    val sysbar = UiController.setUi()
    when (current?.route) {
        BottomBarScreenHolder.Cart.route -> {
            sysbar.setStatusBarColor(Color.Transparent, darkIcons = true)
        }
        BottomBarScreenHolder.Mains.route -> sysbar.setStatusBarColor(Greenify, darkIcons = true)
    }
    BottomNavigation(
        elevation = 12.dp,
        backgroundColor = Color.White,
        modifier = Modifier.clip(
            RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
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
    } == true, onClick = {
        nav.navigate(screen.route) {
            popUpTo(nav.graph.findStartDestination().id)
            launchSingleTop = true
        }
    },
        label = { Text(text = screen.title) }, icon = {
            Icon(imageVector = screen.icon, contentDescription = "bottom nav icon")
        }, unselectedContentColor = Color.Gray, selectedContentColor = Greenify
    )
}

@Composable
fun HomeScreens(nav: NavHostController) {
    val isdark = isSystemInDarkTheme()
    val colorState = IsDarkOrLight.isDarkOrLight()
    val focusmgr = LocalFocusManager.current
    var isEnabledShimmer by remember{
        mutableStateOf(false)
    }
    isEnabledShimmer = true
    val shimmerEffect = listOf(
        Color.LightGray.copy(alpha = 0.6F),
        Color.LightGray.copy(alpha = 0.2F),
        Color.LightGray.copy(alpha = 0.6F)
    )
    val infiniteTransition = rememberInfiniteTransition()
    val animateF = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1400f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearOutSlowInEasing)
        )
    )
    val brush = Brush.linearGradient(
        colors = shimmerEffect,
        start = Offset.Zero,
        end = Offset(x = animateF.value, y = animateF.value)
    )
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
                color = Greenify,
                elevation = 14.dp
            ) {
                CompositionLocalProvider(LocalTextInputService provides null) {
                    TextField(
                        modifier = Modifier.onFocusChanged {
                            if (it.hasFocus) {
                                nav.navigate(NavigatorQueue.SearchMenu.route)
                            }
                        },
                        value = "",
                        onValueChange = {

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
                            backgroundColor = Color.White
                        ), label = {
                            Text(text = "Search bike", color = Greenify)
                        }, keyboardActions = KeyboardActions(onDone = {
                            focusmgr.clearFocus()
                        })
                    )
                }

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
                        elevation = 14.dp,
                        shape = RoundedCornerShape(32.dp)
                    ) {
                        RowsOfHotDeals(brush,isEnabledShimmer)
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
                            color = colorState,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    ) {
                        append("Top ")
                    }
                    withStyle(style = SpanStyle(colorState, fontSize = 17.sp)) {
                        append("Locations")
                    }
                }, modifier = Modifier.weight(3F))
                Text(
                    text = "View all",
                    color = colorState,
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
                        RowsOfLocationsTop(brush,isEnabledShimmer)
                    }
                }
            }
        }
    }
}

@Composable
fun RowsOfHotDeals(brush:Brush,isEnabled:Boolean) {
    Card(
        modifier = Modifier
            .size(202.dp, 231.dp)
            .clickable {}
    ) {
        if(isEnabled){
            Spacer(modifier = Modifier.fillMaxSize().background(brush))
        }
    }
}

@Composable
fun RowsOfLocationsTop(brush:Brush,isEnabled:Boolean) {
    Card(modifier = Modifier
        .size(93.dp, 131.dp)
        .clickable { }) {
        if(isEnabled){
            Spacer(modifier = Modifier.fillMaxSize().background(brush))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun prevs() {
    HomeScreens(rememberNavController())
}