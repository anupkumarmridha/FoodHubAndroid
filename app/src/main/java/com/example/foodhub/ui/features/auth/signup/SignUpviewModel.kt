package com.example.foodhub.ui.features.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodhub.data.FoodApi
import com.example.foodhub.data.models.SignUpRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpviewModel @Inject constructor(val foodApi: FoodApi) : ViewModel() {

    private val _uiState = MutableStateFlow<SignUpEvent>(SignUpEvent.Nothing)
    val uiState = _uiState.asStateFlow()


    private val _navigationEvent = MutableSharedFlow<SignUpNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _email = MutableStateFlow<String>("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow<String>("")
    val password = _password.asStateFlow()

    private val _name = MutableStateFlow<String>("")
    val name = _name.asStateFlow()


    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onSignUpClick() {

        viewModelScope.launch {
            if (email.value.isNotEmpty() && password.value.isNotEmpty() && name.value.isNotEmpty()) {
                _uiState.value = SignUpEvent.Loading
                try {
                    val response = foodApi.signUp(
                        SignUpRequest(
                            name = name.value,
                            email = email.value,
                            password = password.value
                        )
                    )
                    if(response.token.isNotEmpty()){
                        _uiState.value = SignUpEvent.Success
                        _navigationEvent.emit(SignUpNavigationEvent.NavigationToHome)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    _uiState.value= SignUpEvent.Error
                }
            } else {
                _uiState.value = SignUpEvent.Error
            }
        }

    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SignUpNavigationEvent.NavigationToLogin)
        }
    }

    sealed class SignUpNavigationEvent {
        object NavigationToLogin: SignUpNavigationEvent()
        object NavigationToHome: SignUpNavigationEvent()
    }

    sealed class SignUpEvent{
        object Nothing: SignUpEvent()
        object Success: SignUpEvent()
        object Error: SignUpEvent()
        object Loading: SignUpEvent()
    }
}