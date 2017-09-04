/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.annotation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.annotation.CodeAnnotation;
import net.sf.mmm.code.api.annotation.CodeAnnotations;
import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.item.JavaItemContainerWithInheritance;
import net.sf.mmm.code.impl.java.member.JavaMethod;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeAnnotations} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaAnnotations extends JavaItemContainerWithInheritance<CodeAnnotation> implements CodeAnnotations {

  private final JavaElement declaringElement;

  private List<JavaAnnotation> annotations;

  /**
   * The constructor.
   *
   * @param declaringElement the {@link #getDeclaringElement() declaring element}.
   */
  public JavaAnnotations(JavaElement declaringElement) {

    super(declaringElement.getContext());
    this.declaringElement = declaringElement;
    this.annotations = new ArrayList<>();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaAnnotations} to copy.
   * @param declaringElement the {@link #getDeclaringElement() declaring element}.
   */
  public JavaAnnotations(JavaAnnotations template, JavaElement declaringElement) {

    super(template, null);
    if (declaringElement == null) {
      this.declaringElement = template.declaringElement;
    } else {
      this.declaringElement = declaringElement;
    }
    this.annotations = doCopy(template.annotations, declaringElement);
  }

  @Override
  public List<? extends JavaAnnotation> getDeclared() {

    return this.annotations;
  }

  @Override
  public Iterable<? extends JavaAnnotation> getAll() {

    if (this.declaringElement instanceof JavaType) {

    } else if (this.declaringElement instanceof JavaMethod) {

    } else {
      return this.annotations;
    }
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JavaType getDeclaringType() {

    if (this.declaringElement instanceof JavaType) {
      return (JavaType) this.declaringElement;
    }
    return this.declaringElement.getDeclaringType();
  }

  @Override
  public CodeElement getDeclaringElement() {

    return this.declaringElement;
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    String prefix = "";
    for (JavaAnnotation annotation : this.annotations) {
      if (currentIndent == null) {
        sink.append(prefix);
        prefix = " ";
      } else {
        writeNewline(sink);
        sink.append(currentIndent);
      }
      annotation.write(sink, defaultIndent, currentIndent);
    }
    sink.append(prefix);
  }

  @Override
  public JavaAnnotations copy(CodeElement newDeclaringElement) {

    return new JavaAnnotations(this, (JavaElement) newDeclaringElement);
  }

  @Override
  public CodeAnnotations copy(CodeType newDeclaringType) {

    return copy((CodeElement) newDeclaringType);
  }

}
