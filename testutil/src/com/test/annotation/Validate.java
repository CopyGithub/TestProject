/*******************************************************************************
 *
 *    Copyright (c) Baina Info Tech Co. Ltd
 *
 *    testutil
 *
 *    Version
 *    TODO File description or class description.
 *
 *    @author: danliu
 *    @since:  Sep 22, 2014
 *    @version: 1.0
 *
 ******************************************************************************/
package com.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Version of testutil.
 * @author danliu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Validate {
    String value();
}
