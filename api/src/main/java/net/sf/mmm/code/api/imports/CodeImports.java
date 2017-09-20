/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.imports;

import net.sf.mmm.code.api.CodeFile;
import net.sf.mmm.code.api.node.CodeNodeItemContainerFlat;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.api.type.CodeTypeVariables;

/**
 * {@link CodeGenericType} representing a type variable. It is a variable as a placeholder for a
 * {@link CodeGenericType generic} {@link CodeType type}.
 *
 * @see java.lang.reflect.TypeVariable
 * @see CodeType#getTypeParameters()
 * @see CodeGenericType#asTypeVariable()
 * @see CodeTypeVariables
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeImport}s.
 * @since 1.0.0
 */
public abstract interface CodeImports<I extends CodeImport> extends CodeNodeItemContainerFlat<I> {

  @Override
  CodeFile getParent();

  /**
   * @param type the {@link CodeType} to import.
   * @return the new {@link CodeImport} that has been added or {@code null} if no (additional) import is
   *         required.
   */
  I add(CodeType type);

  @Override
  CodeImports<I> copy();

}
