package com.artemkinko.lab4_8.ui.pages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PagesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PagesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is pages fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}