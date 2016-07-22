package com.yknx4.notificationtracker.models

import com.securepreferences.SecurePreferences
import com.yknx4.notificationtracker.PreferencesFields

/**
 * Created by yknx4 on 7/20/16.
 */
class AuthInformation(private val sharedPreferences: SecurePreferences) {

    private fun getProperty(property: String): String {
        return sharedPreferences.getString(property, "")
    }

    val email: String
        get() = getProperty(PreferencesFields.EMAIL)

    val password: String
        get() = getProperty(PreferencesFields.PASSWORD)

    val accessToken: String
        get() = getProperty(PreferencesFields.ACCESS_TOKEN)

    val client: String
        get() = getProperty(PreferencesFields.CLIENT)

    val tokenType: String
        get() = getProperty(PreferencesFields.TOKEN_TYPE)

    val expiry: String
        get() = getProperty(PreferencesFields.EXPIRY)

    val uid: String
        get() = getProperty(PreferencesFields.UID)

    fun edit(): Setter {
        return Setter(sharedPreferences.edit())
    }

    inner class Setter internal constructor(private val _editor: SecurePreferences.Editor) {

        private fun setProperty(property: String, value: Any): Setter {
            _editor.putString(property, value.toString())
            return this
        }

        fun setEmail(value: String): Setter {
            return setProperty(PreferencesFields.EMAIL, value)
        }

        fun setPassword(value: String): Setter {
            return setProperty(PreferencesFields.PASSWORD, value)
        }

        fun setAccessToken(value: String): Setter {
            return setProperty(PreferencesFields.ACCESS_TOKEN, value)
        }

        fun setClient(value: String): Setter {
            return setProperty(PreferencesFields.CLIENT, value)
        }

        fun setTokenType(value: String): Setter {
            return setProperty(PreferencesFields.TOKEN_TYPE, value)
        }

        fun setExpiry(value: String): Setter {
            return setProperty(PreferencesFields.EXPIRY, value)
        }

        fun setUID(value: String): Setter {
            return setProperty(PreferencesFields.UID, value)
        }

        fun apply() {
            _editor.apply()
        }

        fun commit(): Boolean {
            return _editor.commit()
        }

    }

}