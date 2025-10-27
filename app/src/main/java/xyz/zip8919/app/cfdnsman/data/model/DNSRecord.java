package xyz.zip8919.app.cfdnsman.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class DNSRecord implements Serializable {
    @SerializedName("id")
    private String id;
    
    @SerializedName("type")
    private String type;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("content")
    private String content;
    
    @SerializedName("ttl")
    private int ttl;
    
    @SerializedName("proxied")
    private Boolean proxied;
    
    @SerializedName("priority")
    private Integer priority;
    
    @SerializedName("created_on")
    private Date createdOn;
    
    @SerializedName("modified_on")
    private Date modifiedOn;
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public int getTtl() { return ttl; }
    public void setTtl(int ttl) { this.ttl = ttl; }
    
    public Boolean getProxied() { return proxied; }
    public void setProxied(Boolean proxied) { this.proxied = proxied; }
    
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    
    public Date getCreatedOn() { return createdOn; }
    public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }
    
    public Date getModifiedOn() { return modifiedOn; }
    public void setModifiedOn(Date modifiedOn) { this.modifiedOn = modifiedOn; }
}
