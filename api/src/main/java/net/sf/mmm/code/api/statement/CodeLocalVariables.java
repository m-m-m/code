/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.statement;

import java.util.List;

/**
 * {@link CodeAtomicStatement} to declare multiple {@link CodeLocalVariable}s in a single statement. <br>
 * Syntax: <pre>
 * «{@link CodeLocalVariable#getType() type}» «{@link #getVariables() variable-1}», ..., «{@link #getVariables() variable-N}»
 * </pre>
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeLocalVariables extends CodeAtomicStatement {

  /**
   * @return the {@link List} of {@link CodeLocalVariable}s to declare. {@link List#size() Size} should be at
   *         least two in order to make sense. All {@link CodeLocalVariable}s need to have the same
   *         {@link CodeLocalVariable#getType() typ}.
   */
  List<? extends CodeLocalVariable> getVariables();

}
