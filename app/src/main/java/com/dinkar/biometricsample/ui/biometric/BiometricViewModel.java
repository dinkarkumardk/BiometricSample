package com.dinkar.biometricsample.ui.biometric;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BiometricViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BiometricViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is biometric fragment");
    }

    public void setText(String text) {
        if (!TextUtils.isEmpty(text)) {
            this.mText.setValue(text);
        }
    }

    public LiveData<String> getText() {
        return mText;
    }
}