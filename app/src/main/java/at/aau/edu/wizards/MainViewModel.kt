package at.aau.edu.wizards

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

private const val SHARED_PREFERENCE_USERNAME_KEY = "USERNAME"
class MainViewModel (private val sharedPreferences: SharedPreferences) : ViewModel() {


    fun getUsername(): String? {
        return sharedPreferences.getString(SHARED_PREFERENCE_USERNAME_KEY, null)
    }

    fun saveUsername(username: String) {
        sharedPreferences.edit().putString(SHARED_PREFERENCE_USERNAME_KEY, username).apply()
    }

    class Factory(private val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                MainViewModel(sharedPreferences) as T
            } else error("Wrong Factory for instantiating ${modelClass::class.java.canonicalName}")
        }
    }
}