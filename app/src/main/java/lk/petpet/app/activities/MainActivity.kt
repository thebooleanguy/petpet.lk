package lk.petpet.app.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.petadoption.database.PetDatabase
import com.example.petadoption.database.PetEntity
import com.example.petadoption.databinding.ActivityMainBinding
import com.example.petadoption.utils.ImageUtils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentPhotoPath: String
    private var googleMap: GoogleMap? = null
    private val database by lazy { PetDatabase.getDatabase(this) }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            // All permissions granted
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Handle the captured image
            savePetWithImage(currentPhotoPath)
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                // Handle the picked image
                savePetWithImage(uri.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMap()
        setupClickListeners()
        observePets()
        checkPermissions()
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(com.example.petadoption.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            googleMap = map
            map.setOnMapClickListener { latLng ->
                map.clear()
                map.addMarker(MarkerOptions().position(latLng))
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnCamera.setOnClickListener { takePhoto() }
        binding.btnGallery.setOnClickListener { pickImage() }
    }

    private fun observePets() {
        lifecycleScope.launch {
            database.petDao().getAllPets().collect { pets ->
                // Update UI with pets
                updatePetsList(pets)
            }
        }
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permissions.all { permission ->
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
            }) {
            // All permissions granted
        } else {
            requestPermissionLauncher.launch(permissions)
        }
    }

    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager)?.also {
                val photoFile: File = ImageUtils.createImageFile(this)
                currentPhotoPath = photoFile.absolutePath
                val photoURI: Uri = ImageUtils.getUriForFile(this, photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                takePictureLauncher.launch(intent)
            }
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun savePetWithImage(imagePath: String) {
        val pet = PetEntity(
            name = binding.etPetName.text.toString(),
            species = binding.etSpecies.text.toString(),
            breed = binding.etBreed.text.toString(),
            age = binding.etAge.text.toString().toIntOrNull() ?: 0,
            description = binding.etDescription.text.toString(),
            imageUri = imagePath,
            latitude = googleMap?.cameraPosition?.target?.latitude ?: 0.0,
            longitude = googleMap?.cameraPosition?.target?.longitude ?: 0.0
        )

        lifecycleScope.launch {
            database.petDao().insertPet(pet)
        }
    }

    private fun updatePetsList(pets: List<PetEntity>) {
        // Update RecyclerView with pets
        // You'll need to implement a RecyclerView adapter
        googleMap?.clear()
        pets.forEach { pet ->
            googleMap?.addMarker(
                MarkerOptions()
                    .position(LatLng(pet.latitude, pet.longitude))
                    .title(pet.name)
            )
        }
    }
}