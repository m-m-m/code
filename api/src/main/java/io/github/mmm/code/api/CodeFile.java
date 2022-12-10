/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api;

import java.util.List;

import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.imports.CodeImport;
import io.github.mmm.code.api.imports.CodeImports;
import io.github.mmm.code.api.node.CodeNodeWithFileWriting;
import io.github.mmm.code.api.type.CodeType;

/**
 * Abstract top-level interface for any item of code as defined by this API. It reflects code structure.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeFile extends CodePathElement, CodeNodeWithFileWriting, CodeNodeItemCopyable<CodePackage, CodeFile> {

  /**
   * @return the file extension including the dot (e.g. ".java").
   */
  String getExtension();

  /**
   * @return the main type contained in this file.
   */
  default CodeType getType() {

    List<? extends CodeType> types = getTypes();
    if (types.isEmpty()) {
      return null;
    }
    return types.get(0);
  }

  /**
   * @return the {@link List} with all {@link CodeType#isNested() top-level} {@link CodeType}s contained in this file.
   *         For clean Java code this should be only one single type.
   */
  List<? extends CodeType> getTypes();

  /**
   * @return the {@link CodeImports} containing the {@link CodeImport}s. May be {@link List#isEmpty() empty} but is
   *         never {@code null}.
   */
  CodeImports getImports();

  @Override
  Class<?> getReflectiveObject();

  @Override
  CodeFile copy();

  @Override
  CodeFile copy(CodeCopyMapper mapper);

}
