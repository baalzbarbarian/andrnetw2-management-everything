package com.practice.andr_networking_asm.ui.addnewcontent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddNewContentViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddNewContentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is add new content fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}