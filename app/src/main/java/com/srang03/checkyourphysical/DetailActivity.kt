package com.srang03.checkyourphysical

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.takisoft.datetimepicker.DatePickerDialog
import com.takisoft.datetimepicker.TimePickerDialog
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import java.io.File
import java.util.*


class DetailActivity : AppCompatActivity() {

    private var viewModel: DetailViewModel? = null
    private val dialogCalendar = Calendar.getInstance()

    private  val REQUEST_IMAGE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            val intent  = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE)
        }

        viewModel = application!!.let {
            ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory(it))
                .get(DetailViewModel::class.java)
        }

        viewModel!!.memoLiveData.observe(this, Observer {
            supportActionBar?.title = it.title
            contentEdit.setText(it.content)
            alarmInfoView.setAlarmDate(it.alarmTime)
            locationInfoView.setLocation(it.latitude, it.longitude)


            val imageFile = File(
                getDir("image", Context.MODE_PRIVATE),
                it.id + ".jpg")

            bgImage.setImageURI(imageFile.toUri())
        })

        val memoId = intent.getStringExtra("MEMO_ID")
        if(memoId != null) viewModel!!.loadMemo(memoId)

        toolbarLayout.setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.dialog_title, null)
            val titleEdit = view.findViewById<EditText>(R.id.titleEdit)

            AlertDialog.Builder(this)
                .setTitle("Set your Title")
                .setView(view)
                .setNegativeButton("cancel", null)
                .setPositiveButton("save", DialogInterface.OnClickListener { dialog, which ->
                    supportActionBar?.title = titleEdit.text.toString()
                    toolbarLayout.title = titleEdit.text.toString()
                    viewModel!!.memoData.title = titleEdit.text.toString()
                }).show()
        }
        contentEdit.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                viewModel!!.memoData.content = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        locationInfoView.setOnClickListener {
            val latitude = viewModel!!.memoData.latitude
            val longitude = viewModel!!.memoData.longitude

            if(!(latitude == 0.0 && longitude == 0.0)){
                val mapView = MapView(this)
                mapView.getMapAsync {
                    val latitude = viewModel!!.memoData.latitude
                    val longitude = viewModel!!.memoData.longitude
                    val cameraPosition = CameraPosition(LatLng(latitude,longitude),13.0)
                    it.cameraPosition = cameraPosition
                }
                AlertDialog.Builder(this)
                    .setView(mapView)
                    .show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel?.addOrUpdateMemo(this)
    }

    private fun openDateDialog() {
        val datePickerDialog = DatePickerDialog(this)
        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            dialogCalendar.set(year, month, dayOfMonth)
            openTimeDialog()
        }
        datePickerDialog.show()
    }

    private fun openTimeDialog() {
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                dialogCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                dialogCalendar.set(Calendar.MINUTE, minute)

                viewModel?.setAlarm(dialogCalendar.time)
            },
            0, 0, false)
        timePickerDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }
    @SuppressLint("MissingPermission")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.menu_share -> {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, supportActionBar?.title)
                intent.putExtra(Intent.EXTRA_TEXT, contentEdit.text.toString())

                startActivity(intent)
            }
            R.id.menu_alarm -> {
                if(viewModel?.memoData?.alarmTime!!.after(Date())) {
                    AlertDialog.Builder(this)
                        .setTitle("Guide")
                        .setMessage("You can set or delete the alarm in memo")
                        .setPositiveButton("Reset", DialogInterface.OnClickListener { dialog, which ->
                            openDateDialog()
                        })
                        .setNegativeButton("Delete", DialogInterface.OnClickListener { dialog, which ->
                            viewModel?.deleteAlarm()
                        })
                        .show()
                }
                else {
                    openDateDialog()
                }
            }


            R.id.menu_location -> {
                AlertDialog.Builder(this)
                    .setTitle("Guide")
                    .setMessage("You can save or delete the present location in memo")
                    .setPositiveButton("location appointment", DialogInterface.OnClickListener{
                        dialog, which ->
                        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                        if(!isGPSEnabled && !isNetworkEnabled){
                            Snackbar.make(
                                toolbarLayout,
                                "You must turn on the location function of your phone to use it",
                                Snackbar.LENGTH_LONG)
                                .setAction("Setting", View.OnClickListener {
                                    val goToSettings = Intent(Settings.ACTION_SOUND_SETTINGS)
                                    startActivity(goToSettings)
                                }).show()

                        }
                        else {
                            val criteria = Criteria()
                            criteria.accuracy = Criteria.ACCURACY_MEDIUM
                            criteria.powerRequirement = Criteria.POWER_MEDIUM

                            locationManager.requestSingleUpdate(criteria, object : LocationListener {
                                override fun onLocationChanged(location: Location?) {
                                    location?.run {
                                        viewModel!!.setLocation(latitude, longitude)
                                    }
                                }

                                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                                }

                                override fun onProviderEnabled(provider: String?) {
                                }

                                override fun onProviderDisabled(provider: String?) {
                                }

                            }, null)
                        }
                    })
                    .setNegativeButton("Delete", DialogInterface.OnClickListener { dialog, which ->
                        viewModel!!.setLocation(0.0, 0.0)
                    })
                    .show()
            }

            }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK){
            try{
                val inputStream = data?.data?.let{
                    contentResolver.openInputStream(it)
                }
                inputStream?.let{
                    val image = BitmapFactory.decodeStream(it)
                    bgImage.setImageURI(null)
                    image?.let {viewModel?.setImageFile(this, it)}
                    it.close()
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }
}