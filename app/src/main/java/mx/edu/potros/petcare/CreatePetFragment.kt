package mx.edu.potros.petcare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class CreatePetFragment : DialogFragment() {

    private lateinit var btn_add_CP: Button
    private lateinit var name: EditText
    private lateinit var tipo: EditText
    private lateinit var edad: EditText
    private lateinit var color: EditText
    private lateinit var salud: EditText
    private lateinit var mFirestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_create_pet, container, false)
        mFirestore = FirebaseFirestore.getInstance()

        // Inicialización de vistas
        btn_add_CP = v.findViewById(R.id.btn_add_CP)
        name = v.findViewById(R.id.nombre)
        tipo = v.findViewById(R.id.tipo)
        edad = v.findViewById(R.id.edad)
        color = v.findViewById(R.id.color)
        salud = v.findViewById(R.id.salud)


        // Configuración del botón para agregar mascota
        btn_add_CP.setOnClickListener {
            val namePet = name.text.toString().trim()
            val tipoPet = tipo.text.toString().trim()
            val edadPet = edad.text.toString().trim()
            val colorPet = color.text.toString().trim()
            val saludPet = salud.text.toString().trim()

            if (namePet.isEmpty() || edadPet.isEmpty() || colorPet.isEmpty()) {
                // Muestra un mensaje si algún campo está vacío
                // Puedes agregar aquí la lógica adicional según tu requerimiento
            } else {
                postPet(namePet, tipoPet, edadPet, colorPet, saludPet)
            }
        }

        return v
    }

    private fun postPet(namePet: String, tipoPet: String, agePet: String, colorPet: String, saludPet: String) {
        val petMap = hashMapOf(
            "name" to namePet,
            "tipo" to tipoPet,
            "edad" to agePet,
            "color" to colorPet,
            "salud" to saludPet
        )

        mFirestore.collection("pet")
            .add(petMap)
            .addOnSuccessListener {
                // Manejar el éxito aquí

            }
            .addOnFailureListener {
                // Manejar el error aquí
            }
    }
}
