package com.srang03.checkyourphysical

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.srang03.checkyourphysical.data.ListViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_list.fab

class ListActivity : AppCompatActivity() {

    private var viewModel: ListViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar)


        var menuOpen: Boolean = false;

        val fragmentTransation = supportFragmentManager.beginTransaction()
        fragmentTransation.replace(R.id.contentLayout, MemoListFragment())
        fragmentTransation.commit()

        viewModel = application!!.let {
            ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory(it))
                .get(ListViewModel::class.java)
        }
        menu.setOnClickListener {
            if(!menuOpen){
                menuOpen = true;
                fab.animate().translationX(-resources.getDimension(R.dimen.standard_66))

                bmi_fab.animate().translationX(-resources.getDimension(R.dimen.standard_44))
                bmi_fab.animate().translationY(-resources.getDimension(R.dimen.standard_44))


                whr_fab.animate().translationY(-resources.getDimension(R.dimen.standard_66))
            }
            else{
                menuOpen = false
                fab.animate().translationX(0f)

                bmi_fab.animate().translationX(0f)
                bmi_fab.animate().translationY(0f)

                whr_fab.animate().translationY(0f)
                whr_fab.animate().translationX(0f)
            }

        }
        fab.setOnClickListener {
           var intent = Intent(applicationContext, DetailActivity::class.java)
            startActivity(intent)
        }

        bmi_fab.setOnClickListener {
            var intent = Intent(applicationContext, BmiActivity::class.java)
            startActivity(intent)
        }

        whr_fab.setOnClickListener {
            var intent = Intent(applicationContext, WhrActivity::class.java)
            startActivity(intent)
        }

    }

}
