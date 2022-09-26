package mobg5.g55047.projet.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import mobg5.g55047.projet.database.FirebaseUserLiveData

class HomeViewModel : ViewModel() {
    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}
