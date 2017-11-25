/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import net.sf.mmm.code.api.copy.CodeNodeItemCopyable;
import net.sf.mmm.code.api.merge.CodeAdvancedMergeableItem;
import net.sf.mmm.code.api.node.CodeNodeItemContainerHierarchical;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeMembers} as a container for the {@link CodeMethod}s.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <M> the type of the contained {@link CodeMethod}s.
 * @since 1.0.0
 */
public interface CodeMethods<M extends CodeMethod>
    extends CodeOperations<M>, CodeNodeItemContainerHierarchical<M>, CodeAdvancedMergeableItem<CodeMethods<?>>, CodeNodeItemCopyable<CodeType, CodeMethods<M>> {

  /**
   * @param name the {@link CodeOperation#getName() name} of the requested {@link CodeOperation}.
   * @param parameterTypes the {@link CodeGenericType}s of the {@link CodeOperation#getParameters()
   *        parameters}.
   * @return the requested {@link CodeOperation} or {@code null} if not found.
   */
  M getDeclared(String name, CodeGenericType... parameterTypes);

  /**
   * @param name the {@link CodeMethod#getName() method name}.
   * @return a new {@link CodeMethod} that has been added to {@link #getDeclared()}. The
   *         {@link CodeMethod#getReturns() return type} will be initialized as {@link void}. It will not have
   *         any {@link CodeMethod#getParameters() parameters} or {@link CodeMethod#getExceptions()
   *         exceptions}. Simply add those afterwards as needed.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  M add(String name);

  /**
   * @param name the {@link CodeMethod#getName() method name}.
   * @return the first {@link CodeMethod} with the given {@code name} that has been found or {@code null} if
   *         no such method exists. Please note that languages such as Java can have many methods with the
   *         same name. In such case it is unspecified which of these methods is returned. However, for
   *         convenience this method can be handy to find getters, setters, hashCode, etc.
   */
  default M getFirst(String name) {

    for (M method : getDeclared()) {
      if (method.getName().equals(name)) {
        return method;
      }
    }
    return null;
  }

  /**
   * @param name the {@link CodeMethod#getName() method name}.
   * @return the {@link #getFirst(String) first} {@link CodeMethod} with the given {@code name} that has been
   *         found or a {@link #add(String) newly added one} if no such method exists. Please note that
   *         languages such as Java can have many methods with the same name. In such case it is unspecified
   *         which of these methods is returned.
   */
  default M getFirstOrCreate(String name) {

    for (M method : getDeclared()) {
      if (method.getName().equals(name)) {
        return method;
      }
    }
    return null;
  }

}
