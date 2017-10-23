/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.member.CodeFields;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.util.collection.base.AbstractIterator;
import net.sf.mmm.util.exception.api.DuplicateObjectException;

/**
 * Implementation of {@link CodeFields} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaFields extends JavaMembers<JavaField> implements CodeFields<JavaField>, CodeNodeItemWithGenericParent<JavaType, JavaFields> {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaFields(JavaType parent) {

    super(parent);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaFields} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaFields(JavaFields template, JavaType parent) {

    super(template, parent);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    Class<?> reflectiveObject = getParent().getReflectiveObject();
    if (reflectiveObject != null) {
      for (Field field : reflectiveObject.getDeclaredFields()) {
        JavaField javaField = new JavaField(this, field);
        addInternal(javaField);
      }
    }
  }

  @Override
  public List<? extends JavaField> getDeclared() {

    initialize();
    return getList();
  }

  @Override
  public Iterable<? extends JavaField> getAll() {

    JavaType declaringType = getDeclaringType();
    JavaGenericType superClass = declaringType.getSuperTypes().getSuperClass();
    if (superClass == null) {
      return getDeclared();
    } else {
      return () -> new FieldIterator(declaringType);
    }
  }

  @Override
  public JavaField getDeclared(String name) {

    initialize();
    return getByName(name);
  }

  @Override
  public JavaField get(String name) {

    JavaField field = getDeclared(name);
    if (field == null) {
      JavaGenericType superType = getDeclaringType().getSuperTypes().getSuperClass();
      if (superType != null) {
        field = superType.asType().getFields().get(name);
      }
    }
    return field;
  }

  @Override
  public JavaField add(String name) {

    verifyMutalbe();
    if (getByName(name) != null) {
      throw new DuplicateObjectException(getDeclaringType().getSimpleName() + ".fields", name);
    }
    JavaField field = new JavaField(this, name);
    add(field);
    return field;
  }

  @Override
  public JavaFields copy() {

    return copy(getParent());
  }

  @Override
  public JavaFields copy(JavaType newParent) {

    return new JavaFields(this, newParent);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    doWriteFields(sink, newline, defaultIndent, currentIndent, f -> f.getModifiers().isStatic());
    doWriteFields(sink, newline, defaultIndent, currentIndent, f -> !f.getModifiers().isStatic());
  }

  private void doWriteFields(Appendable sink, String newline, String defaultIndent, String currentIndent, Predicate<CodeField> filter) throws IOException {

    for (CodeField field : getDeclared()) {
      if (filter.test(field)) {
        sink.append(newline);
        field.write(sink, defaultIndent, currentIndent);
      }
    }
  }

  private static class FieldIterator extends AbstractIterator<JavaField> {

    private JavaType type;

    private Iterator<? extends JavaField> currentIterator;

    private FieldIterator(JavaType type) {

      super();
      this.type = type;
      this.currentIterator = type.getFields().getDeclared().iterator();
      findFirst();
    }

    @Override
    protected JavaField findNext() {

      if (this.currentIterator == null) {
        return null;
      }
      if (this.currentIterator.hasNext()) {
        return this.currentIterator.next();
      }
      JavaGenericType superClass = this.type.getSuperTypes().getSuperClass();
      if (superClass == null) {
        return null;
      }
      this.type = superClass.asType();
      this.currentIterator = this.type.getFields().getDeclared().iterator();
      return findNext();
    }
  }

}
