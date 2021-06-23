package ipvc.estg.findme.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ipvc.estg.findme.R
import ipvc.estg.findme.messages.LatestMessagesActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import ipvc.estg.findme.AnuncioActivity
import ipvc.estg.findme.DetalhePost
import ipvc.estg.findme.MenuTesteActivity

class MainActivity : AppCompatActivity() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        if(intent.hasExtra("idAnimal")){
            val ss:String = intent.getStringExtra("idAnimal").toString()
            val intent = Intent(this, DetalhePost::class.java)
            intent.putExtra("idCaso", ss)
            startActivity(intent)
        }

        //teste commit
        //teste git
        //Novo commit
        //commit

        auth = Firebase.auth



        btn.setOnClickListener {
            performLogin()
        }
    }

    //Botão Menu teste
    /*fun irMenu(view: View) {
        val intent = Intent(this, MenuTesteActivity::class.java)
        startActivity(intent)
    }*/



    private fun performLogin() {
        val email = insertUser.text.toString()
        val password = insertPassword.text.toString()


        Log.d("Login", "tentar login : $email/***")
         Log.d("Login", "tentar login : $password/***")


        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out email/pw.", Toast.LENGTH_SHORT).show()
            return
        }


        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Login", "Successfully logged in: ${it.result?.user?.uid}")

                val intent = Intent(this, MenuTesteActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
/*
     class User(val uid: String, val username: String, val  profileImageUrl: String){
         constructor() : this("","","")
     }*/

    }

