package mx.edu.potros.petcare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.content.Intent
class LoginActivity : AppCompatActivity() {


    private lateinit var btn_login: Button
    private lateinit var btn_register: Button
    private lateinit var btn_login_anonymous: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        email=findViewById(R.id.correo)
        password=findViewById(R.id.contrasena)
        btn_login=findViewById(R.id.btn_login)



        btn_login.setOnClickListener {
            val emailUser = email.text.toString().trim()
            val passUser = password.text.toString().trim()

            if (emailUser.isEmpty() || passUser.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Ingresar los datos", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(emailUser, passUser)
            }
        }

        btn_register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }


    }


    private fun loginUser(emailUser: String, passUser: String) {
        mAuth.signInWithEmailAndPassword(emailUser, passUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    val user = mAuth.currentUser
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                    Toast.makeText(this@LoginActivity, "Bienvenido", Toast.LENGTH_SHORT).show()
                } else {
                    // Error en el inicio de sesión
                    Toast.makeText(this@LoginActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                // Error en el inicio de sesión
                Toast.makeText(this@LoginActivity, "Error al iniciar sesión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    override fun onStart() {
        super.onStart()
        val user = mAuth.currentUser
        if (user != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }


}