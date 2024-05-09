package mx.edu.potros.petcare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import mx.edu.potros.petcare.adapter.PetAdapter
import mx.edu.potros.petcare.model.Pet
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    private lateinit var btn_add: Button
    private lateinit var btn_close:Button
    private lateinit var btn_add_frag: Button
    private lateinit var mAdapter: PetAdapter
    private lateinit var mRecycler: RecyclerView
    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var query: Query
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()

        mFirestore = FirebaseFirestore.getInstance()

        btn_add = findViewById(R.id.btn_add)
        btn_add_frag = findViewById(R.id.btn_add_frag)
        btn_close = findViewById(R.id.btn_close)

        btn_add.setOnClickListener {
            startActivity(Intent(this@MainActivity, CreatePetActivity::class.java))
        }

        btn_add_frag.setOnClickListener {
            val fm = CreatePetFragment()
            fm.show(supportFragmentManager, "Navegar a fragment")
        }

        btn_close.setOnClickListener {
            mAuth.signOut()
            finish()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

        setUpRecyclerView()

    }


    private fun setUpRecyclerView() {
        mRecycler = findViewById(R.id.RecyclerViewSingle)
        mRecycler.layoutManager = LinearLayoutManager(this)
        query = mFirestore.collection("pet")

        val firestoreRecyclerOptions = FirestoreRecyclerOptions.Builder<Pet>()
            .setQuery(query, Pet::class.java)
            .build()

        mAdapter = PetAdapter(firestoreRecyclerOptions, this, supportFragmentManager)
        mAdapter.notifyDataSetChanged()
        mRecycler.adapter = mAdapter
    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }
}





