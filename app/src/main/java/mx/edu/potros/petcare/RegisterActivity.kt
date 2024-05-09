package mx.edu.potros.petcare
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var btn_register: Button
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()

        name = findViewById(R.id.nombre)
        email = findViewById(R.id.correo)
        password = findViewById(R.id.contrasena)
        btn_register = findViewById(R.id.btn_registro)

        btn_register.setOnClickListener {
            val nameUser = name.text.toString().trim()
            val emailUser = email.text.toString().trim()
            val passUser = password.text.toString().trim()

            if (nameUser.isEmpty() && emailUser.isEmpty() && passUser.isEmpty()) {
                // Código a ejecutar si todos los campos están vacíos
                Toast.makeText(this@RegisterActivity, "Complete los datos", Toast.LENGTH_SHORT).show()

            }else{
                registerUser(nameUser, emailUser, passUser)

            }
        }

    }

    private fun registerUser(nameUser: String, emailUser: String, passUser: String) {
        mAuth.createUserWithEmailAndPassword(emailUser, passUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val id = mAuth.currentUser?.uid ?: ""
                    val map = hashMapOf(
                        "id" to id,
                        "name" to nameUser,
                        "email" to emailUser,
                        "password" to passUser
                    )

                    mFirestore.collection("user").document(id)
                        .set(map)
                        .addOnSuccessListener {
                            finish()
                            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                            Toast.makeText(this@RegisterActivity, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this@RegisterActivity, "Error al guardar", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this@RegisterActivity, "Error al registrar", Toast.LENGTH_SHORT).show()
                }
            }
    }




}
