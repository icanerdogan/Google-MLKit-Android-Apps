package com.ibrahimcanerdogan.naturallanguageapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ibrahimcanerdogan.naturallanguageapp.databinding.ActivityMainBinding
import com.ibrahimcanerdogan.naturallanguageapp.entityextraction.EntityExtractionFragment
import com.ibrahimcanerdogan.naturallanguageapp.identification.LanguageIdentificationFragment
import com.ibrahimcanerdogan.naturallanguageapp.smartreply.SmartReplyFragment
import com.ibrahimcanerdogan.naturallanguageapp.translation.TranslationFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(LanguageIdentificationFragment())
        binding.bottomNavigationView.menu.getItem(0).isChecked = true
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menuIdentification -> replaceFragment(LanguageIdentificationFragment())
                R.id.menuTranslation -> replaceFragment(TranslationFragment())
                R.id.menuSmartReply -> replaceFragment(SmartReplyFragment())
                R.id.menuExtraction -> replaceFragment(EntityExtractionFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragment).commit()
    }
}