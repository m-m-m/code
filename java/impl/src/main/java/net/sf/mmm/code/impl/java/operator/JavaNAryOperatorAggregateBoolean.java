/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.operator;

import net.sf.mmm.code.api.operator.CodeNAryOperator;

/**
 * Implementation of {@link JavaNAryOperatorAggregate} for {@code boolean}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
@SuppressWarnings("javadoc")
class JavaNAryOperatorAggregateBoolean extends JavaNAryOperatorAggregate<Boolean> {

  private boolean value;

  JavaNAryOperatorAggregateBoolean(CodeNAryOperator operator, boolean value) {

    super(operator);
    this.value = value;
  }

  @Override
  public Boolean getValue() {

    return Boolean.valueOf(this.value);
  }

}
