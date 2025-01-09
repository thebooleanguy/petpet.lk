package lk.petpet.app.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import lk.petpet.app.models.Pet

class DatabaseHelper(context: Context) :
SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "PetDatabase"
        private const val DATABASE_VERSION = 1
        private const val TABLE_PETS = "pets"

        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_SPECIES = "species"
        private const val KEY_BREED = "breed"
        private const val KEY_AGE = "age"
        private const val KEY_WEIGHT = "weight"
        private const val KEY_SEX = "sex"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_IMAGE_URI = "image_uri"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
        private const val KEY_FAVORITE = "favorite"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
        CREATE TABLE $TABLE_PETS (
            $KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $KEY_NAME TEXT,
            $KEY_SPECIES TEXT,
            $KEY_BREED TEXT,
            $KEY_AGE TEXT,
            $KEY_WEIGHT TEXT,
            $KEY_SEX TEXT,
            $KEY_DESCRIPTION TEXT,
            $KEY_IMAGE_URI TEXT,
            $KEY_LOCATION TEXT,
            $KEY_LATITUDE REAL,
            $KEY_LONGITUDE REAL,
            $KEY_FAVORITE INTEGER DEFAULT 0
            )
            """.trimIndent()
            db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PETS")
        onCreate(db)
    }

    fun insertPet(pet: Pet): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, pet.name)
            put(KEY_SPECIES, pet.species)
            put(KEY_BREED, pet.breed)
            put(KEY_AGE, pet.age)
            put(KEY_WEIGHT, pet.weight)
            put(KEY_SEX, pet.sex)
            put(KEY_DESCRIPTION, pet.description)
            put(KEY_IMAGE_URI, pet.imageUri)
            put(KEY_LOCATION, pet.location)
            put(KEY_LATITUDE, pet.latitude)
            put(KEY_LONGITUDE, pet.longitude)
            put(KEY_FAVORITE, if (pet.isFavorite) 1 else 0)
        }
        return db.insert(TABLE_PETS, null, values)
    }

    fun getAllPets(): List<Pet> {
        val petsList = mutableListOf<Pet>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_PETS, null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val pet = Pet(
                    id = getLong(getColumnIndexOrThrow(KEY_ID)),
                              name = getString(getColumnIndexOrThrow(KEY_NAME)),
                              species = getString(getColumnIndexOrThrow(KEY_SPECIES)),
                              breed = getString(getColumnIndexOrThrow(KEY_BREED)),
                              age = getString(getColumnIndexOrThrow(KEY_AGE)),
                              weight = getString(getColumnIndexOrThrow(KEY_WEIGHT)),
                              sex = getString(getColumnIndexOrThrow(KEY_SEX)),
                              description = getString(getColumnIndexOrThrow(KEY_DESCRIPTION)),
                              imageUri = getString(getColumnIndexOrThrow(KEY_IMAGE_URI)),
                              location = getString(getColumnIndexOrThrow(KEY_LOCATION)),
                              latitude = getDouble(getColumnIndexOrThrow(KEY_LATITUDE)),
                              longitude = getDouble(getColumnIndexOrThrow(KEY_LONGITUDE)),
                              isFavorite = getInt(getColumnIndexOrThrow(KEY_FAVORITE)) == 1
                )
                petsList.add(pet)
            }
        }
        cursor.close()
        return petsList
    }

    fun getPetsBySpecies(species: String): List<Pet> {
        val petsList = mutableListOf<Pet>()
        val db = this.readableDatabase
        val selection = "$KEY_SPECIES = ?"
        val selectionArgs = arrayOf(species)
        val cursor = db.query(TABLE_PETS, null, selection, selectionArgs, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val pet = Pet(
                    id = getLong(getColumnIndexOrThrow(KEY_ID)),
                              name = getString(getColumnIndexOrThrow(KEY_NAME)),
                              species = getString(getColumnIndexOrThrow(KEY_SPECIES)),
                              breed = getString(getColumnIndexOrThrow(KEY_BREED)),
                              age = getString(getColumnIndexOrThrow(KEY_AGE)),
                              weight = getString(getColumnIndexOrThrow(KEY_WEIGHT)),
                              sex = getString(getColumnIndexOrThrow(KEY_SEX)),
                              description = getString(getColumnIndexOrThrow(KEY_DESCRIPTION)),
                              imageUri = getString(getColumnIndexOrThrow(KEY_IMAGE_URI)),
                              location = getString(getColumnIndexOrThrow(KEY_LOCATION)),
                              latitude = getDouble(getColumnIndexOrThrow(KEY_LATITUDE)),
                              longitude = getDouble(getColumnIndexOrThrow(KEY_LONGITUDE)),
                              isFavorite = getInt(getColumnIndexOrThrow(KEY_FAVORITE)) == 1
                )
                petsList.add(pet)
            }
        }
        cursor.close()
        return petsList
    }

    fun getPetById(id: Long): Pet? {
        val db = this.readableDatabase
        val selection = "$KEY_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = db.query(TABLE_PETS, null, selection, selectionArgs, null, null, null)

        return if (cursor.moveToFirst()) {
            val pet = Pet(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)),
                          name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                          species = cursor.getString(cursor.getColumnIndexOrThrow(KEY_SPECIES)),
                          breed = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BREED)),
                          age = cursor.getString(cursor.getColumnIndexOrThrow(KEY_AGE)),
                          weight = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WEIGHT)),
                          sex = cursor.getString(cursor.getColumnIndexOrThrow(KEY_SEX)),
                          description = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                          imageUri = cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE_URI)),
                          location = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION)),
                          latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)),
                          longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE)),
                          isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_FAVORITE)) == 1
            )
            cursor.close()
            pet
        } else {
            cursor.close()
            null
        }
    }
}
