package com.pratamawijaya.examplerealm.injection.module;

import android.app.Application;
import android.content.Context;
import com.pratamawijaya.examplerealm.injection.ApplicationContext;
import dagger.Module;
import dagger.Provides;

/**
 * Created by : pratama - set.mnemonix@gmail.com
 * Date : 2/15/16
 * Project : ExampleRealm
 */
@Module public class ApplicationModule {
  private final Application application;

  public ApplicationModule(Application application) {
    this.application = application;
  }

  @Provides Application provideApplication() {
    return application;
  }

  @Provides @ApplicationContext Context provideContext() {
    return application;
  }
}
