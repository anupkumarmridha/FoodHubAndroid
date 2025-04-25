package com.example.foodhub.ui.features.auth
import androidx.lifecycle.viewModelScope
import com.example.foodhub.data.FoodApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor( override val foodApi: FoodApi): BaseAuthViewModel(foodApi) {

    private val _uiState = MutableStateFlow<AuthEvent>(AuthEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AuthNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()


    fun onSignUpClick(){
        viewModelScope.launch {
            _navigationEvent.emit(AuthNavigationEvent.NavigateToSignUp)
        }
    }

    override fun loading() {
        viewModelScope.launch {
            _uiState.value= AuthEvent.Loading
        }
    }

    override fun onGoogleError(msg: String) {
        viewModelScope.launch {
            errorMessage= msg
            error="Google Sign In Error"
            _uiState.value= AuthEvent.Error
            _navigationEvent.emit(AuthNavigationEvent.ShowErrorDialog)
        }
    }

    override fun onFacebookError(msg: String) {
        viewModelScope.launch {
            errorMessage= msg
            error="Google Sign In Error"
            _uiState.value= AuthEvent.Error
            _navigationEvent.emit(AuthNavigationEvent.ShowErrorDialog)
        }
    }

    override fun onSocialLoginSuccess(token: String) {
        viewModelScope.launch {
            _uiState.value = AuthEvent.Success
            _navigationEvent.emit(AuthNavigationEvent.NavigateToHome)
        }
    }


    sealed class AuthNavigationEvent{
        object NavigateToHome: AuthNavigationEvent()
        object NavigateToSignUp: AuthNavigationEvent()
        object ShowErrorDialog: AuthNavigationEvent()
    }

    sealed class AuthEvent{
        object Loading: AuthEvent()
        object Success: AuthEvent()
        object Error: AuthEvent()
        object Nothing: AuthEvent()
    }
}