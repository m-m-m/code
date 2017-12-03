/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeMutableItem} that has a {@link #getType() generic type}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeMutableItemWithType extends CodeMutableItem, CodeItemWithType {

  /**
   * @param type the new {@link #getType() type}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setType(CodeGenericType type);

}
