/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.parser;

import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.type.CodeTypeCategory;
import net.sf.mmm.code.base.doc.GenericCodeDocParser;
import net.sf.mmm.code.impl.java.JavaCodeLoader;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.JavaFile;
import net.sf.mmm.code.impl.java.element.JavaElementWithTypeVariables;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.code.impl.java.type.JavaTypeVariable;
import net.sf.mmm.util.filter.api.CharFilter;

/**
 * Extends {@link JavaSourceCodeReaderLowlevel} with high-level parsing.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSourceCodeReaderHighlevel extends JavaSourceCodeReaderLowlevel {

  private static final Logger LOG = LoggerFactory.getLogger(JavaSourceCodeReaderHighlevel.class);

  private JavaCodeLoader byteCodeLoader;

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
   * @param byteLoader the optional {@link JavaCodeLoader} to load the according byte-code. May be
   *        {@code null}. Otherwise it has to be used to load all {@link JavaType}s instead of creating them
   *        manually in the given {@link JavaFile}.
   * @return the parsed {@link JavaType}.
   */
  public JavaType parse(Reader reader, JavaFile javaFile, JavaCodeLoader byteLoader) {

    if (this.file != null) {
      throw new IllegalStateException();
    }
    setReader(reader);
    this.file = javaFile;
    this.byteCodeLoader = byteLoader;
    // parse the source code
    parsePackage();
    parseImports();
    parseTypes();
    // clear
    this.file = null;
    this.byteCodeLoader = null;
    return javaFile.getType();
  }

  private void parsePackage() {

    char c = consume();
    String actualPkg = "";
    if ((c == 'p') && expectStrict("package")) {
      skipWhile(CharFilter.WHITESPACE_FILTER);
      actualPkg = readUntil(';', true).trim();
    }
    String expectedPkg = this.file.getParentPackage().getQualifiedName();
    if (!actualPkg.equals(expectedPkg)) {
      LOG.warn("Expected package '{}' for file '{}' but found package '{}'", expectedPkg, this.file.getSimpleName(), actualPkg);
    }
    this.file.setComment(getElementComment());
  }

  private void parseImports() {

    char c = consume();
    while (c == 'i') {
      if (expectStrict("import")) {
        parseWhitespacesAndComments();
        boolean staticImport = expectStrict("static");
        if (staticImport) {
          parseWhitespacesAndComments();
        }

      }
    }
  }

  private void parseTypes() {

    JavaType type;
    String namePrefix = this.file.getParentPackage().getQualifiedName();
    if (!namePrefix.isEmpty()) {
      namePrefix = namePrefix + ".";
    }
    do {
      type = parseType(null, namePrefix);
    } while (type != null);
  }

  private JavaType parseType(JavaType declaringType, String namePrefix) {

    consume();
    CodeModifiers modifiers = parseModifiers(false);
    CodeTypeCategory category = parseCategory();
    consume();
    String simpleName = parseIdentifier();
    String qualifiedName = namePrefix + simpleName;
    JavaType type = null;
    if (this.byteCodeLoader != null) {
      type = this.byteCodeLoader.getType(qualifiedName);
    }
    boolean merge = (type != null);
    if (type == null) {
      if (simpleName.equals(this.file.getSimpleName())) {
        type = this.file.getType();
      } else {
        type = new JavaType(this.file, simpleName, declaringType, null);
      }
    }
    if (merge) {
      if (type.getCategory() != category) {
        LOG.warn("Cateogry from source '{}' does not match '{}' in {}", category, type.getCategory(), qualifiedName);
      }
      if (!type.getModifiers().equals(modifiers)) {
        LOG.warn("Modifiers from source '{}' do not match '{}' in {}", category, type.getCategory(), qualifiedName);
      }
    } else {
      type.setCategory(category);
      type.setModifiers(modifiers);
    }
    type.setComment(getElementComment());
    parseTypeVariables(type);
    this.docParser.applyDoc(type, this.javaDocLines);
    clearConsumeState();
    if (category.isClass() || category.isInterface()) {
      parseExtends(type);
    }
    if (category.isClass() || category.isEnumeration()) {
      parseImplements(type);
    }
    char c = consume();
    if (c != '{') {
      String dummy = readUntil('{', true);
      LOG.warn("Garbarge before body of type {}: {}", qualifiedName, dummy);
    }

    return type;
  }

  private void parseTypeVariables(JavaType type) {

    // TODO
  }

  private void parseImplements(JavaType type) {

    parseSuperTypes(type);
  }

  private void parseExtends(JavaType type) {

    parseSuperTypes(type);
  }

  private void parseSuperTypes(JavaType type) {

    boolean todo = true;
    while (todo) {
      JavaGenericType superType = parseGenericType(null);
      type.getSuperTypes().add(superType);
      parseWhitespacesAndComments();
      todo = expect(',');
      parseWhitespacesAndComments();
    }
  }

  private JavaGenericType parseGenericType(JavaElementWithTypeVariables element) {

    String typeName = parseQName();
    if (element != null) {
      JavaTypeVariable typeVariable = element.getTypeParameters().get(typeName, true);
      if (typeVariable != null) {
        return typeVariable;
      }
    }
    JavaContext context = this.file.getContext();
    if (typeName.indexOf('.') > 0) {
      typeName = context.getQualifiedName(typeName, this.file, false);
    }
    JavaType type = context.getType(typeName);
    if (type == null) {
      // TODO create getOrCreateType method and use above instead
      throw new IllegalStateException(typeName);
    }
    parseWhitespacesAndComments();
    if (forcePeek() == '<') {
      next();
      // TODO read
    }
    return type;
  }

  private CodeTypeCategory parseCategory() {

    char c = consume();
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
