package mobg5.g55047.projet.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import mobg5.g55047.projet.model.User

@Dao
interface UserDatabaseDao {
    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * from user WHERE email = :key")
    suspend fun get(key: String): User?

    @Query("DELETE FROM user")
    suspend fun clear()

    @Query("SELECT * FROM user ORDER BY email ASC")
    fun getAllUsers(): LiveData<List<User>>
}
