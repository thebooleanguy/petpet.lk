package lk.petpet.app.activities

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import lk.petpet.app.database.PetDatabase
import lk.petpet.app.databinding.ActivityPetDetailBinding
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

                setupToolbar()
                setupMap()
                loadPetDetails(intent.getLongExtra("pet_id", -1))
            }

            private fun setupToolbar() {
                binding.btnBack.setOnClickListener { finish() }
            }

            private fun setupMap() {
                val mapFragment = supportFragmentManager
                .findFragmentById(R.id.mapDetail) as SupportMapFragment
                mapFragment.getMapAsync { map ->
                    googleMap = map
                }
            }

            private fun loadPetDetails(petId: Long) {
                if (petId == -1L) {
                    finish()
                    return
                }

                lifecycleScope.launch {
                    database.petDao().getPetById(petId)?.let { petEntity ->
                        val pet = petEntity.toPet()
                        binding.apply {
                            tvPetName.text = pet.name
                            tvBreed.text = pet.breed

                            chipAge.text = pet.age
                            chipWeight.text = pet.weight
                            chipSex.text = pet.sex

                            tvDescription.text = pet.description

                            try {
                                ivPetImage.setImageURI(Uri.parse(pet.imageUri))
                            } catch (e: Exception) {
                                // Handle error loading image
                            }

                            val petLocation = LatLng(pet.latitude, pet.longitude)
                            googleMap?.apply {
                                addMarker(MarkerOptions().position(petLocation))
                                moveCamera(CameraUpdateFactory.newLatLngZoom(petLocation, 15f))
                            }

                            btnAdopt.setOnClickListener {
                                // Implement adoption process
                            }

                            btnCall.setOnClickListener {
                                // Implement call functionality
                            }
                        }
                    }
                }
            }
}
