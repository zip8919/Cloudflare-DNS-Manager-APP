package xyz.zip8919.app.cfdnsman.view.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import xyz.zip8919.app.cfdnsman.R;
import xyz.zip8919.app.cfdnsman.data.local.PreferencesManager;
import xyz.zip8919.app.cfdnsman.data.model.DNSRecord;
import xyz.zip8919.app.cfdnsman.data.repository.CloudflareRepository;
import xyz.zip8919.app.cfdnsman.view.adapter.DNSRecordAdapter;
import xyz.zip8919.app.cfdnsman.view.dialog.DNSRecordDialog;
import xyz.zip8919.app.cfdnsman.viewmodel.DNSManagementViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class DNSManagementActivity extends AppCompatActivity implements 
    DNSRecordAdapter.OnDNSRecordClickListener,
    DNSRecordDialog.OnDNSRecordSavedListener {
    
    private DNSManagementViewModel viewModel;
    private DNSRecordAdapter adapter;
    
    private String zoneId;
    private String zoneName;
    
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView textEmpty;
    private FloatingActionButton fabAdd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dns_management);
        
        // Get zone info from intent
        zoneId = getIntent().getStringExtra("zone_id");
        zoneName = getIntent().getStringExtra("zone_name");
        
        initViews();
        setupViewModel();
        setupRecyclerView();
        observeViewModel();
        
        // Load DNS records
        viewModel.loadDNSRecords();
    }
    
    private void initViews() {
        setTitle("DNS 记录 - " + zoneName);
        
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        textEmpty = findViewById(R.id.text_empty);
        fabAdd = findViewById(R.id.fab_add);
        
        fabAdd.setOnClickListener(v -> showAddRecordDialog());
    }
    
    private void setupViewModel() {
        PreferencesManager preferencesManager = new PreferencesManager(this);
        CloudflareRepository repository = new CloudflareRepository(preferencesManager);
        repository.initializeApiService(this);
        
        DNSManagementViewModel.Factory factory = new DNSManagementViewModel.Factory(repository);
        viewModel = new ViewModelProvider(this, factory).get(DNSManagementViewModel.class);
        
        viewModel.setCurrentZoneId(zoneId);
    }
    
    private void setupRecyclerView() {
        adapter = new DNSRecordAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    
    private void observeViewModel() {
        viewModel.getDnsRecords().observe(this, records -> {
            adapter.setDnsRecords(records);
            textEmpty.setVisibility(records == null || records.isEmpty() ? View.VISIBLE : View.GONE);
        });
        
        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
        
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Snackbar.make(recyclerView, errorMessage, Snackbar.LENGTH_LONG).show();
            }
        });
        
        viewModel.getSuccessMessage().observe(this, successMessage -> {
            if (successMessage != null) {
                Snackbar.make(recyclerView, successMessage, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    
    private void showAddRecordDialog() {
        DNSRecordDialog dialog = DNSRecordDialog.newInstance(null, zoneName);
        dialog.show(getSupportFragmentManager(), "dns_record_dialog");
    }
    
    private void showEditRecordDialog(DNSRecord record) {
        DNSRecordDialog dialog = DNSRecordDialog.newInstance(record, zoneName);
        dialog.show(getSupportFragmentManager(), "dns_record_dialog");
    }
    
    private void showDeleteConfirmationDialog(DNSRecord record) {
        new AlertDialog.Builder(this)
            .setTitle("删除 DNS 记录")
            .setMessage("你确定要删除 " + record.getName() + "吗?")
            .setPositiveButton("删除", (dialog, which) -> {
                viewModel.deleteDNSRecord(record.getId());
            })
            .setNegativeButton("取消", null)
            .show();
    }
    
    @Override
    public void onDNSRecordClick(DNSRecord record) {
        showEditRecordDialog(record);
    }
    
    @Override
    public void onDNSRecordLongClick(DNSRecord record) {
        showDeleteConfirmationDialog(record);
    }
    
    @Override
    public void onDNSRecordSaved(DNSRecord record, boolean isNew) {
        if (isNew) {
            viewModel.createDNSRecord(record);
        } else {
            viewModel.updateDNSRecord(record.getId(), record);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dns_management, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            viewModel.loadDNSRecords();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}