package com.ibrahimcanerdogan.naturallanguageapp.translation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibrahimcanerdogan.naturallanguageapp.databinding.FragmentTranslationBinding
import com.ibrahimcanerdogan.naturallanguageapp.translation.adapter.TranslationLanguageAdapter
import com.ibrahimcanerdogan.naturallanguageapp.translation.util.SupportedLanguages
import com.ibrahimcanerdogan.naturallanguageapp.translation.util.TranslationResource


class TranslationFragment : Fragment() {

    private var _binding: FragmentTranslationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TranslationViewModel by viewModels()

    private var languageCodeSource: String? = null
    private var languageCodeTarget: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranslationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        handleUIElements()
    }

    private fun handleUIElements() {
        with(binding) {
            // RecyclerView
            recyclerViewSource.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val sourceAdapter = TranslationLanguageAdapter(SupportedLanguages.entries) { selectedLanguage ->
                languageCodeSource = selectedLanguage.lanCode
                Toast.makeText(requireContext(), "Selected: ${selectedLanguage.langNative}", Toast.LENGTH_SHORT).show()
            }
            recyclerViewSource.adapter = sourceAdapter

            recyclerViewTarget.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val targetAdapter = TranslationLanguageAdapter(SupportedLanguages.entries) { selectedLanguage ->
                languageCodeTarget = selectedLanguage.lanCode
                Toast.makeText(requireContext(), "Selected: ${selectedLanguage.langNative}", Toast.LENGTH_SHORT).show()
            }
            recyclerViewTarget.adapter = targetAdapter

            // Scroll Result Text
            textViewTranslation.movementMethod = ScrollingMovementMethod()

            buttonCopyTranslate.setOnClickListener { copyTextToClipboard() }

            val textChangeListener = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (languageCodeSource != null && languageCodeTarget != null) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            viewModel.processTranslation(s.toString(), languageCodeSource!!, languageCodeTarget!!)
                            buttonCopyTranslate.visibility = if (s.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
                        }, 500)
                    } else {
                        Toast.makeText(requireContext(), "Select Source & Target Language!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            textInputTranslation.addTextChangedListener(textChangeListener)
        }
    }

    private fun observeViewModel() {
        viewModel.translateData.observe(viewLifecycleOwner, ::setTranslatedText)
    }

    private fun setTranslatedText(translationResource: TranslationResource<String?>?) {
        when(translationResource) {
            is TranslationResource.Success -> {
                binding.circularProgress.visibility = View.INVISIBLE
                translationResource.data?.let {
                    binding.textViewTranslation.text = it
                }
            }
            is TranslationResource.Error -> {
                binding.circularProgress.visibility = View.INVISIBLE
                translationResource.data?.let {
                    binding.textViewTranslation.text = it
                }
            }
            is TranslationResource.Loading -> {
                binding.circularProgress.visibility = View.VISIBLE
            }
            else -> {
                binding.circularProgress.visibility = View.INVISIBLE
                binding.textViewTranslation.text = "null"
            }
        }
    }

    private fun copyTextToClipboard() {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", binding.textViewTranslation.text.toString())
        clipboardManager.setPrimaryClip(clip)

        Toast.makeText(requireContext(), "Translation copied to clipboard!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}