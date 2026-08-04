package xyz.zip8919.app.cfdnsman.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BatchDNSRecordRequest {

    @SerializedName("deletes")
    private List<BatchDelete> deletes;

    @SerializedName("posts")
    private List<DNSRecord> posts;

    public BatchDNSRecordRequest() {
    }

    public static BatchDNSRecordRequest createReplace(String oldRecordId, DNSRecord newRecord) {
        BatchDNSRecordRequest request = new BatchDNSRecordRequest();

        request.deletes = new ArrayList<>(Collections.singletonList(new BatchDelete(oldRecordId)));
        request.posts = new ArrayList<>(Collections.singletonList(copyWithoutId(newRecord)));

        return request;
    }

    private static DNSRecord copyWithoutId(DNSRecord record) {
        DNSRecord copy = new DNSRecord();
        copy.setType(record.getType());
        copy.setName(record.getName());
        copy.setContent(record.getContent());
        copy.setTtl(record.getTtl());
        copy.setProxied(record.getProxied());
        copy.setPriority(record.getPriority());
        return copy;
    }

    public List<BatchDelete> getDeletes() { return deletes; }
    public void setDeletes(List<BatchDelete> deletes) { this.deletes = deletes; }

    public List<DNSRecord> getPosts() { return posts; }
    public void setPosts(List<DNSRecord> posts) { this.posts = posts; }

    public static class BatchDelete {
        @SerializedName("id")
        private String id;

        public BatchDelete(String id) {
            this.id = id;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
    }
}
