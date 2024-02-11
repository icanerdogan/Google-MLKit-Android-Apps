package com.ibrahimcanerdogan.naturallanguageapp.identification.model

data class IdentificationLanguage(
    val languageEnglish: String? = null,
    val languageNative: String? = null,
    val languageConfidence: Float? = null
) {

    override fun toString(): String {
        return "$languageEnglish - $languageEnglish - $languageConfidence \n"
    }
}


