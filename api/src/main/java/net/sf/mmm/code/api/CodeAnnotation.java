/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import java.util.Map;

import net.sf.mmm.code.api.item.CodeItemWithType;

/**
 * {@link CodeItemWithType} that represents an {@link java.lang.annotation.Annotation} instance.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeAnnotation extends CodeItemWithType {

  /**
   * @return the {@link Map} with the parameters of this annotation. May be {@link Map#isEmpty() empty} but is
   *         never <code>null</code>.
   */
  Map<String, Object> getParameters();

}
