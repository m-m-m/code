/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import net.sf.mmm.code.api.item.CodeMutableItemWithQualifiedName;
import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.util.exception.api.ObjectNotFoundException;

/**
 * Abstract interface used to retrieve {@link CodePackage}s and {@link CodeType}s. Common parent of
 * {@link net.sf.mmm.code.api.source.CodeSource} and {@link CodeContext}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract interface CodeProvider extends CodeNode, CodeLoader {

  /**
   * @param hierarchicalName the hierarchical name as plain {@link String} to parse. E.g. a
   *        {@link CodeMutableItemWithQualifiedName#getQualifiedName() qualified name} or a part of it.
   * @return the parsed {@link CodeName}.
   */
  default CodeName parseName(String hierarchicalName) {

    return new CodeName(hierarchicalName, getContext().getLanguage().getPackageSeparator());
  }

  /**
   * @param qualifiedName the {@link CodeType#getQualifiedName() qualified name} of the requested
   *        {@link CodeType}.
   * @return the requested {@link CodeGenericType}. Typically {@link CodeType}.
   * @throws ObjectNotFoundException if the type was not found.
   */
  CodeType getRequiredType(String qualifiedName);

}
