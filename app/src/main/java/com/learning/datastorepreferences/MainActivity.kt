package com.learning.datastorepreferences

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.learning.datastorepreferences.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //initialize datastore
        dataStore = createDataStore(name = "fork_ds")

        binding.apply {
            btnSave.setOnClickListener {
                lifecycleScope.launch {
                    save(
                        binding.etSaveKey.text.toString(),
                        binding.etSaveValue.text.toString()
                    )
                }
            }
            btnRead.setOnClickListener {
                lifecycleScope.launch {
                    val value = read(binding.etReadkey.text.toString())
                    binding.tvReadValue.text = value ?: "No value found"
                }
            }
        }
    }


    private suspend fun save(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]

    }

}



