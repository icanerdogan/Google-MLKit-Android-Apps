package com.ibrahimcanerdogan.naturallanguageapp.translation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.ibrahimcanerdogan.naturallanguageapp.translation.util.TranslationResource

class TranslationViewModel : ViewModel() {

    private val translate = MutableLiveData<TranslationResource<String?>>()
    val translateData: LiveData<TranslationResource<String?>>
        get() = translate

    fun processTranslation(translateText: String, languageSource: String, languageTarget: String) {
        translate.postValue(TranslationResource.Loading())

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(languageSource)
            .setTargetLanguage(languageTarget)
            .build()

        val translator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translator.translate(translateText)
                    .addOnSuccessListener { translatedText ->
                        translate.postValue(TranslationResource.Success(translatedText))
                    }
                    .addOnFailureListener { exception ->
                        translate.postValue(TranslationResource.Error("Cannot be translate!"))
                    }
            }
            .addOnFailureListener { exception ->
                translate.postValue(TranslationResource.Error("Model couldn’t be downloaded! Internet connection required."))
            }
    }
}