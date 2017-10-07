/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.operator;

import net.sf.mmm.code.api.operator.CodeNAryArbitraryOperator;
import net.sf.mmm.code.api.operator.CodeNAryBooleanOperator;
import net.sf.mmm.code.api.operator.CodeNAryHybridOperator;
import net.sf.mmm.code.api.operator.CodeNAryNumericOperator;
import net.sf.mmm.code.api.operator.CodeNAryOperator;
import net.sf.mmm.util.exception.api.IllegalCaseException;

/**
 * TODO: this class ...
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
abstract class JavaNAryOperatorAggregate<T> {

  protected final CodeNAryOperator operator;

  /**
   * The constructor.
   *
   * @param operator the {@link CodeNAryOperator}.
   */
  public JavaNAryOperatorAggregate(CodeNAryOperator operator) {

    super();
    this.operator = operator;
  }

  public abstract T getValue();

  public JavaNAryOperatorAggregate<?> evaluate(Object arg) {

    String operatorName = this.operator.getName();
    if (arg == null) {
      if (CodeNAryArbitraryOperator.NAME_ADD.equals(operatorName)) {
        return add("null");
      }
      return null;
    }
    boolean numeric = this.operator.isNumeric();
    boolean bool = this.operator.isBoolean();
    if (numeric != bool) {
      if (numeric) {
        if (!(arg instanceof Number)) {
          return null;
        }
      } else {
        if (!(arg instanceof Boolean)) {
          return null;
        }
      }
    }
    switch (operatorName) {
      case CodeNAryArbitraryOperator.NAME_ADD:
        return add(arg);
      case CodeNAryBooleanOperator.NAME_AND:
        return and((Boolean) arg);
      case CodeNAryHybridOperator.NAME_BIT_AND:
        if (arg instanceof Boolean) {
          return and((Boolean) arg);
        }
        return bitAnd((Number) arg);
      case CodeNAryHybridOperator.NAME_BIT_OR:
        if (arg instanceof Boolean) {
          return or((Boolean) arg);
        }
        return bitOr((Number) arg);
      case CodeNAryNumericOperator.NAME_DIV:
        return div((Number) arg);
      case CodeNAryNumericOperator.NAME_MOD:
        return mod((Number) arg);
      case CodeNAryNumericOperator.NAME_MUL:
        return mul((Number) arg);
      case CodeNAryBooleanOperator.NAME_OR:
        return or((Boolean) arg);
      case CodeNAryNumericOperator.NAME_SHIFT_LEFT:
        return shiftLeft((Number) arg);
      case CodeNAryNumericOperator.NAME_SHIFT_RIGHT_SIGNED:
        return shiftRightSigned((Number) arg);
      case CodeNAryNumericOperator.NAME_SHIFT_RIGHT_UNSIGNED:
        return shiftRightUnsigned((Number) arg);
      case CodeNAryNumericOperator.NAME_SUB:
        return sub((Number) arg);
      case CodeNAryHybridOperator.NAME_XOR:
        if (arg instanceof Boolean) {
          return xor((Boolean) arg);
        }
        return xor((Number) arg);
      default :
        throw new IllegalCaseException(operatorName);
    }
  }

  protected JavaNAryOperatorAggregate<?> add(Object arg) {

    if (arg instanceof Number) {
      return add((Number) arg);
    } else {
      return add(arg.toString());
    }
  }

  protected JavaNAryOperatorAggregateString add(String arg) {

    return new JavaNAryOperatorAggregateString(this.operator, getValue().toString()).add(arg);
  }

  protected JavaNAryOperatorAggregate<?> add(Number arg) {

    return null;
  }

  protected JavaNAryOperatorAggregate<? extends Boolean> or(Boolean arg) {

    return null;
  }

  protected JavaNAryOperatorAggregate<? extends Boolean> and(Boolean arg) {

    return null;
  }

  protected JavaNAryOperatorAggregate<? extends Boolean> xor(Boolean arg) {

    return null;
  }

  protected JavaNAryOperatorAggregateNumeric<?> sub(Number arg) {

    return null;
  }

  protected JavaNAryOperatorAggregateNumeric<?> mul(Number arg) {

    return null;
  }

  protected JavaNAryOperatorAggregateNumeric<?> div(Number arg) {

    return null;
  }

  protected JavaNAryOperatorAggregateNumeric<?> mod(Number arg) {

    return null;
  }

  protected JavaNAryOperatorAggregateNumeric<?> shiftLeft(Number arg) {

    return null;
  }

  protected JavaNAryOperatorAggregateNumeric<?> shiftRightUnsigned(Number arg) {

    return null;
  }

  protected JavaNAryOperatorAggregateNumeric<?> shiftRightSigned(Number arg) {

    return null;
  }

  protected JavaNAryOperatorAggregateNumeric<?> xor(Number arg) {

    return null;
  }

  protected JavaNAryOperatorAggregateNumeric<?> bitOr(Number arg) {

    return null;
  }

  protected JavaNAryOperatorAggregateNumeric<?> bitAnd(Number arg) {

    return null;
  }

}
