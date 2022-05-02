package com.artemkinko.lab4_8.ui.weather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WeatherViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public WeatherViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Покажем вам погоду!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}