/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.node;

import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeNodeItem} that has a {@link #getDeclaringType() declaring type}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeNodeItemWithDeclaringType extends CodeNodeItem {

  /**
   * @return the {@link CodeType} declaring this {@link CodeItem}. Here declaring means owning and containing.
   *         May optionally be {@code null} for {@link CodeType} (see {@link CodeType#getDeclaringType()}) and
   *         will always be {@code null} for {@link CodePackage} (where it will always be {@code null}). The
   *         declaring type can never be changed.
   * @see CodeType#getDeclaringType()
   */
  CodeType getDeclaringType();

}
