package io.github.mmm.code.base;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.github.mmm.code.api.CodeFactory;
import io.github.mmm.code.api.arg.CodeParameter;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.member.CodeFields;
import io.github.mmm.code.api.member.CodeMethods;
import io.github.mmm.code.api.modifier.CodeModifiers;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeType;
import io.github.mmm.code.base.arg.BaseReturn;
import io.github.mmm.code.base.expression.BaseExpression;
import io.github.mmm.code.base.member.BaseField;
import io.github.mmm.code.base.member.BaseFields;
import io.github.mmm.code.base.member.BaseMethod;
import io.github.mmm.code.base.member.BaseMethods;

/**
 * Base implementation of {@link CodeFactory}.
 *
 * @since 1.0.0
 */
public abstract class BaseFactory implements CodeFactory {

  @Override
  public abstract BaseExpression createExpression(Object value, boolean primitive);

  @Override
  public BaseField createField(CodeFields parent, String name, Field reflectiveObject) {

    if (reflectiveObject != null) {
      return new BaseField((BaseFields) parent, reflectiveObject);
    } else {
      return new BaseField((BaseFields) parent, name);
    }
  }

  @Override
  public BaseMethod createMethod(CodeMethods parent, String name, Method reflectiveObject) {

    if (reflectiveObject != null) {
      return new BaseMethod((BaseMethods) parent, reflectiveObject);
    } else {
      return new BaseMethod((BaseMethods) parent, name);
    }
  }

  @Override
  public BaseMethod createGetter(CodeType type, String propertyName, CodeGenericType propertyType, boolean implement, String... doc) {

    return createAccessor(type, true, propertyName, propertyType, implement, doc);
  }

  @Override
  public BaseMethod createSetter(CodeType type, String propertyName, CodeGenericType propertyType, boolean implement, String... doc) {

    return createAccessor(type, false, propertyName, propertyType, implement, doc);
  }

  private BaseMethod createAccessor(CodeType type, boolean getter, String propertyName, CodeGenericType propertyType, boolean implement,
      String... doc) {

    String prefix;
    if (getter) {
      CodeType propertySimpleType = propertyType.asType();
      if (propertySimpleType.isBoolean() && propertySimpleType.isPrimitive()) {
        prefix = "is";
      } else {
        prefix = "get";
      }
    } else {
      prefix = "set";
    }
    String name = prefix + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
    BaseMethod method = createMethod(type.getMethods(), name, null);
    if (implement) {
      method.setModifiers(CodeModifiers.MODIFIERS_PUBLIC);
    } else {
      method.setModifiers(CodeModifiers.MODIFIERS_PUBLIC_ABSTRACT);
    }
    CodeLanguage language = type.getLanguage();
    if (getter) {
      BaseReturn returns = method.getReturns();
      returns.setType(propertyType);
      returns.getDoc().add(doc);
      if (implement) {
        method.getBody().addText("return " + language.getVariableNameThis() + "." + propertyName + language.getStatementTerminator());
      }
    } else {
      CodeParameter parameter = method.getParameters().add(propertyName);
      parameter.setType(propertyType);
      parameter.getDoc().add(doc);
      if (implement) {
        method.getBody()
            .addText(language.getVariableNameThis() + "." + propertyName + " = " + propertyName + language.getStatementTerminator());
      }
    }
    return method;
  }

}
