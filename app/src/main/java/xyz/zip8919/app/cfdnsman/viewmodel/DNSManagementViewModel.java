package xyz.zip8919.app.cfdnsman.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import xyz.zip8919.app.cfdnsman.data.model.ApiResponse;
import xyz.zip8919.app.cfdnsman.data.model.DNSRecord;
import xyz.zip8919.app.cfdnsman.data.model.Zone;
import xyz.zip8919.app.cfdnsman.data.repository.CloudflareRepository;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class DNSManagementViewModel extends ViewModel {
    private CloudflareRepository repository;
    private CompositeDisposable disposables = new CompositeDisposable();
    
    private MutableLiveData<List<DNSRecord>> dnsRecords = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<String> successMessage = new MutableLiveData<>();
    
    private String currentZoneId;
    
    public DNSManagementViewModel(CloudflareRepository repository) {
        this.repository = repository;
    }
    
    public void setCurrentZoneId(String zoneId) {
        this.currentZoneId = zoneId;
    }
    
    public void loadDNSRecords() {
        if (currentZoneId == null) return;
        
        isLoading.setValue(true);
        
        disposables.add(repository.getDNSRecords(currentZoneId).subscribe(
            response -> {
                isLoading.setValue(false);
                if (response.isSuccess() && response.getResult() != null) {
                    dnsRecords.setValue(response.getResult());
                } else {
                    errorMessage.setValue("加载 DNS 记录失败");
                }
            },
            throwable -> {
                isLoading.setValue(false);
                errorMessage.setValue("网络错误: " + throwable.getMessage());
            }
        ));
    }
    
    public void createDNSRecord(DNSRecord record) {
        if (currentZoneId == null) return;
        
        isLoading.setValue(true);
        
        disposables.add(repository.createDNSRecord(currentZoneId, record).subscribe(
            response -> {
                isLoading.setValue(false);
                if (response.isSuccess()) {
                    successMessage.setValue("DNS 记录创建成功");
                    loadDNSRecords(); // Refresh the list
                } else {
                    errorMessage.setValue("DNS 记录创建失败");
                }
            },
            throwable -> {
                isLoading.setValue(false);
                errorMessage.setValue("网络错误: " + throwable.getMessage());
            }
        ));
    }
    
    public void updateDNSRecord(String recordId, DNSRecord record) {
        if (currentZoneId == null) return;
        
        isLoading.setValue(true);
        
        disposables.add(repository.updateDNSRecord(currentZoneId, recordId, record).subscribe(
            response -> {
                isLoading.setValue(false);
                if (response.isSuccess()) {
                    successMessage.setValue("DNS 记录更新成功");
                    loadDNSRecords(); // Refresh the list
                } else {
                    errorMessage.setValue("DNS 记录更新失败");
                }
            },
            throwable -> {
                isLoading.setValue(false);
                errorMessage.setValue("网络错误: " + throwable.getMessage());
            }
        ));
    }
    
    public void deleteDNSRecord(String recordId) {
        if (currentZoneId == null) return;
        
        isLoading.setValue(true);
        
        disposables.add(repository.deleteDNSRecord(currentZoneId, recordId).subscribe(
            response -> {
                isLoading.setValue(false);
                if (response.isSuccess()) {
                    successMessage.setValue("DNS 记录删除成功");
                    loadDNSRecords(); // Refresh the list
                } else {
                    errorMessage.setValue("DNS 记录删除失败");
                }
            },
            throwable -> {
                isLoading.setValue(false);
                errorMessage.setValue("网络错误: " + throwable.getMessage());
            }
        ));
    }
    
    public LiveData<List<DNSRecord>> getDnsRecords() {
        return dnsRecords;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
    
    // 添加 Factory 类
    public static class Factory implements ViewModelProvider.Factory {
        private CloudflareRepository repository;
        
        public Factory(CloudflareRepository repository) {
            this.repository = repository;
        }
        
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(DNSManagementViewModel.class)) {
                return (T) new DNSManagementViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}