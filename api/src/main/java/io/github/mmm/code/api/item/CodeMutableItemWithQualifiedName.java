/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.item;

import io.github.mmm.code.api.CodePackage;

/**
 * {@link CodeItem} that has a {@link #getSimpleName() simple name} and a {@link #getQualifiedName() qualified name},
 * that is qualified via the {@link #getParentPackage() parent package}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeMutableItemWithQualifiedName
    extends CodeMutableItem, CodeItemWithQualifiedNameAndParentPackage {

  /**
   * @param simpleName the new {@link #getSimpleName() simple name}.
   * @throws io.github.mmm.base.exception.ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setSimpleName(String simpleName);

  /**
   * @param parentPackage the new {@link #getParentPackage() parent package}.
   * @throws io.github.mmm.base.exception.ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setParentPackage(CodePackage parentPackage);

}
