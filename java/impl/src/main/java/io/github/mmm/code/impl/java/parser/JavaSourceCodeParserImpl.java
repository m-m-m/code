/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.parser;

import java.io.Reader;

import io.github.mmm.code.base.BaseFile;
import io.github.mmm.code.base.BasePackage;
import io.github.mmm.code.base.parser.SourceCodeParser;
import io.github.mmm.code.base.type.BaseType;

/**
 * Implementation of {@link SourceCodeParser}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSourceCodeParserImpl implements SourceCodeParser {

  private static JavaSourceCodeParserImpl instance;

  private final JavaSourceCodeReaderHighlevel codeReader;

  /**
   * The constructor.
   */
  public JavaSourceCodeParserImpl() {

    super();
    this.codeReader = new JavaSourceCodeReaderHighlevel();
  }

  @Override
  public BaseType parseType(Reader reader, BaseFile file) {

    return this.codeReader.parse(reader, file);
  }

  @Override
  public void parsePackage(Reader reader, BasePackage pkg) {

  }

  /**
   * @return the default instance of this class.
   */
  public static JavaSourceCodeParserImpl get() {

    if (instance == null) {
      synchronized (JavaSourceCodeParserImpl.class) {
        if (instance == null) {
          instance = new JavaSourceCodeParserImpl();
        }
      }
    }
    return instance;
  }

}
