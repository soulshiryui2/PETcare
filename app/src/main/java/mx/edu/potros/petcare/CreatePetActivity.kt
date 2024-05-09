package mx.edu.potros.petcare

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class CreatePetActivity : AppCompatActivity() {

    private lateinit var photo_pet: ImageView
    private lateinit var btn_add: Button
    private lateinit var btn_cu_photo: Button
    private lateinit var btn_r_photo: Button
    private lateinit var linearLayout_image_btn: LinearLayout
    private lateinit var name: EditText
    private lateinit var edad: EditText
    private lateinit var color: EditText
    private lateinit var precio_vacuna: EditText
    private lateinit var mfirestore: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    private lateinit var storageReference: StorageReference
    private val storage_path = "pet/*"

    private val COD_SEL_STORAGE = 200
    private val COD_SEL_IMAGE = 300

    private var image_url: Uri? = null
    private val photo = "photo"
    private var idd: String? = null

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pet)
        val id: String? = intent.getStringExtra("id_pet")
        title = "Mascota"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        progressDialog = ProgressDialog(this)
        mfirestore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        linearLayout_image_btn = findViewById(R.id.images_btn)
        name = findViewById(R.id.nombre)
        edad = findViewById(R.id.edad)
        color = findViewById(R.id.color)
        photo_pet = findViewById(R.id.pet_photo)
        btn_cu_photo = findViewById(R.id.btn_photo)
        btn_r_photo = findViewById(R.id.btn_remove_photo)
        btn_add = findViewById(R.id.btn_add)

        btn_cu_photo.setOnClickListener {
            uploadPhoto()
        }

        btn_r_photo.setOnClickListener {
            val map = hashMapOf<String, Any>("photo" to "")
            mfirestore.collection("pet").document(idd!!).update(map)
            Toast.makeText(this@CreatePetActivity, "Foto eliminada", Toast.LENGTH_SHORT).show()
        }

        if (id == null || id == "") {
            linearLayout_image_btn.visibility = View.GONE
            btn_add.setOnClickListener {
                val namepet = name.text.toString().trim()
                val edadpet = edad.text.toString().trim()
                val colorpet = color.text.toString().trim()

                if (namepet.isEmpty() && edadpet.isEmpty() && colorpet.isEmpty()) {
                    Toast.makeText(applicationContext, "Ingresar los datos", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    postPet(namepet, edadpet, colorpet)
                }
            }
        } else {
            idd = id
            btn_add.text = "Update"
          /*  getPet(id)*/
            btn_add.setOnClickListener {
                val namepet = name.text.toString().trim()
                val edadpet = edad.text.toString().trim()
                val colorpet = color.text.toString().trim()

                if (namepet.isEmpty() && edadpet.isEmpty() && colorpet.isEmpty()) {
                    Toast.makeText(applicationContext, "Ingresar los datos", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    updatePet(namepet, edadpet, colorpet, id)
                }
            }
        }
    }

    private fun uploadPhoto() {
        val i = Intent(Intent.ACTION_PICK)
        i.type = "image/*"
        startActivityForResult(i, COD_SEL_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == COD_SEL_IMAGE) {
                image_url = data?.data
                subirPhoto(image_url!!)
            }
        }
    }

    private fun subirPhoto(image_url: Uri) {
        progressDialog.setMessage("Actualizando foto")
        progressDialog.show()
        val rute_storage_photo = "$storage_path$photo${mAuth.uid}$idd"
        val reference = storageReference.child(rute_storage_photo)
        reference.putFile(image_url).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            uriTask.addOnSuccessListener { uri ->
                val download_uri = uri.toString()
                val map = hashMapOf<String, Any>("photo" to download_uri)
                mfirestore.collection("pet").document(idd!!).update(map)
                    .addOnSuccessListener {
                        Toast.makeText(this@CreatePetActivity, "Foto actualizada", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this@CreatePetActivity, "Error al cargar foto", Toast.LENGTH_SHORT).show()
        }
    }

  private fun postPet(
        namepet: String,
        edadpet: String,
        colorpet: String,
    ) {
        val idUser = mAuth.currentUser!!.uid
        val id = mfirestore.collection("pet").document()
        val map: MutableMap<String, Any> = java.util.HashMap()
        map["id_user"] = idUser
        map["id"] = id.id
        map["name"] = namepet
        map["edad"] = edadpet
        map["color"] = colorpet
        mfirestore.collection("pet").document(id.id).set(map).addOnSuccessListener {
            Toast.makeText(applicationContext, "Creado exitosamente", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(
                applicationContext,
                "Error al ingresar",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

  /*  private fun getPet(id: String) {
        mfirestore.collection("pet").document(id).get().addOnSuccessListener { documentSnapshot ->
            val format = DecimalFormat("0.00")
            val namePet = documentSnapshot.getString("name")
            val agePet = documentSnapshot.getString("edad")
            val colorPet = documentSnapshot.getString("color")
            val precioVacunaPet = documentSnapshot.getDouble("vaccine_price")
            val photoPet = documentSnapshot.getString("photo")

            name.setText(namePet)
            edad.setText(agePet)
            color.setText(colorPet)
            precio_vacuna.setText(format.format(precioVacunaPet))
            try {
                if (!photoPet.isNullOrEmpty()) {
                    val toast = Toast.makeText(applicationContext, "Cargando foto", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.TOP, 0, 200)
                    toast.show()
                    Picasso.get()
                        .load(photoPet)
                        .resize(150, 150)
                        .into(photo_pet)
                }
            } catch (e: Exception) {
                Log.v("Error", "e: $e")
            }
        }.addOnFailureListener { e ->
            Toast.makeText(applicationContext, "Error al obtener los datos", Toast.LENGTH_SHORT).show()
        }
    }
*/
    private fun updatePet(
        namepet: String,
        edadpet: String,
        colorpet: String,
        id: String
    ) {
        val map: MutableMap<String, Any> = HashMap()
        map["name"] = namepet
        map["edad"] = edadpet
        map["color"] = colorpet
        mfirestore.collection("pet").document(id).update(map).addOnSuccessListener {
            Toast.makeText(applicationContext, "Actualizado exitosamente", Toast.LENGTH_SHORT)
                .show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(
                applicationContext,
                "Error al actualizar",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}














