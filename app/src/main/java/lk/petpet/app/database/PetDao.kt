package lk.petpet.app.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pets")
    fun getAllPets(): Flow<List<PetEntity>>
    
    @Insert
    suspend fun insertPet(pet: PetEntity)
    
    @Query("SELECT * FROM pets WHERE id = :petId")
    suspend fun getPetById(petId: Long): PetEntity?
}