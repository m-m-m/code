/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeMembers} as a container for the {@link CodeConstructor}s.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeConstructors extends CodeOperations<CodeConstructor> {

  /**
   * @deprecated constructors can not be inherited. Use {@link #getDeclared()} instead.
   */
  @Deprecated
  @Override
  default Iterable<? extends CodeConstructor> getAll() {

    return getDeclared();
  }

  /**
   * @deprecated constructors can not be inherited. Use {@link #getDeclared()} instead.
   */
  @Deprecated
  @Override
  default Iterable<? extends CodeConstructor> getInherited() {

    return getDeclared();
  }

  /**
   * @param name the {@link CodeOperation#getName() name} of the requested {@link CodeOperation}.
   * @param parameterTypes the {@link net.sf.mmm.code.api.CodeGenericType#asType() raw} {@link CodeType}s
   *        of the {@link CodeOperation#getParameters() parameters}.
   * @return the requested {@link CodeOperation} or {@code null} if not found.
   */
  CodeConstructor get(CodeType... parameterTypes);

  /**
   * @return a new {@link CodeConstructor} that has been added to {@link #getDeclared()}. The
   *         {@link CodeMethod#getReturns() return type} will be initialized as {@link void}. It will not have
   *         any {@link CodeMethod#getParameters() parameters} or {@link CodeMethod#getExceptions()
   *         exceptions}. Simply add those afterwards as needed.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  CodeConstructor add();
}
