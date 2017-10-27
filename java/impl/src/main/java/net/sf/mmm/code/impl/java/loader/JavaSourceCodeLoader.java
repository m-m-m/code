/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.loader;

import net.sf.mmm.code.api.java.parser.JavaSourceCodeParser;
import net.sf.mmm.code.base.BaseFile;
import net.sf.mmm.code.base.BasePackage;
import net.sf.mmm.code.base.loader.BaseCodeLoader;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.impl.java.AbstractJavaCodeLoader;
import net.sf.mmm.code.impl.java.parser.JavaSourceCodeParserImpl;

/**
 * Implementation of {@link AbstractJavaCodeLoader} to load from {@link #isSupportSourceCode() source code}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaSourceCodeLoader extends AbstractJavaCodeLoader {

  private JavaSourceCodeParser parser;

  private BaseCodeLoader byteCodeLoader;

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
  public BaseType getType(String qualifiedName) {

    if (this.byteCodeLoader != null) {
      BaseType type = this.byteCodeLoader.getType(qualifiedName);
      if (type != null) {
        BaseFile file = type.getFile();
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
    BasePackage pkg = getContext().getSource().getPackage(pkgName);
    BaseFile file = pkg.getChildren().createFile(simpleName);
    parseType(file, simpleName);
    return file.getType(); // TODO: nested types?
  }

  protected abstract void parseType(BaseFile file, String simpleName);

}
