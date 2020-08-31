package com.dinkar.biometricsample.ui.biometric;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dinkar.biometricsample.R;

import java.util.concurrent.Executor;

public class BiometricFragment extends Fragment {

    private BiometricViewModel biometricViewModel;
    private boolean isBiometricEnabled;
    private boolean isBiometricPresent;
    private static final String TAG = BiometricFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        biometricViewModel = new
                ViewModelProvider(this).get(BiometricViewModel.class);
        View root = inflater.inflate(R.layout.fragment_biometric, container, false);
        final TextView textView = root.findViewById(R.id.text_biometric);
        biometricViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        checkIfBiometricIsSupported();
        if (isBiometricEnabled) {
            showBiometricPrompt();
        } else if (isBiometricPresent) {
            biometricViewModel.setText(getString(R.string.biometric_not_enabled));
        } else {
            biometricViewModel.setText(getString(R.string.biometric_not_present));
        }
        return root;
    }

    private void checkIfBiometricIsSupported() {
        int id = BiometricManager.from(getContext()).canAuthenticate();
        if (id == BiometricManager.BIOMETRIC_SUCCESS) {
            isBiometricEnabled = true;
            isBiometricPresent = true;
        } else if (id == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE) {
            Log.d(TAG, "No biometric features available on this device.");
            isBiometricEnabled = false;
            isBiometricPresent = false;
        } else if (id == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE) {
            Log.d(TAG, "Biometric features are currently unavailable.");
            isBiometricEnabled = false;
            isBiometricPresent = false;
        } else if (id == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
            Log.d(TAG, "he user hasn't associated any biometric credentials with their account.");
            isBiometricEnabled = false;
            isBiometricPresent = true;
        }
    }

    private void showBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(getContext());
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Log.e(TAG, "onAuthenticationError");
                        biometricViewModel.setText(getString(R.string.failed_biometric));

                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Log.d(TAG, "onAuthenticationSucceeded");
                        biometricViewModel.setText(getString(R.string.successfully_validated_biometric));
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Log.e(TAG, "onAuthenticationFailed");
                        biometricViewModel.setText(getString(R.string.failed_biometric));
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Validation")
                .setSubtitle("This is is to validate using your biometric to check whether you are valid user to see the screen content")
                .setNegativeButtonText("Cancel")
                .build();
        biometricPrompt.authenticate(promptInfo);
    }
}