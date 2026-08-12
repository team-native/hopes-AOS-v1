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
private val accessTokenKey = stringPreferencesKey("encrypted_access_token")
private const val keyAlias = "hopes_access_token_key"

/** Android Keystore 키로 DataStore의 access token을 AES-GCM 암호화한다. */
@Singleton
class EncryptedTokenStore @Inject constructor(@ApplicationContext private val applicationContext: Context) {
    suspend fun save(accessToken: String) { applicationContext.sessionDataStore.edit { it[accessTokenKey] = encrypt(accessToken) } }
    suspend fun clear() { applicationContext.sessionDataStore.edit { it.remove(accessTokenKey) } }
    suspend fun read(): String? = applicationContext.sessionDataStore.data.first()[accessTokenKey]?.let(::decrypt)

    private fun secretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        (keyStore.getKey(keyAlias, null) as? SecretKey)?.let { return it }
        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        generator.init(KeyGenParameterSpec.Builder(keyAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).build())
        return generator.generateKey()
    }
    private fun encrypt(value: String): String { val cipher = Cipher.getInstance("AES/GCM/NoPadding"); cipher.init(Cipher.ENCRYPT_MODE, secretKey()); return Base64.encodeToString(cipher.iv + cipher.doFinal(value.toByteArray()), Base64.NO_WRAP) }
    private fun decrypt(value: String): String? = runCatching { val bytes = Base64.decode(value, Base64.NO_WRAP); val cipher = Cipher.getInstance("AES/GCM/NoPadding"); cipher.init(Cipher.DECRYPT_MODE, secretKey(), GCMParameterSpec(128, bytes.copyOfRange(0, 12))); String(cipher.doFinal(bytes.copyOfRange(12, bytes.size))) }.getOrNull()
}
