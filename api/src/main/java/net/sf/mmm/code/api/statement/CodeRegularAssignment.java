/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.statement;

import net.sf.mmm.code.api.operator.CodeNAryNumericOperator;

/**
 * {@link CodeAssignment} that is a regular assignment statement (unlike a
 * {@link net.sf.mmm.code.api.statement.CodeLocalVariable}).<br>
 * Syntax: <pre>
 * «{@link #getVariable() variable}» [«{@link #getOperator()}operator»]= «{@link #getExpression() expression}»
 * </pre> Example: <pre>
 * i += 5 + (y - 1)
 * </pre>
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeRegularAssignment extends CodeAssignment {

  /**
   * @return the optional {@link CodeNAryNumericOperator} to use with the assignment.
   */
  CodeNAryNumericOperator getOperator();

}
