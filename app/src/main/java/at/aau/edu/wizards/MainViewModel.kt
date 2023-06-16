package at.aau.edu.wizards

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

const val SHARED_PREFERENCE_USERNAME_KEY = "USERNAME"
const val SHARED_PREFERENCE_AVATAR_KEY = "AVATAR_ID"

class MainViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    val avatarsList = intArrayOf(
        R.drawable.icon1,
        R.drawable.icon2,
        R.drawable.icon3,
        R.drawable.icon4,
        R.drawable.icon5,
        R.drawable.icon6,
        R.drawable.icon7,
        R.drawable.icon8,
        R.drawable.icon9,
        R.drawable.icon10,
        R.drawable.icon11,
        R.drawable.icon12,
        R.drawable.icon13,
        R.drawable.icon14,
        R.drawable.icon15,
        R.drawable.icon16,
        R.drawable.icon17,
        R.drawable.icon18,
        R.drawable.icon19,
    )

    fun getUsername(): String? {
        return sharedPreferences.getString(SHARED_PREFERENCE_USERNAME_KEY, null)
    }

    fun saveUsername(username: String) {
        sharedPreferences.edit().putString(SHARED_PREFERENCE_USERNAME_KEY, username).apply()
    }

    fun getAvatar(): Int {
        return sharedPreferences.getInt(SHARED_PREFERENCE_AVATAR_KEY, selectRandomAvatar())
    }

    fun saveAvatar(avatar: Int) {
        sharedPreferences.edit().putInt(SHARED_PREFERENCE_AVATAR_KEY, avatar).apply()
    }

    fun selectRandomAvatar(): Int {
        val savedAvatar = sharedPreferences.getInt(SHARED_PREFERENCE_AVATAR_KEY, -1)
        val randomAvatar = if (savedAvatar != -1) {
            savedAvatar
        } else {
            val newRandomAvatar = avatarsList.random()
            saveAvatar(newRandomAvatar)
            newRandomAvatar
        }
        return randomAvatar
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