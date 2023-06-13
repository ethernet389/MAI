package com.example.mai2.main_programme;

import android.app.Application;

import com.yariksoffice.lingver.Lingver;
import com.yariksoffice.lingver.store.PreferenceLocaleStore;

import java.util.Locale;

public class App extends Application {
    @Override
    public void onCreate() {
        PreferenceLocaleStore store = new PreferenceLocaleStore(this, new Locale("ru"));
        Lingver.init(this, store);
        super.onCreate();
    }
}
