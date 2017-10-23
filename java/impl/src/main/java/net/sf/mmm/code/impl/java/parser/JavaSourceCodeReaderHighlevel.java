/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.parser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.block.CodeBlockInitializer;
import net.sf.mmm.code.api.comment.CodeComment;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.statement.CodeStatement;
import net.sf.mmm.code.api.type.CodeTypeCategory;
import net.sf.mmm.code.api.type.CodeTypePlaceholder;
import net.sf.mmm.code.base.block.GenericBlockBody;
import net.sf.mmm.code.base.block.GenericBlockInitializer;
import net.sf.mmm.code.base.doc.GenericCodeDocParser;
import net.sf.mmm.code.base.statement.GenericTextStatement;
import net.sf.mmm.code.impl.java.JavaFile;
import net.sf.mmm.code.impl.java.annotation.JavaAnnotation;
import net.sf.mmm.code.impl.java.annotation.JavaAnnotations;
import net.sf.mmm.code.impl.java.arg.JavaExceptions;
import net.sf.mmm.code.impl.java.arg.JavaParameter;
import net.sf.mmm.code.impl.java.arg.JavaParameters;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.element.JavaElementWithTypeVariables;
import net.sf.mmm.code.impl.java.member.JavaConstructor;
import net.sf.mmm.code.impl.java.member.JavaConstructors;
import net.sf.mmm.code.impl.java.member.JavaField;
import net.sf.mmm.code.impl.java.member.JavaMember;
import net.sf.mmm.code.impl.java.member.JavaMethod;
import net.sf.mmm.code.impl.java.member.JavaOperation;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.code.impl.java.type.JavaTypeVariable;
import net.sf.mmm.code.impl.java.type.JavaTypeVariables;
import net.sf.mmm.util.filter.api.CharFilter;

/**
 * Extends {@link JavaSourceCodeReaderLowlevel} with high-level parsing.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSourceCodeReaderHighlevel extends JavaSourceCodeReaderLowlevel {

  private static final Logger LOG = LoggerFactory.getLogger(JavaSourceCodeReaderHighlevel.class);

  private GenericCodeDocParser docParser;

  /**
   * The constructor.
   */
  public JavaSourceCodeReaderHighlevel() {

    this(4096);
  }

  /**
   * The constructor.
   *
   * @param capacity the buffer capacity.
   */
  public JavaSourceCodeReaderHighlevel(int capacity) {

    super(capacity);
    this.docParser = new GenericCodeDocParser();
  }

  /**
   * @param reader the {@link Reader} to read the source-code from.
   * @param javaFile the {@link JavaFile} to read.
   * @return the parsed {@link JavaType}.
   */
  public JavaType parse(Reader reader, JavaFile javaFile) {

    if (this.file != null) {
      throw new IllegalStateException();
    }
    setReader(reader);
    this.file = javaFile;
    // parse the source code
    parsePackage();
    parseImports();
    parseTypes();
    // clear
    this.file = null;
    return javaFile.getType();
  }

  private void parsePackage() {

    consume();
    String actualPkg = "";
    char c = forcePeek();
    if ((c == 'p') && expectStrict("package")) {
      skipWhile(CharFilter.WHITESPACE_FILTER);
      actualPkg = readUntil(';', true).trim();
    }
    String expectedPkg = this.file.getParentPackage().getQualifiedName();
    if (!actualPkg.equals(expectedPkg)) {
      LOG.warn("Expected package '{}' for file '{}' but found package '{}'", expectedPkg, this.file.getSimpleName(), actualPkg);
    }
    this.file.setComment(getElementComment());
    this.elementComment = null;
  }

  private void parseImports() {

    consume();
    while (forcePeek() == 'i') {
      if (expectStrict("import")) {
        parseWhitespacesAndComments();
        boolean staticImport = expectStrict("static");
        if (staticImport) {
          parseWhitespacesAndComments();
        }
        String reference = readUntil(';', false);
        this.file.getImports().add(reference, staticImport);
        parseWhitespacesAndComments();
      }
    }
  }

  private void parseTypes() {

    JavaType type;
    do {
      type = parseType(null);
    } while ((type != null) && hasNext());
  }

  private JavaType parseType(JavaType declaringType) {

    consume();
    CodeModifiers modifiers = parseModifiers(false);
    CodeTypeCategory category = parseCategory();
    return parseType(declaringType, modifiers, category);
  }

  private JavaType parseType(JavaType declaringType, CodeModifiers modifiers, CodeTypeCategory category) {

    consume();
    String simpleName = parseIdentifier();
    if (simpleName == null) {
      return null;
    }
    JavaType type = this.file.getType(simpleName, false);
    boolean merge;
    if (type == null) {
      type = new JavaType(this.file, simpleName, declaringType, null);
      this.file.getTypes().add(type);
      merge = false;
    } else {
      merge = (type.getReflectiveObject() != null);
    }
    if (merge) {
      if (type.getCategory() != category) {
        LOG.warn("Cateogry from source '{}' does not match '{}' in {} for type {}", category, type.getCategory(), this.file.getQualifiedName(), simpleName);
      }
      if (!type.getModifiers().equals(modifiers)) {
        LOG.warn("Modifiers from source '{}' do not match '{}' in {} for type {}", category, type.getCategory(), this.file.getQualifiedName(), simpleName);
      }
    } else {
      type.setCategory(category);
      type.setModifiers(modifiers);
    }
    if (type.isMutable()) {
      type.setComment(getElementComment());
    } else {
      type.setComment(getElementComment());
    }
    parseTypeVariables(type, type);
    this.docParser.parseDoc(type, this.javaDocLines);
    if (!this.annotations.isEmpty()) {
      JavaAnnotations typeAnnotations = type.getAnnotations();
      for (JavaAnnotation annotation : this.annotations) {
        typeAnnotations.add(annotation);
      }
    }
    clearConsumeState();
    parseExtends(type);
    parseImplements(type);
    consume();
    if (!expect('{')) {
      String dummy = readUntil('{', true);
      LOG.warn("Garbarge before body in {} for type {}: {}", this.file.getQualifiedName(), simpleName, dummy);
    }
    parseTypeBody(type);
    return type;
  }

  private void parseTypeBody(JavaType type) {

    boolean elementFound;
    do {
      elementFound = parseTypeElement(type);
    } while (elementFound);
    if (!expect('}')) {
      String dummy = readUntil('}', true);
      LOG.warn("Garbarge at the end of body in {} for type {}: {}", this.file.getQualifiedName(), type.getSimpleName(), dummy);
    }
  }

  private boolean parseTypeElement(JavaType type) {

    consume();
    CodeModifiers modifiers = parseModifiers(type.isInterface());
    CodeTypeCategory category = parseCategory();
    if (category != null) {
      parseType(type, modifiers, category);
      return true;
    }
    CodeComment memberComment = getElementComment();
    this.elementComment = null;
    List<JavaAnnotation> memberAnnotations = null;
    if (!this.annotations.isEmpty()) {
      memberAnnotations = new ArrayList<>(this.annotations);
      this.annotations.clear();
    }
    JavaTypeVariablesFromSource typeVariables = parseTypeVariables(null, type);
    String name = parseIdentifier();
    if (name == null) {
      List<CodeStatement> statements = parseBlock();
      if (statements == null) {
        LOG.warn("Invalid member of type {} at {}", type.getSimpleName(), this.file.getQualifiedName());
        return false;
      }
      CodeBlockInitializer initializer;
      if (modifiers.isStatic()) {
        assert (modifiers.getModifiers().size() == 1);
        initializer = type.getStaticInitializer();
      } else {
        assert (modifiers.getModifiers().size() == 0);
        initializer = type.getNonStaticInitializer();
      }
      if (initializer != null) {
        statements.addAll(initializer.getStatements());
      }
      initializer = new GenericBlockInitializer(type, statements);
      if (modifiers.isStatic()) {
        type.setStaticInitializer(initializer);
      } else {
        type.setNonStaticInitializer(initializer);
      }
      return true;
    }
    JavaMember member = null;
    if (name.equals(type.getSimpleName())) { // constructor
      JavaConstructors constructors = type.getConstructors();
      JavaConstructor constructor;
      if (typeVariables == null) {
        constructor = new JavaConstructor(constructors);
      } else {
        constructor = new JavaConstructor(typeVariables, constructors);
      }
      parseWhitespacesAndComments();
      if (!expect('(')) {
        LOG.warn("Constructor {} without opening brace in {}.", name, this.file.getQualifiedName());
        readUntil('(', false);
      }
      parseOperation(constructor);
      constructors.add(constructor);
      member = constructor;
    } else { // field or method
      JavaElementWithTypeVariables element = type;
      if (typeVariables != null) {
        element = typeVariables;
      }
      JavaGenericType memberType = parseGenericType(name, element, true, true, false);
      consume();
      name = parseIdentifier();
      parseWhitespacesAndComments();
      if (expect('(')) { // method?
        JavaMethod method = type.getMethods().add(name);
        method.getReturns().setType(memberType);
        parseOperation(method);
        member = method;
      } else {
        JavaField field = type.getFields().add(name);
        field.setType(memberType);
        parseWhitespacesAndComments();
        if (!expect(';')) {
          CodeExpression expression = parseAssignmentValue();
          field.setInitializer(expression);
          if (!expect(';')) {
            LOG.warn("Missing ;");
          }
        }
        member = field;
      }
    }
    if (member != null) {
      member.setModifiers(modifiers);
      if (memberComment != null) {
        member.setComment(memberComment);
      }
      if (memberAnnotations != null) {
        for (JavaAnnotation annotation : memberAnnotations) {
          member.getAnnotations().add(annotation);
        }
      }
      if (!this.javaDocLines.isEmpty()) {
        if (member instanceof CodeOperation) {
          this.docParser.parseDoc((CodeOperation) member, this.javaDocLines);
        } else {
          this.docParser.parseDoc((CodeField) member, this.javaDocLines);
        }
        this.javaDocLines.clear();
      }
      return true;
    }
    return false;
  }

  private void parseOperation(JavaOperation operation) {

    parseOperationArgs(operation);
    parseOperationThrows(operation);
    parseOperationBody(operation);
  }

  private void parseOperationArgs(JavaOperation operation) {

    JavaParameters parameters = operation.getParameters();
    boolean todo = !(expect(')'));
    while (todo) {
      parseWhitespacesAndComments();
      JavaGenericType argType = parseGenericType(operation, true, true, false);
      requireWhitespace(operation, operation.getName(), operation.getParent().getParent());
      String name = parseIdentifier();
      JavaParameter parameter = parameters.add(name);
      parameter.setType(argType);
      todo = !(expect(')'));
      if (todo && !expect(',')) {
        LOG.warn("Expecting ',' or ')' to terminate signature of operation {} but found '{}' in {}", operation, "" + forcePeek(), this.file.getQualifiedName());
      }
    }
  }

  private void parseOperationThrows(JavaOperation operation) {

    parseWhitespacesAndComments();
    if (!expectStrict("throws")) {
      return;
    }
    requireWhitespace(operation, operation.getName(), operation.getParent().getParent());
    JavaExceptions exceptions = operation.getExceptions();
    boolean todo = true;
    while (todo) {
      parseWhitespacesAndComments();
      JavaGenericType exceptionType = parseGenericType(operation, true, true, false);
      exceptions.add(exceptionType);
      parseWhitespacesAndComments();
      todo = expect(',');
    }

  }

  private void parseOperationBody(JavaOperation operation) {

    parseWhitespacesAndComments();
    if (expect(';')) {
      return;
    }
    List<CodeStatement> statements = parseBlock();
    if (statements == null) {
      LOG.warn("Expecting ';' or '{' to terminate signature of operation {} but found '{}' in {}", operation, "" + forcePeek(), this.file.getQualifiedName());
      return;
    }
    operation.setBody(new GenericBlockBody(operation, statements));
  }

  private List<CodeStatement> parseBlock() {

    if (!expect('{')) {
      return null;
    }
    skipWhile(CharFilter.WHITESPACE_FILTER);
    List<CodeStatement> statements = new ArrayList<>();
    // TODO HACK: this only works for formatted source code!
    int braceCount = 1;
    while (braceCount > 0) {
      String statement = readLine(true);
      braceCount += countMatches(statement, '{');
      braceCount -= countMatches(statement, '}');
      if (braceCount > 0) {
        statements.add(new GenericTextStatement(statement));
      } else {
        assert (statement.equals("}"));
      }
    }
    return statements;
  }

  private static int countMatches(String str, char c) {

    int count = 0;
    int index = 0;
    while ((index = str.indexOf(c, index)) != -1) {
      count++;
      index++;
    }
    return count;
  }

  private JavaTypeVariablesFromSource parseTypeVariables(JavaElementWithTypeVariables element, JavaElement owner) {

    JavaTypeVariablesFromSource result = null;
    if (expect('<')) {
      JavaTypeVariables typeVariables;
      if (element == null) {
        result = new JavaTypeVariablesFromSource();
        typeVariables = result;
      } else {
        typeVariables = element.getTypeParameters();
      }
      boolean todo = true;
      while (todo) {
        consume();
        String identifier = parseIdentifier();
        JavaTypeVariable typeVariable = new JavaTypeVariable(typeVariables, identifier);
        CodeComment comment = getElementComment();
        if (comment != null) {
          typeVariable.setComment(comment);
          this.elementComment = null;
        }
        parseWhitespacesAndComments();
        if (result == null) {
          parseBound(typeVariable, element);
        } else {
          parseBound(typeVariable, result);
        }
        typeVariables.add(typeVariable);
        todo = !expect('>');
        if (todo) {
          if (!expect(',')) {
            LOG.warn("Expecting '>' or ',' to terminate type parameter for {} at {} of {}", identifier, owner, this.file.getQualifiedName());
          }
        }
      }
    }
    return result;
  }

  private void parseTypeParameters(JavaGenericTypeFromSource type, JavaElementWithTypeVariables element, boolean withTypeParams) {

    if (expect('<')) {
      type.ensureTypeParameters();
      boolean todo = true;
      while (todo) {
        parseWhitespacesAndComments();
        JavaGenericType typeParam = parseGenericType(element, withTypeParams, true, false);
        type.addTypeParameter(typeParam);
        parseWhitespacesAndComments();
        todo = !expect('>');
        if (todo) {
          if (!expect(',')) {
            LOG.warn("Expecting '>' or ',' to terminate type parameter for {} at {} of {}", type.getName(), element, this.file.getQualifiedName());
          }
        }
      }
    }
  }

  private void parseImplements(JavaType type) {

    consume();
    boolean found = expectStrict("implements");
    if (!found) {
      return;
    }
    if (type.isAnnotation() || type.isInterface()) {
      LOG.warn("Type {} is {} and cannot use implements keyword.", type, type.getCategory());
    }
    consume();
    parseSuperTypes(type);
  }

  private void parseExtends(JavaType type) {

    consume();
    boolean found = expectStrict("extends");
    if (!found) {
      return;
    }
    if (type.isAnnotation() || type.isEnumeration()) {
      LOG.warn("Type {} is {} and cannot use extends keyword.", type, type.getCategory());
    }
    consume();
    parseSuperTypes(type);
  }

  private void parseSuperTypes(JavaType type) {

    boolean todo = true;
    while (todo) {
      JavaGenericType superType = parseGenericType(type, true, false, false);
      type.getSuperTypes().add(superType);
      parseWhitespacesAndComments();
      todo = expect(',');
      parseWhitespacesAndComments();
    }
  }

  private JavaGenericType parseGenericType(JavaElementWithTypeVariables element, boolean withTypeParams, boolean withArray, boolean withComposedTypes) {

    String typeName = parseQName();
    if (typeName == null) {
      if (expect('?')) {
        typeName = CodeTypePlaceholder.NAME_WILDCARD;
      } else {
        throw new IllegalStateException();
      }
    }
    return parseGenericType(typeName, element, withTypeParams, withArray, withComposedTypes);
  }

  private JavaGenericType parseGenericType(String typeName, JavaElementWithTypeVariables element, boolean withTypeParams, boolean withArray,
      boolean withComposedTypes) {

    JavaGenericTypeFromSource type = new JavaGenericTypeFromSource(element, typeName, this.file);
    parseWhitespacesAndComments();
    applyCommentAndAnnotations(type);
    if (CodeTypePlaceholder.NAME_WILDCARD.equals(typeName)) {
      boolean found = parseBound(type, false, element, true);
      if (!found) {
        parseBound(type, true, element, true);
      }
    }
    if (withTypeParams) {
      parseTypeParameters(type, element, withTypeParams);
    }
    if (withComposedTypes) {
      while (expect('&')) {
        parseWhitespacesAndComments();
        JavaGenericType composedType = parseGenericType(element, withTypeParams, false, false);
        type.addComposedType(composedType);
      }
    }
    if (withArray) {
      while (expect('[')) {
        parseWhitespacesAndComments();
        if (!expect(']')) {
          String text = readUntil(']', false); // TODO actually parse expression here instead of string
          if (text == null) {
            LOG.error("Unterminated array after {} at {} of {}", typeName, element, this.file.getQualifiedName());
            return type;
          }
          text = text.trim();
          if (!text.isEmpty()) {
            type.setArrayLengthExpression(text);
          }
        }
        type.incArrayCount();
        parseWhitespacesAndComments();
      }
    }
    return type;
  }

  private boolean parseBound(JavaTypeVariable typeVariable, JavaElementWithTypeVariables element) {

    String keyword = "extends";
    if (expectStrict(keyword)) {
      String name = typeVariable.getName();
      requireWhitespace(element, keyword, name);
      JavaGenericType bound = parseGenericType(element, true, true, true);
      if (bound == null) {
        LOG.warn("Missing {} bound after type parameter {} at {} of {}", keyword, name, element, this.file.getQualifiedName());
        return false;
      }
      typeVariable.setBound(bound);
      return true;
    }
    return false;
  }

  private void requireWhitespace(JavaElement element, String keyword, Object context) {

    int count = skipWhile(CharFilter.WHITESPACE_FILTER);
    if (count == 0) {
      LOG.warn("Missing whitespace after '{}' for {} at {} in {}", keyword, context, element, this.file.getQualifiedName());
    }
  }

  private boolean parseBound(JavaGenericTypeFromSource type, boolean superBound, JavaElementWithTypeVariables element, boolean withComposedTypes) {

    String keyword;
    if (superBound) {
      keyword = "super";
    } else {
      keyword = "extends";
    }
    if (expectStrict(keyword)) {
      int count = skipWhile(CharFilter.WHITESPACE_FILTER);
      if (count == 0) {
        LOG.warn("Missing whitespace after {} expression of type parameter {} at {} of {}", keyword, type.getName(), element, this.file.getQualifiedName());
      }
      JavaGenericType bound = parseGenericType(element, true, true, withComposedTypes);
      if (bound == null) {
        LOG.warn("Missing {} bound after type parameter {} at {} of {}", keyword, type.getName(), element, this.file.getQualifiedName());
        return false;
      }
      if (superBound) {
        type.setSuperBound(bound);
      } else {
        type.setExtendsBound(bound);
      }
      return true;
    }
    return false;
  }

  private void applyCommentAndAnnotations(JavaElement element) {

    CodeComment comment = getElementComment();
    if (comment != null) {
      element.setComment(comment);
      this.elementComment = null;
    }
    if (!this.annotations.isEmpty()) {
      for (JavaAnnotation annotation : this.annotations) {
        element.getAnnotations().add(annotation);
      }
      this.annotations.clear();
    }
  }

  private CodeTypeCategory parseCategory() {

    consume();
    char c = forcePeek();
    if (c == 'c') {
      if (expectStrict("class")) {
        return CodeTypeCategory.CLASS;
      }
    } else if (c == 'i') {
      if (expectStrict("interface")) {
        return CodeTypeCategory.INTERFACE;
      }
    } else if (c == 'e') {
      if (expectStrict("enum")) {
        return CodeTypeCategory.ENUMERAION;
      }
    } else if (c == '@') {
      if (expectStrict("@interface")) {
        return CodeTypeCategory.ANNOTATION;
      }
    }
    return null;
  }

}
