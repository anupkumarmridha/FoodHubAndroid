package com.example.foodhub.ui.features.auth.login
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodhub.R
import com.example.foodhub.ui.BasicDialog
import com.example.foodhub.ui.FoodHubTextField
import com.example.foodhub.ui.GroupSocialButtons
import com.example.foodhub.ui.navigation.AuthScreen
import com.example.foodhub.ui.navigation.Home
import com.example.foodhub.ui.navigation.SignUp
import com.example.foodhub.ui.theme.Orange
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
){
    val emailState = viewModel.email.collectAsStateWithLifecycle()
    val passwordState = viewModel.password.collectAsStateWithLifecycle()
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val isLoading = remember { mutableStateOf(false) }

    val uiState = viewModel.uiState.collectAsState()

    when(uiState.value){
        is LoginViewModel.LoginEvent.Error -> {
            errorMessage.value = "Falied to login"
            isLoading.value = false
        }
        is LoginViewModel.LoginEvent.Loading -> {
            isLoading.value = true
            errorMessage.value= null
        }
        else -> {
            // Handle other states if needed
            isLoading.value = false
            errorMessage.value = null
        }
    }

    val sheetState=rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(errorMessage.value) {
        if (errorMessage.value != null)
            scope.launch {
                showDialog = true
            }
    }

    LaunchedEffect(true) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is LoginViewModel.LoginNavigationEvent.NavigateToHome -> {
                    navController.navigate(Home) {
                        popUpTo(AuthScreen) {
                            inclusive = true
                        }
                    }
                }
               is LoginViewModel.LoginNavigationEvent.NavigateToSignUp -> {
                    navController.navigate(SignUp)
                }
                else -> {
                    // Handle other navigation events if needed
                }
            }
        }
    }



    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id=R.drawable.ic_auth_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.login),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
                    .fillMaxWidth(),
                color = Color.Black
            )

            Spacer(modifier = Modifier.size(20.dp))

            FoodHubTextField(
                value = emailState.value,
                onValueChange = {
                    viewModel.onEmailChange(it)
                },
                label = {
                    Text(
                        text = stringResource(R.string.email),
                        modifier = Modifier.fillMaxWidth(),
                        color= Color.Gray
                    )
                },
                textStyle = LocalTextStyle.current.copy(
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Light
                ),
                modifier = Modifier.fillMaxWidth(),
            )
            FoodHubTextField(
                value = passwordState.value,
                onValueChange = {
                    viewModel.onPasswordChange(it)
                },
                label = {
                    Text(
                        text = stringResource(R.string.password),
                        modifier = Modifier.fillMaxWidth(),
                        color= Color.Gray
                    )
                },
                textStyle = LocalTextStyle.current.copy(
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Light
                ),
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_eye),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = stringResource(R.string.forgot_password),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(8.dp)
                    .fillMaxWidth()
                    .clickable { /*TODO*/ },
                textAlign = TextAlign.Center,
                color = Orange
            )

            Box(modifier = Modifier.height(20.dp)) {
                Text(text = errorMessage.value ?: "", color = Color.Red)
            }
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                onClick = viewModel::onLoginClick,
                modifier = Modifier.height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange
                ),

                ) {
                Box {
                    AnimatedContent(
                        targetState = isLoading.value,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.8f) togetherWith
                                    fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.8f)
                        }
                    ) { target ->

                        if (target) {
                            // show loading indicator
                            CircularProgressIndicator(
                                color = Color.White
                            )
                        }else{
                            Text(
                                text = stringResource(R.string.login),
                                color= Color.White,
                                modifier = Modifier
                                    .padding(horizontal = 32.dp),
                                fontSize = 16.sp
                            )
                        }

                    }

                }

            }
            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.DarkGray)) {
                        append(stringResource(R.string.dont_have_an_account))
                    }
                    withStyle(style = SpanStyle(color = Orange)) {
                        append(" ${stringResource(R.string.sign_up)}")
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        viewModel.onSignUpClick()
                    }
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            GroupSocialButtons(
                color = Color.Black,
                viewModel = viewModel
            )
        }
    }

    if (showDialog) {
        ModalBottomSheet(onDismissRequest = { showDialog = false }, sheetState = sheetState) {
            BasicDialog(
                title = viewModel.error,
                message = viewModel.errorMessage,
                onDismiss = {
                    scope.launch {
                        sheetState.hide()
                        showDialog = false
                    }
                },
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview(){
    LoginScreen(rememberNavController())
}


