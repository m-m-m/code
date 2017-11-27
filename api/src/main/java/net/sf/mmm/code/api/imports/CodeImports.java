/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.imports;

import net.sf.mmm.code.api.CodeFile;
import net.sf.mmm.code.api.copy.CodeNodeItemCopyable;
import net.sf.mmm.code.api.node.CodeNodeItemContainerFlat;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeNodeItemContainerFlat} containing the {@link CodeImport}s of a {@link CodeFile}.
 *
 * @see CodeFile#getImports()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeImports extends CodeNodeItemContainerFlat<CodeImport>, CodeNodeItemCopyable<CodeFile, CodeImports> {

  /**
   * @param type the {@link CodeType} to import.
   * @return the new {@link CodeImport} that has been added or {@code null} if no (additional) import is required.
   */
  CodeImport add(CodeType type);

  @Override
  CodeImports copy();
}
