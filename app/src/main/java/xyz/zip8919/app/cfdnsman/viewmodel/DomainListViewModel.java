package xyz.zip8919.app.cfdnsman.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import xyz.zip8919.app.cfdnsman.data.model.ApiResponse;
import xyz.zip8919.app.cfdnsman.data.model.Zone;
import xyz.zip8919.app.cfdnsman.data.repository.CloudflareRepository;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class DomainListViewModel extends ViewModel {
    private CloudflareRepository repository;
    private CompositeDisposable disposables = new CompositeDisposable();
    
    private MutableLiveData<List<Zone>> zones = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    public DomainListViewModel(CloudflareRepository repository) {
        this.repository = repository;
    }
    
    public void loadZones() {
        isLoading.setValue(true);
        
        disposables.add(repository.getZones().subscribe(
            response -> {
                isLoading.setValue(false);
                if (response.isSuccess() && response.getResult() != null) {
                    zones.setValue(response.getResult());
                } else {
                    errorMessage.setValue("加载域名失败");
                }
            },
            throwable -> {
                isLoading.setValue(false);
                errorMessage.setValue("网络错误: " + throwable.getMessage());
            }
        ));
    }
    
    public LiveData<List<Zone>> getZones() {
        return zones;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
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
            if (modelClass.isAssignableFrom(DomainListViewModel.class)) {
                return (T) new DomainListViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
