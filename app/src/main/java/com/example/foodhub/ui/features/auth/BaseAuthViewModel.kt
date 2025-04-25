package com.example.foodhub.ui.features.auth
import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodhub.data.FoodApi
import com.example.foodhub.data.auth.GoogleAuthUiProvider
import com.example.foodhub.data.models.OAuthRequest
import kotlinx.coroutines.launch

abstract class BaseAuthViewModel(
    open val foodApi: FoodApi
) : ViewModel() {

    private val googleAuthUiProvider = GoogleAuthUiProvider()

    abstract fun loading()
    abstract fun onGoogleError(msg: String)
    abstract fun onFacebookError(msg: String)
    abstract fun onSocialLoginSuccess(token: String)

    fun onGoogleSignInClick(context: ComponentActivity) {
        initiateGoogleSignIn(context)
    }

    fun onFacebookSignInClick(context: ComponentActivity) {
        initiateFacebookSignIn(context)
    }

    protected fun initiateGoogleSignIn(context: ComponentActivity) {
        viewModelScope.launch {
            loading()
            try {

                val response =googleAuthUiProvider.signIn(
                    context,
                    CredentialManager.create(context)
                )

                if (response != null) {

                    val request = OAuthRequest(
                        provider = "google",
                        token = response.token,
                    )

                    val res = foodApi.oAuth(request)

                    if (res.token.isNotEmpty()) {
                        onSocialLoginSuccess(res.token)
                    }else{
                       onGoogleError("Error signing in with Google")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onGoogleError("Error signing in with Google")
            }
        }
    }

    protected fun initiateFacebookSignIn(context: ComponentActivity) {
        TODO("Not yet implemented")
    }
}