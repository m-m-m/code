package io.github.mmm.code.base;

import io.github.mmm.code.base.expression.BaseConstant;
import io.github.mmm.code.base.expression.BaseExpression;

/**
 * Dummy implementation of {@link BaseFactory}.
 */
public class TestFactory extends BaseFactory {

  @Override
  public BaseExpression createExpression(Object value, boolean primitive) {

    return new BaseConstant(value.toString());
  }

}
