package mobg5.g55047.projet.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import mobg5.g55047.projet.model.History

@Dao
interface HistoryDatabaseDao {
    @Insert
    suspend fun insert(history: History)

    @Query("SELECT * from history WHERE search = :key")
    suspend fun get(key: String): History?

    @Query("DELETE FROM history")
    suspend fun clear()

    @Query("SELECT * FROM history")
    fun getHistory(): LiveData<List<History>>
}
