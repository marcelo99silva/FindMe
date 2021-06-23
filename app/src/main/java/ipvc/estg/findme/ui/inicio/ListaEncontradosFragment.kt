package ipvc.estg.findme.ui.inicio

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.core.Repo
import ipvc.estg.findme.API.EndPoints
import ipvc.estg.findme.API.OutputReports
import ipvc.estg.findme.API.Reports
import ipvc.estg.findme.API.ServiceBuilder
import ipvc.estg.findme.R
import ipvc.estg.findme.dataclasses.ItemsViewModel
import kotlinx.android.synthetic.main.fragment_listar.*
import kotlinx.android.synthetic.main.fragment_listar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListaEncontradosFragment : Fragment() {
    private lateinit var reports: List<Reports>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_listar, container, false)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getReports(1)
        call.enqueue(object : Callback<List<Reports>> {
            override fun onResponse(
                call: retrofit2.Call<List<Reports>>,
                response: Response<List<Reports>>
            ) {
                if (response.body() != null) {
                    progressBar.visibility = View.GONE
                    reports = response.body()!!
                    // recycler view
                    val recyclerView: RecyclerView = view.recyclerCasos
                    recyclerView.layoutManager = LinearLayoutManager(activity)
                    val adapter = CasoAdapter(reports)
                    recyclerView.adapter = adapter
                }
                else {
                    progressBar.visibility = View.GONE
                    tvSemCasos.visibility = View.VISIBLE
                }
            }
            override fun onFailure(call: Call<List<Reports>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: " + t.message, Toast.LENGTH_SHORT).show()
                Log.e("XXX", t.toString())
            }
        })

        // Inflate the layout for this fragment
        return view
    }

}