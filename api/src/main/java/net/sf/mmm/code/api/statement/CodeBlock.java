/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.statement;

import java.util.List;

/**
 * {@link CodeStatement} for a block that groups multiple {@link #getStatements() statements}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeBlock extends CodeStatement {

  /**
   * @return the {@link List} of {@link CodeStatement} contained inside the block (typically within curly
   *         braces). May be {@link List#isEmpty() empty} but never {@code null}.
   */
  List<CodeStatement> getStatements();

}
