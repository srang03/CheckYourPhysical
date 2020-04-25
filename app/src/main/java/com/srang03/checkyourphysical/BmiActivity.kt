package com.srang03.checkyourphysical

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bmi.*
import kotlinx.android.synthetic.main.customer_toast.*

class BmiActivity : AppCompatActivity() {
    private var weight: Int = 0
    private var height: Int = 0
    private var BMI: Float = 0.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        val weightSeekBar = findViewById<SeekBar>(R.id.weightSeekBar)
        weightSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                weightProgressBar.setProgress(progress)
                weight = progress;
                textviewWeight.setText(progress.toString())

                if(weight != 0 && height != 0){
                    BMI = (10000f * progress / (height * height))
                }
                else{
                    BMI = 0.0f
                }

                if(BMI > 60.0){
                    BMI = 60.0f
                    showToast()
                }

                BMIColor(BMIresultTextview, BMIColorTextview)
                BMIresultProgressBar.setProgress(BMI.toInt())
                BMIresultTextview.setText(String.format("%.2f", BMI))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        val heightSeekBar = findViewById<SeekBar>(R.id.heightSeekBar)
        heightSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                heightProgressBar.setProgress(progress)
                height = progress
                textviewHeight.setText(progress.toString())
                if(weight !=0 && height !=0 ){
                    BMI = (10000f * weight / (progress * progress))
                }
                else{
                    BMI = 0.0f
                }
                if(BMI > 60.0){
                    BMI = 60.0f
                    showToast()
                }
                BMIColor(BMIresultTextview, BMIColorTextview)
                BMIresultProgressBar.setProgress(BMI.toInt())
                BMIresultTextview.setText(String.format("%.2f", BMI))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }

    fun BMIColor(textView: TextView, textView2: TextView) {
        if (BMI < 18.4) {
            textView.setTextColor(resources.getColor(R.color.color1))
            textView2.setTextColor(resources.getColor(R.color.color1))
        } else if (BMI < 23.1) {
            textView.setTextColor(resources.getColor(R.color.color2))
            textView2.setTextColor(resources.getColor(R.color.color2))
        } else if (BMI < 25.1) {
            textView.setTextColor(resources.getColor(R.color.color3))
            textView2.setTextColor(resources.getColor(R.color.color3))
        } else if (BMI < 30.1) {
            textView.setTextColor(resources.getColor(R.color.color4))
            textView2.setTextColor(resources.getColor(R.color.color4))
        } else {
            textView.setTextColor(resources.getColor(R.color.color5))
            textView2.setTextColor(resources.getColor(R.color.color5))
        }
    }

    fun showToast(){
        val layoutInflater:LayoutInflater = LayoutInflater.from(applicationContext)
        val view: View = layoutInflater.inflate(
            R.layout.customer_toast, // Custom view/ layout
            toast_root, // Root layout to attach the view
            false // Attach with root layout or not
        )

        val toast = Toast(applicationContext)
        toast.setGravity(Gravity.BOTTOM, 0, 30)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        toast.show()

    }
}
