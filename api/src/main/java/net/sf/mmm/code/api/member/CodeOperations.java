/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeMembers} as a container for the {@link CodeOperation}s.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <O> type of the contained {@link CodeOperation}s.
 * @since 1.0.0
 */
public abstract interface CodeOperations<O extends CodeOperation> extends CodeMembers<O> {

  @Override
  CodeOperations<O> copy(CodeType newDeclaringType);

}
