package at.aau.edu.wizards.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.sample.SampleDataSource
import at.aau.edu.wizards.sample.SampleViewModel

class ModelTestViewModel() : ViewModel() {


    class Factory() : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(SampleViewModel::class.java)) {
                ModelTestViewModel() as T
            } else throw IllegalStateException("Wrong Factory for instantiating ${modelClass::class.java.canonicalName}")
        }
    }
}