/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.parser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.annotation.CodeAnnotation;
import net.sf.mmm.code.api.comment.CodeComment;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.modifier.CodeVisibility;
import net.sf.mmm.code.api.operator.CodeNAryOperator;
import net.sf.mmm.code.api.operator.CodeOperator;
import net.sf.mmm.code.base.BaseFile;
import net.sf.mmm.code.base.annoation.BaseAnnotation;
import net.sf.mmm.code.base.comment.BaseBlockComment;
import net.sf.mmm.code.base.comment.BaseComments;
import net.sf.mmm.code.base.comment.BaseSingleLineComment;
import net.sf.mmm.code.base.expression.BaseArrayInstatiation;
import net.sf.mmm.code.base.expression.BaseFieldReferenceLazy;
import net.sf.mmm.code.base.expression.BaseMethodInvocation;
import net.sf.mmm.code.base.imports.BaseImport;
import net.sf.mmm.code.base.member.BaseMethod;
import net.sf.mmm.code.base.operator.GenericOperator;
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
import net.sf.mmm.util.scanner.base.CharReaderScanner;

/**
 * Wrapper for a {@link Reader} with internal char buffer to read and parse textual data.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaSourceCodeReaderLowlevel extends CharReaderScanner {

  private static final Logger LOG = LoggerFactory.getLogger(JavaSourceCodeReaderLowlevel.class);

  static final CharFilter CHAR_FILTER_SPACES = c -> ((c == ' ') || (c == '\t'));

  static final CharFilter CHAR_FILTER_IDENTIFIER = c -> Character.isJavaIdentifierPart(c);

  static final CharFilter CHAR_FILTER_QNAME = c -> Character.isJavaIdentifierPart(c) || (c == '.');

  static final CharFilter CHAR_FILTER_ANNOTATION_KEY = c -> ((c == '{') || (c == '=') || (c == ','));

  static final CharFilter CHAR_FILTER_OPERATOR = c -> ((c == '+') || (c == '-') || (c == '*') || (c == '/') || (c == '^') || (c == '%') || (c == '>')
      || (c == '<') || (c == '!') || (c == '~') || (c == '='));

  static final CharFilter CHAR_FILTER_NUMBER_LITERAL_START = c -> ((c >= '0') && (c <= '9') || (c == '+') || (c == '-'));

  static final CharFilter CHAR_FILTER_STATEMENT_END = c -> ((c == ';') || (c == '\r') || (c == '\n'));

  /** {@link List} of plain JavaDoc lines collected whilst parsing. */
  protected final List<String> javaDocLines;

  /** {@link List} of {@link CodeComment}s collected whilst parsing. */
  protected final List<CodeComment> comments;

  /** @see #getElementComment() */
  protected CodeComment elementComment;

  /** {@link #getAnnotations()} */
  protected final List<CodeAnnotation> annotations;

  /** The current {@link BaseFile} to parse. */
  protected BaseFile file;

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
        this.elementComment = new BaseComments(new ArrayList<>(this.comments));
      }
      this.comments.clear();
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
   * @return the {@link List} of {@link CodeAnnotation}s that have been parsed by the last invocation of
   *         {@link #consume()}.
   */
  public List<CodeAnnotation> getAnnotations() {

    return this.annotations;
  }

  /**
   * Consumes all standard text like spaces, comments, JavaDoc and annotations
   */
  public void consume() {

    // clearConsumeState();
    skipWhile(CharFilter.WHITESPACE_FILTER);
    char c = forcePeek();
    if (c == '/') { // comment or doc?
      next();
      parseDocOrComment();
      consume();
    } else if (c == '@') { // annotation?
      next();
      getElementComment();
      parseAnnotations();
      consume();
    }
  }

  /**
   * Skips all whitespaces and parses all {@link CodeComment}s.
   */
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
        comment = new BaseComments(new ArrayList<>(this.comments));
      }
      this.comments.clear();
    }
    return comment;
  }

  /**
   * @return the current identifier or {@code null} if not pointing to such.
   */
  protected String parseIdentifier() {

    if (Character.isJavaIdentifierStart(forcePeek())) {
      return readWhile(CHAR_FILTER_IDENTIFIER);
    }
    return null;
  }

  /**
   * @return the current (qualified) name or {@code null} if not pointing to such.
   */
  protected String parseQName() {

    if (Character.isJavaIdentifierStart(forcePeek())) {
      return readWhile(CHAR_FILTER_QNAME);
    }
    return null;
  }

  private void parseAnnotations() {

    String annotationTypeName = parseQName();
    String annotationQName = getQualifiedName(annotationTypeName);
    CodeAnnotation annotation = new BaseAnnotation(this.file.getSource(), annotationTypeName, annotationQName);
    if (expect('(')) {
      parseAnnotationParameters(annotation, annotationTypeName);
    }
    CodeComment comment = getAndClearComments();
    if (comment != null) {
      annotation.setComment(comment);
    }
    this.annotations.add(annotation);
  }

  private void parseAnnotationParameters(CodeAnnotation annotation, String annotationTypeName) {

    parseWhitespacesAndComments();
    if (expect(')')) {
      return;
    }
    Map<String, CodeExpression> parameters = annotation.getParameters();
    boolean first = true;
    while (!expect(')')) {
      CodeExpression value = null;
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
          for (BaseImport importStatement : this.file.getImports()) {
            if (importStatement.isStatic()) {
              String reference = importStatement.getReference();
              int index = reference.length() - key.length() - 1;
              if (reference.endsWith(key) && (index > 0)) {
                if ((reference.charAt(index) == '.')) {
                  String fieldName = key;
                  key = "value";
                  String typeName = reference.substring(0, index);
                  value = new BaseFieldReferenceLazy(this.file.getContext(), typeName, null, fieldName);
                  break;
                }
              }
            }
          }
          if (value == null) {
            LOG.warn("Invalid character '{}' after annotation parameter {}.{} name in {}.", Character.toString(forcePeek()), annotationTypeName, key,
                this.file);
          }
        }
      }
      if (value == null) {
        if (expect('{')) {
          List<CodeExpression> args = new ArrayList<>();
          CodeExpression arg;
          do {
            arg = parseAssignmentValue();
            if (arg != null) {
              args.add(arg);
            }
          } while ((arg != null) && expect(','));
          if (!expect('}')) {
            LOG.warn("Invalid annotation array value - missing closing curly brace '}' for annotation {} at value {}", annotationTypeName, key);
          }
          value = new BaseArrayInstatiation(args);
        } else {
          value = parseAssignmentValue();
        }
      }
      parameters.put(key, value);
    }

  }

  CodeExpression parseAssignmentValue() {

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
            Objects.requireNonNull(expressions);
            expressions.clear();
            expressions.add(expression);
          }
          operator = nextOperator;
        }

      } else {
        break;
      }
    }
    return expression;
  }

  private CodeExpression parseSingleAssignmentValue() {

    CodeExpression expression = parseLiteral();
    if (expression != null) {
      return expression;
    }
    String qName = parseQName();
    parseWhitespacesAndComments();
    if (expect('(')) {
      List<CodeExpression> arguments = new ArrayList<>();
      CodeExpression arg = parseSingleAssignmentValue();
      if (arg != null) {
        arguments.add(arg);
        parseWhitespacesAndComments();
        while (expect(',')) {
          arg = parseSingleAssignmentValue();
          if (arg == null) {
            LOG.debug("Missing argument after ','");
            break;
          }
          arguments.add(arg);
          parseWhitespacesAndComments();
        }
      }
      if (!expect(')')) {
        LOG.debug("Missing ')'");
      }
      CodeName codeName = new CodeName(qName, '.');
      CodeName parent = codeName.getParent();
      if (parent != null) {

      }
      BaseMethod method = null;
      expression = new BaseMethodInvocation(method, arguments);
    } else {

    }
    // TODO parse constants, static method references, etc.
    return expression;
  }

  private JavaLiteral<?> parseLiteral() {

    String stringLiteral = readJavaStringLiteral(true);
    if (stringLiteral != null) {
      return JavaLiteralString.of(stringLiteral);
    }
    JavaLiteral<?> literal = parseBooleanLiteral();
    if (literal != null) {
      return literal;
    }
    Character charLiteral = readJavaCharLiteral(true);
    if (charLiteral != null) {
      return JavaLiteralChar.of(charLiteral);
    }
    if (CHAR_FILTER_NUMBER_LITERAL_START.accept(forcePeek())) {
      return parseNumberLiteral();
    }
    return literal;
  }

  private JavaLiteral<Boolean> parseBooleanLiteral() {

    if (expectStrict("true")) {
      return JavaLiteralBoolean.TRUE;
    } else if (expectStrict("false")) {
      return JavaLiteralBoolean.FALSE;
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
      this.comments.add(new BaseSingleLineComment(line));
    } else if (c == '*') {
      next();
      c = forcePeek();
      if (c == '*') { // JavaDoc or regular comment
        next();
        if (!this.javaDocLines.isEmpty()) {
          LOG.warn("Duplicate JavaDoc in {}.", this.file);
        }
        parseDocOrBlockComment(this.javaDocLines);
      } else {
        List<String> lines = new ArrayList<>();
        parseDocOrBlockComment(lines);
        BaseBlockComment comment = new BaseBlockComment(lines);
        this.comments.add(comment);
      }
    } else {
      LOG.warn("Illegal language: {} in {}.", "/" + c, this.file);
    }
  }

  private void parseDocOrBlockComment(List<String> lines) {

    String line = readDocOrCommentLine();
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
      }
      line = readDocOrCommentLine();
      lines.add(line);
    }
  }

  private String readDocOrCommentLine() {

    expect(' ');
    String line = readUntil(CharFilter.NEWLINE_FILTER, true, "*/", false, false);
    // trim end
    int max = line.length() - 1;
    int end = max;
    while ((end > 0) && (line.charAt(end) == ' ')) {
      end--;
    }
    if (end < max) {
      line = line.substring(0, end + 1);
    }
    return line;
  }

  /**
   * @param inInterface - {@code true} if in the context of an interface (where public is the default),
   *        {@code false} otherwise.
   * @return the parsed {@link CodeModifiers}.
   */
  protected CodeModifiers parseModifiers(boolean inInterface) {

    CodeVisibility visibility = parseVisibility(getVisibilityFallback(inInterface));
    Set<String> modifiers = new HashSet<>();
    boolean found = true;
    while (found) {
      skipWhile(CharFilter.WHITESPACE_FILTER);
      char c = forcePeek();
      if (c == 'a') {
        found = parseModifierKeyword(modifiers, CodeModifiers.KEY_ABSTRACT);
      } else if (c == 'd') {
        found = parseModifierKeyword(modifiers, CodeModifiers.KEY_DEFAULT);
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
            found = parseModifierKeyword(modifiers, CodeModifiers.KEY_SYNCHRONIZED);
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
