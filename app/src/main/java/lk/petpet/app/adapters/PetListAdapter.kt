package lk.petpet.app.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import lk.petpet.app.databinding.ItemPetBinding
import lk.petpet.app.models.Pet

class PetListAdapter : ListAdapter<Pet, PetListAdapter.PetViewHolder>(PetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        return PetViewHolder(
            ItemPetBinding.inflate(
                LayoutInflater.from(parent.context),
                                   parent,
                                   false
            )
        )
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PetViewHolder(
        private val binding: ItemPetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pet: Pet) {
            binding.apply {
                tvPetNameList.text = pet.name
                tvPetInfoList.text = "${pet.breed} • ${pet.age}"

                try {
                    ivPetThumb.setImageURI(Uri.parse(pet.imageUri))
                } catch (e: Exception) {
                    // Handle error loading image
                }

                root.setOnClickListener {
                    val intent = Intent(root.context, PetDetailActivity::class.java).apply {
                        putExtra("pet_id", pet.id)
                    }
                    root.context.startActivity(intent)
                }
            }
        }
    }

    private class PetDiffCallback : DiffUtil.ItemCallback<Pet>() {
        override fun areItemsTheSame(oldItem: Pet, newItem: Pet) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Pet, newItem: Pet) = oldItem == newItem
    }
}
