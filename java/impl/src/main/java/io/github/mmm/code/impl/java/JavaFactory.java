package io.github.mmm.code.impl.java;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.base.BaseFactory;
import io.github.mmm.code.base.expression.BaseArrayInstatiation;
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

    if (value != null) {
      Class<? extends Object> valueClass = value.getClass();
      if (valueClass.isArray()) {
        Class<?> componentType = valueClass.getComponentType();
        primitive = componentType.isPrimitive();
        List<CodeExpression> list;
        if (primitive) {
          int length = Array.getLength(value);
          list = new ArrayList<>(length);
          for (int i = 0; i < length; i++) {
            Object item = Array.get(value, i);
            list.add(createExpression(item, primitive));
          }
        } else {
          Object[] array = (Object[]) value;
          int length = array.length;
          list = new ArrayList<>(length);
          for (int i = 0; i < length; i++) {
            list.add(createExpression(array[i], primitive));
          }
        }
        return new BaseArrayInstatiation(list);
      }
    }
    return JavaConstant.of(value, primitive);
  }

}
