package lk.petpet.app.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import lk.petpet.app.models.Pet

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val species: String,
    val breed: String,
    val age: String,
    val weight: String,
    val sex: String,
    val description: String,
    val imageUri: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val isFavorite: Boolean = false
) {
    fun toPet() = Pet(
        id = id,
        name = name,
        species = species,
        breed = breed,
        age = age,
        weight = weight,
        sex = sex,
        description = description,
        imageUri = imageUri,
        location = location,
        latitude = latitude,
        longitude = longitude,
        isFavorite = isFavorite
    )
}
