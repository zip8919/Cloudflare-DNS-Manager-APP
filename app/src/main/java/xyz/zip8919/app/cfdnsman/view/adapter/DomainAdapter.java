package xyz.zip8919.app.cfdnsman.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import xyz.zip8919.app.cfdnsman.R;
import xyz.zip8919.app.cfdnsman.data.model.Zone;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DomainAdapter extends RecyclerView.Adapter<DomainAdapter.DomainViewHolder> {
    
    private List<Zone> zones;
    private OnDomainClickListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    
    public interface OnDomainClickListener {
        void onDomainClick(Zone zone);
    }
    
    public DomainAdapter(OnDomainClickListener listener) {
        this.listener = listener;
    }
    
    public void setZones(List<Zone> zones) {
        this.zones = zones;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public DomainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_domain, parent, false);
        return new DomainViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull DomainViewHolder holder, int position) {
        Zone zone = zones.get(position);
        
        holder.textDomainName.setText(zone.getName());
        holder.textStatus.setText(zone.getStatus());
        holder.textCreated.setText(dateFormat.format(zone.getCreatedOn()));
        
        if (zone.getNameServers() != null) {
            holder.textNameServers.setText(
                String.format("NS: %s", zone.getNameServers()[0])
            );
        }
        
        holder.cardDomain.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDomainClick(zone);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return zones != null ? zones.size() : 0;
    }
    
    static class DomainViewHolder extends RecyclerView.ViewHolder {
        CardView cardDomain;
        TextView textDomainName;
        TextView textStatus;
        TextView textCreated;
        TextView textNameServers;
        
        public DomainViewHolder(@NonNull View itemView) {
            super(itemView);
            cardDomain = itemView.findViewById(R.id.card_domain);
            textDomainName = itemView.findViewById(R.id.text_domain_name);
            textStatus = itemView.findViewById(R.id.text_status);
            textCreated = itemView.findViewById(R.id.text_created);
            textNameServers = itemView.findViewById(R.id.text_name_servers);
        }
    }
}