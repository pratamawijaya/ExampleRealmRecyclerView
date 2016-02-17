package com.pratamawijaya.examplerealm;

import android.app.Application;
import com.pratamawijaya.examplerealm.model.Migration;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

/**
 * Created by : pratama - set.mnemonix@gmail.com
 * Date : 2/15/16
 * Project : ExampleRealm
 */
public class App extends Application {
  private static final long DB_VERSION = 1;

  @Override public void onCreate() {
    super.onCreate();
    RealmConfiguration realmConfiguration =
        new RealmConfiguration.Builder(this).name("pratama_book.realm")
            .schemaVersion(DB_VERSION)
            .migration(new Migration())
            .build();
    Realm.setDefaultConfiguration(realmConfiguration);

    if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());
  }
}
