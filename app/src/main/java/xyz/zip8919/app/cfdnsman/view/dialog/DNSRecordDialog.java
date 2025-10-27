package xyz.zip8919.app.cfdnsman.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import xyz.zip8919.app.cfdnsman.R;
import xyz.zip8919.app.cfdnsman.data.model.DNSRecord;
import com.google.android.material.textfield.TextInputEditText;

public class DNSRecordDialog extends DialogFragment {
    
    private static final String ARG_RECORD = "record";
    private static final String ARG_ZONE_NAME = "zone_name";
    
    private DNSRecord record;
    private String zoneName;
    private OnDNSRecordSavedListener listener;
    
    private TextInputEditText editName;
    private Spinner spinnerType;
    private TextInputEditText editContent;
    private TextInputEditText editTtl;
    private CheckBox checkProxied;
    private TextInputEditText editPriority;
    private Button btnSave;
    private Button btnCancel;
    
    public interface OnDNSRecordSavedListener {
        void onDNSRecordSaved(DNSRecord record, boolean isNew);
    }
    
    public static DNSRecordDialog newInstance(DNSRecord record, String zoneName) {
        DNSRecordDialog dialog = new DNSRecordDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECORD, record);
        args.putString(ARG_ZONE_NAME, zoneName);
        dialog.setArguments(args);
        return dialog;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getArguments() != null) {
            record = (DNSRecord) getArguments().getSerializable(ARG_RECORD);
            zoneName = getArguments().getString(ARG_ZONE_NAME);
        }
        
        try {
            listener = (OnDNSRecordSavedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnDNSRecordSavedListener");
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_dns_record, container, false);
        
        initViews(view);
        setupSpinner();
        populateData();
        setupListeners();
        
        return view;
    }
    
    private void initViews(View view) {
        editName = view.findViewById(R.id.edit_name);
        spinnerType = view.findViewById(R.id.spinner_type);
        editContent = view.findViewById(R.id.edit_content);
        editTtl = view.findViewById(R.id.edit_ttl);
        checkProxied = view.findViewById(R.id.check_proxied);
        editPriority = view.findViewById(R.id.edit_priority);
        btnSave = view.findViewById(R.id.btn_save);
        btnCancel = view.findViewById(R.id.btn_cancel);
    }
    
    private void setupSpinner() {
        String[] recordTypes = {"A", "AAAA", "CNAME", "MX", "TXT", "NS", "SRV"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireContext(), 
            android.R.layout.simple_spinner_item, 
            recordTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }
    
    private void populateData() {
        if (record != null) {
            // Editing existing record
            editName.setText(record.getName());
            
            // Set spinner selection
            String[] recordTypes = {"A", "AAAA", "CNAME", "MX", "TXT", "NS", "SRV"};
            for (int i = 0; i < recordTypes.length; i++) {
                if (recordTypes[i].equals(record.getType())) {
                    spinnerType.setSelection(i);
                    break;
                }
            }
            
            editContent.setText(record.getContent());
            editTtl.setText(String.valueOf(record.getTtl()));
            
            if (record.getProxied() != null) {
                checkProxied.setChecked(record.getProxied());
            }
            
            if (record.getPriority() != null) {
                editPriority.setText(String.valueOf(record.getPriority()));
            }
        } else {
            // New record - set defaults
            editTtl.setText("300");
            checkProxied.setChecked(true);
        }
    }
    
    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveRecord());
        btnCancel.setOnClickListener(v -> dismiss());
        
        // Show/hide priority field for MX records
        spinnerType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedType = (String) parent.getItemAtPosition(position);
                if ("MX".equals(selectedType)) {
                    editPriority.setVisibility(View.VISIBLE);
                } else {
                    editPriority.setVisibility(View.GONE);
                }
            }
            
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }
    
    private void saveRecord() {
        String name = editName.getText().toString().trim();
        String type = (String) spinnerType.getSelectedItem();
        String content = editContent.getText().toString().trim();
        String ttlStr = editTtl.getText().toString().trim();
        String priorityStr = editPriority.getText().toString().trim();
        
        // Validation
        if (name.isEmpty() || content.isEmpty() || ttlStr.isEmpty()) {
            // Show error
            return;
        }
        
        int ttl = Integer.parseInt(ttlStr);
        DNSRecord newRecord = new DNSRecord();
        
        if (record != null) {
            newRecord.setId(record.getId());
        }
        
        newRecord.setType(type);
        newRecord.setName(name);
        newRecord.setContent(content);
        newRecord.setTtl(ttl);
        newRecord.setProxied(checkProxied.isChecked());
        
        if (!priorityStr.isEmpty()) {
            newRecord.setPriority(Integer.parseInt(priorityStr));
        }
        
        if (listener != null) {
            listener.onDNSRecordSaved(newRecord, record == null);
        }
        
        dismiss();
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (record != null) {
            dialog.setTitle("编辑 DNS 记录");
        } else {
            dialog.setTitle("添加 DNS 记录");
        }
        return dialog;
    }
}