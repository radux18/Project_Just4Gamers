package com.example.project_just4gamers.firestore;

public interface Callback<R> {
    void runResultOnUiThread(R result);
}
