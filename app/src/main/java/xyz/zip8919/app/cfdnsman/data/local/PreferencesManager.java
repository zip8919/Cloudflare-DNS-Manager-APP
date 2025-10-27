package xyz.zip8919.app.cfdnsman.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class PreferencesManager {
    private static final String PREFS_NAME = "cloudflare_dns_prefs";
    private static final String KEY_API_TOKEN = "api_token";
    private static final String KEY_LAST_ZONE_DATA = "last_zone_data";
    
    private SharedPreferences encryptedPreferences;
    
    public PreferencesManager(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            
            encryptedPreferences = EncryptedSharedPreferences.create(
                PREFS_NAME,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            // Fallback to regular SharedPreferences (less secure)
            encryptedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
    }
    
    public void saveApiToken(String token) {
        encryptedPreferences.edit().putString(KEY_API_TOKEN, token).apply();
    }
    
    public String getApiToken() {
        return encryptedPreferences.getString(KEY_API_TOKEN, null);
    }
    
    public void clearApiToken() {
        encryptedPreferences.edit().remove(KEY_API_TOKEN).apply();
    }
    
    public boolean hasApiToken() {
        return getApiToken() != null && !getApiToken().isEmpty();
    }
    
    public void saveLastZoneData(String data) {
        encryptedPreferences.edit().putString(KEY_LAST_ZONE_DATA, data).apply();
    }
    
    public String getLastZoneData() {
        return encryptedPreferences.getString(KEY_LAST_ZONE_DATA, null);
    }
}