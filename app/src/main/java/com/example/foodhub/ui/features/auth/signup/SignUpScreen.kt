package com.example.foodhub.ui.features.auth.signup

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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodhub.R
import com.example.foodhub.ui.FoodHubTextField
import com.example.foodhub.ui.GroupSocialButtons
import com.example.foodhub.ui.theme.Orange

@Composable
fun SignUpScreen() {
    val nameState = remember { mutableStateOf(TextFieldValue("")) }
    val emailState = remember { mutableStateOf(TextFieldValue("")) }
    val passwordState = remember { mutableStateOf(TextFieldValue("")) }

    Box(modifier = Modifier.fillMaxSize())
    {
        Image(
            painter = painterResource(id = R.drawable.ic_auth_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier.fillMaxSize()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.sign_up),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                color = Color.Black
            )
            Spacer(modifier = Modifier.size(20.dp))
            FoodHubTextField(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                label = {
                    Text(
                        text = stringResource(R.string.full_name),
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray
                    )
                },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                modifier = Modifier.fillMaxWidth(),
            )

            FoodHubTextField(
                value=emailState.value,
                onValueChange = { emailState.value = it },
                label = {
                    Text(
                        text = stringResource(R.string.email),
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray
                    )
                },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                modifier = Modifier.fillMaxWidth(),
            )

            FoodHubTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = {
                    Text(
                        text = stringResource(R.string.password),
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray

                    )

                },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),

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
            Button(
                onClick = { },
                modifier = Modifier.height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange
                ),

            ) {
                Text(
                    text = stringResource(R.string.sign_up),
                    modifier = Modifier.padding(horizontal = 32.dp),
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text= stringResource(R.string.already_have_an_account),
                modifier = Modifier.padding(8.dp)
                    .clickable {}
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            GroupSocialButtons(onFacebookClick = { /*TODO*/ }, onGoogleClick = { /*TODO*/ }, color = Color.Black)
        }
    }
}

@Preview
@Composable
fun PreviewSignUpScreen() {
    SignUpScreen()
}