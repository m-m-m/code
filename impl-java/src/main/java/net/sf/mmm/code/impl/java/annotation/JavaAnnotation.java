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
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.node.JavaNodeItem;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.util.nls.api.NlsBundleOptions;

/**
 * Implementation of {@link CodeAnnotation} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
@NlsBundleOptions(productive = true)
public class JavaAnnotation extends JavaNodeItem implements CodeAnnotation, CodeNodeItemWithGenericParent<JavaAnnotations, JavaAnnotation> {

  private static final Logger LOG = LoggerFactory.getLogger(JavaAnnotation.class);

  private JavaAnnotations parent;

  private JavaType type;

  private Map<String, CodeExpression> parameters;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaAnnotation(JavaAnnotations parent) {

    super();
    this.parent = parent;
    this.parameters = new HashMap<>();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaAnnotation} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaAnnotation(JavaAnnotation template, JavaAnnotations parent) {

    super(template);
    this.parent = parent;
    this.type = template.type;
    this.parameters = new HashMap<>(template.parameters);
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.parameters = Collections.unmodifiableMap(this.parameters);
  }

  @Override
  public JavaAnnotations getParent() {

    return this.parent;
  }

  @Override
  public JavaElement getDeclaringElement() {

    return getParent().getParent();
  }

  @Override
  public JavaType getDeclaringType() {

    return getParent().getDeclaringType();
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
  public Map<String, CodeExpression> getParameters() {

    return this.parameters;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    sink.append('@');
    if (this.type == null) {
      LOG.warn("Annotation without type in {}.", getDeclaringType().getSimpleName());
      sink.append("Undefined");
    } else {
      this.type.writeReference(sink, false);
    }
    if (!this.parameters.isEmpty()) {
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
  }

  private String formatValue(CodeExpression value) {

    return value.toString();
  }

  @Override
  public JavaAnnotation copy() {

    return copy(this.parent);
  }

  @Override
  public JavaAnnotation copy(JavaAnnotations newParent) {

    return new JavaAnnotation(this, newParent);
  }

}
