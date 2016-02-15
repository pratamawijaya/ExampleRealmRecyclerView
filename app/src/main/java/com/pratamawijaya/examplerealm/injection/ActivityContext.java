package com.pratamawijaya.examplerealm.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;

/**
 * Created by : pratama - set.mnemonix@gmail.com
 * Date : 2/15/16
 * Project : ExampleRealm
 */
@Qualifier @Retention(RetentionPolicy.RUNTIME) public @interface ActivityContext {
}
