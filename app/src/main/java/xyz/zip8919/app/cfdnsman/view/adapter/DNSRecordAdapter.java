package xyz.zip8919.app.cfdnsman.view.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import xyz.zip8919.app.cfdnsman.R;
import xyz.zip8919.app.cfdnsman.data.model.DNSRecord;

import java.util.List;

public class DNSRecordAdapter extends RecyclerView.Adapter<DNSRecordAdapter.DNSRecordViewHolder> {
    
    private List<DNSRecord> dnsRecords;
    private OnDNSRecordClickListener listener;
    
    public interface OnDNSRecordClickListener {
        void onDNSRecordClick(DNSRecord record);
        void onDNSRecordLongClick(DNSRecord record);
    }
    
    public DNSRecordAdapter(OnDNSRecordClickListener listener) {
        this.listener = listener;
    }
    
    public void setDnsRecords(List<DNSRecord> dnsRecords) {
        this.dnsRecords = dnsRecords;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public DNSRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_dns_record, parent, false);
        return new DNSRecordViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull DNSRecordViewHolder holder, int position) {
        DNSRecord record = dnsRecords.get(position);
        
        holder.textRecordType.setText(record.getType());
        holder.textRecordName.setText(record.getName());
        holder.textRecordContent.setText(record.getContent());
        holder.textRecordTTL.setText(String.valueOf(record.getTtl()));
        
        if (record.getProxied() != null) {
            holder.textRecordProxied.setText(record.getProxied() ? "Cloudflare 代理" : "仅 DNS");
        }
        
        // 动态设置记录类型标签的背景颜色
        setRecordTypeColor(holder.textRecordType, record.getType());
        
        holder.cardRecord.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDNSRecordClick(record);
            }
        });
        
        holder.cardRecord.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onDNSRecordLongClick(record);
                return true;
            }
            return false;
        });
    }
    
    private void setRecordTypeColor(TextView textView, String recordType) {
        int colorRes;
        switch (recordType.toUpperCase()) {
            case "A":
                colorRes = R.color.record_type_a;
                break;
            case "AAAA":
                colorRes = R.color.record_type_aaaa;
                break;
            case "CNAME":
                colorRes = R.color.record_type_cname;
                break;
            case "MX":
                colorRes = R.color.record_type_mx;
                break;
            case "TXT":
                colorRes = R.color.record_type_txt;
                break;
            case "NS":
                colorRes = R.color.purple_500;
                break;
            case "SRV":
                colorRes = R.color.teal_700;
                break;
            default:
                colorRes = R.color.record_type_a;
        }
        
        GradientDrawable background = (GradientDrawable) textView.getBackground();
        if (background != null) {
            background.setColor(ContextCompat.getColor(textView.getContext(), colorRes));
        }
    }
    
    @Override
    public int getItemCount() {
        return dnsRecords != null ? dnsRecords.size() : 0;
    }
    
    static class DNSRecordViewHolder extends RecyclerView.ViewHolder {
        CardView cardRecord;
        TextView textRecordType;
        TextView textRecordName;
        TextView textRecordContent;
        TextView textRecordTTL;
        TextView textRecordProxied;
        
        public DNSRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            cardRecord = itemView.findViewById(R.id.card_record);
            textRecordType = itemView.findViewById(R.id.text_record_type);
            textRecordName = itemView.findViewById(R.id.text_record_name);
            textRecordContent = itemView.findViewById(R.id.text_record_content);
            textRecordTTL = itemView.findViewById(R.id.text_record_ttl);
            textRecordProxied = itemView.findViewById(R.id.text_record_proxied);
        }
    }
}