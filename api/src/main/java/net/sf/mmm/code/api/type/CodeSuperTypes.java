/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import java.util.List;
import java.util.stream.Collectors;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.node.CodeNodeItemContainerHierarchical;

/**
 * {@link CodeItem} containing the super types of a {@link CodeType}.
 *
 * @see CodeType#getSuperTypes()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeSuperTypes extends CodeNodeItemContainerHierarchical<CodeGenericType> {

  @Override
  CodeType getParent();

  /**
   * @return the (first) {@link CodeGenericType#isClass() class} of the {@link CodeType#getSuperTypes() super
   *         types} or {@code null} if none exists. In case the {@link #getParent() type} is a
   *         {@link CodeGenericType#isClass() class} or {@link CodeGenericType#isEnumeration() enumeration}
   *         the {@link net.sf.mmm.code.api.CodeContext#getRootType() root type} is considered as super class
   *         even if is not explicitly in the {@link #getDeclared() list of extended types}.
   */
  default CodeGenericType getSuperClass() {

    for (CodeGenericType type : getDeclared()) {
      if (type.isClass()) {
        return type;
      }
    }
    return null;
  }

  /**
   * @return the {@link List} of {@link CodeGenericType#isInterface() interfaces} of the {@link #getAll()
   *         super types}. May be {@link List#isEmpty() empty} but is never {@code null}.
   */
  default List<? extends CodeGenericType> getSuperInterfaces() {

    return getDeclared().stream().filter(x -> x.isInterface()).collect(Collectors.toList());
  }

  /**
   * @param superType the {@link CodeGenericType} to add.
   */
  void add(CodeGenericType superType);

  @Override
  CodeSuperTypes copy();

}
