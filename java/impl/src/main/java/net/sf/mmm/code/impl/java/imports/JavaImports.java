/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.imports;

import java.io.IOException;
import java.util.List;
import java.util.TreeSet;

import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.imports.CodeImport;
import net.sf.mmm.code.api.imports.CodeImports;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.impl.java.JavaFile;
import net.sf.mmm.code.impl.java.JavaPackage;
import net.sf.mmm.code.impl.java.node.JavaNodeItemContainerFlat;

/**
 * Implementation of {@link CodeImports} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaImports extends JavaNodeItemContainerFlat<JavaImport>
    implements CodeImports<JavaImport>, CodeNodeItemWithGenericParent<JavaFile, JavaImports> {

  private static final String DUMMY_PACKAGE_PREFIX = ".";

  private final JavaFile parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaImports(JavaFile parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaImports} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaImports(JavaImports template, JavaFile parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  public JavaFile getParent() {

    return this.parent;
  }

  @Override
  public JavaImport add(CodeType type) {

    verifyMutalbe();
    CodePackage pkg = type.getParentPackage();
    if (!pkg.isRequireImport()) {
      return null;
    }
    JavaPackage myPkg = getParent().getParentPackage();
    if ((pkg == myPkg) || pkg.getQualifiedName().equals(myPkg.getQualifiedName())) {
      return null;
    }
    String qname = type.getQualifiedName();
    for (JavaImport imp : getAll()) {
      if (!imp.isStatic() && imp.getReference().equals(qname)) {
        return null;
      }
    }
    JavaImport imp = new JavaImport(qname, false);
    add(imp);
    return imp;
  }

  /**
   * @param reference the {@link JavaImport#getReference() reference} to import.
   * @param staticFlag the {@link JavaImport#isStatic() static} flag.
   * @return the {@link JavaImport} that has been created and added.
   */
  public JavaImport add(String reference, boolean staticFlag) {

    verifyMutalbe();
    JavaImport imp = new JavaImport(reference, staticFlag);
    add(imp);
    return imp;
  }

  @Override
  public JavaImports copy() {

    return copy(this.parent);
  }

  @Override
  public JavaImports copy(JavaFile newParent) {

    return new JavaImports(this, newParent);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    List<JavaImport> imports = getList();
    if (imports.isEmpty()) {
      return;
    }
    TreeSet<CodeImport> sortedImportSet = new TreeSet<>(imports);
    String currentSegmentPrefix = DUMMY_PACKAGE_PREFIX;
    for (CodeImport imp : sortedImportSet) {
      String reference = imp.getReference();
      if (!reference.startsWith(currentSegmentPrefix)) {
        if (!DUMMY_PACKAGE_PREFIX.equals(currentSegmentPrefix)) {
          sink.append(newline);
        }
        int firstDot = reference.indexOf('.');
        if (firstDot > 0) {
          currentSegmentPrefix = reference.substring(0, firstDot + 1);
        } else {
          currentSegmentPrefix = DUMMY_PACKAGE_PREFIX + DUMMY_PACKAGE_PREFIX;
        }
      }
      imp.write(sink, defaultIndent, currentIndent);
    }
    sink.append(newline);
  }

}
