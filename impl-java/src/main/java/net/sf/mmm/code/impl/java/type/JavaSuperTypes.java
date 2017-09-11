/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeSuperTypes;
import net.sf.mmm.code.impl.java.node.JavaNodeItemContainerHierarchical;
import net.sf.mmm.util.collection.base.AbstractIterator;

/**
 * Implementation of {@link CodeSuperTypes} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSuperTypes extends JavaNodeItemContainerHierarchical<CodeGenericType, JavaGenericType>
    implements CodeSuperTypes, CodeNodeItemWithGenericParent<JavaType, JavaSuperTypes> {

  private static final Logger LOG = LoggerFactory.getLogger(JavaSuperTypes.class);

  private final JavaType parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaSuperTypes(JavaType parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaSuperTypes} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaSuperTypes(JavaSuperTypes template, JavaType parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  public JavaType getParent() {

    return this.parent;
  }

  @Override
  public Iterable<? extends JavaGenericType> getAll() {

    return () -> new SuperTypeIterator(this.parent);
  }

  @Override
  public void add(JavaGenericType superType) {

    if (superType.asType().equals(this.parent)) {
      throw new IllegalStateException("Type " + this.parent.getQualifiedName() + " can not extend itself");
    }
    super.add(superType);
  }

  @Override
  public void add(CodeGenericType superType) {

    add((JavaGenericType) superType);
  }

  @Override
  public JavaGenericType getSuperClass() {

    JavaGenericType superClass = getSuperClassAsDeclared();
    if (superClass != null) {
      return superClass;
    }
    JavaType declaringType = getDeclaringType();
    JavaType rootType = getContext().getRootType();
    if (declaringType != rootType) {
      return rootType;
    }
    return null;
  }

  private JavaGenericType getSuperClassAsDeclared() {

    if (this.parent.isInterface() || this.parent.isAnnotation()) {
      return null;
    }
    for (JavaGenericType type : getDeclared()) {
      if (type.isClass()) {
        return type;
      }
    }
    return null;
  }

  @Override
  public List<? extends JavaGenericType> getSuperInterfaces() {

    return getDeclared().stream().filter(x -> x.isInterface()).collect(Collectors.toList());
  }

  @Override
  public JavaSuperTypes copy() {

    return copy(this.parent);
  }

  @Override
  public JavaSuperTypes copy(JavaType newParent) {

    return new JavaSuperTypes(this, newParent);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    String keywordInherit;
    JavaType declaringType = getDeclaringType();
    if (declaringType.isInterface()) {
      keywordInherit = " extends ";
    } else {
      JavaGenericType superClass = getSuperClassAsDeclared();
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
    for (JavaGenericType superType : getDeclared()) {
      assert (superType.isInterface());
      sink.append(separator);
      superType.writeReference(sink, false);
      separator = ", ";
    }
  }

  private static class SuperTypeIterator extends AbstractIterator<JavaGenericType> {

    private InternalSuperTypeIterator iterator;

    /**
     * The constructor.
     *
     * @param type the initial {@link JavaType}. All its transitive super-types will be iterated.
     */
    public SuperTypeIterator(JavaType type) {

      super();
      this.iterator = new InternalSuperTypeIterator(type);
      findFirst();
    }

    @Override
    protected JavaGenericType findNext() {

      JavaGenericType next = this.iterator.nextSuperType();
      if (this.iterator.hasNext()) {
        this.iterator = this.iterator.next();
        if (next == null) {
          return findNext();
        }
      }
      return next;
    }

  }

}
