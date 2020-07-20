package com.practice.andr_networking_asm.ui.searchuser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchUserViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SearchUserViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is search user fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}