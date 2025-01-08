package lk.petpet.app.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petadoption.activities.PetDetailActivity
import com.example.petadoption.database.PetEntity
import com.example.petadoption.databinding.ItemPetBinding

class PetListAdapter : ListAdapter<PetEntity, PetListAdapter.PetViewHolder>(PetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val binding = ItemPetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PetViewHolder(
        private val binding: ItemPetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pet: PetEntity) {
            binding.apply {
                tvPetNameList.text = pet.name
                tvPetInfoList.text = "${pet.species} - ${pet.breed}, ${pet.age} years"
                
                // Load image
                try {
                    ivPetThumb.setImageURI(Uri.parse(pet.imageUri))
                } catch (e: Exception) {
                    // Handle error
                }

                // Set click listener
                root.setOnClickListener {
                    val intent = Intent(root.context, PetDetailActivity::class.java).apply {
                        putExtra("pet_id", pet.id)
                    }
                    root.context.startActivity(intent)
                }
            }
        }
    }

    private class PetDiffCallback : DiffUtil.ItemCallback<PetEntity>() {
        override fun areItemsTheSame(oldItem: PetEntity, newItem: PetEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PetEntity, newItem: PetEntity): Boolean {
            return oldItem == newItem
        }
    }
}