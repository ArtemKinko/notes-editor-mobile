package com.artemkinko.lab4_8.ui.document;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DocumentViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DocumentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is document fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}