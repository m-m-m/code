/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.function.Predicate;

import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyMapperNone;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.member.CodeFields;
import net.sf.mmm.code.api.merge.CodeMergeStrategy;
import net.sf.mmm.code.api.merge.CodeMergeStrategyDecider;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.util.collection.base.AbstractIterator;
import net.sf.mmm.util.exception.api.DuplicateObjectException;

/**
 * Base implementation of {@link CodeFields}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseFields extends BaseMembers<CodeField> implements CodeFields {

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
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseFields(BaseFields template, BaseType parent, CodeCopyMapper mapper) {

    super(template, parent, mapper);
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
  public Iterable<? extends CodeField> getAll() {

    BaseType declaringType = getDeclaringType();
    CodeGenericType superClass = declaringType.getSuperTypes().getSuperClass();
    if (superClass == null) {
      return getDeclared();
    } else {
      return () -> new FieldIterator(declaringType);
    }
  }

  @Override
  public CodeField getDeclared(String name) {

    initialize();
    return getByName(name);
  }

  @Override
  public CodeField get(String name) {

    CodeField field = getDeclared(name);
    if (field == null) {
      CodeGenericType superType = getDeclaringType().getSuperTypes().getSuperClass();
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
  public CodeFields getSourceCodeObject() {

    CodeType sourceType = getParent().getSourceCodeObject();
    if (sourceType == null) {
      return null;
    }
    return sourceType.getFields();
  }

  @Override
  public CodeFields merge(CodeFields o, CodeMergeStrategyDecider decider, CodeMergeStrategy parentStrategy) {

    if (parentStrategy == CodeMergeStrategy.KEEP) {
      return this;
    }
    BaseFields other = (BaseFields) o;
    if (parentStrategy == CodeMergeStrategy.OVERRIDE) {
      clear();
      for (CodeField otherField : other.getDeclared()) {
        add(otherField.copy(this));
      }
    } else {
      for (CodeField otherField : other.getDeclared()) {
        CodeField myField = get(otherField.getName());
        if (myField == null) {
          add(otherField.copy(this));
        } else {
          myField.merge(otherField, decider, parentStrategy);
        }
      }
    }
    return this;
  }

  @Override
  public BaseFields copy() {

    return copy(getParent());
  }

  @Override
  public BaseFields copy(CodeType newParent) {

    return copy(newParent, CodeCopyMapperNone.INSTANCE);
  }

  @Override
  public BaseFields copy(CodeType newParent, CodeCopyMapper mapper) {

    return new BaseFields(this, (BaseType) newParent, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

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

  private static class FieldIterator extends AbstractIterator<CodeField> {

    private CodeType type;

    private Iterator<? extends CodeField> currentIterator;

    private FieldIterator(CodeType type) {

      super();
      this.type = type;
      this.currentIterator = type.getFields().getDeclared().iterator();
      findFirst();
    }

    @Override
    protected CodeField findNext() {

      if (this.currentIterator == null) {
        return null;
      }
      if (this.currentIterator.hasNext()) {
        return this.currentIterator.next();
      }
      CodeGenericType superClass = this.type.getSuperTypes().getSuperClass();
      if (superClass == null) {
        return null;
      }
      this.type = superClass.asType();
      this.currentIterator = this.type.getFields().getDeclared().iterator();
      return findNext();
    }
  }

}
