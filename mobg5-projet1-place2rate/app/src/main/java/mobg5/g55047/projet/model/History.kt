package mobg5.g55047.projet.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History(
    @PrimaryKey()
    var search: String = "",
)
