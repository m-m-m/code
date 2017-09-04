/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.annotation;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.annotation.CodeAnnotation;
import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.item.JavaItem;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.util.nls.api.NlsBundleOptions;

/**
 * Implementation of {@link CodeAnnotation} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
@NlsBundleOptions(productive = true)
public class JavaAnnotation extends JavaItem implements CodeAnnotation {

  private static final Logger LOG = LoggerFactory.getLogger(JavaAnnotation.class);

  private final JavaElement declaringElement;

  private JavaType type;

  private Map<String, Object> parameters;

  /**
   * The constructor.
   *
   * @param declaringElement the {@link #getDeclaringElement() declaring element}.
   */
  public JavaAnnotation(JavaElement declaringElement) {

    super(declaringElement.getContext());
    this.declaringElement = declaringElement;
    this.parameters = new HashMap<>();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaAnnotation} to copy.
   * @param declaringElement the {@link #getDeclaringElement() declaring element}.
   */
  public JavaAnnotation(JavaAnnotation template, JavaElement declaringElement) {

    super(template);
    if (declaringElement == null) {
      this.declaringElement = template.declaringElement;
    } else {
      this.declaringElement = declaringElement;
    }
    this.type = template.type;
    this.parameters = new HashMap<>(template.parameters);
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.parameters = Collections.unmodifiableMap(this.parameters);
  }

  @Override
  public JavaElement getDeclaringElement() {

    return this.declaringElement;
  }

  @Override
  public JavaType getDeclaringType() {

    if (this.declaringElement instanceof JavaType) {
      return (JavaType) this.declaringElement;
    }
    return this.declaringElement.getDeclaringType();
  }

  @Override
  public JavaType getType() {

    return this.type;
  }

  @Override
  public void setType(CodeGenericType type) {

    if (!type.isAnnotation()) {
      throw new IllegalArgumentException("Invalid type for annotation: " + type);
    }
    verifyMutalbe();
    this.type = (JavaType) type;
  }

  @Override
  public Map<String, Object> getParameters() {

    return this.parameters;
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    sink.append('@');
    if (this.type == null) {
      LOG.warn("Annotation without type in {}.", getDeclaringType().getSimpleName());
      sink.append("Undefined");
    } else {
      this.type.writeReference(sink, false);
    }
    if (!this.parameters.isEmpty()) {
      String prefix = "(";
      for (Entry<String, Object> entry : this.parameters.entrySet()) {
        sink.append(prefix);
        sink.append(entry.getKey());
        sink.append(" = ");
        sink.append(formatValue(entry.getValue()));
        prefix = ", ";
      }
      sink.append(')');
    }
  }

  private String formatValue(Object value) {

    return value.toString();
  }

  @Override
  public JavaAnnotation copy(CodeElement newDeclaringElement) {

    return new JavaAnnotation(this, (JavaElement) newDeclaringElement);
  }

  @Override
  public JavaAnnotation copy(CodeType newDeclaringType) {

    return copy((CodeElement) newDeclaringType);
  }

}
