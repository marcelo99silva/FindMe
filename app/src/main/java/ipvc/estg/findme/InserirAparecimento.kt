package ipvc.estg.findme

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ipvc.estg.findme.API.EndPoints
import ipvc.estg.findme.API.OutputReports
import ipvc.estg.findme.API.ServiceBuilder
import ipvc.estg.findme.login.MainActivity
import ipvc.estg.findme.messages.LatestMessagesActivity
import ipvc.estg.findme.ml.MobilenetV110224Quant
import ipvc.estg.findme.models.User
import kotlinx.android.synthetic.main.activity_inserir_aparecimento.*
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.Base64;

private const val REQUEST_CODE = 43
private val IMAGE_PICK_CODE=1005
private val IMAGE_GAL_CODE=100
private val PERMISSION_CODE=1006

class InserirAparecimento : AppCompatActivity() {
    lateinit var bitmap: Bitmap

    companion object {
        var currentUser: User? = null
        var id:String = ""
        val TAG = "LatestMessages"
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserir_aparecimento)

        val labels = application.assets.open("labels.txt").bufferedReader().use { it.readText() }.split("\n")

        fetchCurrentUser()

        photo_btn.setOnClickListener{
            val tirarPhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if(tirarPhotoIntent.resolveActivity(this.packageManager) != null){
                startActivityForResult(tirarPhotoIntent, REQUEST_CODE)
            }else{
                Toast.makeText(this@InserirAparecimento, "eee", Toast.LENGTH_SHORT).show()
            }
        }

        galeria_btn.setOnClickListener {
            var intent : Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(intent, IMAGE_GAL_CODE)
        }

        predict1.setOnClickListener{
            if (!this::bitmap.isInitialized){
                Toast.makeText(this, "Selecione uma fotografia!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val model = MobilenetV110224Quant.newInstance(this)

            var tbuffer = TensorImage.fromBitmap(resized)
            var byteBuffer = tbuffer.buffer

            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
            inputFeature0.loadBuffer(byteBuffer)

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            var max = getMax(outputFeature0.floatArray)

            ed_txt_racaAP.setText(labels[max])

            // Releases model resources if no longer used.
            model.close()
        }

        inserirReportAP.setOnClickListener{

            val tamanho =  findViewById<EditText>(R.id.ed_txt_tamanhoAP)

            val raca = findViewById<EditText>(R.id.ed_txt_racaAP)

            val sexo = findViewById<EditText>(R.id.ed_txt_sexoAP)
            val localizacao =  findViewById<EditText>(R.id.ed_txt_localizacaoAP)
            val data = findViewById<EditText>(R.id.ed_txt_dataAP)
            var descricao =  findViewById<EditText>(R.id.ed_txt_descricaoAP)
            val image = findViewById<ImageView>(R.id.imagemAP)



            if(TextUtils.isEmpty(raca.text) || TextUtils.isEmpty(tamanho.text) || TextUtils.isEmpty(sexo.text) || TextUtils.isEmpty(localizacao.text) || TextUtils.isEmpty(data.text) || TextUtils.isEmpty(descricao.text) ){
                Toast.makeText(this, "Preencha os campos", Toast.LENGTH_LONG).show()
            }else{

                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val bitmap = image.drawable.toBitmap()
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
                val encodedImage = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray())

                val call = request.adicionarPonto(raca.text.toString() , tamanho.text.toString() , sexo.text.toString() , localizacao.text.toString(), data.text.toString(), descricao.text.toString(), "" , encodedImage, id,  1)

                call.enqueue(object : Callback<OutputReports> {
                    override fun onResponse(call: Call<OutputReports>, response: Response<OutputReports>) {
                        if (response.isSuccessful) {
                            Log.d("***", "funcionou insert")
                                val intent = Intent(applicationContext, AnuncioActivity::class.java)
                                 startActivity(intent)
                                 finish()
                        }
                    }

                    override fun onFailure(call: Call<OutputReports>, t: Throwable) {
                        Log.d("arrr", "ErrorOccur:  ${t.message}" )
                        val intent = Intent(applicationContext, AnuncioActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                })
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val takenImage = data?.extras?.get("data") as Bitmap
            imagemAP.setImageBitmap(takenImage)
            bitmap = takenImage
        }
        else if( requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK ){
            imagemAP.setImageURI(data?.data)
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)
        }
        else if( requestCode == IMAGE_GAL_CODE && resultCode == Activity.RESULT_OK ){
            imagemAP.setImageURI(data?.data)
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                id = currentUser?.uid.toString()
                Log.d("testeeee", "CurrentUser ${currentUser?.uid}")


            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    fun getMax(arr:FloatArray) : Int{
        var ind = 0;
        var min = 0.0f;

        for(i in 0..1000)
        {
            if(arr[i] > min)
            {
                min = arr[i]
                ind = i;
            }
        }
        return ind
    }
}