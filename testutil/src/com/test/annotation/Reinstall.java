/*******************************************************************************
 *
 *    Copyright (c) Baina Info Tech Co. Ltd
 *
 *    testutil
 *
 *    Reinstall
 *    TODO File description or class description.
 *
 *    @author: danliu
 *    @since:  Sep 18, 2014
 *    @version: 1.0
 *
 ******************************************************************************/
package com.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Reinstall of testutil.
 * @author danliu
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Reinstall {
}
