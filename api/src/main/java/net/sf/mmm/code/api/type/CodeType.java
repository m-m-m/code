/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import java.util.List;

import net.sf.mmm.code.api.CodeFile;
import net.sf.mmm.code.api.block.CodeBlockInitializer;
import net.sf.mmm.code.api.copy.CodeNodeItemCopyable;
import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.element.CodeElementWithModifiers;
import net.sf.mmm.code.api.element.CodeElementWithTypeVariables;
import net.sf.mmm.code.api.item.CodeMutableItemWithQualifiedName;
import net.sf.mmm.code.api.member.CodeConstructors;
import net.sf.mmm.code.api.member.CodeFields;
import net.sf.mmm.code.api.member.CodeMethods;
import net.sf.mmm.code.api.member.CodeProperties;
import net.sf.mmm.code.api.member.CodeProperty;
import net.sf.mmm.code.api.merge.CodeAdvancedMergeableItem;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeElement} representing a type (similar to {@link Class}).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeType extends CodeGenericType, CodeElementWithModifiers, CodeMutableItemWithQualifiedName, CodeElementWithTypeVariables,
    CodeAdvancedMergeableItem<CodeType>, CodeNodeItemCopyable<CodeElement, CodeType> {

  /**
   * @return the parent element of this type. Will either be the {@link #getFile() file} or the
   *         {@link #getDeclaringType() declaring type}.
   */
  @Override
  CodeElement getParent();

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
   * @return the {@link List} of {@link CodeGenericType}s inherited by this type. This model does not distinguish
   *         {@code extends} vs. {@code inherits} and potentially allows multi-inheritance of classes for languages
   *         other than Java.
   */
  CodeSuperTypes getSuperTypes();

  /**
   * @return the {@link CodeFields} containing the actual {@link net.sf.mmm.code.api.member.CodeField}s.
   */
  CodeFields getFields();

  /**
   * @return the {@link CodeMethods} containing the actual {@link net.sf.mmm.code.api.member.CodeMethod}s.
   */
  CodeMethods getMethods();

  /**
   * @return the {@link CodeConstructors} containing the actual {@link net.sf.mmm.code.api.member.CodeConstructor}s.
   */
  CodeConstructors getConstructors();

  /**
   * @return the {@link CodeProperties} containing instances of {@link CodeProperty}.
   */
  CodeProperties getProperties();

  /**
   * @return the {@link CodeType} containing this {@link CodeType} or {@code this} type itself if not a
   *         {@link #isNested() nested type}.
   */
  @Override
  CodeType getDeclaringType();

  /**
   * @return {@code true} if this a nested type (sometimes called "inner class"), {@code false} otherwise (called a
   *         "top-level" type).
   * @see #getDeclaringType()
   */
  default boolean isNested() {

    return (getDeclaringType() != this);
  }

  /**
   * @return the {@link CodeNestedTypes} containing the {@link #isNested() nested} {@link CodeType}s. May be
   *         {@link List#isEmpty() empty} but is never {@code null}.
   */
  CodeNestedTypes getNestedTypes();

  /**
   * @return the static initializer block. Will be omitted if empty. To create an empty static initializer simply add an
   *         empty statement to it. For simplicity only a single static initializer is supported by this API. Multiple
   *         static initializers will be joined (appended) automatically.
   */
  CodeBlockInitializer getStaticInitializer();

  /**
   * @param initializer the new value of {@link #getStaticInitializer()}. Has to be
   *        {@link CodeBlockInitializer#isStatic() static}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setStaticInitializer(CodeBlockInitializer initializer);

  /**
   * @return the non-static initializer block. Will be omitted if empty. To create an empty non-static initializer
   *         simply add an empty statement to it. For simplicity only a single non-static initializer is supported by
   *         this API. Multiple non-static initializers will be joined (appended) automatically.
   */
  CodeBlockInitializer getNonStaticInitializer();

  /**
   * @param initializer the new value of {@link #getNonStaticInitializer()}. Shall not be
   *        {@link CodeBlockInitializer#isStatic() static}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setNonStaticInitializer(CodeBlockInitializer initializer);

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

  /**
   * @return {@code true} if this type represents an exception (in Java any type assignable to {@link Throwable}).
   */
  boolean isException();

  @Override
  default boolean isArray() {

    return false;
  }

  @Override
  default boolean isQualified() {

    return false;
  }

  @Override
  default CodeTypeVariable asTypeVariable() {

    return null;
  }

  @Override
  default CodeType asType() {

    return this;
  }

  /**
   * This method should only be called if this type actually has {@link #getTypeParameters() type variables}.
   *
   * @param parent the {@link CodeParameterizedType#getParent() parent} of the new {@link CodeParameterizedType}.
   * @return a new {@link CodeParameterizedType} with this type as {@link CodeParameterizedType#getType() raw type}.
   */
  CodeParameterizedType createParameterizedType(CodeElement parent);

  /**
   * @return the {@link CodeGenericType} representing this {@link CodeType} as {@link #isQualified() fully qualified}
   *         type. If this {@link CodeType} {@link #getParentPackage() is contained} in the default package this will
   *         return this type itself (e.g. for primitive types).
   */
  CodeGenericType getQualifiedType();

  @Override
  Class<?> getReflectiveObject();

  @Override
  default CodeType resolve(CodeGenericType context) {

    return this;
  }

  @Override
  CodeType copy();
}
