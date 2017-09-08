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
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeItem} for Java reflection.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class JavaFile extends JavaPathElement implements CodeFile, CodeNodeItemWithGenericParent<JavaPackage, JavaFile> {

  private final JavaImports imports;

  private List<JavaType> types;

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   */
  public JavaFile(JavaPackage parentPackage, String simpleName) {

    this(parentPackage, simpleName, true);
  }

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param createDefaultType {@code true} to create the {@link #getType() main type} as empty class,
   *        {@code false} otherwise (to create an empty file that has to be initialized externally).
   */
  JavaFile(JavaPackage parentPackage, String simpleName, boolean createDefaultType) {

    super(parentPackage, simpleName);
    this.imports = new JavaImports(this);
    this.types = new ArrayList<>();
    if (createDefaultType) {
      this.types.add(new JavaType(this));
    }
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

    return this.types;
  }

  /**
   * @param simpleName the {@link JavaType#getSimpleName() simple name} of the requested {@link JavaType} to
   *        search {@link JavaType#getNestedTypes() recursively}.
   * @return the requested {@link JavaType} or {@code null} if not found.
   */
  public JavaType getType(String simpleName) {

    for (JavaType type : this.types) {
      if (type.getSimpleName().equals(simpleName)) {
        return type;
      }
      JavaType nestedType = type.getNestedTypes().get(simpleName);
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
      for (JavaType type : this.types) {
        type.write(sink, newline, defaultIndent, currentIndent);
      }
    }
  }

}
