package ipvc.estg.findme

import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import ipvc.estg.findme.API.EndPoints
import ipvc.estg.findme.API.Reports
import ipvc.estg.findme.API.ServiceBuilder
import kotlinx.android.synthetic.main.activity_detalhe_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class DetalhePost : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_post)


        if(intent.hasExtra("idCaso")){
            val idCaso: String = intent.getStringExtra("idCaso").toString()
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.obterReportId(idCaso)
            call.enqueue(object : Callback<List<Reports>> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<List<Reports>>,
                    response: Response<List<Reports>>
                ) {
                    if (response.body() != null) {
                        val caso = response.body()!![0]
                        if (caso.TipoReport == 0){
                            txt.text = "Perdido"
                        } else {txt.text = "Encontrado"}

                        val decodedString = Base64.getDecoder().decode(caso.foto)
                        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        imagemAP.setImageBitmap(decodedByte)
                        ed_txt_racaAP.setText(caso.raca)
                        ed_txt_racaAP.keyListener = null
                        ed_txt_sexoAP.setText(caso.sexo)
                        ed_txt_sexoAP.keyListener = null
                        ed_txt_tamanhoAP.setText(caso.tamanho)
                        ed_txt_tamanhoAP.keyListener = null
                        ed_txt_dataAP.setText(caso.data)
                        ed_txt_dataAP.keyListener = null
                        ed_txt_localizacaoAP.setText(caso.localizacao)
                        ed_txt_localizacaoAP.keyListener = null
                        ed_txt_descricaoAP.setText(caso.descricao)
                        ed_txt_descricaoAP.keyListener = null

                        progressBar.visibility = View.GONE
                        layout.visibility = View.VISIBLE
                    }
                }
                override fun onFailure(call: Call<List<Reports>>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error: " + t.message, Toast.LENGTH_SHORT).show()
                    Log.e("XXX", t.toString())
                }

            })
        }

    }
}