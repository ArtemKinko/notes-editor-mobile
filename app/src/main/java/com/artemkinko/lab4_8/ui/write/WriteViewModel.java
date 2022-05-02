package com.artemkinko.lab4_8.ui.write;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WriteViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public WriteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is write fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}