package xyz.zip8919.app.cfdnsman.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import xyz.zip8919.app.cfdnsman.R;
import xyz.zip8919.app.cfdnsman.data.local.PreferencesManager;
import xyz.zip8919.app.cfdnsman.data.model.Zone;
import xyz.zip8919.app.cfdnsman.data.repository.CloudflareRepository;
import xyz.zip8919.app.cfdnsman.view.adapter.DomainAdapter;
import xyz.zip8919.app.cfdnsman.viewmodel.DomainListViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class DomainListActivity extends AppCompatActivity implements DomainAdapter.OnDomainClickListener {
    
    private DomainListViewModel viewModel;
    private DomainAdapter adapter;
    private List<Zone> allZones = new ArrayList<>();
    
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SearchView searchView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domain_list);
        
        initViews();
        setupViewModel();
        setupRecyclerView();
        observeViewModel();
        
        // Load domains
        viewModel.loadZones();
    }
    
    private void initViews() {
        setTitle("Cloudflare 域名");
        
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        searchView = findViewById(R.id.search_view);
        
        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.loadZones();
        });
        
        setupSearchView();
    }
    
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                filterDomains(newText);
                return true;
            }
        });
    }
    
    private void filterDomains(String query) {
        List<Zone> filtered = new ArrayList<>();
        if (query.isEmpty()) {
            filtered.addAll(allZones);
        } else {
            for (Zone zone : allZones) {
                if (zone.getName().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(zone);
                }
            }
        }
        adapter.setZones(filtered);
    }
    
    private void setupViewModel() {
        PreferencesManager preferencesManager = new PreferencesManager(this);
        CloudflareRepository repository = new CloudflareRepository(preferencesManager);
        repository.initializeApiService(this);
        
        DomainListViewModel.Factory factory = new DomainListViewModel.Factory(repository);
        viewModel = new ViewModelProvider(this, factory).get(DomainListViewModel.class);
    }
    
    private void setupRecyclerView() {
        adapter = new DomainAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    
    private void observeViewModel() {
        viewModel.getZones().observe(this, zones -> {
            allZones.clear();
            allZones.addAll(zones);
            adapter.setZones(zones);
        });
        
        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            swipeRefreshLayout.setRefreshing(isLoading);
        });
        
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Snackbar.make(recyclerView, errorMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }
    
    @Override
    public void onDomainClick(Zone zone) {
        Intent intent = new Intent(this, DNSManagementActivity.class);
        intent.putExtra("zone_id", zone.getId());
        intent.putExtra("zone_name", zone.getName());
        startActivity(intent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_domain_list, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            // Clear token and go back to main activity
            PreferencesManager preferencesManager = new PreferencesManager(this);
            preferencesManager.clearApiToken();
            
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}