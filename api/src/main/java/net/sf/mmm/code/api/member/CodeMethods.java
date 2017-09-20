/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import net.sf.mmm.code.api.node.CodeNodeItemContainerHierarchical;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeMembers} as a container for the {@link CodeMethod}s.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <M> the type of the contained {@link CodeMethod}s.
 * @since 1.0.0
 */
public interface CodeMethods<M extends CodeMethod> extends CodeOperations<M>, CodeNodeItemContainerHierarchical<M> {

  /**
   * @param name the {@link CodeOperation#getName() name} of the requested {@link CodeOperation}.
   * @param parameterTypes the {@link net.sf.mmm.code.api.type.CodeGenericType#asType() raw} {@link CodeType}s
   *        of the {@link CodeOperation#getParameters() parameters}.
   * @return the requested {@link CodeOperation} or {@code null} if not found.
   */
  M getDeclared(String name, CodeType... parameterTypes);

  /**
   * @param name the {@link CodeMethod#getName() method name}.
   * @return a new {@link CodeMethod} that has been added to {@link #getDeclared()}. The
   *         {@link CodeMethod#getReturns() return type} will be initialized as {@link void}. It will not have
   *         any {@link CodeMethod#getParameters() parameters} or {@link CodeMethod#getExceptions()
   *         exceptions}. Simply add those afterwards as needed.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  M add(String name);

  @Override
  CodeMethods<M> copy();

}
