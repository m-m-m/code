/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.item.CodeMutableItemWithQualifiedName;
import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.util.exception.api.ObjectNotFoundException;

/**
 * The top-level context used to retrieve existing {@link CodeElement}s or create new ones.
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeProvider extends CodeNode, CodeLoader {

  /**
   * @return the root {@link CodePackage package} of this source. Will typically be lazy initialized and
   *         {@link CodePackage#getChildren() traversal} (especially {@link CodePathElements#getAll()}) can be
   *         expensive. Results will however be cached to speed up subsequent calls.
   */
  CodePackage getRootPackage();

  /**
   * @param hierarchicalName the hierarchical name as plain {@link String} to parse. E.g. a
   *        {@link CodeMutableItemWithQualifiedName#getQualifiedName() qualified name} or a part of it.
   * @return the parsed {@link CodeName}.
   */
  default CodeName parseName(String hierarchicalName) {

    return new CodeName(hierarchicalName, getContext().getPackageSeparator());
  }

  /**
   * @param qualifiedName the {@link CodeName} of the requested {@link CodePackage}.
   * @return the requested {@link CodePackage} or {@code null} if not found.
   */
  CodePackage getPackage(CodeName qualifiedName);

  /**
   * @param qualifiedName the {@link CodeName} of the requested {@link CodeType}.
   * @return the requested {@link CodeType} or {@code null} if not found.
   */
  CodeType getType(CodeName qualifiedName);

  /**
   * @param qualifiedName the {@link CodeType#getQualifiedName() qualified name} of the requested
   *        {@link CodeType}.
   * @return the requested {@link CodeType}.
   * @throws ObjectNotFoundException if the type was not found.
   */
  default CodeType getRequiredType(String qualifiedName) {

    CodeType type = getType(qualifiedName);
    if (type == null) {
      throw new ObjectNotFoundException(CodeType.class, qualifiedName);
    }
    return type;
  }

}
