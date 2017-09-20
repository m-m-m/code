/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.CodeFile;
import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.impl.java.imports.JavaImports;
import net.sf.mmm.code.impl.java.item.JavaReflectiveObject;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.util.exception.api.ObjectMismatchException;

/**
 * Implementation of {@link CodeItem} for Java reflection.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class JavaFile extends JavaPathElement implements CodeFile, CodeNodeItemWithGenericParent<JavaPackage, JavaFile>, JavaReflectiveObject<Class<?>> {

  private final JavaImports imports;

  private final Class<?> reflectioveObject;

  private List<JavaType> types;

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   */
  public JavaFile(JavaPackage parentPackage, String simpleName) {

    this(parentPackage, simpleName, null);
  }

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public JavaFile(JavaPackage parentPackage, Class<?> reflectiveObject) {

    this(parentPackage, reflectiveObject.getSimpleName(), reflectiveObject);
    Package pkg = reflectiveObject.getPackage();
    Package pkg2 = parentPackage.getReflectiveObject();
    if (pkg != pkg2) {
      if (pkg2 != null) {
        throw new ObjectMismatchException(Package.class, pkg, pkg2);
      }
      String pkgName = parentPackage.getQualifiedName();
      if (!pkgName.equals(pkg.getName())) {
        throw new ObjectMismatchException(Package.class, pkg, pkgName);
      }
    }
  }

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  private JavaFile(JavaPackage parentPackage, String simpleName, Class<?> reflectiveObject) {

    super(parentPackage, simpleName);
    this.imports = new JavaImports(this);
    this.types = new ArrayList<>();
    this.types.add(new JavaType(this, reflectiveObject));
    this.reflectioveObject = reflectiveObject;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaFile} to copy.
   * @param parentPackage the {@link #getParentPackage() parent package}.
   */
  public JavaFile(JavaFile template, JavaPackage parentPackage) {

    super(template, parentPackage);
    this.imports = template.imports.copy(this);
    this.types = doCopy(template.types, this);
    this.reflectioveObject = null;
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.types = makeImmutable(this.types);
    this.imports.setImmutable();;
  }

  @Override
  public JavaPackage getParent() {

    return getParentPackage();
  }

  @Override
  public boolean isFile() {

    return true;
  }

  @Override
  public String getExtension() {

    return ".java";
  }

  @Override
  public List<JavaType> getTypes() {

    return getTypes(true);
  }

  List<JavaType> getTypes(boolean init) {

    initialize(init);
    return this.types;
  }

  /**
   * @param simpleName the {@link JavaType#getSimpleName() simple name} of the requested {@link JavaType} to
   *        search {@link JavaType#getNestedTypes() recursively}.
   * @return the requested {@link JavaType} or {@code null} if not found.
   */
  public JavaType getType(String simpleName) {

    return getType(simpleName, true);
  }

  JavaType getType(String simpleName, boolean init) {

    for (JavaType type : getTypes(init)) {
      if (type.getSimpleName().equals(simpleName)) {
        return type;
      }
      JavaType nestedType = getContainerItem(type.getNestedTypes(), simpleName, init);
      if (nestedType != null) {
        return nestedType;
      }
    }
    return null;
  }

  @Override
  public JavaType getType() {

    if (this.types.isEmpty()) {
      return null;
    }
    return this.types.get(0);
  }

  @Override
  public JavaImports getImports() {

    return this.imports;
  }

  @Override
  public JavaType getDeclaringType() {

    return null;
  }

  @Override
  public Class<?> getReflectiveObject() {

    return this.reflectioveObject;
  }

  @Override
  public JavaFile copy() {

    return copy(getParent());
  }

  @Override
  public JavaFile copy(JavaPackage newParent) {

    return new JavaFile(this, newParent);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    if (currentIndent == null) {
      getType().writeReference(sink, true);
    } else {
      doWriteComment(sink, newline, defaultIndent, currentIndent);
      getParentPackage().doWrite(sink, newline, defaultIndent, currentIndent);
      getImports().write(sink, newline, defaultIndent, currentIndent);
      doWriteDoc(sink, newline, defaultIndent, currentIndent);
      doWriteAnnotations(sink, newline, defaultIndent, currentIndent);
      sink.append(newline);
      for (JavaType type : getTypes()) {
        type.write(sink, newline, defaultIndent, currentIndent);
      }
    }
  }

}
