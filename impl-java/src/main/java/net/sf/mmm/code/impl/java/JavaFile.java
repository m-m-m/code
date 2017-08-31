/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import net.sf.mmm.code.api.CodeFile;
import net.sf.mmm.code.api.CodeItem;
import net.sf.mmm.code.api.CodeType;
import net.sf.mmm.code.api.imports.CodeImport;
import net.sf.mmm.code.api.statement.CodeComment;

/**
 * Implementation of {@link CodeItem} for Java reflection.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaFile extends JavaItemWithQualifiedName implements CodeFile {

  private static final String DUMMY_PACKAGE_PREFIX = ".";

  private List<CodeType> types;

  private List<CodeImport> imports;

  private CodeComment header;

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
    this.header = null;
  }

  @Override
  public String getExtension() {

    return ".java";
  }

  @Override
  public List<CodeType> getTypes() {

    return this.types;
  }

  @Override
  public List<CodeImport> getImports() {

    return this.imports;
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.types = makeImmutable(this.types);
    this.imports = makeImmutable(this.imports);
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    if (this.header != null) {
      this.header.write(sink, defaultIndent, currentIndent);
    }
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
