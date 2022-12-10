/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.annotation;

import java.lang.annotation.Annotation;
import java.util.Map;

import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.api.item.CodeMutableItemWithComment;
import io.github.mmm.code.api.item.CodeMutableItemWithType;

/**
 * {@link CodeMutableItemWithType} that represents an {@link java.lang.annotation.Annotation} instance.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeAnnotation extends CodeMutableItemWithType, CodeMutableItemWithComment, CodeNodeItemCopyable<CodeAnnotations, CodeAnnotation> {

  /** The character used as prefix for an annotation. */
  char PREFIX_CHAR = '@';

  /** The {@link String} used as prefix for an annotation. */
  String PREFIX = "@";

  /**
   * @return the {@link Map} with the parameters of this annotation. May be {@link Map#isEmpty() empty} but is never
   *         <code>null</code>. The {@link CodeExpression}s are most likely a
   *         {@link io.github.mmm.code.api.expression.CodeConstant}.
   */
  Map<String, CodeExpression> getParameters();

  @Override
  Annotation getReflectiveObject();

  @Override
  CodeAnnotations getParent();

  @Override
  CodeAnnotation copy();

  @Override
  CodeAnnotation copy(CodeCopyMapper mapper);

}
