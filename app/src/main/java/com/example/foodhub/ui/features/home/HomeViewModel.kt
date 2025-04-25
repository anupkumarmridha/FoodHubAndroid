package com.example.foodhub.ui.features.home

import androidx.lifecycle.ViewModel
import com.example.foodhub.data.FoodApi
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val foodApi: FoodApi
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState.Nothing)

    internal sealed class HomeUiState
    {
        object Nothing: HomeUiState()
        object Loading: HomeUiState()
        object Success: HomeUiState()
        object Error: HomeUiState()
    }



}