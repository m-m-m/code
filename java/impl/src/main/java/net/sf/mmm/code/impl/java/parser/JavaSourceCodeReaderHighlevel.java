/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.parser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.annotation.CodeAnnotation;
import net.sf.mmm.code.api.annotation.CodeAnnotations;
import net.sf.mmm.code.api.arg.CodeParameter;
import net.sf.mmm.code.api.block.CodeBlockInitializer;
import net.sf.mmm.code.api.comment.CodeComment;
import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.element.CodeElementWithTypeVariables;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.statement.CodeStatement;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeTypeCategory;
import net.sf.mmm.code.api.type.CodeTypePlaceholder;
import net.sf.mmm.code.base.BaseFile;
import net.sf.mmm.code.base.arg.BaseExceptions;
import net.sf.mmm.code.base.arg.BaseParameters;
import net.sf.mmm.code.base.block.BaseBlockBody;
import net.sf.mmm.code.base.block.BaseBlockInitializer;
import net.sf.mmm.code.base.doc.BaseDocParser;
import net.sf.mmm.code.base.element.BaseElement;
import net.sf.mmm.code.base.member.BaseConstructor;
import net.sf.mmm.code.base.member.BaseConstructors;
import net.sf.mmm.code.base.member.BaseField;
import net.sf.mmm.code.base.member.BaseMember;
import net.sf.mmm.code.base.member.BaseMethod;
import net.sf.mmm.code.base.member.BaseOperation;
import net.sf.mmm.code.base.statement.BaseTextStatement;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeVariable;
import net.sf.mmm.code.base.type.BaseTypeVariables;
import net.sf.mmm.util.filter.api.CharFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  /**
   * @param reader the {@link Reader} to read the source-code from.
   * @param javaFile the {@link BaseFile} to read.
   * @return the full qualified name of the Java file.
   */
  public String parseQualifiedName(Reader reader, BaseFile javaFile) {

    if (this.file != null) {
      throw new IllegalStateException();
    }
    setReader(reader);
    this.file = javaFile;
    String actualPkg = getActualPackage();
    String simpleName = this.file.getSimpleName();

    return actualPkg + "." + simpleName;
  }

  private void parsePackage() {

    String actualPkg = getActualPackage();
    String expectedPkg = this.file.getParentPackage().getQualifiedName();
    if (!actualPkg.equals(expectedPkg)) {
      LOG.warn("Expected package '{}' for file '{}' but found package '{}'", expectedPkg, this.file.getSimpleName(),
          actualPkg);
    }

    this.file.setComment(getElementComment());
    this.elementComment = null;
  }

  /**
   * Reads the Java file in order to find the package.
   *
   * @return the parsed package of the file
   */
  private String getActualPackage() {

    consume();
    String actualPkg = "";
    char c = forcePeek();
    if ((c == 'p') && expectStrict("package")) {
      skipWhile(CharFilter.WHITESPACE_FILTER);
      actualPkg = readUntil(';', true).trim();
    }
    return actualPkg;
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
      type = new BaseType(this.file, simpleName, declaringType, null);
      this.file.getTypes().add(type);
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
    if (!expect('{')) {
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
    if (!expect('}')) {
      String dummy = readUntil('}', true);
      LOG.warn("Garbarge at the end of body in {} for type {}: {}", this.file.getQualifiedName(), type.getSimpleName(),
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
    if (name.equals(type.getSimpleName()) && expect('(')) { // constructor
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
      if (expect('(')) { // method?
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
        if (expect('=')) {
          field.setInitializer(parseAssignmentValue());
        }
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
    boolean todo = !(expect(')'));
    while (todo) {
      parseWhitespacesAndComments();
      BaseGenericType argType = parseGenericType(operation, true, true, false);
      // TODO: this is all nuts. We might however throw away all this code anyhow...
      skipWhile(CharFilter.WHITESPACE_FILTER);
      // requireWhitespace(operation, operation.getName(), operation.getParent().getParent());
      String name = parseIdentifier();
      if (name == null) {
        LOG.warn("Missing parameter name for operation {}", operation);
      } else {
        CodeParameter parameter = parameters.add(name);
        parameter.setType(argType);
      }
      todo = !(expect(')'));
      if (todo && !expect(',')) {
        LOG.warn("Expecting ',' or ')' to terminate signature of operation {} but found '{}' in {}", operation,
            "" + forcePeek(), this.file.getQualifiedName());
      }
    }
  }

  private void parseOperationThrows(BaseOperation operation) {

    parseWhitespacesAndComments();
    if (!expectStrict("throws")) {
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
      todo = expect(',');
    }

  }

  private void parseOperationBody(BaseOperation operation) {

    parseWhitespacesAndComments();
    if (expect(';')) {
      return;
    }
    List<CodeStatement> statements = parseBlock();
    if (statements == null) {
      LOG.warn("Expecting ';' or '{' to terminate signature of operation {} but found '{}' in {}", operation,
          "" + forcePeek(), this.file.getQualifiedName());
      return;
    }
    operation.setBody(new BaseBlockBody(operation, statements));
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
    if (expect('<')) {
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
        todo = !expect('>');
        if (todo) {
          if (!expect(',')) {
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

    if (expect('<')) {
      type.ensureTypeParameters();
      boolean todo = true;
      while (todo) {
        parseWhitespacesAndComments();
        BaseGenericType typeParam = parseGenericType(element, withTypeParams, true, false);
        type.addTypeParameter(typeParam);
        parseWhitespacesAndComments();
        todo = !expect('>');
        if (todo) {
          if (!expect(',')) {
            LOG.warn("Expecting '>' or ',' to terminate type parameter for {} at {} of {}", type.getName(), element,
                this.file.getQualifiedName());
          }
        }
      }
    }
  }

  private void parseImplements(BaseType type) {

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

  private void parseExtends(BaseType type) {

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

  private void parseSuperTypes(BaseType type) {

    boolean todo = true;
    while (todo) {
      BaseGenericType superType = parseGenericType(type, true, false, false);
      type.getSuperTypes().add(superType);
      parseWhitespacesAndComments();
      todo = expect(',');
      parseWhitespacesAndComments();
    }
  }

  private BaseGenericType parseGenericType(CodeElementWithTypeVariables element, boolean withTypeParams,
      boolean withArray, boolean withComposedTypes) {

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
      while (expect('&')) {
        parseWhitespacesAndComments();
        BaseGenericType composedType = parseGenericType(element, withTypeParams, false, false);
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

  private boolean parseBound(BaseTypeVariable typeVariable, CodeElementWithTypeVariables element) {

    String keyword = "extends";
    if (expectStrict(keyword)) {
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

    int count = skipWhile(CharFilter.WHITESPACE_FILTER);
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
    if (expectStrict(keyword)) {
      int count = skipWhile(CharFilter.WHITESPACE_FILTER);
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
