/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import net.sf.mmm.code.api.CodeGenericType;
import net.sf.mmm.code.api.type.CodeSuperTypes;
import net.sf.mmm.code.impl.java.item.JavaItemContainerWithInheritance;
import net.sf.mmm.util.collection.base.AbstractIterator;

/**
 * Implementation of {@link CodeSuperTypes} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSuperTypes extends JavaItemContainerWithInheritance<CodeGenericType> implements CodeSuperTypes {

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
   */
  public JavaSuperTypes(JavaSuperTypes template) {

    super(template);
  }

  @Override
  public List<? extends JavaGenericType> getAll() {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void add(CodeGenericType superType) {

    verifyMutalbe();
    this.superTypes.add((JavaGenericType) superType);
  }

  @Override
  public List<? extends JavaGenericType> getDeclared() {

    return this.superTypes;
  }

  @Override
  public Iterable<? extends JavaGenericType> getInherited() {

    // TODO Auto-generated method stub
    return null;
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

    // TODO Auto-generated method stub
    String keywordInherit;
    JavaType declaringType = getDeclaringType();
    if (declaringType.isInterface()) {
      keywordInherit = " extends ";
    } else {
      CodeGenericType superClass = getSuperClass();
      if (superClass != null) {
        sink.append(" extends ");
        superClass.writeReference(sink, false);
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

  private static class SuperTypeIterator extends AbstractIterator<JavaGenericType> {

    private Iterator<JavaGenericType> superTypeIterator;

    private JavaType type;

    private Iterator<JavaGenericType> currentIterator;

    private SuperTypeIterator(JavaType type) {

      super();
      this.type = type;
      this.superTypeIterator = type.getSuperTypes().superTypes.iterator();
      this.currentIterator = type.getSuperTypes().superTypes.iterator();
      findFirst();
    }

    @Override
    protected JavaGenericType findNext() {

      if (this.currentIterator == null) {
        return null;
      }
      if (this.currentIterator.hasNext()) {
        return this.currentIterator.next();
      }
      if (this.superTypeIterator.hasNext()) {

      }
      // JavaGenericType superTypes = this.type.getSuperTypes().superTypes;
      // if (superClass == null) {
      // return null;
      // }
      // this.type = superClass.asType();
      // this.currentIterator = this.type.getFields().fields.iterator();
      return findNext();
    }
  }

}
