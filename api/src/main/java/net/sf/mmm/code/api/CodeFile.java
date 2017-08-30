/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import java.util.List;

import net.sf.mmm.code.api.imports.CodeImport;

/**
 * Abstract top-level interface for any item of code as defined by this API. It reflects code structure.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeFile extends CodeItemWithQualifiedName {

  /**
   * @return the file extension including the dot (e.g. ".java").
   */
  String getExtension();

  /**
   * @return the main type contained in this file.
   */
  default CodeType getType() {

    List<CodeType> types = getTypes();
    if (types.isEmpty()) {
      return null;
    }
    return types.get(0);
  }

  /**
   * @return the {@link List} with all {@link CodeType#isNested() top-level} {@link CodeType}s contained in
   *         this file. For clean Java code this should be only one single type.
   */
  List<CodeType> getTypes();

  /**
   * @return the {@link List} of {@link CodeImport}s. May be {@link List#isEmpty() empty} but is never
   *         {@code null}.
   */
  List<CodeImport> getImports();

}
