/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.loader;

import net.sf.mmm.code.api.java.parser.JavaSourceCodeParser;
import net.sf.mmm.code.impl.java.AbstractJavaCodeLoader;
import net.sf.mmm.code.impl.java.JavaFile;
import net.sf.mmm.code.impl.java.JavaPackage;
import net.sf.mmm.code.impl.java.parser.JavaSourceCodeParserImpl;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link AbstractJavaCodeLoader} to load from {@link #isSupportSourceCode() source code}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaSourceCodeLoader extends AbstractJavaCodeLoader {

  private JavaSourceCodeParser parser;

  private JavaCodeLoader byteCodeLoader;

  /**
   * The constructor.
   */
  public JavaSourceCodeLoader() {

    super();
  }

  /**
   * @return the {@link JavaSourceCodeParser} used to parse source code files.
   */
  public JavaSourceCodeParser getParser() {

    if (this.parser == null) {
      this.parser = JavaSourceCodeParserImpl.get();
    }
    return this.parser;
  }

  /**
   * @param parser the new value of {@link #getParser()}.
   */
  public void setParser(JavaSourceCodeParser parser) {

    if (this.parser != null) {
      throw new IllegalStateException("Already initialized!");
    }
    this.parser = parser;
  }

  @Override
  public boolean isSupportByteCode() {

    return false;
  }

  @Override
  public boolean isSupportSourceCode() {

    return true;
  }

  @Override
  public JavaType getType(String qualifiedName) {

    if (this.byteCodeLoader != null) {
      JavaType type = this.byteCodeLoader.getType(qualifiedName);
      if (type != null) {
        JavaFile file = type.getFile();
        parseType(file, file.getType().getSimpleName());
        return type;
      }
    }
    int index = qualifiedName.lastIndexOf('.');
    String simpleName;
    String pkgName;
    if (index > 0) {
      simpleName = qualifiedName.substring(index + 1);
      pkgName = qualifiedName.substring(0, index);
    } else {
      simpleName = qualifiedName;
      pkgName = "";
    }
    JavaPackage pkg = getContext().getSource().getPackage(pkgName);
    JavaFile file = pkg.getChildren().createFile(simpleName);
    parseType(file, simpleName);
    return file.getType(); // TODO: nested types?
  }

  protected abstract void parseType(JavaFile file, String simpleName);

}
