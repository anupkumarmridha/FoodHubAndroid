package com.example.foodhub.ui.features.auth.login
import androidx.lifecycle.viewModelScope
import com.example.foodhub.data.FoodApi
import com.example.foodhub.data.models.LoginRequest
import com.example.foodhub.ui.features.auth.BaseAuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(override val foodApi: FoodApi): BaseAuthViewModel(foodApi) {

    private val _uiState = MutableStateFlow<LoginEvent>(LoginEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<LoginNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _email = MutableStateFlow<String>("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow<String>("")
    val password = _password.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onLoginClick(){
        viewModelScope.launch {
            if(email.value.isNotEmpty() && password.value.isNotEmpty()){
                _uiState.value= LoginEvent.Loading

                try{
                    val response = foodApi.login(
                        LoginRequest(
                            email = email.value,
                            password = password.value
                        )
                    )
                    if(response.token.isNotEmpty()){
                        _uiState.value= LoginEvent.Success
                        _navigationEvent.emit(LoginNavigationEvent.NavigateToHome)
                    }
                }
                catch (e: Exception){
                    e.printStackTrace()
                    _uiState.value=LoginEvent.Error
                }
            }
        }
    }

    fun onSignUpClick(){
        viewModelScope.launch {
            _navigationEvent.emit(LoginNavigationEvent.NavigateToSignUp)
        }
    }

    fun onForgotPasswordClick() {
        TODO("Not yet implemented")
    }


    override fun loading() {
        viewModelScope.launch {
            _uiState.value= LoginEvent.Loading
        }
    }

    override fun onGoogleError(msg: String) {
        viewModelScope.launch {
            _uiState.value= LoginEvent.Error
        }
    }

    override fun onFacebookError(msg: String) {
        viewModelScope.launch {
            _uiState.value= LoginEvent.Error
        }
    }

    override fun onSocialLoginSuccess(token: String) {
        viewModelScope.launch {
            _uiState.value = LoginEvent.Success
            _navigationEvent.emit(LoginNavigationEvent.NavigateToHome)
        }
    }


    sealed class LoginNavigationEvent{
        object NavigateToHome: LoginNavigationEvent()
        object NavigateToSignUp: LoginNavigationEvent()
    }

    sealed class LoginEvent{
        object Loading: LoginEvent()
        object Success: LoginEvent()
        object Error: LoginEvent()
        object Nothing: LoginEvent()
    }
}