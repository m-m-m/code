/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import net.sf.mmm.code.api.CodePathElement;
import net.sf.mmm.code.api.node.CodeNodeItem;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeNodeItem} that has a {@link #getDeclaringType() declaring type}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeItemWithDeclaringType extends CodeItem {

  /**
   * @return the {@link CodeType} declaring this {@link CodeItem}. Here declaring means owning and containing.
   *         Will always be {@code null} for {@link CodePathElement}s (packages or files). Has a special
   *         meaning and may return itself for {@link CodeType} (see {@link CodeType#getDeclaringType()}). The
   *         declaring type can never be changed.
   * @see CodeType#getDeclaringType()
   */
  CodeType getDeclaringType();

}
