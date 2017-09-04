/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import net.sf.mmm.code.api.CodeFile;
import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.imports.CodeImport;
import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.impl.java.item.JavaItemWithQualifiedName;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeItem} for Java reflection.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaFile extends JavaItemWithQualifiedName implements CodeFile {

  private static final String DUMMY_PACKAGE_PREFIX = ".";

  private List<JavaType> types;

  private List<JavaImport> imports;

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   */
  public JavaFile(JavaPackage parentPackage, String simpleName) {

    super(parentPackage, simpleName);
    this.imports = new ArrayList<>();
    this.types = new ArrayList<>();
    if (!parentPackage.isImmutable()) {
      parentPackage.getChildFiles().add(this);
    }
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaItemWithQualifiedName} to copy.
   * @param parentPackage the {@link #getParentPackage() parent package}.
   */
  public JavaFile(JavaFile template, JavaPackage parentPackage) {

    super(template, parentPackage);
    this.imports = doCopy(template.imports);
    this.types = doCopy(template.types);
  }

  @Override
  public String getExtension() {

    return ".java";
  }

  @Override
  public List<JavaType> getTypes() {

    return this.types;
  }

  @Override
  public List<? extends JavaImport> getImports() {

    return this.imports;
  }

  @Override
  public void addImport(CodeType type) {

    CodePackage pkg = type.getParentPackage();
    if (!pkg.isRequireImport()) {
      return;
    }
    JavaPackage myPkg = getParentPackage();
    if ((pkg == myPkg) || pkg.getQualifiedName().equals(myPkg.getQualifiedName())) {
      return;
    }
    String name = type.getQualifiedName();
    for (JavaImport imp : this.imports) {
      if (!imp.isStatic() && imp.getSource().equals(name)) {
        return;
      }
    }
    JavaImport imp = getContext().createImport(type);
    this.imports.add(imp);
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.types = makeImmutable(this.types);
    this.imports = makeImmutable(this.imports);
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    super.doWrite(sink, defaultIndent, currentIndent);
    if (currentIndent == null) {
      getType().writeReference(sink, true);
    } else {
      getParentPackage().doWrite(sink, defaultIndent, currentIndent);
      writeNewline(sink);
      TreeSet<CodeImport> sortedImportSet = new TreeSet<>(this.imports);
      String currentSegmentPrefix = DUMMY_PACKAGE_PREFIX;
      for (CodeImport imp : sortedImportSet) {
        String source = imp.getSource();
        if (!source.startsWith(currentSegmentPrefix)) {
          if (!DUMMY_PACKAGE_PREFIX.equals(currentSegmentPrefix)) {
            writeNewline(sink);
          }
          int firstDot = source.indexOf('.');
          if (firstDot > 0) {
            currentSegmentPrefix = source.substring(0, firstDot + 1);
          } else {
            currentSegmentPrefix = DUMMY_PACKAGE_PREFIX + DUMMY_PACKAGE_PREFIX;
          }
        }
        imp.write(sink, defaultIndent, currentIndent);
      }
      if (this.imports.size() > 0) {
        writeNewline(sink);
      }
      for (CodeType type : this.types) {
        type.write(sink, defaultIndent, currentIndent);
      }
    }
  }

  /**
   * @param simpleName the {@link JavaType#getSimpleName() simple name} of the requested {@link JavaType}.
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
  public JavaFile copy(CodePackage newParentPackage) {

    return new JavaFile(this, (JavaPackage) newParentPackage);
  }

}
