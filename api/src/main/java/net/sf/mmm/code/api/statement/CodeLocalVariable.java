/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.statement;

import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.expression.CodeVariable;
import net.sf.mmm.code.api.item.CodeItemWithModifiers;
import net.sf.mmm.code.api.modifier.CodeModifiers;

/**
 * {@link CodeVariable} that is locally defined in a {@link net.sf.mmm.code.api.block.CodeBlock}.<br>
 * Syntax: <pre>
 * [{@link #isFinal() final}] «{@link #getType() type}» «{@link #getName() name}» [ = «{@link #getExpression() expression}»]
 * </pre>
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeLocalVariable extends CodeVariable, CodeItemWithModifiers, CodeAssignment {

  /**
   * @return true if {@link CodeModifiers#isFinal() final}.
   */
  boolean isFinal();

  @Override
  default CodeLocalVariable getVariable() {

    return this;
  }

  /**
   * @return the optional {@link CodeExpression} used to initialize this variable. May be {@code null} for no
   *         initialization.
   */
  @Override
  CodeExpression getExpression();

  @Override
  default CodeModifiers getModifiers() {

    if (isFinal()) {
      return CodeModifiers.MODIFIERS_FINAL;
    } else {
      return CodeModifiers.MODIFIERS;
    }
  }

}
