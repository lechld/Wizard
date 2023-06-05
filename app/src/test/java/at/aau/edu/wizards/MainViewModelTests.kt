package at.aau.edu.wizards

import android.content.SharedPreferences
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.*

class MainViewModelTests {
    private val sharedPrefs = mock<SharedPreferences>()
    private val mainViewModel = MainViewModel.Factory(sharedPrefs).create(MainViewModel::class.java)

    @Test
    fun getUsernameTests_testNull_returnsNull() {
        whenever(sharedPrefs.getString(SHARED_PREFERENCE_USERNAME_KEY, null)).doReturn(null)
        assertNull(mainViewModel.getUsername())
    }

    @Test
    fun getUsernameTests_testEmptyString_returnsEmptyString() {
        whenever(sharedPrefs.getString(SHARED_PREFERENCE_USERNAME_KEY, null)).doReturn("")
        assertEquals("", mainViewModel.getUsername())
    }

    @Test
    fun getUsernameTests_testExactString_returnsExactString() {
        val expectedString = "some random string"
        whenever(sharedPrefs.getString(SHARED_PREFERENCE_USERNAME_KEY, null)).doReturn(expectedString)
        assertEquals(expectedString, mainViewModel.getUsername())
    }

    @Test
    fun saveUsernameTests_setUsername_accepted() {
        val editor = mock<SharedPreferences.Editor>()
        val username = "dummy"

        whenever(sharedPrefs.edit()).doReturn(editor)
        whenever(editor.putString(SHARED_PREFERENCE_USERNAME_KEY, username)).doReturn(editor)

        mainViewModel.saveUsername(username)

        then(sharedPrefs).should().edit()
        then(editor).should().putString(SHARED_PREFERENCE_USERNAME_KEY, username)
    }

    companion object {
        fun createEditor() : SharedPreferences.Editor {
            return object: SharedPreferences.Editor {
                override fun putString(p0: String?, p1: String?): SharedPreferences.Editor {
                    return this as SharedPreferences.Editor
                }

                override fun putStringSet(
                    p0: String?,
                    p1: MutableSet<String>?
                ): SharedPreferences.Editor {
                    return this
                }

                override fun putInt(p0: String?, p1: Int): SharedPreferences.Editor {
                    return this
                }

                override fun putLong(p0: String?, p1: Long): SharedPreferences.Editor {
                    return this
                }

                override fun putFloat(p0: String?, p1: Float): SharedPreferences.Editor {
                    return this
                }

                override fun putBoolean(p0: String?, p1: Boolean): SharedPreferences.Editor {
                    return this
                }

                override fun remove(p0: String?): SharedPreferences.Editor {
                    return this
                }

                override fun clear(): SharedPreferences.Editor {
                    return this
                }

                override fun commit(): Boolean {
                    return true
                }

                override fun apply() {
                    return
                }
            }
        }
    }
}