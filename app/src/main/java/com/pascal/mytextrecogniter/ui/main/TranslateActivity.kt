package com.pascal.mytextrecogniter.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.pascal.mytextrecogniter.R
import com.pascal.mytextrecogniter.model.ResponseTranslate
import com.pascal.mytextrecogniter.viewModel.ViewModelTranslate
import kotlinx.android.synthetic.main.activity_translate.*

class TranslateActivity : AppCompatActivity() {

    private var textData: String? = null
    private lateinit var viewmodel: ViewModelTranslate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)
        supportActionBar?.title = "Translate Activity"

        initView()
        attachObserve()
    }

    private fun attachObserve() {
        viewmodel = ViewModelProviders.of(this).get(ViewModelTranslate::class.java)

        viewmodel.responseTranslate.observe(this, Observer { showData(it) })
        viewmodel.isError.observe(this, Observer { showError(it) })
    }

    private fun initView() {
        textData = intent?.getStringExtra("data")

        if (tvSL.text == "Indonesia") {
            initLanguage(
                TranslateLanguage.INDONESIAN,
                TranslateLanguage.ENGLISH,
                textData
            )
        } else {
            initLanguage(
                TranslateLanguage.ENGLISH,
                TranslateLanguage.INDONESIAN,
                textData
            )
        }

        btnSwap.setOnClickListener {
            initView()
            if (tvSL.text == "Indonesia") {
                tvSL.text = "English"
                tvTL.text = "Indonesia"
            } else {
                tvSL.text = "Indonesia"
                tvTL.text = "Englis"
            }
        }
    }

    private fun initLanguage(idSL: String?, idTL: String?, text: String?) {
        val option = TranslatorOptions.Builder()
            .setSourceLanguage(idSL.toString())
            .setTargetLanguage(idTL.toString())
            .build()

        val textTranslator = Translation.getClient(option)

        //dowload model
        textTranslator.downloadModelIfNeeded()
            .addOnCanceledListener {
                Log.d("translate", "Download Success")
            }
            .addOnFailureListener {
                Log.d("translate", "Download Failed : $it")
            }

        //translate
        textTranslator.translate(text.toString())
            .addOnSuccessListener {
                tvResult.text = it
                Log.d("translate", "Translate Success: $it")

                translateSunda(it)
            }
            .addOnFailureListener {
                Log.d("translate", "Translate Failed: $it")
            }
    }

    private fun translateSunda(it: String) {
        if (it.isNotEmpty()) {
            viewmodel.getTranslateView("google", it, "su")
        }
    }

    private fun showData(it: ResponseTranslate?) {
        val hasil = it?.data?.result
        btn_sunda.setOnClickListener {
            tv_sunda.setText(hasil)
        }
    }

    private fun showError(it: Throwable?) {
        showToast(it.toString())
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}