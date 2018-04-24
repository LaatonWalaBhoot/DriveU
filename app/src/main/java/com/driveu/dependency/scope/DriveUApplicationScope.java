package com.driveu.dependency.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Aishwarya on 4/23/2018.
 */

@Scope
@Retention(RetentionPolicy.CLASS)
public @interface DriveUApplicationScope {
}
