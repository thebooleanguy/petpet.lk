// MainActivity.kt
package lk.petpet.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import lk.petpet.app.adapters.PetListAdapter
import lk.petpet.app.database.PetDatabase
import lk.petpet.app.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
        private lateinit var petAdapter: PetListAdapter
            private val database by lazy { PetDatabase.getDatabase(this) }
            private var currentSpecies: String = "All"

                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)
                    binding = ActivityMainBinding.inflate(layoutInflater)
                    setContentView(binding.root)

                    setupRecyclerView()
                    setupChipGroup()
                    setupSearchBar()
                    observePets()
                }

                private fun setupRecyclerView() {
                    petAdapter = PetListAdapter()
                    binding.recyclerPets.apply {
                        layoutManager = GridLayoutManager(this@MainActivity, 2)
                        adapter = petAdapter
                    }
                }

                private fun setupChipGroup() {
                    binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                        currentSpecies = when (checkedIds.firstOrNull()) {
                            binding.chipDogs.id -> "Dog"
                            binding.chipCats.id -> "Cat"
                            binding.chipBirds.id -> "Bird"
                            else -> "All"
                        }
                        observePets()
                    }
                }

                private fun setupSearchBar() {
                    binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            // Implement search functionality
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            // Implement search filtering
                            return true
                        }
                    })
                }

                private fun observePets() {
                    lifecycleScope.launch {
                        if (currentSpecies == "All") {
                            database.petDao().getAllPets()
                        } else {
                            database.petDao().getPetsBySpecies(currentSpecies)
                        }.collectLatest { pets ->
                            petAdapter.submitList(pets.map { it.toPet() })
                        }
                    }
                }
}
