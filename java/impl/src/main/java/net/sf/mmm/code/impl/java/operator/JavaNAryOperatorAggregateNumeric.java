/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.operator;

import net.sf.mmm.code.api.operator.CodeNAryOperator;

abstract class JavaNAryOperatorAggregateNumeric<T extends Number> extends JavaNAryOperatorAggregate<T> {

  JavaNAryOperatorAggregateNumeric(CodeNAryOperator operator) {

    super(operator);
  }
}