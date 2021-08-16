package com.sunmkim.app.part2.profilecardlayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.sunmkim.app.part2.profilecardlayout.ui.theme.ProfileCardLayoutTheme
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileCardLayoutTheme{
                    Navigation()
            }

        }
    }
}

@Composable
fun Navigation(userProfiles: List<UserProfile> = userProfileList){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "user_list") {
        composable("user_list") {
            UserListScreen(userProfiles, navController)
        }
        composable(
            route = "user_details/{userId}",
            arguments = listOf(navArgument("userId"){
                type = NavType.IntType
            })) { entry ->
            UserProfileDetailScreen(entry.arguments!!.getInt("userId"), navController)
        }
    }

}



@Composable
fun UserListScreen(userProfiles: List<UserProfile>, navController: NavHostController) {
    Scaffold(
        topBar = { AppBar(
            title = "Users List",
            icon = Icons.Default.Home,
        ){

        } },
    ){
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn {
                items(userProfiles.size){ index ->
                    ProfileCard(userProfile = userProfiles[index]){
                        navController.navigate("user_details/${userProfiles[index].id}")
                    }
                }
            }

        }
    }

}

@Composable
fun AppBar(title: String, icon: ImageVector, iconClickAction: () -> Unit){
    TopAppBar(
        navigationIcon = { Icon(
            imageVector = icon,
            contentDescription = null ,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .clickable( onClick = iconClickAction ),
            ) },
        title = { Text(text = title )}
    )
}

@Composable
fun ProfileCard(userProfile: UserProfile, clickAction: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .clickable( onClick = clickAction ),
        elevation = 8.dp,
        backgroundColor = Color.White,
        shape = CutCornerShape(topEnd = 16.dp),

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            ProfilePicture(userProfile.drawableId, userProfile.status, 72.dp)
            ProfileContent(userProfile.name, userProfile.status, Alignment.Start)
        }
    }
}

@Composable
fun ProfilePicture(drawableId: String, onlineStatus: Boolean, imageSize: Dp) {
    Card(
        shape = CircleShape,
        border = BorderStroke(
            width = 2.dp,
            color = if (onlineStatus) Color.Green else Color.Red),
        elevation = 4.dp,
        modifier = Modifier.padding(16.dp)
    ){
        Image(
            painter = rememberImagePainter(
                data = drawableId,
                builder = {
                    transformations(CircleCropTransformation())
                }
                ),
            contentDescription = "ProfilePicture",
            modifier = Modifier
                .size(imageSize),
        )
    }


}

@Composable
fun ProfileContent(userName: String, onlineStatus: Boolean, alignment: Alignment.Horizontal) {
    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = alignment,
    ) {
        Text(
            text = userName,
            style = MaterialTheme.typography.h5,
        )
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = if (onlineStatus) "Active Now" else "Offline",
                style = MaterialTheme.typography.body2,
            )
        }

    }
}

@Composable
fun UserProfileDetailScreen(userId: Int, navController: NavHostController) {
    val userProfile = userProfileList.first { userProfile -> userId == userProfile.id}
    Scaffold(
        topBar = { AppBar(
            title = "Users profile details",
            icon = Icons.Default.ArrowBack,
        ){
            navController.navigateUp()
        } },
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            ProfilePicture(userProfile.drawableId, userProfile.status, 240.dp)
            ProfileContent(userProfile.name, userProfile.status, Alignment.CenterHorizontally)
        }
    }

}