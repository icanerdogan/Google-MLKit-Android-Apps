package com.ibrahimcanerdogan.naturallanguageapp.identification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.nl.languageid.IdentifiedLanguage
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import kotlin.Exception

class LanguageIdentificationViewModel : ViewModel() {

    private val language = MutableLiveData< List<IdentifiedLanguage>?>()
    val languageData : LiveData<List<IdentifiedLanguage>?>
        get() = language

    private val languageIdentifier = LanguageIdentification
        .getClient(
            LanguageIdentificationOptions.Builder().setConfidenceThreshold(0.2f).build()
        )

    fun processIdentification(identificationText: String) {
        languageIdentifier.identifyPossibleLanguages(identificationText)
            .addOnSuccessListener { identifiedLanguages ->
                language.postValue(identifiedLanguages)
            }
            .addOnFailureListener {
                language.postValue(null)
                Log.e(TAG, it.message.toString())
                throw Exception(it.message.toString())
            }
    }

    companion object {
        private val TAG = LanguageIdentificationViewModel::class.java.simpleName.toString()
    }
}