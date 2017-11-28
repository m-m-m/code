/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.sf.mmm.code.api.CodeFile;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyType;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.imports.BaseImports;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.util.exception.api.ObjectMismatchException;

/**
 * Base implementation of {@link CodeFile}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class BaseFile extends BasePathElement implements CodeFile {

  private final BaseImports imports;

  private final Class<?> reflectioveObject;

  private BaseFile sourceCodeObject;

  private Supplier<BaseFile> sourceSupplier;

  private List<BaseType> types;

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   */
  public BaseFile(BasePackage parentPackage, String simpleName) {

    this(parentPackage, simpleName, null);
  }

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   * @param sourceSupplier the {@link Supplier} of the lazy-loaded {@link #getSourceCodeObject() source code object}.
   */
  public BaseFile(BasePackage parentPackage, Class<?> reflectiveObject, Supplier<BaseFile> sourceSupplier) {

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
    this.sourceSupplier = sourceSupplier;
  }

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  private BaseFile(BasePackage parentPackage, String simpleName, Class<?> reflectiveObject) {

    super(parentPackage, simpleName);
    this.imports = new BaseImports(this);
    this.types = new ArrayList<>();
    this.types.add(new BaseType(this, reflectiveObject));
    this.reflectioveObject = reflectiveObject;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseFile} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseFile(BaseFile template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.imports = template.imports.copy(mapper);

    this.types = doMapList(template.types, mapper, CodeCopyType.CHILD);
    this.reflectioveObject = null;
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.types = makeImmutable(this.types);
    this.imports.setImmutableIfNotSystemImmutable();
  }

  @Override
  public BasePackage getParent() {

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
  public List<BaseType> getTypes() {

    return getTypes(true);
  }

  List<BaseType> getTypes(boolean init) {

    initialize(init);
    return this.types;
  }

  /**
   * @param simpleName the {@link BaseType#getSimpleName() simple name} of the requested {@link BaseType}
   *        {@link BaseType#getNestedTypes() recursively} contained in this {@link BaseFile}.
   * @return the requested {@link BaseType} or {@code null} if not found.
   */
  public CodeType getType(String simpleName) {

    return getType(simpleName, true);
  }

  /**
   * @param simpleName the {@link BaseType#getSimpleName() simple name} of the requested {@link BaseType}
   *        {@link BaseType#getNestedTypes() recursively} contained in this {@link BaseFile}.
   * @param init - {@code true} to ensure the child types are properly initialized and the result is adequate,
   *        {@code false} otherwise (to avoid initialization e.g. for internal calls during initialization).
   * @return the requested {@link BaseType} or {@code null} if not found.
   */
  public CodeType getType(String simpleName, boolean init) {

    for (BaseType type : getTypes(init)) {
      if (type.getSimpleName().equals(simpleName)) {
        return type;
      }
      CodeType nestedType = getContainerItem(type.getNestedTypes(), simpleName, init);
      if (nestedType != null) {
        return nestedType;
      }
    }
    return null;
  }

  /**
   * @param simpleName the {@link BaseType#getSimpleName() simple name} of the requested {@link BaseType} to resolve in
   *        the context of this {@link BaseFile}.
   * @param init - {@code true} to ensure the child types are properly initialized and the result is adequate,
   *        {@code false} otherwise (to avoid initialization e.g. for internal calls during initialization).
   * @param resolve - {@code true} to resolve the requested {@link BaseType} in the
   *        {@link BaseContext#getQualifiedName(String, CodeFile, boolean) context} of this file, {@code false}
   *        otherwise (to only search for files contained in this file like {@link #getType(String, boolean)}).
   * @return the requested {@link BaseType} or {@code null} if not found.
   */
  public CodeType getType(String simpleName, boolean init, boolean resolve) {

    CodeType type = getType(simpleName, init);
    if ((type == null) && resolve) {
      String qualifiedName = getContext().getQualifiedName(simpleName, this, false);
      type = getContext().getType(qualifiedName);
    }
    return type;
  }

  @Override
  public BaseType getType() {

    if ((this.types == null) || this.types.isEmpty()) {
      return null;
    }
    return this.types.get(0);
  }

  @Override
  public BaseImports getImports() {

    return this.imports;
  }

  @Override
  public Class<?> getReflectiveObject() {

    return this.reflectioveObject;
  }

  @Override
  public BaseFile getSourceCodeObject() {

    if (this.sourceCodeObject == null) {
      if (this.sourceSupplier != null) {
        this.sourceCodeObject = this.sourceSupplier.get();
      }
    }
    return this.sourceCodeObject;
  }

  @Override
  public BaseFile copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseFile copy(CodeCopyMapper mapper) {

    return new BaseFile(this, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    if (defaultIndent == null) {
      BaseType type = getType();
      if (type == null) {
        sink.append(getSimpleName());
      } else {
        type.writeReference(sink, true);
      }
    } else {
      doWriteComment(sink, newline, defaultIndent, currentIndent, language);
      getParentPackage().doWrite(sink, newline, null, currentIndent, language);
      getImports().write(sink, newline, defaultIndent, currentIndent, language);
      doWriteDoc(sink, newline, defaultIndent, currentIndent, language);
      doWriteAnnotations(sink, newline, defaultIndent, currentIndent, language);
      for (BaseType type : getTypes()) {
        type.write(sink, newline, defaultIndent, currentIndent);
      }
    }
  }

}
