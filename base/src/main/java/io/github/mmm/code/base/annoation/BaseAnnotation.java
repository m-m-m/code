/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.annoation;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.code.api.annotation.CodeAnnotation;
import io.github.mmm.code.api.annotation.CodeAnnotations;
import io.github.mmm.code.api.comment.CodeComment;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.copy.CodeCopyType;
import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeType;
import io.github.mmm.code.base.node.BaseNodeItem;

/**
 * Base implementation of {@link CodeAnnotation}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseAnnotation extends BaseNodeItem implements CodeAnnotation {

  private static final Logger LOG = LoggerFactory.getLogger(BaseAnnotation.class);

  private final Annotation reflectiveObject;

  private CodeAnnotations parent;

  private CodeComment comment;

  private CodeGenericType type;

  private String typeName;

  private String qualifiedTypeName;

  private Map<String, CodeExpression> parameters;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}.
   */
  public BaseAnnotation(CodeAnnotations parent, Annotation reflectiveObject) {

    super();
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param type the {@link #getType() type}.
   */
  public BaseAnnotation(CodeAnnotations parent, CodeType type) {

    super();
    this.parent = parent;
    this.type = type;
    this.reflectiveObject = null;
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param typeName the name of the {@link #getType() type} from the source-code.
   * @param qualifiedTypeName the qualified {@link #getType() type}.
   */
  public BaseAnnotation(CodeAnnotations parent, String typeName, String qualifiedTypeName) {

    super();
    this.parent = parent;
    if ((typeName != qualifiedTypeName) && !qualifiedTypeName.endsWith("." + typeName)) {
      throw new IllegalArgumentException(typeName + "::" + qualifiedTypeName);
    }
    this.reflectiveObject = null;
    this.typeName = typeName;
    this.qualifiedTypeName = qualifiedTypeName;
    this.parameters = new HashMap<>();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseAnnotation} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseAnnotation(BaseAnnotation template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
    this.reflectiveObject = null;
    this.type = mapper.map(template.type, CodeCopyType.REFERENCE);
    this.parameters = new HashMap<>(template.parameters);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    getType();
    getParameters();
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.parameters = Collections.unmodifiableMap(this.parameters);
  }

  @Override
  public CodeAnnotations getParent() {

    return this.parent;
  }

  void setParent(CodeAnnotations parent) {

    if (this.parent != null) {
      verifyMutalbe();
    }
    this.parent = parent;
  }

  @Override
  public CodeGenericType getType() {

    if (this.type == null) {
      if (this.qualifiedTypeName != null) {
        CodeGenericType rawType = getContext().getRequiredType(this.qualifiedTypeName);
        if ((this.typeName == this.qualifiedTypeName) && (this.typeName.indexOf('.') > 0)) {
          this.type = ((CodeType) rawType).getQualifiedType();
        }
        this.type = rawType;
        this.qualifiedTypeName = null;
        this.typeName = null;
      } else if (this.reflectiveObject != null) {
        this.type = getContext().getType(this.reflectiveObject.annotationType());
      } else {
        throw new IllegalStateException();
      }
    }
    return this.type;
  }

  @Override
  public void setType(CodeGenericType type) {

    if (!type.isAnnotation()) {
      throw new IllegalArgumentException("Invalid type for annotation: " + type);
    }
    verifyMutalbe();
    this.type = type;
    this.typeName = null;
    this.qualifiedTypeName = null;
  }

  @Override
  public Map<String, CodeExpression> getParameters() {

    if (this.parameters == null) {
      Map<String, CodeExpression> map = new HashMap<>();
      if (this.reflectiveObject != null) {
        Class<? extends Annotation> annotationType = this.reflectiveObject.annotationType();
        for (Method method : annotationType.getDeclaredMethods()) {
          String key = method.getName();
          try {
            Object value = method.invoke(this.reflectiveObject, (Object[]) null);
            Class<?> returnType = method.getReturnType();
            this.parameters.put(key, getContext().getFactory().createExpression(value, returnType.isPrimitive()));
          } catch (Exception e) {
            LOG.warn("Failed to read attribute {} of annotation {}.", key, this.reflectiveObject, e);
          }
        }
      }
      this.parameters = map;
    }
    return this.parameters;
  }

  @Override
  public CodeComment getComment() {

    return this.comment;
  }

  @Override
  public void setComment(CodeComment comment) {

    verifyMutalbe();
    this.comment = comment;
  }

  @Override
  public Annotation getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent,
      CodeLanguage language) throws IOException {

    sink.append(currentIndent);
    if (this.comment != null) {
      this.comment.write(sink, newline, defaultIndent, currentIndent);
    }
    sink.append(language.getAnnotationStart());
    if (this.typeName != null) {
      sink.append(this.typeName);
    } else if (getType() == null) {
      LOG.warn("Annotation without type.");
      sink.append("Undefined");
    } else {
      this.type.writeReference(sink, false);
    }
    if (getParameters().isEmpty()) {
      sink.append(language.getAnnotationEndIfEmpty());
    } else {
      String prefix = "(";
      for (Entry<String, CodeExpression> entry : this.parameters.entrySet()) {
        sink.append(prefix);
        sink.append(entry.getKey());
        sink.append(" = ");
        sink.append(formatValue(entry.getValue()));
        prefix = ", ";
      }
      sink.append(')');
    }
    sink.append(newline);
  }

  private String formatValue(CodeExpression value) {

    return value.toString();
  }

  @Override
  public BaseAnnotation copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseAnnotation copy(CodeCopyMapper mapper) {

    return new BaseAnnotation(this, mapper);
  }

}
