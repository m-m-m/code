/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.annotation;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.mmm.code.api.annotation.CodeAnnotations;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.impl.java.element.JavaElementImpl;
import net.sf.mmm.code.impl.java.member.JavaMethod;
import net.sf.mmm.code.impl.java.node.JavaNodeItemContainerHierarchical;
import net.sf.mmm.code.impl.java.type.InternalSuperTypeIterator;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.util.collection.base.AbstractIterator;

/**
 * Implementation of {@link CodeAnnotations} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaAnnotations extends JavaNodeItemContainerHierarchical<JavaAnnotation>
    implements CodeAnnotations<JavaAnnotation>, CodeNodeItemWithGenericParent<JavaElementImpl, JavaAnnotations> {

  private final JavaElementImpl parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaAnnotations(JavaElementImpl parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaAnnotations} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaAnnotations(JavaAnnotations template, JavaElementImpl parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  public JavaElementImpl getParent() {

    return this.parent;
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    Object reflectiveObject = this.parent.getReflectiveObject();
    if (reflectiveObject instanceof AnnotatedElement) {
      for (Annotation annotation : ((AnnotatedElement) reflectiveObject).getDeclaredAnnotations()) {
        JavaAnnotation child = new JavaAnnotation(getContext(), annotation);
        getList().add(child);
      }
    }
  }

  @Override
  protected boolean isSystemImmutable() {

    if (super.isSystemImmutable()) {
      return true;
    }
    Object reflectiveObject = this.parent.getReflectiveObject();
    if (reflectiveObject != null) {
      return true;
    }
    return false;
  }

  @Override
  public JavaAnnotation getDeclared(CodeType type) {

    for (JavaAnnotation annotation : getDeclared()) {
      if (annotation.getType().equals(type)) {
        return annotation;
      }
    }
    return null;
  }

  @Override
  public JavaAnnotation add(CodeType type) {

    verifyMutalbe();
    JavaAnnotation annotation = new JavaAnnotation(getContext(), (JavaType) type);
    add(annotation);
    return annotation;
  }

  @Override
  public void add(JavaAnnotation item) {

    super.add(item);
  }

  @Override
  public JavaAnnotation getDeclaredOrAdd(CodeType type) {

    JavaAnnotation annotation = getDeclared(type);
    if (annotation == null) {
      annotation = add(type);
    }
    return annotation;
  }

  @Override
  public Iterable<? extends JavaAnnotation> getAll() {

    if (this.parent instanceof JavaType) {
      return () -> new TypeAnnotationIterator((JavaType) this.parent);
    } else if (this.parent instanceof JavaMethod) {
      return () -> new MethodAnnotationIterator((JavaMethod) this.parent);
    } else {
      return getDeclared();
    }
  }

  @Override
  public JavaAnnotations copy() {

    return copy(this.parent);
  }

  @Override
  public JavaAnnotations copy(JavaElementImpl newParent) {

    return new JavaAnnotations(this, newParent);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    String prefix = "";
    for (JavaAnnotation annotation : getDeclared()) {
      if (defaultIndent == null) {
        sink.append(prefix);
        prefix = " ";
      } else {
        sink.append(newline);
        sink.append(currentIndent);
      }
      annotation.write(sink, newline, defaultIndent, currentIndent, syntax);
    }
    sink.append(prefix);
  }

  private abstract class AnnotationIterator extends AbstractIterator<JavaAnnotation> {

    private final Set<JavaType> iteratedAnnotations;

    private Iterator<JavaAnnotation> currentIterator;

    protected AnnotationIterator() {

      super();
      this.iteratedAnnotations = new HashSet<>();
      this.currentIterator = getList().iterator();
    }

    @Override
    protected JavaAnnotation findNext() {

      while (this.currentIterator.hasNext()) {
        JavaAnnotation annotation = this.currentIterator.next();
        JavaType annotationType = annotation.getType().asType();
        boolean added = this.iteratedAnnotations.add(annotationType);
        if (added) {
          return annotation;
        }
      }
      this.currentIterator = nextParent();
      if (this.currentIterator == null) {
        return null;
      }
      return findNext();
    }

    protected abstract Iterator<JavaAnnotation> nextParent();
  }

  private class MethodAnnotationIterator extends AnnotationIterator {

    private JavaMethod method;

    private MethodAnnotationIterator(JavaMethod method) {

      super();
      this.method = method;
      findFirst();
    }

    @Override
    protected Iterator<JavaAnnotation> nextParent() {

      this.method = this.method.getParentMethod();
      if (this.method == null) {
        return null;
      }
      return this.method.getAnnotations().getList().iterator();
    }
  }

  private class TypeAnnotationIterator extends AnnotationIterator {

    private InternalSuperTypeIterator iterator;

    private TypeAnnotationIterator(JavaType type) {

      super();
      this.iterator = new InternalSuperTypeIterator(type);
      findFirst();
    }

    @Override
    protected Iterator<JavaAnnotation> nextParent() {

      if (this.iterator.hasNext()) {
        this.iterator = this.iterator.next();
      } else {
        return null;
      }
      if (this.iterator == null) {
        return null;
      }
      return this.iterator.getType().asType().getAnnotations().getList().iterator();
    }
  }

}
