/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.parser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.comment.CodeComment;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.modifier.CodeVisibility;
import net.sf.mmm.code.api.operator.CodeNAryOperator;
import net.sf.mmm.code.api.operator.CodeOperator;
import net.sf.mmm.code.base.comment.GenericBlockComment;
import net.sf.mmm.code.base.comment.GenericComments;
import net.sf.mmm.code.base.comment.GenericSingleLineComment;
import net.sf.mmm.code.base.operator.GenericOperator;
import net.sf.mmm.code.impl.java.JavaFile;
import net.sf.mmm.code.impl.java.annotation.JavaAnnotation;
import net.sf.mmm.code.impl.java.expression.JavaNAryOperatorExpression;
import net.sf.mmm.code.impl.java.expression.literal.JavaLiteral;
import net.sf.mmm.code.impl.java.expression.literal.JavaLiteralBoolean;
import net.sf.mmm.code.impl.java.expression.literal.JavaLiteralChar;
import net.sf.mmm.code.impl.java.expression.literal.JavaLiteralDouble;
import net.sf.mmm.code.impl.java.expression.literal.JavaLiteralFloat;
import net.sf.mmm.code.impl.java.expression.literal.JavaLiteralInt;
import net.sf.mmm.code.impl.java.expression.literal.JavaLiteralLong;
import net.sf.mmm.code.impl.java.expression.literal.JavaLiteralString;
import net.sf.mmm.util.filter.api.CharFilter;
import net.sf.mmm.util.lang.base.StringUtilImpl;
import net.sf.mmm.util.scanner.base.CharReaderScanner;

/**
 * Wrapper for a {@link Reader} with internal char buffer to read and parse textual data.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaSourceCodeReaderLowlevel extends CharReaderScanner {

  private static final Logger LOG = LoggerFactory.getLogger(JavaSourceCodeReaderLowlevel.class);

  private static final CharFilter CHAR_FILTER_SPACES = c -> ((c == ' ') || (c == '\t'));

  private static final CharFilter CHAR_FILTER_IDENTIFIER = c -> Character.isJavaIdentifierPart(c);

  private static final CharFilter CHAR_FILTER_QNAME = c -> Character.isJavaIdentifierPart(c) || (c == '.');

  private static final CharFilter CHAR_FILTER_ANNOTATION_KEY = c -> ((c == '{') || (c == '=') || (c == ','));

  private static final CharFilter CHAR_FILTER_OPERATOR = c -> ((c == '+') || (c == '-') || (c == '*') || (c == '/') || (c == '^') || (c == '%') || (c == '>')
      || (c == '<') || (c == '!') || (c == '~') || (c == '='));

  private static final CharFilter CHAR_FILTER_NUMBER_LITERAL_START = c -> ((c >= '0') && (c <= '9') || (c == '+') || (c == '-'));

  protected final List<String> javaDocLines;

  protected final List<CodeComment> comments;

  protected CodeComment elementComment;

  private final List<JavaAnnotation> annotations;

  /** The current {@link JavaFile} to parse. */
  protected JavaFile file;

  /**
   * The constructor.
   */
  public JavaSourceCodeReaderLowlevel() {

    this(4096);
  }

  /**
   * The constructor.
   *
   * @param capacity the buffer capacity.
   */
  public JavaSourceCodeReaderLowlevel(int capacity) {

    super(capacity);
    this.javaDocLines = new ArrayList<>();
    this.comments = new ArrayList<>();
    this.annotations = new ArrayList<>();
  }

  @Override
  protected void reset() {

    super.reset();
    clearConsumeState();
    this.file = null;
  }

  /**
   * Clears all collected comments, annotations and javaDocs.
   */
  protected void clearConsumeState() {

    this.javaDocLines.clear();
    this.comments.clear();
    this.elementComment = null;
    this.annotations.clear();
  }

  /**
   * @return the (last) comments that have been parsed by the last invocation of {@link #consume()}. Will be
   *         {@link List#isEmpty() empty} for none.
   * @see #getElementComment()
   */
  public List<CodeComment> getComments() {

    return this.comments;
  }

  /**
   * @return the {@link CodeComment} for the currently parsed "element" (type, member, etc.) parsed by the
   *         last invocation of {@link #consume()}.
   */
  public CodeComment getElementComment() {

    if (this.elementComment == null) {
      int size = this.comments.size();
      if (size == 1) {
        this.elementComment = this.comments.get(0);
      } else if (size > 1) {
        this.elementComment = new GenericComments(new ArrayList<>(this.comments));
      }
    }
    return this.elementComment;
  }

  /**
   * @return the plain JavaDoc lines that have been parsed by the last invocation of {@link #consume()}. Will
   *         be {@link List#isEmpty() empty} for no JavaDoc.
   */
  public List<String> getJavaDocLines() {

    return this.javaDocLines;
  }

  /**
   * @return the {@link List} of {@link JavaAnnotation}s that have been parsed by the last invocation of
   *         {@link #consume()}.
   */
  public List<JavaAnnotation> getAnnotations() {

    return this.annotations;
  }

  /**
   * Consumes all standard text like spaces, comments, JavaDoc and annotations
   *
   * @return the {@link #forcePeek() peeked} character (no whitespace, doc or comment).
   */
  public char consume() {

    // clearConsumeState();
    skipWhile(CHAR_FILTER_SPACES);
    char c = forcePeek();
    if (c == '/') { // comment or doc?
      next();
      parseDocOrComment();
    } else if (c == '@') { // annotation?
      next();
      this.elementComment = getAndClearComments();
      parseAnnotations();
    }
    return c;
  }

  protected void parseWhitespacesAndComments() {

    skipWhile(CharFilter.WHITESPACE_FILTER);
    if (expect('/')) {
      parseDocOrComment();
    }
  }

  private CodeComment getAndClearComments() {

    CodeComment comment = null;
    int commentCount = this.comments.size();
    if (commentCount > 0) {
      if (commentCount == 1) {
        comment = this.comments.get(0);
      } else {
        comment = new GenericComments(new ArrayList<>(this.comments));
      }
      this.comments.clear();
    }
    return comment;
  }

  protected String parseIdentifier() {

    if (Character.isJavaIdentifierStart(forcePeek())) {
      return readWhile(CHAR_FILTER_IDENTIFIER);
    }
    return null;
  }

  protected String parseQName() {

    if (Character.isJavaIdentifierStart(forcePeek())) {
      return readWhile(CHAR_FILTER_QNAME);
    }
    return null;
  }

  private void parseAnnotations() {

    String annotationTypeName = readWhile(CHAR_FILTER_IDENTIFIER);
    String annotationQName = getQualifiedName(annotationTypeName);
    JavaAnnotation annotation = new JavaAnnotation(this.file.getContext(), annotationQName);
    if (expect('(')) {
      parseAnnotationParameters(annotation, annotationTypeName);
    }
    CodeComment comment = getAndClearComments();
    if (comment != null) {
      annotation.setComment(comment);
    }
    this.annotations.add(annotation);
  }

  private void parseAnnotationParameters(JavaAnnotation annotation, String annotationTypeName) {

    parseWhitespacesAndComments();
    if (expect(')')) {
      return;
    }
    Map<String, CodeExpression> parameters = annotation.getParameters();
    boolean first = true;
    while (true) {
      String key = readUntil(CHAR_FILTER_ANNOTATION_KEY, false, ")", false, true);
      if (key.isEmpty()) {
        if (first) {
          key = "value"; // Java build in default
        } else {
          LOG.warn("Annotation {} parameter without name in {}.", annotationTypeName, Character.toString(forcePeek()), this.file);
        }
      } else {
        parseWhitespacesAndComments();
        if (!expect('=')) {
          LOG.warn("Invalid character '{}' after annotation parameter {}.{} name in {}.", Character.toString(forcePeek()), annotationTypeName, key, this.file);
        }
      }
      CodeExpression value = parseAssignmentValue();
      parameters.put(key, value);
    }

  }

  private CodeExpression parseAssignmentValue() {

    parseWhitespacesAndComments();
    CodeExpression expression = parseSingleAssignmentValue();
    CodeOperator operator = null;
    List<CodeExpression> expressions = null;
    while (true) {
      parseWhitespacesAndComments();
      char c = forcePeek();
      if (CHAR_FILTER_OPERATOR.accept(c)) {
        String operatorName = readWhile(CHAR_FILTER_OPERATOR);
        CodeOperator nextOperator = GenericOperator.of(operatorName);
        if (operator == null) {
          operator = nextOperator;
          if (expressions == null) {
            expressions = new ArrayList<>();
          }
          expressions.add(expression);
        } else if (operator != nextOperator) {
          if (operator.isNAry()) {
            expression = new JavaNAryOperatorExpression((CodeNAryOperator) operator, new ArrayList<>(expressions));
            expressions.clear();
            expressions.add(expression);
          }
          operator = nextOperator;
        }

      } else {
        break;
      }
    }
    // TODO Auto-generated method stub
    return expression;
  }

  private CodeExpression parseSingleAssignmentValue() {

    CodeExpression expression = parseLiteral();
    if (expression != null) {
      return expression;
    }
    // TODO parse constants, static method references, etc.
    return expression;
  }

  private JavaLiteral<?> parseLiteral() {

    JavaLiteral<?> literal = parseStringLiteral();
    if (literal != null) {
      return literal;
    }
    literal = parseBooleanLiteral();
    if (literal != null) {
      return literal;
    }
    literal = parseCharLiteral();
    if (literal != null) {
      return literal;
    }
    if (CHAR_FILTER_NUMBER_LITERAL_START.accept(forcePeek())) {
      return parseNumberLiteral();
    }
    return literal;
  }

  private JavaLiteral<Character> parseCharLiteral() {

    if (expect('\'')) {
      char c = forceNext();
      if (c == '\\') {
        StringBuilder error = null;
        c = forceNext();
        char next = forceNext();
        if (next == '\'') {
          Character character = StringUtilImpl.getInstance().resolveEscape(c);
          if (character != null) {
            return JavaLiteralChar.of(character.charValue());
          }
        } else if (CharFilter.OCTAL_DIGIT_FILTER.accept(c) && CharFilter.OCTAL_DIGIT_FILTER.accept(next)) {
          int value = ((c - '0') * 7) + (next - 'c');
          char last = forceNext();
          if (CharFilter.OCTAL_DIGIT_FILTER.accept(last) && (value <= 37)) {
            value = (value * 7) + (last - 'c');
            last = forceNext();
          }
          if (last == '\'') {
            return JavaLiteralChar.of((char) value);
          }
          error = new StringBuilder("'\\");
          error.append(Integer.toString(value, 8));
          error.append(last);
        }
        if (error == null) {
          error = new StringBuilder("'\\");
          error.append(c);
          error.append(next);
        }
        String rest = readUntil('\'', true);
        error.append(rest);
        LOG.warn("Invalid char literal {} in {}.", error.toString(), this.file);
        return JavaLiteralChar.of('?');
      } else {
        return JavaLiteralChar.of(c);
      }
    }
    return null;

  }

  private JavaLiteral<Boolean> parseBooleanLiteral() {

    if (expectStrict("true")) {
      return JavaLiteralBoolean.TRUE;
    } else if (expectStrict("false")) {
      return JavaLiteralBoolean.FALSE;
    }
    return null;
  }

  private JavaLiteral<String> parseStringLiteral() {

    if (forcePeek() == '"') {
      String value = readJavaStringLiteral();
      return JavaLiteralString.of(value);
    }
    return null;
  }

  private JavaLiteral<? extends Number> parseNumberLiteral() {

    String decimal = consumeDecimal();
    char c = Character.toLowerCase(forcePeek());
    if (c == 'l') {
      return JavaLiteralLong.of(Long.parseLong(decimal));
    } else if (c == 'f') {
      return JavaLiteralFloat.of(Float.parseFloat(decimal));
    } else if (c == 'd') {
      return JavaLiteralDouble.of(Double.parseDouble(decimal));
    }
    if ((decimal.indexOf('.') >= 0) || (decimal.indexOf('e') >= 0)) {
      return JavaLiteralDouble.of(Double.parseDouble(decimal));
    } else {
      return JavaLiteralInt.of(Integer.parseInt(decimal));
    }
  }

  private String getQualifiedName(String name) {

    if (name.indexOf('.') == -1) {
      return this.file.getContext().getQualifiedName(name, this.file, false);
    }
    return name;
  }

  private void parseDocOrComment() {

    char c = forcePeek();
    if (c == '/') {
      String line = readLine(true);
      this.comments.add(new GenericSingleLineComment(line));
    } else if (c == '*') {
      next();
      c = forcePeek();
      if (c == '*') { // JavaDoc or regular comment
        if (!this.javaDocLines.isEmpty()) {
          LOG.warn("Duplicate JavaDoc in {}.", this.file);
        }
        parseDocOrBlockComment(this.javaDocLines);
      } else {
        List<String> lines = new ArrayList<>();
        parseDocOrBlockComment(lines);
        GenericBlockComment comment = new GenericBlockComment(lines);
        this.comments.add(comment);
      }
    } else {
      LOG.warn("Illegal syntax: {} in {}.", "/" + c, this.file);
    }
  }

  private void parseDocOrBlockComment(List<String> lines) {

    String line = readUntil(CharFilter.NEWLINE_FILTER, true, "*/", false, true);
    if (!line.isEmpty()) {
      lines.add(line);
    }
    if (expectStrict("*/")) {
      return;
    }
    while (true) {
      skipWhile(CharFilter.WHITESPACE_FILTER);
      char c = forcePeek();
      if (c == '*') {
        next();
        c = forcePeek();
        if (c == '/') {
          next();
          skipWhile(CharFilter.WHITESPACE_FILTER);
          return;
        }
        skipWhile(CHAR_FILTER_SPACES); // trim does not consider tabs
      }
      line = readUntil(CharFilter.NEWLINE_FILTER, true, "*/", false, true);
      lines.add(line);
    }
  }

  protected CodeModifiers parseModifiers(boolean inInterface) {

    CodeVisibility visibility = parseVisibility(getVisibilityFallback(inInterface));
    Set<String> modifiers = new HashSet<>();
    boolean found = true;
    while (found) {
      skipWhile(CharFilter.WHITESPACE_FILTER);
      char c = forcePeek();
      if (c == 'a') {
        found = parseModifierKeyword(modifiers, CodeModifiers.KEY_ABSTRACT);
      } else if (c == 'n') {
        found = parseModifierKeyword(modifiers, CodeModifiers.KEY_NATIVE);
      } else if (c == 'f') {
        found = parseModifierKeyword(modifiers, CodeModifiers.KEY_FINAL);
      } else if (c == 'v') {
        found = parseModifierKeyword(modifiers, CodeModifiers.KEY_VOLATILE);
      } else if (c == 's') {
        found = parseModifierKeyword(modifiers, CodeModifiers.KEY_STATIC);
        if (!found) {
          found = parseModifierKeyword(modifiers, CodeModifiers.KEY_SYNCHRONIZED);
          if (!found) {
            found = parseModifierKeyword(modifiers, "strictfp");
          }
        }
      } else if (c == 't') {
        found = parseModifierKeyword(modifiers, CodeModifiers.KEY_TRANSIENT);
      } else {
        found = false;
      }
    }
    return new CodeModifiers(visibility, modifiers);
  }

  private boolean parseModifierKeyword(Set<String> modifiers, String modifier) {

    if (expectStrict(modifier)) {
      modifiers.add(modifier);
      return true;
    }
    return false;
  }

  private CodeVisibility getVisibilityFallback(boolean inInterface) {

    if (inInterface) {
      return CodeVisibility.PUBLIC;
    } else {
      return CodeVisibility.DEFAULT;
    }
  }

  private CodeVisibility parseVisibility(CodeVisibility fallback) {

    // private, protected, public
    // static final volatitle strictfp
    // class enum interface @interface
    if (forcePeek() != 'p') {
      return fallback;
    }
    if (expectStrict("private")) {
      return CodeVisibility.PRIVATE;
    } else if (expectStrict("public")) {
      return CodeVisibility.PUBLIC;
    } else if (expectStrict("protected")) {
      return CodeVisibility.PROTECTED;
    }
    return fallback;
  }

}
