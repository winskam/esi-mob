package mobg5.g55047.projet.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0L,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "date")
    var date: String,
)
