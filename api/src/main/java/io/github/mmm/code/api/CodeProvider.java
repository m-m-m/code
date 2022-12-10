/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api;

import io.github.mmm.code.api.item.CodeMutableItemWithQualifiedName;
import io.github.mmm.code.api.node.CodeNode;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeType;

/**
 * Abstract interface used to retrieve {@link CodePackage}s and {@link CodeType}s. Common parent of
 * {@link io.github.mmm.code.api.source.CodeSource} and {@link CodeContext}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract interface CodeProvider extends CodeNode, CodeLoader, AutoCloseable {

  /**
   * @param hierarchicalName the hierarchical name as plain {@link String} to parse. E.g. a
   *        {@link CodeMutableItemWithQualifiedName#getQualifiedName() qualified name} or a part of it.
   * @return the parsed {@link CodeName}.
   */
  default CodeName parseName(String hierarchicalName) {

    return new CodeName(hierarchicalName, getContext().getLanguage().getPackageSeparator());
  }

  /**
   * @param qualifiedName the {@link CodeType#getQualifiedName() qualified name} of the requested {@link CodeType}.
   * @return the requested {@link CodeGenericType}. Typically {@link CodeType}.
   * @throws io.github.mmm.base.exception.ObjectNotFoundException if the type was not found.
   */
  CodeType getRequiredType(String qualifiedName);

  @Override
  void close();

}
