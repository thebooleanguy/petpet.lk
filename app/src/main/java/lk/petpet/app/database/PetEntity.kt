package lk.petpet.app.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val species: String,
    val breed: String,
    val age: Int,
    val description: String,
    val imageUri: String,
    val latitude: Double,
    val longitude: Double
)
