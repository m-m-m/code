/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.parser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.filter.CharFilter;
import io.github.mmm.code.api.annotation.CodeAnnotation;
import io.github.mmm.code.api.annotation.CodeAnnotations;
import io.github.mmm.code.api.arg.CodeParameter;
import io.github.mmm.code.api.block.CodeBlockInitializer;
import io.github.mmm.code.api.comment.CodeComment;
import io.github.mmm.code.api.element.CodeElement;
import io.github.mmm.code.api.element.CodeElementWithTypeVariables;
import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.api.member.CodeField;
import io.github.mmm.code.api.member.CodeOperation;
import io.github.mmm.code.api.modifier.CodeModifiers;
import io.github.mmm.code.api.statement.CodeStatement;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeTypeCategory;
import io.github.mmm.code.api.type.CodeTypePlaceholder;
import io.github.mmm.code.base.BaseFile;
import io.github.mmm.code.base.arg.BaseExceptions;
import io.github.mmm.code.base.arg.BaseParameters;
import io.github.mmm.code.base.block.BaseBlockBody;
import io.github.mmm.code.base.block.BaseBlockInitializer;
import io.github.mmm.code.base.doc.BaseDocParser;
import io.github.mmm.code.base.element.BaseElement;
import io.github.mmm.code.base.member.BaseConstructor;
import io.github.mmm.code.base.member.BaseConstructors;
import io.github.mmm.code.base.member.BaseField;
import io.github.mmm.code.base.member.BaseMember;
import io.github.mmm.code.base.member.BaseMethod;
import io.github.mmm.code.base.member.BaseOperation;
import io.github.mmm.code.base.statement.BaseTextStatement;
import io.github.mmm.code.base.type.BaseGenericType;
import io.github.mmm.code.base.type.BaseType;
import io.github.mmm.code.base.type.BaseTypeVariable;
import io.github.mmm.code.base.type.BaseTypeVariables;

/**
 * Extends {@link JavaSourceCodeReaderLowlevel} with high-level parsing.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSourceCodeReaderHighlevel extends JavaSourceCodeReaderLowlevel {

  private static final Logger LOG = LoggerFactory.getLogger(JavaSourceCodeReaderHighlevel.class);

  private BaseDocParser docParser;

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
    this.docParser = new BaseDocParser();
  }

  /**
   * @param reader the {@link Reader} to read the source-code from.
   * @param javaFile the {@link BaseFile} to read.
   * @return the parsed {@link BaseType}.
   */
  public BaseType parse(Reader reader, BaseFile javaFile) {

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
    int cp = peek();
    if ((cp == 'p') && expect("package")) {
      skipWhile(CharFilter.WHITESPACE);
      actualPkg = readUntil(';', true).trim();
    }
    String expectedPkg = this.file.getParentPackage().getQualifiedName();
    if (!actualPkg.equals(expectedPkg)) {
      LOG.warn("Expected package '{}' for file '{}' but found package '{}'", expectedPkg, this.file.getSimpleName(),
          actualPkg);
    }

    this.file.setComment(getElementComment());
    this.elementComment = null;
  }

  private void parseImports() {

    consume();
    while (peek() == 'i') {
      if (expect("import")) {
        parseWhitespacesAndComments();
        boolean staticImport = expect("static");
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

    BaseType type;
    do {
      type = parseType(null);
    } while ((type != null) && hasNext());
  }

  private BaseType parseType(BaseType declaringType) {

    consume();
    CodeModifiers modifiers = parseModifiers(false);
    CodeTypeCategory category = parseCategory();
    return parseType(declaringType, modifiers, category);
  }

  private BaseType parseType(BaseType declaringType, CodeModifiers modifiers, CodeTypeCategory category) {

    consume();
    String simpleName = parseIdentifier();
    if (simpleName == null) {
      return null;
    }
    BaseType type = (BaseType) this.file.getType(simpleName, false);
    if (type == null) {
      type = (BaseType) declaringType.getNestedTypes().getDeclaredOrCreate(simpleName);
    }
    type.setCategory(category);
    type.setModifiers(modifiers);
    if (type.isMutable()) {
      type.setComment(getElementComment());
    } else {
      type.setComment(getElementComment());
    }
    parseTypeVariables(type, type);
    this.docParser.parseDoc(type, this.javaDocLines);
    if (!this.annotations.isEmpty()) {
      CodeAnnotations typeAnnotations = type.getAnnotations();
      for (CodeAnnotation annotation : this.annotations) {
        typeAnnotations.add(annotation);
      }
    }
    clearConsumeState();
    parseExtends(type);
    parseImplements(type);
    consume();
    if (!expectOne('{')) {
      String dummy = readUntil('{', true);
      LOG.warn("Garbarge before body in {} for type {}: {}", this.file.getQualifiedName(), simpleName, dummy);
    }
    parseTypeBody(type);
    return type;
  }

  private void parseTypeBody(BaseType type) {

    boolean elementFound;
    do {
      elementFound = parseTypeElement(type);
    } while (elementFound);
    if (!expectOne('}')) {
      String dummy = readUntil('}', true);
      LOG.warn("Garbage at the end of body in {} for type {}: {}", this.file.getQualifiedName(), type.getSimpleName(),
          dummy);
    }
  }

  private boolean parseTypeElement(BaseType type) {

    consume();
    if (peek() == '}') {
      return false;
    }
    CodeModifiers modifiers = parseModifiers(type.isInterface());
    CodeTypeCategory category = parseCategory();
    if (category != null) {
      parseType(type, modifiers, category);
      return true;
    }
    CodeComment memberComment = getElementComment();
    this.elementComment = null;
    List<CodeAnnotation> memberAnnotations = null;
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
      initializer = new BaseBlockInitializer(type, statements);
      if (modifiers.isStatic()) {
        type.setStaticInitializer(initializer);
      } else {
        type.setNonStaticInitializer(initializer);
      }
      return true;
    }
    BaseMember member = null;
    if (name.equals(type.getSimpleName()) && expectOne('(')) { // constructor
      BaseConstructors constructors = type.getConstructors();
      BaseConstructor constructor;
      if (typeVariables == null) {
        constructor = new BaseConstructor(constructors);
      } else {
        constructor = new BaseConstructor(constructors, typeVariables);
      }
      parseWhitespacesAndComments();
      parseOperation(constructor);
      constructors.add(constructor);
      member = constructor;
    } else { // field or method
      CodeElementWithTypeVariables element = type;
      if (typeVariables != null) {
        element = typeVariables;
      }
      CodeGenericType memberType = parseGenericType(name, element, true, true, false);
      consume();
      name = parseIdentifier();
      if (name == null) {
        throw new IllegalStateException();
      }
      parseWhitespacesAndComments();
      if (expectOne('(')) { // method?
        BaseMethod method;
        if (typeVariables == null) {
          method = new BaseMethod(type.getMethods(), name);
        } else {
          method = new BaseMethod(type.getMethods(), name, typeVariables);
        }
        type.getMethods().add(method);
        method.getReturns().setType(memberType);
        parseOperation(method);
        member = method;
      } else {
        BaseField field = type.getFields().add(name);
        field.setType(memberType);
        parseWhitespacesAndComments();
        if (expectOne('=')) {
          field.setInitializer(parseAssignmentValue());
        }
        if (!expectOne(';')) {
          CodeExpression expression = parseAssignmentValue();
          field.setInitializer(expression);
          if (!expectOne(';')) {
            LOG.warn("Missing ;");
          }
        }
        member = field;
      }
    }
    return parseTypeElementForMember(modifiers, memberComment, memberAnnotations, member);
  }

  private boolean parseTypeElementForMember(CodeModifiers modifiers, CodeComment memberComment,
      List<CodeAnnotation> memberAnnotations, BaseMember member) {

    if (member != null) {
      member.setModifiers(modifiers);
      if (memberComment != null) {
        member.setComment(memberComment);
      }
      if (memberAnnotations != null) {
        for (CodeAnnotation annotation : memberAnnotations) {
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

  private void parseOperation(BaseOperation operation) {

    parseOperationArgs(operation);
    parseOperationThrows(operation);
    if ((operation instanceof BaseMethod) && expect("default")) { // default result of annotation...
      requireWhitespace(operation, "default", operation);
      CodeExpression value = parseAssignmentValue();
      ((BaseMethod) operation).setDefaultValue(value);
    }
    parseOperationBody(operation);
  }

  private void parseOperationArgs(BaseOperation operation) {

    BaseParameters parameters = operation.getParameters();
    boolean todo = !(expectOne(')'));
    while (todo) {
      parseWhitespacesAndComments();
      BaseGenericType argType = parseGenericType(operation, true, true, false);
      // TODO: this is all nuts. We might however throw away all this code anyhow...
      skipWhile(CharFilter.WHITESPACE);
      // requireWhitespace(operation, operation.getName(), operation.getParent().getParent());
      String name = parseIdentifier();
      if (name == null) {
        LOG.warn("Missing parameter name for operation {}", operation);
      } else {
        CodeParameter parameter = parameters.add(name);
        parameter.setType(argType);
      }
      todo = !(expectOne(')'));
      if (todo && !expectOne(',')) {
        LOG.warn("Expecting ',' or ')' to terminate signature of operation {} but found '{}' in {}", operation,
            "" + peek(), this.file.getQualifiedName());
      }
    }
  }

  private void parseOperationThrows(BaseOperation operation) {

    parseWhitespacesAndComments();
    if (!expect("throws")) {
      return;
    }
    requireWhitespace(operation, operation.getName(), operation.getParent().getParent());
    BaseExceptions exceptions = operation.getExceptions();
    boolean todo = true;
    while (todo) {
      parseWhitespacesAndComments();
      BaseGenericType exceptionType = parseGenericType(operation, true, true, false);
      exceptions.add(exceptionType);
      parseWhitespacesAndComments();
      todo = expectOne(',');
    }

  }

  private void parseOperationBody(BaseOperation operation) {

    parseWhitespacesAndComments();
    if (expectOne(';')) {
      return;
    }
    List<CodeStatement> statements = parseBlock();
    if (statements == null) {
      LOG.warn("Expecting ';' or '{' to terminate signature of operation {} but found '{}' in {}", operation,
          "" + peek(), this.file.getQualifiedName());
      return;
    }
    operation.setBody(new BaseBlockBody(operation, statements));
  }

  private List<CodeStatement> parseBlock() {

    if (!expectOne('{')) {
      return null;
    }
    skipWhile(CharFilter.WHITESPACE);
    List<CodeStatement> statements = new ArrayList<>();
    // TODO HACK: this only works for formatted source code!
    int braceCount = 1;
    while (braceCount > 0) {
      String statement = readLine(true);
      braceCount += countMatches(statement, '{');
      braceCount -= countMatches(statement, '}');
      if (braceCount > 0) {
        statements.add(new BaseTextStatement(statement));
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

  private JavaTypeVariablesFromSource parseTypeVariables(CodeElementWithTypeVariables element, BaseElement owner) {

    JavaTypeVariablesFromSource result = null;
    if (expectOne('<')) {
      BaseTypeVariables typeVariables;
      if (element == null) {
        result = new JavaTypeVariablesFromSource();
        typeVariables = result;
      } else {
        typeVariables = (BaseTypeVariables) element.getTypeParameters();
      }
      boolean todo = true;
      while (todo) {
        consume();
        String identifier = parseIdentifier();
        BaseTypeVariable typeVariable = new BaseTypeVariable(typeVariables, identifier);
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
        todo = !expectOne('>');
        if (todo) {
          if (!expectOne(',')) {
            LOG.warn("Expecting '>' or ',' to terminate type parameter for {} at {} of {}", identifier, owner,
                this.file.getQualifiedName());
          }
        }
      }
    }
    parseWhitespacesAndComments();
    return result;
  }

  private void parseTypeParameters(JavaGenericTypeFromSource type, CodeElementWithTypeVariables element,
      boolean withTypeParams) {

    if (expectOne('<')) {
      type.ensureTypeParameters();
      boolean todo = true;
      while (todo) {
        parseWhitespacesAndComments();
        BaseGenericType typeParam = parseGenericType(element, withTypeParams, true, false);
        type.addTypeParameter(typeParam);
        parseWhitespacesAndComments();
        todo = !expectOne('>');
        if (todo) {
          if (!expectOne(',')) {
            LOG.warn("Expecting '>' or ',' to terminate type parameter for {} at {} of {}", type.getName(), element,
                this.file.getQualifiedName());
          }
        }
      }
    }
  }

  private void parseImplements(BaseType type) {

    consume();
    boolean found = expect("implements");
    if (!found) {
      return;
    }
    if (type.isAnnotation() || type.isInterface()) {
      LOG.warn("Type {} is {} and cannot use implements keyword.", type, type.getCategory());
    }
    consume();
    parseSuperTypes(type);
  }

  private void parseExtends(BaseType type) {

    consume();
    boolean found = expect("extends");
    if (!found) {
      return;
    }
    if (type.isAnnotation() || type.isEnumeration()) {
      LOG.warn("Type {} is {} and cannot use extends keyword.", type, type.getCategory());
    }
    consume();
    parseSuperTypes(type);
  }

  private void parseSuperTypes(BaseType type) {

    boolean todo = true;
    while (todo) {
      BaseGenericType superType = parseGenericType(type, true, false, false);
      type.getSuperTypes().add(superType);
      parseWhitespacesAndComments();
      todo = expectOne(',');
      parseWhitespacesAndComments();
    }
  }

  private BaseGenericType parseGenericType(CodeElementWithTypeVariables element, boolean withTypeParams,
      boolean withArray, boolean withComposedTypes) {

    String typeName = parseQName();
    if (typeName == null) {
      if (expectOne('?')) {
        typeName = CodeTypePlaceholder.NAME_WILDCARD;
      } else {
        throw new IllegalStateException();
      }
    }
    return parseGenericType(typeName, element, withTypeParams, withArray, withComposedTypes);
  }

  private BaseGenericType parseGenericType(String typeName, CodeElementWithTypeVariables element,
      boolean withTypeParams, boolean withArray, boolean withComposedTypes) {

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
      while (expectOne('&')) {
        parseWhitespacesAndComments();
        BaseGenericType composedType = parseGenericType(element, withTypeParams, false, false);
        type.addComposedType(composedType);
      }
    }
    if (withArray) {
      while (expectOne('[')) {
        parseWhitespacesAndComments();
        if (!expectOne(']')) {
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

  private boolean parseBound(BaseTypeVariable typeVariable, CodeElementWithTypeVariables element) {

    String keyword = "extends";
    if (expect(keyword)) {
      String name = typeVariable.getName();
      requireWhitespace(element, keyword, name);
      BaseGenericType bound = parseGenericType(element, true, true, true);
      if (bound == null) {
        LOG.warn("Missing {} bound after type parameter {} at {} of {}", keyword, name, element,
            this.file.getQualifiedName());
        return false;
      }
      typeVariable.setBound(bound);
      return true;
    }
    return false;
  }

  private void requireWhitespace(CodeElement element, String keyword, Object context) {

    int count = skipWhile(CharFilter.WHITESPACE);
    if (count == 0) {
      // LOG.warn("Missing whitespace after '{}' for {} at {} in {}", keyword, context, element,
      // this.file.getQualifiedName());
      LOG.warn("Missing whitespace after '{}'.", keyword);
    }
  }

  private boolean parseBound(JavaGenericTypeFromSource type, boolean superBound, CodeElementWithTypeVariables element,
      boolean withComposedTypes) {

    String keyword;
    if (superBound) {
      keyword = "super";
    } else {
      keyword = "extends";
    }
    if (expect(keyword)) {
      int count = skipWhile(CharFilter.WHITESPACE);
      if (count == 0) {
        LOG.warn("Missing whitespace after {} expression of type parameter {} at {} of {}", keyword, type.getName(),
            element, this.file.getQualifiedName());
      }
      BaseGenericType bound = parseGenericType(element, true, true, withComposedTypes);
      if (bound == null) {
        LOG.warn("Missing {} bound after type parameter {} at {} of {}", keyword, type.getName(), element,
            this.file.getQualifiedName());
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

  private void applyCommentAndAnnotations(BaseElement element) {

    CodeComment comment = getElementComment();
    if (comment != null) {
      element.setComment(comment);
      this.elementComment = null;
    }
    if (!this.annotations.isEmpty()) {
      for (CodeAnnotation annotation : this.annotations) {
        element.getAnnotations().add(annotation);
      }
      this.annotations.clear();
    }
  }

  private CodeTypeCategory parseCategory() {

    consume();
    int cp = peek();
    if (cp == 'c') {
      if (expect("class")) {
        return CodeTypeCategory.CLASS;
      }
    } else if (cp == 'i') {
      if (expect("interface")) {
        return CodeTypeCategory.INTERFACE;
      }
    } else if (cp == 'e') {
      if (expect("enum")) {
        return CodeTypeCategory.ENUMERAION;
      }
    } else if (cp == 'r') {
      if (expect("record")) {
        return CodeTypeCategory.RECORD;
      }
    } else if (cp == '@') {
      if (expect("@interface")) {
        return CodeTypeCategory.ANNOTATION;
      }
    }
    return null;
  }

}
