package es.elchecf.app.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.elchecf.app.domain.repository.AuthRepository
import es.elchecf.app.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val benefits = profileRepository.getBenefits()
            _uiState.update { it.copy(isLoading = false, benefits = benefits) }
        }
        viewModelScope.launch {
            profileRepository.profile.collectLatest { profile ->
                _uiState.update { it.copy(profile = profile) }
            }
        }
        viewModelScope.launch {
            authRepository.isLoggedIn.collectLatest { loggedIn ->
                _uiState.update { it.copy(isLoggedIn = loggedIn) }
            }
        }
    }

    fun login() {
        viewModelScope.launch { authRepository.login() }
    }

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
    }

    fun updateProfile(
        firstName: String,
        lastName: String,
    ) = profileRepository.updateProfile(firstName, lastName)

    fun updateAvatar(avatarUrl: String) = profileRepository.updateAvatar(avatarUrl)
}
