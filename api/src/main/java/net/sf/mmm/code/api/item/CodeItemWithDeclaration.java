/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import java.io.IOException;

/**
 * {@link CodeItem} representing a declaration.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeItemWithDeclaration extends CodeItem {

  /**
   * Internal variant of {@link #write(Appendable)} to write a reference for the usage of this item.
   *
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code from
   *        this {@link CodeItem}.
   * @param declaration {@code true} if used as a declaration, {@code false} otherwise (for usage as
   *        reference).
   * @throws IOException if thrown by {@link Appendable}.
   */
  void writeReference(Appendable sink, boolean declaration) throws IOException;

}
