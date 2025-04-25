package com.example.foodhub.ui.features.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodhub.R
import com.example.foodhub.ui.BasicDialog
import com.example.foodhub.ui.GroupSocialButtons
import com.example.foodhub.ui.navigation.AuthScreen
import com.example.foodhub.ui.navigation.Home
import com.example.foodhub.ui.navigation.Login
import com.example.foodhub.ui.navigation.SignUp
import com.example.foodhub.ui.theme.Orange
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthScreenViewModel = hiltViewModel()
    ) {

    val sheetState=rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showDialog by remember {
        mutableStateOf(false)
    }

    val imageSize = remember {
        mutableStateOf(IntSize.Zero)
    }
    val brush = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            Color.Black
        ),
        startY = imageSize.value.height.toFloat()/3,
    )

    LaunchedEffect(true) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is AuthScreenViewModel.AuthNavigationEvent.NavigateToHome -> {
                    navController.navigate(Home) {
                        popUpTo(AuthScreen) {
                            inclusive = true
                        }
                    }
                }
                is AuthScreenViewModel.AuthNavigationEvent.NavigateToSignUp -> {
                    navController.navigate(SignUp)
                }
                is AuthScreenViewModel.AuthNavigationEvent.ShowErrorDialog -> {
                    showDialog = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
      Image(
          painter = painterResource(id=R.drawable.background),
          contentDescription = null,
          modifier = Modifier.onGloballyPositioned{
                imageSize.value = it.size
          }.alpha(0.6f),
          contentScale = ContentScale.FillBounds
      )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(brush=brush)
        )

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ){
            Text(
                text = stringResource(id=R.string.skip),
                color = Orange
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(top=110.dp).padding(16.dp),
        ) {
            Text(
                text = stringResource(id = R.string.welcome),
                color = Color.Black,
                modifier = Modifier,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.food_hub),
                color = Orange,
                modifier = Modifier,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.food_hub_desc),
                color = Color.DarkGray,
                modifier = Modifier.padding(top=8.dp),
                fontSize = 20.sp
            )
        }


        Column(
            modifier = Modifier.fillMaxWidth()
                .align(alignment = Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            GroupSocialButtons(viewModel=viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate(SignUp)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.White)
            ) {
                Text(
                    text = stringResource(id = R.string.sign_up_with_email),
                    color = Color.White
                )
            }
            TextButton(onClick = {
                navController.navigate(Login)
            }) {
                Text(
                    text = stringResource(id = R.string.already_have_an_account),
                    color = Color.White
                )
            }

        }

    }

    if(showDialog){
        ModalBottomSheet(
            onDismissRequest = {showDialog=false},
            sheetState = sheetState,
        ) {
            BasicDialog(
                title = viewModel.error,
                message = viewModel.errorMessage,
                onDismiss = {
                    scope.launch {
                        sheetState.hide()
                        showDialog = false
                    }
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    AuthScreen(rememberNavController())
}