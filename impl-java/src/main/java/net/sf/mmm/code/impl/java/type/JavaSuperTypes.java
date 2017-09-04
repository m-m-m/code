/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeSuperTypes;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.impl.java.item.JavaItemContainerWithInheritance;

/**
 * Implementation of {@link CodeSuperTypes} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSuperTypes extends JavaItemContainerWithInheritance<CodeGenericType> implements CodeSuperTypes {

  private static final Logger LOG = LoggerFactory.getLogger(JavaSuperTypes.class);

  private List<JavaGenericType> superTypes;

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaSuperTypes(JavaType declaringType) {

    super(declaringType);
    this.superTypes = new ArrayList<>();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaSuperTypes} to copy.
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaSuperTypes(JavaSuperTypes template, JavaType declaringType) {

    super(template, declaringType);
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.superTypes = Collections.unmodifiableList(this.superTypes);
  }

  @Override
  public List<? extends JavaGenericType> getDeclared() {

    return this.superTypes;
  }

  @Override
  public Iterable<? extends JavaGenericType> getAll() {

    Set<JavaGenericType> set = new HashSet<>();
    collectSuperTypes(set);
    return set;
  }

  private void collectSuperTypes(Set<JavaGenericType> set) {

    set.addAll(this.superTypes);
    for (JavaGenericType superType : this.superTypes) {
      superType.asType().getSuperTypes().collectSuperTypes(set);
    }
  }

  @Override
  public void add(CodeGenericType superType) {

    verifyMutalbe();
    this.superTypes.add((JavaGenericType) superType);
  }

  @Override
  public JavaGenericType getSuperClass() {

    for (JavaGenericType type : getDeclared()) {
      if (type.isClass()) {
        return type;
      }
    }
    JavaType declaringType = getDeclaringType();
    JavaType rootType = getContext().getRootType();
    if (declaringType != rootType) {
      return rootType;
    }
    return null;
  }

  @Override
  public List<? extends JavaGenericType> getSuperInterfaces() {

    return getDeclared().stream().filter(x -> x.isInterface()).collect(Collectors.toList());
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    String keywordInherit;
    JavaType declaringType = getDeclaringType();
    if (declaringType.isInterface()) {
      keywordInherit = " extends ";
    } else {
      CodeGenericType superClass = getSuperClass();
      if (superClass != null) {
        if (declaringType.isClass()) {
          sink.append(" extends ");
          superClass.writeReference(sink, false);
        } else {
          LOG.warn("Illegal {} {}: can not have super-class {}.", declaringType.getCategory(), declaringType.getSimpleName(),
              superClass.asType().getSimpleName());
        }
      }
      keywordInherit = " implements ";
    }
    String separator = keywordInherit;
    for (CodeGenericType superType : this.superTypes) {
      assert (superType.isInterface());
      sink.append(separator);
      superType.writeReference(sink, false);
      separator = ", ";
    }
  }

  @Override
  public JavaSuperTypes copy(CodeType newDeclaringType) {

    return new JavaSuperTypes(this, (JavaType) newDeclaringType);
  }

}
