/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.member.CodeFields;
import net.sf.mmm.code.api.type.CodeType;
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
public class JavaFields extends JavaMembers<CodeField> implements CodeFields {

  private List<JavaField> fields;

  private Map<String, JavaField> fieldMap;

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaFields(JavaType declaringType) {

    super(declaringType);
    this.fields = new ArrayList<>();
    this.fieldMap = new HashMap<>();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaFields} to copy.
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaFields(JavaFields template, JavaType declaringType) {

    super(template, declaringType);
    this.fields = doCopy(template.fields, declaringType);
    this.fieldMap = new HashMap<>(this.fields.size());
    for (JavaField field : this.fields) {
      this.fieldMap.put(field.getName(), field);
    }
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.fields = makeImmutable(this.fields);
    this.fieldMap = Collections.unmodifiableMap(this.fieldMap);
  }

  @Override
  public List<? extends JavaField> getDeclared() {

    return this.fields;
  }

  @Override
  public Iterable<? extends JavaField> getAll() {

    JavaGenericType superClass = getDeclaringType().getSuperTypes().getSuperClass();
    if (superClass == null) {
      return this.fields;
    } else {
      return () -> new FieldIterator(getDeclaringType());
    }
  }

  @Override
  public JavaField getDeclared(String name) {

    return this.fieldMap.get(name);
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
    if (this.fieldMap.containsKey(name)) {
      throw new DuplicateObjectException(getDeclaringType().getSimpleName() + ".fields", name);
    }
    JavaField field = new JavaField(getDeclaringType());
    field.setName(name);
    this.fields.add(field);
    this.fieldMap.put(name, field);
    return field;
  }

  /**
   * @param field the {@link JavaField} that has been renamed.
   * @param oldName the old {@link JavaField#getName() name}.
   */
  void rename(JavaField field, String oldName) {

    JavaField oldField = this.fieldMap.remove(oldName);
    assert (field == oldField);
    JavaField duplicate = this.fieldMap.put(field.getName(), field);
    assert (duplicate == null);
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    doWriteFields(sink, defaultIndent, currentIndent, f -> f.getModifiers().isStatic());
    doWriteFields(sink, defaultIndent, currentIndent, f -> !f.getModifiers().isStatic());
  }

  private void doWriteFields(Appendable sink, String defaultIndent, String currentIndent, Predicate<CodeField> filter) throws IOException {

    for (CodeField field : this.fields) {
      if (filter.test(field)) {
        writeNewline(sink);
        field.write(sink, defaultIndent, currentIndent);
      }
    }
  }

  @Override
  public JavaFields copy(CodeType newDeclaringType) {

    return new JavaFields(this, (JavaType) newDeclaringType);
  }

  private static class FieldIterator extends AbstractIterator<JavaField> {

    private JavaType type;

    private Iterator<JavaField> currentIterator;

    private FieldIterator(JavaType type) {

      super();
      this.type = type;
      this.currentIterator = type.getFields().fields.iterator();
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
      this.currentIterator = this.type.getFields().fields.iterator();
      return findNext();
    }
  }

}
