package lk.petpet.app.activities

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.petadoption.database.PetDatabase
import com.example.petadoption.databinding.ActivityPetDetailBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class PetDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetDetailBinding
    private var googleMap: GoogleMap? = null
    private val database by lazy { PetDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val petId = intent.getLongExtra("pet_id", -1)
        if (petId != -1L) {
            setupMap()
            loadPetDetails(petId)
        } else {
            finish()
        }
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(com.example.petadoption.R.id.mapDetail) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            googleMap = map
        }
    }

    private fun loadPetDetails(petId: Long) {
        lifecycleScope.launch {
            val pet = database.petDao().getPetById(petId)
            pet?.let {
                binding.apply {
                    tvPetName.text = it.name
                    tvSpecies.text = "Species: ${it.species}"
                    tvBreed.text = "Breed: ${it.breed}"
                    tvAge.text = "Age: ${it.age} years"
                    tvDescription.text = it.description

                    try {
                        ivPetImage.setImageURI(Uri.parse(it.imageUri))
                    } catch (e: Exception) {
                        // Handle error
                    }

                    // Update map
                    val petLocation = LatLng(it.latitude, it.longitude)
                    googleMap?.apply {
                        addMarker(MarkerOptions().position(petLocation).title(it.name))
                        moveCamera(CameraUpdateFactory.newLatLngZoom(petLocation, 15f))
                    }
                }
            }
        }
    }
}