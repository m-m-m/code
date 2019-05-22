package net.sf.mmm.code.impl.java;

import net.sf.mmm.code.base.BaseFactory;
import net.sf.mmm.code.base.expression.BaseExpression;
import net.sf.mmm.code.impl.java.expression.constant.JavaConstant;

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
