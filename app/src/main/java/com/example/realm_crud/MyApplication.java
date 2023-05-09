package com.example.realm_crud;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        io.realm.RealmConfiguration config = new io.realm.RealmConfiguration.Builder()
                .allowQueriesOnUiThread(true).allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
    }
}
