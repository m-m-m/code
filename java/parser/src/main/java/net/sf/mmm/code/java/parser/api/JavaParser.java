/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.java.parser.api;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import net.sf.mmm.code.java.parser.base.JavaSourceCodeLexer;
import net.sf.mmm.code.java.parser.base.JavaSourceCodeParser;
import net.sf.mmm.code.java.parser.base.JavaSourceCodeParser.CompilationUnitContext;
import net.sf.mmm.util.io.api.IoMode;
import net.sf.mmm.util.io.api.RuntimeIoException;

/**
 * This is the front-end (API entry point) for a parser that can read Java source code and return it as
 * abstract syntax tree. Unlike many other existing parsers it also includes JavaDoc. For a high-level API you
 * most probably want to use {@code net.sf.mmm.code.impl.java.JavaContext} instead.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaParser {

  public static CompilationUnitContext parse(String filename) {

    try {
      CharStream charStream = CharStreams.fromFileName(filename);
      return parse(charStream);
    } catch (IOException e) {
      throw new RuntimeIoException(e, IoMode.READ);
    }
  }

  public static CompilationUnitContext parse(Path file) {

    try {
      CharStream charStream = CharStreams.fromPath(file);
      return parse(charStream);
    } catch (IOException e) {
      throw new RuntimeIoException(e, IoMode.READ);
    }
  }

  public static CompilationUnitContext parse(Reader reader, String sourceName) {

    try {
      CharStream charStream = CharStreams.fromReader(reader, sourceName);
      return parse(charStream);
    } catch (IOException e) {
      throw new RuntimeIoException(e, IoMode.READ);
    }
  }

  public static CompilationUnitContext parse(CharStream charStream) {

    JavaSourceCodeLexer lexer = new JavaSourceCodeLexer(charStream);
    JavaSourceCodeParser parser = new JavaSourceCodeParser(new CommonTokenStream(lexer));

    return parser.compilationUnit();
  }

}
