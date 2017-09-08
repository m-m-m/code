/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.expression;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.statement.CodeStatement;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeItem} for an expression. An expression is a sub-item of a {@link CodeStatement} that is
 * typically not a {@link CodeStatement} for itself.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeExpression extends CodeItem {

  /**
   * @param type the optional {@link CodeType} as context for evaluation. May be {@code null}.
   * @return the evaluated value. May be {@code null}. Will also be {@code null} if the expression can not be
   *         evaluated.
   */
  Object evaluate(CodeType type);

}
