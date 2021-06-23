package ipvc.estg.findme.ui.inicio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import ipvc.estg.findme.R
import kotlinx.android.synthetic.main.fragment_inicio.view.*


class InicioFragment : Fragment() {
    internal lateinit var viewpageradapter:ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_inicio, container, false)

        viewpageradapter= ViewPagerAdapter((activity as FragmentActivity).supportFragmentManager)
        view.viewPager.adapter=viewpageradapter  //Binding PagerAdapter with ViewPager
        view.tab_layout.setupWithViewPager(view.viewPager) //Binding ViewPager with TabLayout

        return view
    }

}