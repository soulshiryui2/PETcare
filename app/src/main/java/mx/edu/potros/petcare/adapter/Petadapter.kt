package mx.edu.potros.petcare.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.potros.petcare.CreatePetActivity
import mx.edu.potros.petcare.model.Pet
import java.text.DecimalFormat
import mx.edu.potros.petcare.R

class PetAdapter(options: FirestoreRecyclerOptions<Pet>, private val activity: Activity, private val fm: FragmentManager) :
    FirestoreRecyclerAdapter<Pet, PetAdapter.ViewHolder>(options) {

    private val mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onBindViewHolder(holder: ViewHolder, position: Int, pet: Pet) {
        val format = DecimalFormat("0.00")
        val documentSnapshot: DocumentSnapshot = snapshots.getSnapshot(holder.adapterPosition)
        val id = documentSnapshot.id

        holder.name.text = pet.name
        holder.tipo.text = pet.tipo
        holder.age.text = pet.edad
        holder.color.text = pet.color
        holder.salud.text = pet.salud


        holder.btn_delete.setOnClickListener {
            deletePet(id)
        }
        holder.btn_edit.setOnClickListener {
            val intent = Intent(activity, CreatePetActivity::class.java)
            intent.putExtra("id_pet", id)
            activity.startActivity(intent)
        }

    }

    private fun deletePet(id: String) {
        mFirestore.collection("pet").document(id).delete()
            .addOnSuccessListener {
                Toast.makeText(activity, "Eliminado correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.view_pet_single, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nombre)
        val tipo: TextView = itemView.findViewById(R.id.tipo)
        val age: TextView = itemView.findViewById(R.id.edad)
        val color: TextView = itemView.findViewById(R.id.color)
        val salud: TextView = itemView.findViewById(R.id.salud)
        val btn_delete: ImageView = itemView.findViewById(R.id.btn_eliminar)
       val btn_edit: ImageView=itemView.findViewById(R.id.btn_editar)
    }
}









