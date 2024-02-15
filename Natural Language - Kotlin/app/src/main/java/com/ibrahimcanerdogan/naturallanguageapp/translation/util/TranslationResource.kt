package com.ibrahimcanerdogan.naturallanguageapp.translation.util

sealed class TranslationResource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : TranslationResource<T>(data)
    class Loading<T>(data: T? = null) : TranslationResource<T>(data)
    class Error<T>(message: String, data: T? = null) : TranslationResource<T>(data, message)
}