package com.example.foodhub.ui.features.auth
import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodhub.data.FoodApi
import com.example.foodhub.data.auth.GoogleAuthUiProvider
import com.example.foodhub.data.models.OAuthRequest
import com.example.foodhub.data.remote.ApiResponse
import com.example.foodhub.data.remote.safeApiCall
import kotlinx.coroutines.launch

abstract class BaseAuthViewModel(
    open val foodApi: FoodApi
) : ViewModel() {

    var error: String = ""
    var errorMessage: String = ""

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
                fetchFoodAppToken(response.token, "google") {
                    onGoogleError(it)
                }
            }catch (e: Throwable){
                onGoogleError(e.message.toString())
            }
        }
    }

    protected fun initiateFacebookSignIn(context: ComponentActivity) {
        TODO("Not yet implemented")
    }

    fun fetchFoodAppToken(token: String, provider: String, onError: (String) -> Unit) {
        viewModelScope.launch {
            val request = OAuthRequest(
                provider = provider,
                token = token,
            )
            val res = safeApiCall { foodApi.oAuth(request) }
            when(res){
                is ApiResponse.Success -> {
                    if (res.data.token.isNotEmpty()) {
                        onSocialLoginSuccess(res.data.token)
                    } else {
                        onError("Error signing in with $provider")
                    }
                }
                is ApiResponse.Error -> {
                    val error = res.code
                    if(error != null){
                        when(error){
                            401 -> onError("Unauthorized")
                            403 -> onError("Forbidden")
                            404 -> onError("Not Found")
                            500 -> onError("Internal Server Error")
                            else -> onError("Unknown error")
                        }
                    }
                    onError("Error signing in with $provider")
                }
                is ApiResponse.Exception -> {
                    onError("Exception: ${res.exception.message}")
                }
            }
        }
    }
}