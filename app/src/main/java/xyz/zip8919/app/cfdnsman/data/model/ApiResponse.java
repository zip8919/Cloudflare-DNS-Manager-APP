package xyz.zip8919.app.cfdnsman.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse<T> {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("errors")
    private List<ApiError> errors;
    
    @SerializedName("messages")
    private List<String> messages;
    
    @SerializedName("result")
    private T result;
    
    @SerializedName("result_info")
    private ResultInfo resultInfo;
    
    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public List<ApiError> getErrors() { return errors; }
    public void setErrors(List<ApiError> errors) { this.errors = errors; }
    
    public List<String> getMessages() { return messages; }
    public void setMessages(List<String> messages) { this.messages = messages; }
    
    public T getResult() { return result; }
    public void setResult(T result) { this.result = result; }
    
    public ResultInfo getResultInfo() { return resultInfo; }
    public void setResultInfo(ResultInfo resultInfo) { this.resultInfo = resultInfo; }
    
    public static class ApiError {
        @SerializedName("code")
        private int code;
        
        @SerializedName("message")
        private String message;
        
        // Getters and Setters
        public int getCode() { return code; }
        public void setCode(int code) { this.code = code; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class ResultInfo {
        @SerializedName("page")
        private int page;
        
        @SerializedName("per_page")
        private int perPage;
        
        @SerializedName("total_pages")
        private int totalPages;
        
        @SerializedName("count")
        private int count;
        
        @SerializedName("total_count")
        private int totalCount;
        
        // Getters and Setters
        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        
        public int getPerPage() { return perPage; }
        public void setPerPage(int perPage) { this.perPage = perPage; }
        
        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
        
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
        
        public int getTotalCount() { return totalCount; }
        public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
    }
}