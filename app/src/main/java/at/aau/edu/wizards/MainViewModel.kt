package at.aau.edu.wizards

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

public const val SHARED_PREFERENCE_USERNAME_KEY = "USERNAME"
public const val SHARED_PREFERENCE_AVATAR_KEY = "AVATAR"
class MainViewModel (private val sharedPreferences: SharedPreferences) : ViewModel() {

    fun getUsername(): String? {
        return sharedPreferences.getString(SHARED_PREFERENCE_USERNAME_KEY, null)
    }

    fun saveUsername(username: String) {
        sharedPreferences.edit().putString(SHARED_PREFERENCE_USERNAME_KEY, username).apply()
    }

    fun getAvatar(): String? {
        return sharedPreferences.getString(SHARED_PREFERENCE_AVATAR_KEY, null)
    }

    fun saveAvatar(avatar: String) {
        sharedPreferences.edit().putString(SHARED_PREFERENCE_AVATAR_KEY, avatar).apply()
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