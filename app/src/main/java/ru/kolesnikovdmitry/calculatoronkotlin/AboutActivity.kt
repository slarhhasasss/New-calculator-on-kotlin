package ru.kolesnikovdmitry.calculatoronkotlin

import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.card_for_licenses.view.*

class AboutActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        try {
            supportActionBar!!.title = "Licenses"
            supportActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

            setUpLicenses()
        } catch (th: Throwable) {
            Toast.makeText(applicationContext, th.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun setUpLicenses() {
        //Добавляем карточки с лицензиями
        val cardViewLicenseExp4j = CardView(applicationContext)
        layoutInflater.inflate(R.layout.card_for_licenses, cardViewLicenseExp4j)
        cardViewLicenseExp4j.textViewNameOfLicense.text = getString(R.string.exp5j_license_name)
        cardViewLicenseExp4j.textViewDateOfLicense.text = getString(R.string.exp5j_license_date)
        cardViewLicenseExp4j.textViewBodyOfLicense.text = getString(R.string.exp5j_license_body)

        linearLayActAbout.addView(cardViewLicenseExp4j)

        val cardViewLicenseAppCompat = CardView(applicationContext)
        layoutInflater.inflate(R.layout.card_for_licenses, cardViewLicenseAppCompat)
        cardViewLicenseAppCompat.textViewNameOfLicense.text = getString(R.string.app_compat_and_android_support_lib_name)
        cardViewLicenseAppCompat.textViewDateOfLicense.text = getString(R.string.app_compat_and_android_support_lib_date)
        cardViewLicenseAppCompat.textViewBodyOfLicense.text = getString(R.string.app_compat_and_android_support_lib_body)

        linearLayActAbout.addView(cardViewLicenseAppCompat)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}