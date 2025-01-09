package lk.petpet.app.activities

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import lk.petpet.app.adapters.PetListAdapter
import lk.petpet.app.database.DatabaseHelper
import lk.petpet.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
        private lateinit var petAdapter: PetListAdapter
            private lateinit var dbHelper: DatabaseHelper
                private var currentSpecies: String = "All"

                    override fun onCreate(savedInstanceState: Bundle?) {
                        super.onCreate(savedInstanceState)
                        binding = ActivityMainBinding.inflate(layoutInflater)
                        setContentView(binding.root)

                        dbHelper = DatabaseHelper(this)
                        setupRecyclerView()
                        setupChipGroup()
                        setupSearchBar()
                        loadPets()
                    }

                    private fun setupRecyclerView() {
                        petAdapter = PetListAdapter()
                        binding.recyclerPets.apply {
                            layoutManager = GridLayoutManager(this@MainActivity, 2)
                            adapter = petAdapter
                        }
                    }

                    private fun setupChipGroup() {
                        binding.chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
                            currentSpecies = when (checkedIds.firstOrNull()) {
                                binding.chipDogs.id -> "Dog"
                                binding.chipCats.id -> "Cat"
                                binding.chipBirds.id -> "Bird"
                                else -> "All"
                            }
                            loadPets()
                        }
                    }

                    private fun setupSearchBar() {
                        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                return true
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                // Implement search filtering
                                return true
                            }
                        })
                    }

                    private fun loadPets() {
                        val pets = if (currentSpecies == "All") {
                            dbHelper.getAllPets()
                        } else {
                            dbHelper.getPetsBySpecies(currentSpecies)
                        }
                        petAdapter.submitList(pets)
                    }

                    override fun onResume() {
                        super.onResume()
                        loadPets()
                    }
}
