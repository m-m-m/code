/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.expression;

import net.sf.mmm.code.api.CodeItem;
import net.sf.mmm.code.api.statement.CodeStatement;

/**
 * {@link CodeItem} for an expression. An expression is a sub-item of a {@link CodeStatement} that is
 * typically not a {@link CodeStatement} for itself.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract interface CodeExpression extends CodeItem {

}
