/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.node;

import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.item.CodeItem;

/**
 * {@link CodeItem} containing {@link CodeItem}s of a particular type. It groups these items and all the methods to
 * operate on them. This makes the API more structured as you do not get too much methods for top-level types such as
 * {@link io.github.mmm.code.api.type.CodeType}. Further it supports reuse and avoids redundant declaration of methods with
 * their JavaDoc. So instead of {@link Class#getDeclaredMethods()} you will do
 * {@link io.github.mmm.code.api.type.CodeType#getMethods()}.{@link io.github.mmm.code.api.member.CodeMethods#getDeclared()
 * getDeclared()}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}s.
 * @since 1.0.0
 */
public abstract interface CodeNodeItemContainer<I extends CodeItem> extends CodeNodeItem, CodeNodeContainer<I> {

  @Override
  CodeNodeItem getParent();

  /**
   * @param item the child item to remove.
   * @return {@code true} if successfully removed (inheritance not supported), {@code false} otherwise.
   */
  boolean remove(I item);

  @Override
  CodeNodeItemContainer<I> copy();

  @Override
  CodeNodeItemContainer<I> copy(CodeCopyMapper mapper);

}
