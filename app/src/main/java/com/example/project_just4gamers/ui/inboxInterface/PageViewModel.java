package com.example.project_just4gamers.ui.inboxInterface;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> index = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(index, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });

    public void setIndex(int index) {
        this.index.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}