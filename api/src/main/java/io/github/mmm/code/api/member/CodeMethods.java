/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.member;

import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.merge.CodeAdvancedMergeableItem;
import io.github.mmm.code.api.node.CodeNodeItemContainerHierarchical;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeType;

/**
 * {@link CodeMembers} as a container for the {@link CodeMethod}s.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeMethods extends CodeOperations<CodeMethod>, CodeNodeItemContainerHierarchical<CodeMethod>,
    CodeAdvancedMergeableItem<CodeMethods>, CodeNodeItemCopyable<CodeType, CodeMethods> {

  /**
   * @param name the {@link CodeOperation#getName() name} of the requested {@link CodeOperation}.
   * @param parameterTypes the {@link CodeGenericType}s of the {@link CodeOperation#getParameters() parameters}.
   * @return the requested {@link CodeOperation} or {@code null} if not found.
   */
  CodeMethod getDeclared(String name, CodeGenericType... parameterTypes);

  /**
   * @param name the {@link CodeMethod#getName() method name}.
   * @return a new {@link CodeMethod} that has been added to {@link #getDeclared()}. The {@link CodeMethod#getReturns()
   *         return type} will be initialized as {@link void}. It will not have any {@link CodeMethod#getParameters()
   *         parameters} or {@link CodeMethod#getExceptions() exceptions}. Simply add those afterwards as needed.
   * @throws io.github.mmm.base.exception.ReadOnlyException if {@link #isImmutable() immutable}.
   */
  CodeMethod add(String name);

  /**
   * @param name the {@link CodeMethod#getName() method name}.
   * @return the first {@link CodeMethod} with the given {@code name} that has been found or {@code null} if no such
   *         method exists. Please note that languages such as Java can have many methods with the same name. In such
   *         case it is unspecified which of these methods is returned. However, for convenience this method can be
   *         handy to find getters, setters, hashCode, etc.
   */
  default CodeMethod getFirst(String name) {

    for (CodeMethod method : getDeclared()) {
      if (method.getName().equals(name)) {
        return method;
      }
    }
    return null;
  }

  /**
   * @param name the {@link CodeMethod#getName() method name}.
   * @return the {@link #getFirst(String) first} {@link CodeMethod} with the given {@code name} that has been found or a
   *         {@link #add(String) newly added one} if no such method exists. Please note that languages such as Java can
   *         have many methods with the same name. In such case it is unspecified which of these methods is returned.
   */
  default CodeMethod getFirstOrCreate(String name) {

    for (CodeMethod method : getDeclared()) {
      if (method.getName().equals(name)) {
        return method;
      }
    }
    return null;
  }

  @Override
  CodeMethods copy();
}
