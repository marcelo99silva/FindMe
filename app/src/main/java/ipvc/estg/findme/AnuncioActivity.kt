package ipvc.estg.findme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ipvc.estg.findme.messages.LatestMessagesActivity
import kotlinx.android.synthetic.main.activity_menu_teste.*

class AnuncioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anuncio)

        bottomNavigationView2.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_pagina_inicial -> {
                    val intent = Intent(this, MenuTesteActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    startActivity(intent)
                    true
                }
                R.id.ic_adicionar -> {
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


    fun button1(view: View) {
        val intent = Intent(this, InserirDesaparecimento::class.java)
        startActivity(intent)
    }

    fun button2(view: View) {
        val intent = Intent(this, InserirAparecimento::class.java)
        startActivity(intent)
    }
}