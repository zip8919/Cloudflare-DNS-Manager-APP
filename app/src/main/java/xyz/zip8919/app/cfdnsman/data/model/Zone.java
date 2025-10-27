package xyz.zip8919.app.cfdnsman.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Zone {
    @SerializedName("id")
    private String id;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("created_on")
    private Date createdOn;
    
    @SerializedName("modified_on")
    private Date modifiedOn;
    
    @SerializedName("name_servers")
    private String[] nameServers;
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Date getCreatedOn() { return createdOn; }
    public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }
    
    public Date getModifiedOn() { return modifiedOn; }
    public void setModifiedOn(Date modifiedOn) { this.modifiedOn = modifiedOn; }
    
    public String[] getNameServers() { return nameServers; }
    public void setNameServers(String[] nameServers) { this.nameServers = nameServers; }
}