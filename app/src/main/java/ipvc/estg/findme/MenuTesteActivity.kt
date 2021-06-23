package ipvc.estg.findme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import ipvc.estg.findme.login.MainActivity
import ipvc.estg.findme.messages.LatestMessagesActivity
import ipvc.estg.findme.ui.inicio.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_menu_teste.*
import kotlinx.android.synthetic.main.fragment_inicio.view.*


class MenuTesteActivity : AppCompatActivity() {
    internal lateinit var viewpageradapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_teste)



        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = token
            if (msg != null) {
                Log.e("TAG", msg)
            }
        })

        Firebase.messaging.subscribeToTopic("animais")
            .addOnCompleteListener { task ->
                var msg ="subscriot"
                if (!task.isSuccessful) {
                    msg = "nao subscriot"
                }
                Log.e("TAG", msg)
            }

        viewpageradapter= ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = viewpageradapter  //Binding PagerAdapter with ViewPager
        tab_layout.setupWithViewPager(viewPager) //Binding ViewPager with TabLayout

        bottomNavigationView2.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_pagina_inicial -> {
                    true
                }
                R.id.ic_adicionar -> {
                    val intent = Intent(this, AnuncioActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    startActivity(intent)
                    true
                }
                R.id.ic_chat -> {
                    val intent = Intent(this, LatestMessagesActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.sign_out -> {
                 FirebaseAuth.getInstance().signOut()
                 val intent = Intent(this, MainActivity::class.java)
                 intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                 startActivity(intent)
             }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_sign_out, menu)
        return super.onCreateOptionsMenu(menu)
    }

}