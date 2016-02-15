package com.pratamawijaya.examplerealm.injection.module;

import android.app.Activity;
import android.content.Context;
import com.pratamawijaya.examplerealm.injection.ActivityContext;
import dagger.Module;
import dagger.Provides;

/**
 * Created by : pratama - set.mnemonix@gmail.com
 * Date : 2/15/16
 * Project : ExampleRealm
 */
@Module public class ActivityModule {
  private Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides Activity provideActivity() {
    return activity;
  }

  @Provides @ActivityContext Context provideContext() {
    return activity;
  }
}
