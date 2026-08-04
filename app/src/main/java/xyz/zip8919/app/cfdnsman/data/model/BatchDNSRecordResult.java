package xyz.zip8919.app.cfdnsman.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BatchDNSRecordResult {

    @SerializedName("deletes")
    private List<DNSRecord> deletes;

    @SerializedName("posts")
    private List<DNSRecord> posts;

    @SerializedName("puts")
    private List<DNSRecord> puts;

    @SerializedName("patches")
    private List<DNSRecord> patches;

    public List<DNSRecord> getDeletes() { return deletes; }
    public void setDeletes(List<DNSRecord> deletes) { this.deletes = deletes; }

    public List<DNSRecord> getPosts() { return posts; }
    public void setPosts(List<DNSRecord> posts) { this.posts = posts; }

    public List<DNSRecord> getPuts() { return puts; }
    public void setPuts(List<DNSRecord> puts) { this.puts = puts; }

    public List<DNSRecord> getPatches() { return patches; }
    public void setPatches(List<DNSRecord> patches) { this.patches = patches; }
}
