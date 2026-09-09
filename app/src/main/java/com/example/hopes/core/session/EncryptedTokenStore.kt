package com.example.hopes.core.session

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

private val Context.sessionDataStore by preferencesDataStore("secure_session")
private val accessTokenPreferenceKey = stringPreferencesKey("encrypted_access_token")
private val tokenTypePreferenceKey = stringPreferencesKey("encrypted_token_type")
private const val KEY_ALIAS = "hopes_access_token_key"
private const val GCM_AUTHENTICATION_TAG_LENGTH_BITS = 128
private const val GCM_INITIALIZATION_VECTOR_LENGTH_BYTES = 12

/** Android Keystore 키로 DataStore의 세션 인증 정보를 AES-GCM 암호화한다. */
@Singleton
class EncryptedTokenStore @Inject constructor(
    @param:ApplicationContext private val applicationContext: Context,
) {
    /** 로그인 성공 후 access token과 tokenType을 함께 암호화해 저장한다. */
    suspend fun save(
        accessToken: String,
        tokenType: String,
    ) {
        applicationContext.sessionDataStore.edit { preferences ->
            preferences[accessTokenPreferenceKey] = encrypt(accessToken)
            preferences[tokenTypePreferenceKey] = encrypt(tokenType)
        }
    }

    /** 로그아웃 또는 인증 만료 시 저장된 세션 인증 정보를 제거한다. */
    suspend fun clear() {
        applicationContext.sessionDataStore.edit { preferences ->
            preferences.remove(accessTokenPreferenceKey)
            preferences.remove(tokenTypePreferenceKey)
        }
    }

    /** 앱 시작 시 암호화된 access token과 tokenType을 복호화해 반환한다. */
    suspend fun read(): StoredSession? {
        val preferences = applicationContext.sessionDataStore.data.first()
        val encryptedAccessToken = preferences[accessTokenPreferenceKey]
        val encryptedTokenType = preferences[tokenTypePreferenceKey]

        if (encryptedAccessToken == null || encryptedTokenType == null) {
            if (encryptedAccessToken != null || encryptedTokenType != null) {
                clear()
            }
            return null
        }

        val accessToken = decrypt(encryptedAccessToken)
        val tokenType = decrypt(encryptedTokenType)

        if (accessToken.isNullOrBlank() || tokenType.isNullOrBlank()) {
            // Keystore 키가 초기화되었거나 저장 값이 손상된 경우 잘못된 세션을 남기지 않는다.
            clear()
            return null
        }

        return StoredSession(
            accessToken = accessToken,
            tokenType = tokenType,
        )
    }

    private fun secretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }
        val existingKey = keyStore.getKey(KEY_ALIAS, null) as? SecretKey

        if (existingKey != null) {
            return existingKey
        }

        val generator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore",
        )
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        generator.init(keyGenParameterSpec)
        return generator.generateKey()
    }

    private fun encrypt(value: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey())

        val encryptedValue = cipher.doFinal(value.toByteArray())
        return Base64.encodeToString(cipher.iv + encryptedValue, Base64.NO_WRAP)
    }

    private fun decrypt(value: String): String? {
        return runCatching {
            val encryptedBytes = Base64.decode(value, Base64.NO_WRAP)
            val initializationVector = encryptedBytes.copyOfRange(
                0,
                GCM_INITIALIZATION_VECTOR_LENGTH_BYTES,
            )
            val cipherText = encryptedBytes.copyOfRange(
                GCM_INITIALIZATION_VECTOR_LENGTH_BYTES,
                encryptedBytes.size,
            )
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val parameterSpec = GCMParameterSpec(
                GCM_AUTHENTICATION_TAG_LENGTH_BITS,
                initializationVector,
            )

            cipher.init(Cipher.DECRYPT_MODE, secretKey(), parameterSpec)
            String(cipher.doFinal(cipherText))
        }.getOrNull()
    }
}
