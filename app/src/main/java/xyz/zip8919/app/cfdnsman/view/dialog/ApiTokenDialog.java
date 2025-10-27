package xyz.zip8919.app.cfdnsman.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import xyz.zip8919.app.cfdnsman.R;
import com.google.android.material.textfield.TextInputEditText;

public class ApiTokenDialog extends DialogFragment {
    
    private OnTokenSavedListener listener;
    
    private TextInputEditText editToken;
    private Button btnSave;
    private Button btnCancel;
    
    public interface OnTokenSavedListener {
        void onTokenSaved(String token);
    }
    
    public static ApiTokenDialog newInstance() {
        return new ApiTokenDialog();
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            listener = (OnTokenSavedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnTokenSavedListener");
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_api_token, container, false);
        
        initViews(view);
        setupListeners();
        
        return view;
    }
    
    private void initViews(View view) {
        editToken = view.findViewById(R.id.edit_token);
        btnSave = view.findViewById(R.id.btn_save);
        btnCancel = view.findViewById(R.id.btn_cancel);
    }
    
    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveToken());
        btnCancel.setOnClickListener(v -> dismiss());
    }
    
    private void saveToken() {
        String token = editToken.getText().toString().trim();
        if (token.isEmpty()) {
            // Show error
            return;
        }
        
        if (listener != null) {
            listener.onTokenSaved(token);
        }
        
        dismiss();
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("API 令牌设置");
        return dialog;
    }
}