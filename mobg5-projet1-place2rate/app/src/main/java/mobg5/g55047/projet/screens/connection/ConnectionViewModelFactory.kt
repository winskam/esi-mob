package mobg5.g55047.projet.screens.connection

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mobg5.g55047.projet.database.UserDatabaseDao

class ConnectionViewModelFactory(
    private val dataSource: UserDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConnectionViewModel::class.java)) {
            return ConnectionViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
