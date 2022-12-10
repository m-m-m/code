/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.member;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.member.CodeField;
import io.github.mmm.code.api.member.CodeFields;
import io.github.mmm.code.api.merge.CodeMergeStrategy;
import io.github.mmm.code.api.merge.CodeMergeStrategyDecider;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeType;
import io.github.mmm.code.base.BaseFactory;
import io.github.mmm.code.base.type.BaseType;

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
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseFields(BaseFields template, CodeCopyMapper mapper) {

    super(template, mapper);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    Class<?> reflectiveObject = getParent().getReflectiveObject();
    if (reflectiveObject != null) {
      BaseFactory factory = getContext().getFactory();
      for (Field field : reflectiveObject.getDeclaredFields()) {
        BaseField javaField = factory.createField(this, null, field);
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
    BaseField field = getContext().getFactory().createField(this, name, null);
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
        add(doCopyNode(otherField, this));
      }
    } else {
      for (CodeField otherField : other.getDeclared()) {
        CodeField myField = get(otherField.getName());
        if (myField == null) {
          add(doCopyNode(otherField, this));
        } else {
          myField.merge(otherField, decider, parentStrategy);
        }
      }
    }
    return this;
  }

  @Override
  public BaseFields copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseFields copy(CodeCopyMapper mapper) {

    return new BaseFields(this, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent,
      CodeLanguage language) throws IOException {

    doWriteFields(sink, newline, defaultIndent, currentIndent, language, f -> f.getModifiers().isStatic());
    doWriteFields(sink, newline, defaultIndent, currentIndent, language, f -> !f.getModifiers().isStatic());
  }

  private void doWriteFields(Appendable sink, String newline, String defaultIndent, String currentIndent,
      CodeLanguage language, Predicate<CodeField> filter) throws IOException {

    for (CodeField field : getDeclared()) {
      if (filter.test(field)) {
        sink.append(newline);
        field.write(sink, newline, defaultIndent, currentIndent, language);
      }
    }
  }

  private static class FieldIterator implements Iterator<CodeField> {

    private CodeField next;

    private boolean done;

    private CodeType type;

    private Iterator<? extends CodeField> currentIterator;

    private FieldIterator(CodeType type) {

      super();
      this.type = type;
      this.currentIterator = type.getFields().getDeclared().iterator();
      this.next = findNext();
    }

    @Override
    public final boolean hasNext() {

      if (this.next != null) {
        return true;
      }
      if (this.done) {
        return false;
      }
      this.next = findNext();
      if (this.next == null) {
        this.done = true;
      }
      return (!this.done);
    }

    @Override
    public final CodeField next() {

      if (this.next == null) {
        throw new NoSuchElementException();
      } else {
        CodeField result = this.next;
        this.next = null;
        return result;
      }
    }

    private CodeField findNext() {

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
