package com.example.foodhub

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodhub.data.FoodApi
import com.example.foodhub.ui.features.auth.AuthScreen
import com.example.foodhub.ui.features.auth.signup.SignUpScreen
import com.example.foodhub.ui.navigation.AuthScreen
import com.example.foodhub.ui.navigation.Home
import com.example.foodhub.ui.navigation.Login
import com.example.foodhub.ui.navigation.SignUp
import com.example.foodhub.ui.theme.FoodHubAndroidTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var showSplashScreen = true

    @Inject
    lateinit var foodApi: FoodApi

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            // Set a listener to be notified when the splash screen is shown
            setKeepOnScreenCondition {
                showSplashScreen
            }
            // Set a listener to be notified when the splash screen is dismissed
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.5f,
                    0f
                )
                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.5f,
                    0f
                )

                zoomX.duration = 500
                zoomY.duration = 500

                zoomX.interpolator=OvershootInterpolator()
                zoomY.interpolator=OvershootInterpolator()

                zoomX.doOnEnd {
                    screen.remove()
                }
                zoomY.doOnEnd {
                    screen.remove()
                }
                zoomX.start()
                zoomY.start()
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodHubAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                        val navController = rememberNavController()
                        NavHost (
                            navController=navController,
                            startDestination = AuthScreen,
                            modifier = Modifier
                                .padding(innerPadding)
                        ){
                            composable<SignUp>{
                                SignUpScreen(navController)
                            }
                            composable<AuthScreen>{
                                AuthScreen(navController)
                            }
                            composable<Login>{
                                Box(modifier = Modifier.fillMaxSize().background(color = Color.Green)){
                                    Text(text = "Login")
                                }
                            }
                            composable<Home>{
                                Box(modifier = Modifier.fillMaxSize().background(color = Color.Red)){
                                    Text(text = "Home")
                                }
                            }
                        }

                }
            }
        }

        // Simulate a network call or some initialization
        if(::foodApi.isInitialized){
            Log.d("MainActivity", "FoodApi is initialized")
        }

        // Simulate a delay for the splash screen
        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            showSplashScreen = false
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FoodHubAndroidTheme {
        Greeting("Android")
    }
}