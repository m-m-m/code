/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.statement;

import io.github.mmm.code.api.expression.CodeVariable;
import io.github.mmm.code.api.statement.CodeStatement;
import io.github.mmm.code.base.item.BaseItem;

/**
 * Base implementation of {@link CodeStatement}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseStatement extends BaseItem implements CodeStatement {

  @Override
  public CodeVariable getVariable(String name) {

    return null;
  }

}
