/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.function.Predicate;

import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.member.CodeFields;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.util.collection.base.AbstractIterator;
import net.sf.mmm.util.exception.api.DuplicateObjectException;

/**
 * Base implementation of {@link CodeFields}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseFields extends BaseMembers<BaseField> implements CodeFields<BaseField>, CodeNodeItemWithGenericParent<BaseType, BaseFields> {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseFields(BaseType parent) {

    super(parent);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseFields} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public BaseFields(BaseFields template, BaseType parent) {

    super(template, parent);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    Class<?> reflectiveObject = getParent().getReflectiveObject();
    if (reflectiveObject != null) {
      for (Field field : reflectiveObject.getDeclaredFields()) {
        BaseField javaField = new BaseField(this, field);
        addInternal(javaField);
      }
    }
  }

  @Override
  public Iterable<? extends BaseField> getAll() {

    BaseType declaringType = getDeclaringType();
    CodeGenericType superClass = declaringType.getSuperTypes().getSuperClass();
    if (superClass == null) {
      return getDeclared();
    } else {
      return () -> new FieldIterator(declaringType);
    }
  }

  @Override
  public BaseField getDeclared(String name) {

    initialize();
    return getByName(name);
  }

  @Override
  public BaseField get(String name) {

    BaseField field = getDeclared(name);
    if (field == null) {
      BaseGenericType superType = getDeclaringType().getSuperTypes().getSuperClass();
      if (superType != null) {
        field = superType.asType().getFields().get(name);
      }
    }
    return field;
  }

  @Override
  public BaseField add(String name) {

    verifyMutalbe();
    if (getByName(name) != null) {
      throw new DuplicateObjectException(getDeclaringType().getSimpleName() + ".fields", name);
    }
    BaseField field = new BaseField(this, name);
    add(field);
    return field;
  }

  @Override
  public BaseFields getSourceCodeObject() {

    BaseType sourceType = getParent().getSourceCodeObject();
    if (sourceType == null) {
      return null;
    }
    return sourceType.getFields();
  }

  @Override
  public BaseFields copy() {

    return copy(getParent());
  }

  @Override
  public BaseFields copy(BaseType newParent) {

    return new BaseFields(this, newParent);
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

  private static class FieldIterator extends AbstractIterator<BaseField> {

    private BaseType type;

    private Iterator<? extends BaseField> currentIterator;

    private FieldIterator(BaseType type) {

      super();
      this.type = type;
      this.currentIterator = type.getFields().getDeclared().iterator();
      findFirst();
    }

    @Override
    protected BaseField findNext() {

      if (this.currentIterator == null) {
        return null;
      }
      if (this.currentIterator.hasNext()) {
        return this.currentIterator.next();
      }
      BaseGenericType superClass = this.type.getSuperTypes().getSuperClass();
      if (superClass == null) {
        return null;
      }
      this.type = superClass.asType();
      this.currentIterator = this.type.getFields().getDeclared().iterator();
      return findNext();
    }
  }

}
