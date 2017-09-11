/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import net.sf.mmm.code.api.node.CodeNodeItemContainerFlat;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeMembers} as a container for the {@link CodeConstructor}s.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeConstructors extends CodeOperations<CodeConstructor>, CodeNodeItemContainerFlat<CodeConstructor> {

  /**
   * @param name the {@link CodeOperation#getName() name} of the requested {@link CodeOperation}.
   * @param parameterTypes the {@link net.sf.mmm.code.api.type.CodeGenericType#asType() raw} {@link CodeType}s
   *        of the {@link CodeOperation#getParameters() parameters}.
   * @return the requested {@link CodeOperation} or {@code null} if not found.
   */
  CodeConstructor get(CodeType... parameterTypes);

  /**
   * @return a new {@link CodeConstructor} that has been added to {@link #getAll()}. It will not have any
   *         {@link CodeMethod#getParameters() parameters} or {@link CodeMethod#getExceptions() exceptions}.
   *         Simply add those afterwards as needed.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  CodeConstructor add();

  @Override
  CodeConstructors copy();

}