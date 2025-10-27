package xyz.zip8919.app.cfdnsman.data.repository;

import android.content.Context;

import xyz.zip8919.app.cfdnsman.data.api.ApiClient;
import xyz.zip8919.app.cfdnsman.data.api.CloudflareApiService;
import xyz.zip8919.app.cfdnsman.data.local.PreferencesManager;
import xyz.zip8919.app.cfdnsman.data.model.ApiResponse;
import xyz.zip8919.app.cfdnsman.data.model.DNSRecord;
import xyz.zip8919.app.cfdnsman.data.model.Zone;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CloudflareRepository {
    private CloudflareApiService apiService;
    private PreferencesManager preferencesManager;
    
    public CloudflareRepository(PreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }
    
    private CloudflareApiService getApiService() {
        return apiService;
    }
    
    public void initializeApiService(Context context) {
        this.apiService = ApiClient.getApiService(context);
    }
    
    private String getAuthorizationHeader() {
        String token = preferencesManager.getApiToken();
        return "Bearer " + token;
    }
    
    public Observable<ApiResponse<List<Zone>>> getZones() {
        return getApiService().getZones(getAuthorizationHeader(), 1, 50)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
    
    public Observable<ApiResponse<List<DNSRecord>>> getDNSRecords(String zoneId) {
        return getApiService().getDNSRecords(getAuthorizationHeader(), zoneId, 1, 100)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
    
    public Observable<ApiResponse<DNSRecord>> createDNSRecord(String zoneId, DNSRecord record) {
        return getApiService().createDNSRecord(getAuthorizationHeader(), zoneId, record)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
    
    public Observable<ApiResponse<DNSRecord>> updateDNSRecord(String zoneId, String recordId, DNSRecord record) {
        return getApiService().updateDNSRecord(getAuthorizationHeader(), zoneId, recordId, record)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
    
    public Observable<ApiResponse<Void>> deleteDNSRecord(String zoneId, String recordId) {
        return getApiService().deleteDNSRecord(getAuthorizationHeader(), zoneId, recordId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
    
    public boolean isAuthenticated() {
        return preferencesManager.hasApiToken();
    }
    
    public void saveApiToken(String token) {
        preferencesManager.saveApiToken(token);
    }
    
    public void clearAuthentication() {
        preferencesManager.clearApiToken();
    }
}
