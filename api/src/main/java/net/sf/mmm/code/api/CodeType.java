/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import java.util.List;
import java.util.Map;

import net.sf.mmm.code.api.member.CodeConstructor;
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.member.CodeMemberSelector;
import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.member.CodeProperty;
import net.sf.mmm.code.api.member.CodePropertySelector;
import net.sf.mmm.code.api.modifier.CodeElementWithModifiers;
import net.sf.mmm.code.api.statement.CodeStaticBlock;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeElement} representing a type (similar to {@link Class}).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeType extends CodeElementWithQualifiedName, CodeElementWithModifiers, CodeGenericType {

  /**
   * @return the {@link CodeFile} {@link CodeFile#getTypes() containing} this type.
   */
  CodeFile getFile();

  @Override
  CodeTypeCategory getCategory();

  /**
   * @param category the {@link #getCategory() category}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setCategory(CodeTypeCategory category);

  /**
   * @return the {@link List} of {@link CodeGenericType}s inherited by this type. This model does not
   *         distinguish {@code extends} vs. {@code inherits} and potentially allows multi-inheritance of
   *         classes for languages other than Java.
   */
  List<CodeGenericType> getSuperTypes();

  /**
   * @return the {@link List} of {@link CodeGenericType generic type} {@link #getTypeVariable() variables}
   *         declared by this type. May be {@link List#isEmpty() empty} but is never {@code null}.
   * @see Class#getTypeParameters()
   */
  List<CodeGenericType> getTypeParameters();

  /**
   * @return the {@link List} of {@link CodeField}s declared by this type. May be {@link List#isEmpty() empty}
   *         but is never {@code null}.
   */
  List<CodeField> getFields();

  /**
   * @param selector the {@link CodeMemberSelector}.
   * @return the {@link List} of {@link CodeField}s of this type matching the given {@link CodeMemberSelector
   *         selector}. May be {@link List#isEmpty() empty} but is never {@code null}.
   */
  List<CodeField> getFields(CodeMemberSelector selector);

  /**
   * @return the {@link List} of {@link CodeMethod}s declared by this type. May be {@link List#isEmpty()
   *         empty} but is never {@code null}.
   */
  List<CodeMethod> getMethods();

  /**
   * @param selector the {@link CodeMemberSelector}.
   * @return the {@link List} of {@link CodeMethod}s of this type matching the given {@link CodeMemberSelector
   *         selector}. May be {@link List#isEmpty() empty} but is never {@code null}.
   */
  List<CodeMethod> getMethods(CodeMemberSelector selector);

  /**
   * @return the {@link List} of {@link CodeConstructor}s of this type. May be {@link List#isEmpty() empty}
   *         but is never {@code null}.
   */
  List<CodeConstructor> getConstructors();

  /**
   * This method can be expensive so avoid subsequent calls if possible.
   *
   * @param memberSelector the {@link CodeMemberSelector}.
   * @param propertySelector the {@link CodePropertySelector}.
   * @return a {@link Map} of the {@link CodeProperty} {@link Map#get(Object) associated} by their
   *         {@link CodeProperty#getName() name}.
   */
  Map<String, CodeProperty> getProperties(CodeMemberSelector memberSelector, CodePropertySelector propertySelector);

  /**
   * @return the {@link CodeType} containing this {@link CodeType} or {@code null} if not a {@link #isNested()
   *         nested type}.
   */
  CodeType getDeclaringType();

  /**
   * @param declaringType the new {@link #getDeclaringType() declaring type}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setDeclaringType(CodeType declaringType);

  /**
   * @return {@code true} if this a nested type (sometimes called "inner class"), {@code false} otherwise
   *         (called a "top-level" type).
   * @see #getDeclaringType()
   */
  default boolean isNested() {

    return (getDeclaringType() != null);
  }

  /**
   * @return the {@link List} of the {@link #isNested() nested} {@link CodeType}s. May be
   *         {@link List#isEmpty() empty} but is never {@code null}. This {@link List} is always
   *         {@link java.util.Collections#unmodifiableCollection(java.util.Collection) unmodifiable}. Create a
   *         new nested type to add it here.
   */
  List<CodeType> getNestedTypes();

  /**
   * @return the static initializer block or {@code null} for none.
   */
  CodeStaticBlock getStaticInitializer();

  /**
   * @param initializer the new {@link #getStaticInitializer() static initializer}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setStaticInitializer(CodeStaticBlock initializer);

  /**
   * @return {@code true} if this is a primitive type, {@code false} otherwise.
   */
  boolean isPrimitive();

  /**
   * @return the corresponding non-{@link #isPrimitive() primitive} type if this type is {@link #isPrimitive()
   *         primitive} or this type itself otherwise.
   */
  CodeType getNonPrimitiveType();

  /**
   * @return {@code true} if this type represents {@link void} or {@link Void}.
   */
  boolean isVoid();

  /**
   * @return {@code true} if this type represents {@link boolean} or {@link Boolean}.
   */
  boolean isBoolean();

  @Override
  default boolean isArray() {

    return false;
  }

  @Override
  default boolean isQualified() {

    return false;
  }

  @Override
  default CodeTypeVariable getTypeVariable() {

    return null;
  }

  @Override
  default CodeType getRawType() {

    return this;
  }

  @Override
  default CodeType resolve(CodeGenericType context) {

    return this;
  }

}
