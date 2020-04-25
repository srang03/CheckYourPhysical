package com.srang03.checkyourphysical

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.customer_toast.*

class WhrActivity : AppCompatActivity() {
    private var myHeight: Float = 0.0f
    private var myWaist: Float = 0.0f
    private var Whtresult: Float =0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whr)

        var input_age = findViewById(R.id.input_age) as TextInputLayout
        var input_height = findViewById(R.id.input_height) as TextInputLayout
        var input_waist = findViewById(R.id.input_waist) as TextInputLayout

        var ageEditText = findViewById(R.id.ageEditText) as EditText
        var heightEditText = findViewById(R.id.heightEditText) as EditText
        var waistEditText = findViewById(R.id.waistEditText) as EditText
        var WHRresultProgressBar = findViewById<ProgressBar>(R.id.WHRresultProgressBar)
        var WHRresultTextview = findViewById<TextView>(R.id.WHRresultTextview)
        var WHRTextView = findViewById<TextView>(R.id.WHRTextView)

        heightEditText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var StringinputHeight =
                    input_height.editText!!.text.toString().trim { it <= ' ' }
                if (count == 0) {
                    StringinputHeight = "0.0"
                }
                myHeight = StringinputHeight.toFloat()
                if (myHeight != 0f && myWaist != 0f) {
                    Whtresult = myWaist / myHeight
                    if(Whtresult >= 1){
                        showToast()
                    }
                    else{
                        WHRresultTextview.text = String.format("%.02f", Whtresult)
                        WHRresultProgressBar.setProgress((Whtresult * 100).toInt())
                        BMIColor(WHRresultTextview, WHRTextView)
                    }

                } else Whtresult = 0f
            }
        })

        waistEditText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                var StringinputWaist = input_waist.toString().trim()
//                if(count == 0){
//                    StringinputWaist = "0.0"
//                }
//                myWaist = StringinputWaist.toFloat()
//                if(myHeight != 0.0f && myWaist != 0.0f){
//                    Whtresult = (myWaist / myHeight)
//                    WHRresultTextview.setText(Whtresult.toString().format("%.02f"))
//                    BMIColor(WHRresultTextview, WHRTextView)
//                }
//                else{
//                    Whtresult = 0.0f
//                }

               var StringinputWaist =
                    input_waist.editText!!.text.toString().trim { it <= ' ' }
                if (count == 0) {
                    StringinputWaist = "0.0"
                }
                myWaist = StringinputWaist.toFloat()
                if (myHeight != 0f && myWaist != 0f) {
                    Whtresult = myWaist / myHeight

                    if(Whtresult >= 1){
                        showToast()
                    }
                    else{
                        WHRresultTextview.text = String.format("%.02f", Whtresult)
                        WHRresultProgressBar.setProgress((Whtresult * 100).toInt())
                        BMIColor(WHRresultTextview, WHRTextView)
                    }

                } else Whtresult = 0f




            }

        })
    }

    fun BMIColor(textView: TextView, textView2: TextView) {
        if (Whtresult < 0.35 || Whtresult == 35f) {
            textView.setTextColor(resources.getColor(R.color.color1))
            textView2.setTextColor(resources.getColor(R.color.color1))
        } else if (Whtresult < 0.42 || Whtresult.toDouble() == 0.42) {
            textView.setTextColor(resources.getColor(R.color.color11))
            textView2.setTextColor(resources.getColor(R.color.color11))
        } else if (Whtresult < 0.52 || Whtresult.toDouble() == 0.52) {
            textView.setTextColor(resources.getColor(R.color.color2))
            textView2.setTextColor(resources.getColor(R.color.color2))
        } else if (Whtresult < 0.57 || Whtresult.toDouble() == 0.57) {
            textView.setTextColor(resources.getColor(R.color.color3))
            textView2.setTextColor(resources.getColor(R.color.color3))
        } else if (Whtresult < 0.62 || Whtresult.toDouble() == 0.62) {
            textView.setTextColor(resources.getColor(R.color.color4))
            textView2.setTextColor(resources.getColor(R.color.color4))
        } else {
            textView.setTextColor(resources.getColor(R.color.color5))
            textView2.setTextColor(resources.getColor(R.color.color5))
        }
    }

    fun showToast(){
        val layoutInflater: LayoutInflater = LayoutInflater.from(applicationContext)
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
