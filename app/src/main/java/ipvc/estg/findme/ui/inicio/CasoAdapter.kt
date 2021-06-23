package ipvc.estg.findme.ui.inicio

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.findme.API.OutputReports
import ipvc.estg.findme.API.Reports
import ipvc.estg.findme.DetalhePost
import ipvc.estg.findme.R
import ipvc.estg.findme.dataclasses.ItemsViewModel
import ipvc.estg.findme.MenuTesteActivity
import java.util.*

class CasoAdapter(private val mList: List<Reports>) : RecyclerView.Adapter<CasoAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.caso_card_view, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the data to the imageview from our itemHolder class
        val decodedString = Base64.getDecoder().decode(ItemsViewModel.foto)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        holder.imageView.setImageBitmap(decodedByte)
        holder.raca.text = ItemsViewModel.raca.toString()
        holder.data.text = ItemsViewModel.data
        holder.localidade.text = ItemsViewModel.localizacao

        holder.itemView.setOnClickListener {
            // abrir activity de ver detalhes do caso
            val intent = Intent(it.context, DetalhePost::class.java)
            // To pass any data to next activity
            intent.putExtra("idCaso", ItemsViewModel.idReport.toString())
            // Start your next activity
            it.context.startActivity(intent)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val raca: TextView = itemView.findViewById(R.id.tvraca)
        val data: TextView = itemView.findViewById(R.id.tvdata)
        val localidade: TextView = itemView.findViewById(R.id.tvlocalidade)
    }
}