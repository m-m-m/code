/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.operator;

import net.sf.mmm.code.api.operator.CodeNAryOperator;

/**
 * Implementation of {@link JavaNAryOperatorAggregate} for {@link String}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
@SuppressWarnings("javadoc")
class JavaNAryOperatorAggregateString extends JavaNAryOperatorAggregate<String> {

  private String value;

  private StringBuilder builder;

  JavaNAryOperatorAggregateString(CodeNAryOperator operator, String value) {

    super(operator);
    this.value = value;
  }

  @Override
  public String getValue() {

    if (this.builder != null) {
      return this.builder.toString();
    }
    return this.value;
  }

  @Override
  protected JavaNAryOperatorAggregate<?> add(Object arg) {

    return add(arg.toString());
  }

  @Override
  protected JavaNAryOperatorAggregate<?> add(Number arg) {

    return add(arg.toString());
  }

  @Override
  protected JavaNAryOperatorAggregateString add(String arg) {

    if (this.builder == null) {
      this.builder = new StringBuilder(this.value);
    }
    this.builder.append(arg);
    return this;
  }

}
