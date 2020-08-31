package com.dinkar.biometricsample.ui.biometric_pin;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BiometricAndPinViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BiometricAndPinViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Biometric + Pin fragment");
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