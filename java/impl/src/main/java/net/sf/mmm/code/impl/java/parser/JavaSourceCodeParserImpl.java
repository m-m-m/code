/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.parser;

import java.io.Reader;

import net.sf.mmm.code.api.java.parser.JavaSourceCodeParser;
import net.sf.mmm.code.impl.java.JavaCodeLoader;
import net.sf.mmm.code.impl.java.JavaFile;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link JavaSourceCodeParser}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSourceCodeParserImpl implements JavaSourceCodeParser {

  private final JavaSourceCodeReaderHighlevel codeReader;

  /**
   * The constructor.
   */
  public JavaSourceCodeParserImpl() {

    super();
    this.codeReader = new JavaSourceCodeReaderHighlevel();
  }

  @Override
  public JavaType parse(Reader reader, JavaFile file, JavaCodeLoader byteCodeLoader) {

    return this.codeReader.parse(reader, file, byteCodeLoader);
  }

}
