/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.annotation;

import java.util.Map;

import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.item.CodeChildItem;
import net.sf.mmm.code.api.item.CodeMutableItemWithComment;
import net.sf.mmm.code.api.item.CodeMutableItemWithType;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeMutableItemWithType} that represents an {@link java.lang.annotation.Annotation} instance.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeAnnotation extends CodeChildItem, CodeMutableItemWithType, CodeMutableItemWithComment {

  /** The character used as prefix for an annotation. */
  char PREFIX_CHAR = '@';

  /** The {@link String} used as prefix for an annotation. */
  String PREFIX = "@";

  /**
   * @return the {@link Map} with the parameters of this annotation. May be {@link Map#isEmpty() empty} but is
   *         never <code>null</code>. The {@link CodeExpression}s are most likely a
   *         {@link net.sf.mmm.code.api.expression.CodeConstant}.
   */
  Map<String, CodeExpression> getParameters();

  @Override
  CodeType getType();

  @Override
  CodeAnnotation copy();

}
