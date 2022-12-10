/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.operator;

import io.github.mmm.code.api.operator.CodeNAryOperator;

abstract class JavaNAryOperatorAggregateNumeric<T extends Number> extends JavaNAryOperatorAggregate<T> {

  JavaNAryOperatorAggregateNumeric(CodeNAryOperator operator) {

    super(operator);
  }
}