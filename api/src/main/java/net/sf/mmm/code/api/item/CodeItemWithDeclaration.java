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
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code from this
   *        {@link CodeItem}.
   * @param declaration {@code true} if used as a declaration, {@code false} otherwise (for usage as reference).
   * @param qualified {@code true} for {@link CodeItemWithQualifiedFlag#isQualified() fully qualified} type names,
   *        {@code false} otherwise.
   * @throws IOException if thrown by {@link Appendable}.
   */
  default void writeReference(Appendable sink, boolean declaration) throws IOException {

    writeReference(sink, declaration, null);
  }

  /**
   * Internal variant of {@link #write(Appendable)} to write a reference for the usage of this item.
   *
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code from this
   *        {@link CodeItem}.
   * @param declaration {@code true} if used as a declaration, {@code false} otherwise (for usage as reference).
   * @param qualified {@code Boolean#TRUE} to use {@link CodeItemWithQualifiedName#getQualifiedName() qualified name}
   *        for type names, {@code Boolean#FALSE} to use {@link CodeItemWithQualifiedName#getSimpleName() simple name}s,
   *        or {@code null} to use {@link CodeItemWithQualifiedFlag#isQualified() qualified flag}.
   * @throws IOException if thrown by {@link Appendable}.
   */
  void writeReference(Appendable sink, boolean declaration, Boolean qualified) throws IOException;

}
