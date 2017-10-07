/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.block;

import net.sf.mmm.code.api.statement.CodeStatement;

/**
 * {@link CodeBlock} that is a regular {@link CodeStatement}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeBlockStatement extends CodeBlock, CodeStatement {

  @Override
  CodeBlock getParent();

  @Override
  CodeBlockStatement copy();

}
