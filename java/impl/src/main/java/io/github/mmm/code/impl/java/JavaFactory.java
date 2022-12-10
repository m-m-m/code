package io.github.mmm.code.impl.java;

import io.github.mmm.code.base.BaseFactory;
import io.github.mmm.code.base.expression.BaseExpression;
import io.github.mmm.code.impl.java.expression.constant.JavaConstant;

/**
 * Implementation of {@link BaseFactory} for Java.
 *
 * @since 1.0.0
 */
public class JavaFactory extends BaseFactory {

  @Override
  public BaseExpression createExpression(Object value, boolean primitive) {

    return JavaConstant.of(value, primitive);
  }

}
