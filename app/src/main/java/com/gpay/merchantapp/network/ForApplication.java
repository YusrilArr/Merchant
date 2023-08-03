package com.gpay.merchantapp.network;

/**
 * Created by GPay on 23/06/20.
 */

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier @Retention(RUNTIME)
public @interface ForApplication {
}
