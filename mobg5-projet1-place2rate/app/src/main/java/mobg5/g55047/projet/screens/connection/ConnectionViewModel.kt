package mobg5.g55047.projet.screens.connection

import android.app.Application
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mobg5.g55047.projet.model.User
import mobg5.g55047.projet.database.UserDatabaseDao
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConnectionViewModel(
    val database: UserDatabaseDao,
    application: Application
) : AndroidViewModel(application) {
    private val users = database.getAllUsers()

    val usersString = Transformations.map(users) { users ->
        users.map { user -> user.email }
    }

    private val _validEmail = MutableLiveData<Boolean>()
    val validEmail: MutableLiveData<Boolean>
        get() = _validEmail

    val mail = MutableLiveData<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun isMailValid(): Boolean {
        if (!TextUtils.isEmpty(mail.value) && android.util.Patterns.EMAIL_ADDRESS.matcher(mail.value)
                .matches()
        ) {
            viewModelScope.launch {
                val userInDatabase = mail.value?.let { database.get(it) }
                val timeNow = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")
                if (userInDatabase != null) {
                    mail.value?.let { User(userInDatabase.userId, it, timeNow.format(formatter)) }
                        ?.let { update(it) }
                } else {
                    val userToAdd = mail.value?.let { User(0, it, timeNow.format(formatter)) }
                    if (userToAdd != null) {
                        insert(userToAdd)
                    }
                }
            }
            return true
        } else {
            return false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onConnect() {
        validEmail.value = isMailValid()
    }

    private suspend fun update(user: User) {
        database.update(user)
    }

    private suspend fun insert(user: User) {
        database.insert(user)
    }
}
