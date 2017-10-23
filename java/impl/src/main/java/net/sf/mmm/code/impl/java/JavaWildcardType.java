/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple class to get access to an unbounded {@link WildcardType}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
class JavaWildcardType {

  private static final Logger LOG = LoggerFactory.getLogger(JavaWildcardType.class);

  public Class<?> type;

  static final WildcardType UNBOUNDED_WILDCARD;

  static {

    WildcardType wildcard = null;
    try {
      ParameterizedType pType = (ParameterizedType) JavaWildcardType.class.getField("type").getGenericType();
      wildcard = (WildcardType) pType.getActualTypeArguments()[0];
    } catch (Exception e) {
      LOG.error("Failed to retrieve unbounded wildcard type via reflection - JVM broken?", e);
    }
    UNBOUNDED_WILDCARD = wildcard;
  }

}
