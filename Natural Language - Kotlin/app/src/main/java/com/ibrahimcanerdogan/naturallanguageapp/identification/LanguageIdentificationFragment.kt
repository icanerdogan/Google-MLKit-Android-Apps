package com.ibrahimcanerdogan.naturallanguageapp.identification

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.mlkit.nl.languageid.IdentifiedLanguage
import com.ibrahimcanerdogan.naturallanguageapp.databinding.FragmentLanguageIdentificationBinding
import com.ibrahimcanerdogan.naturallanguageapp.identification.model.IdentificationLanguage
import com.ibrahimcanerdogan.naturallanguageapp.identification.util.LanguageIdentificationSupportedLanguages

class LanguageIdentificationFragment : Fragment() {

    private var _binding: FragmentLanguageIdentificationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LanguageIdentificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanguageIdentificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonLanguage.setOnClickListener {
                try {
                    viewModel.processIdentification(editTextLanguage.text.toString())
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            textViewLanguage.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable?) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    buttonLanguageCopy.visibility = if (s.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
                }
            })

            buttonLanguageCopy.setOnClickListener { copyTextToClipboard() }
        }

        viewModel.languageData.observe(viewLifecycleOwner, ::getLanguageData)
    }

    private fun getLanguageData(languages: List<IdentifiedLanguage>?) {
        var langList : String? = ""
        languages?.let { listIdentifiedLanguages ->
            for (language in listIdentifiedLanguages) {
                val languageTag = language.languageTag
                val confidenceLevel = language.confidence
                LanguageIdentificationSupportedLanguages.entries.forEach{
                    if (it.langCode == languageTag) {
                        langList += IdentificationLanguage(it.langEnglish, it.langNative, confidenceLevel).toString()
                    }
                }
                Log.i(TAG, "Language: $languageTag - Confidence: $confidenceLevel")
            }

            binding.textViewLanguage.text = langList
        }
    }

    private fun copyTextToClipboard() {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", binding.textViewLanguage.text.toString())
        clipboardManager.setPrimaryClip(clip)

        Toast.makeText(requireContext(), "Text copied to clipboard!", Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val TAG = LanguageIdentificationFragment::class.java.simpleName.toString()
    }
}