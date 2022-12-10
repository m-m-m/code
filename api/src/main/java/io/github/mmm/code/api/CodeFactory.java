package io.github.mmm.code.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.api.member.CodeField;
import io.github.mmm.code.api.member.CodeFields;
import io.github.mmm.code.api.member.CodeMethod;
import io.github.mmm.code.api.member.CodeMethods;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeType;

/**
 * This is a low-level factory for code objects. It is not designed for end-users of this API but rather for internal
 * and SPI usage.
 *
 * @since 1.0.0
 */
public interface CodeFactory {

  /**
   * @param value the Java value to wrap as constant/literal {@link CodeExpression}.
   * @param primitive - {@code true} for a primitive type literal, {@code false} otherwise.
   * @return the according {@link CodeExpression}.
   */
  CodeExpression createExpression(Object value, boolean primitive);

  /**
   * @param parent the {@link CodeField#getParent() parent}.
   * @param name the {@link CodeField#getName() name}. May be {@code null}.
   * @param reflectiveObject the {@link CodeField#getReflectiveObject() reflective object}. May be {@code null}.
   * @return a new {@link CodeField}.
   */
  CodeField createField(CodeFields parent, String name, Field reflectiveObject);

  /**
   * @param parent the {@link CodeMethod#getParent() parent}.
   * @param name the {@link CodeMethod#getName() name}. May be {@code null}.
   * @param reflectiveObject the {@link CodeMethod#getReflectiveObject() reflective object}. May be {@code null}.
   * @return the new {@link CodeMethod}
   */
  CodeMethod createMethod(CodeMethods parent, String name, Method reflectiveObject);

  /**
   * @param type the {@link CodeType} in which the getter shall be created.
   * @param propertyName the {@link io.github.mmm.code.api.member.CodeProperty#getName() property name}.
   * @param propertyType the {@link io.github.mmm.code.api.member.CodeProperty#getType() property type}.
   * @param implement - {@code true} to also add a method body with the implementation, {@code false} otherwise
   *        (abstract method signature).
   * @param doc the optional documentation of the property.
   * @return the new getter {@link CodeMethod method}.
   */
  CodeMethod createGetter(CodeType type, String propertyName, CodeGenericType propertyType, boolean implement,
      String... doc);

  /**
   * @param type the {@link CodeType} in which the setter shall be created.
   * @param propertyName the {@link io.github.mmm.code.api.member.CodeProperty#getName() property name}.
   * @param propertyType the {@link io.github.mmm.code.api.member.CodeProperty#getType() property type}.
   * @param implement - {@code true} to also add a method body with the implementation, {@code false} otherwise
   *        (abstract method signature).
   * @param doc the optional documentation of the property.
   * @return the new setter {@link CodeMethod method}.
   */
  CodeMethod createSetter(CodeType type, String propertyName, CodeGenericType propertyType, boolean implement,
      String... doc);

}
