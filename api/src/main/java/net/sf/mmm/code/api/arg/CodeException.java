/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.arg;

import net.sf.mmm.code.api.member.CodeOperation;

/**
 * {@link CodeOperationArg} for an {@link Throwable exception} in a throws declaration of a
 * {@link CodeOperation}.
 *
 * @see CodeOperation#getExceptions()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeException extends CodeOperationArg {

  @Override
  CodeExceptions getParent();

  @Override
  CodeException copy();

}
