package xyz.zip8919.app.cfdnsman.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import xyz.zip8919.app.cfdnsman.R;
import xyz.zip8919.app.cfdnsman.data.local.PreferencesManager;
import xyz.zip8919.app.cfdnsman.data.repository.CloudflareRepository;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    
    private PreferencesManager preferencesManager;
    private CloudflareRepository repository;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        preferencesManager = new PreferencesManager(this);
        repository = new CloudflareRepository(preferencesManager);
        repository.initializeApiService(this);
        
        if (repository.isAuthenticated()) {
            // User is already authenticated, go to domain list
            startDomainListActivity();
            finish();
        } else {
            // Show API token input
            showApiTokenInput();
        }
    }
    
    private void showApiTokenInput() {
        Button btnSaveToken = findViewById(R.id.btn_save_token);
        TextInputEditText editToken = findViewById(R.id.edit_token);
        
        btnSaveToken.setOnClickListener(v -> {
            String token = editToken.getText().toString().trim();
            if (token.isEmpty()) {
                Toast.makeText(this, "请输入你的 API 令牌", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Save token and verify
            repository.saveApiToken(token);
            verifyAndProceed();
        });
    }
    
    private void verifyAndProceed() {
        // Simple verification by trying to fetch zones
        repository.getZones().subscribe(
            response -> {
                if (response.isSuccess()) {
                    startDomainListActivity();
                    finish();
                } else {
                    repository.clearAuthentication();
                    Toast.makeText(this, "无效 API 令牌", Toast.LENGTH_LONG).show();
                }
            },
            throwable -> {
                repository.clearAuthentication();
                Toast.makeText(this, "网络错误: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        );
    }
    
    private void startDomainListActivity() {
        Intent intent = new Intent(this, DomainListActivity.class);
        startActivity(intent);
    }
}
