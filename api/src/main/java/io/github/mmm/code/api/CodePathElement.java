/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api;

import io.github.mmm.code.api.element.CodeElement;
import io.github.mmm.code.api.item.CodeItemWithQualifiedNameAndParentPackage;
import io.github.mmm.code.api.node.CodeContainer;

/**
 * Abstract interface for any top-level {@link CodeElement} such as {@link CodePackage} or {@link CodeFile}. It reflects
 * code structure.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodePathElement extends CodeElement, CodeItemWithQualifiedNameAndParentPackage {

  @Override
  CodeContainer getParent();

  /**
   * @return {@code true} if this is a {@link CodeFile}, {@code false} otherwise (if a {@link CodePackage}).
   */
  boolean isFile();

}
