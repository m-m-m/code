/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.node;

import io.github.mmm.code.api.item.CodeItemWithDeclaringType;
import io.github.mmm.code.api.member.CodeOperation;

/**
 * {@link CodeItemWithDeclaringType} that has an optional {@link #getDeclaringOperation() declaring
 * operation}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeNodeItemWithDeclaringOperation extends CodeItemWithDeclaringType {

  /**
   * @return the {@link CodeOperation} declaring this item or {@code null} if {@link #getDeclaringType()
   *         declared by a type}.
   */
  CodeOperation getDeclaringOperation();

}
