package lk.petpet.app.models

data class Pet(
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
    var isFavorite: Boolean = false
)
