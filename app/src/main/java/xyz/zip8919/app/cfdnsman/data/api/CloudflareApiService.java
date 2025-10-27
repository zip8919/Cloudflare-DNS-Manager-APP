package xyz.zip8919.app.cfdnsman.data.api;

import xyz.zip8919.app.cfdnsman.data.model.ApiResponse;
import xyz.zip8919.app.cfdnsman.data.model.DNSRecord;
import xyz.zip8919.app.cfdnsman.data.model.Zone;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CloudflareApiService {
    
    @GET("zones")
    Observable<ApiResponse<List<Zone>>> getZones(
        @Header("Authorization") String authorization,
        @Query("page") Integer page,
        @Query("per_page") Integer perPage
    );
    
    @GET("zones/{zone_id}/dns_records")
    Observable<ApiResponse<List<DNSRecord>>> getDNSRecords(
        @Header("Authorization") String authorization,
        @Path("zone_id") String zoneId,
        @Query("page") Integer page,
        @Query("per_page") Integer perPage
    );
    
    @POST("zones/{zone_id}/dns_records")
    Observable<ApiResponse<DNSRecord>> createDNSRecord(
        @Header("Authorization") String authorization,
        @Path("zone_id") String zoneId,
        @Body DNSRecord record
    );
    
    @PUT("zones/{zone_id}/dns_records/{record_id}")
    Observable<ApiResponse<DNSRecord>> updateDNSRecord(
        @Header("Authorization") String authorization,
        @Path("zone_id") String zoneId,
        @Path("record_id") String recordId,
        @Body DNSRecord record
    );
    
    @DELETE("zones/{zone_id}/dns_records/{record_id}")
    Observable<ApiResponse<Void>> deleteDNSRecord(
        @Header("Authorization") String authorization,
        @Path("zone_id") String zoneId,
        @Path("record_id") String recordId
    );
}