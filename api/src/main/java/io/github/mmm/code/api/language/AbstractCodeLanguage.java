/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.language;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.exception.ObjectMismatchException;
import io.github.mmm.code.api.CodeFile;
import io.github.mmm.code.api.CodePackage;
import io.github.mmm.code.api.annotation.CodeAnnotations;
import io.github.mmm.code.api.arg.CodeParameters;
import io.github.mmm.code.api.arg.CodeReturn;
import io.github.mmm.code.api.block.CodeBlockInitializer;
import io.github.mmm.code.api.comment.CodeComment;
import io.github.mmm.code.api.doc.CodeDoc;
import io.github.mmm.code.api.element.CodeElement;
import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.api.expression.CodeVariable;
import io.github.mmm.code.api.item.CodeItem;
import io.github.mmm.code.api.item.CodeItemWithName;
import io.github.mmm.code.api.item.CodeItemWithQualifiedName;
import io.github.mmm.code.api.member.CodeConstructor;
import io.github.mmm.code.api.member.CodeField;
import io.github.mmm.code.api.member.CodeMethod;
import io.github.mmm.code.api.member.CodeOperation;
import io.github.mmm.code.api.modifier.CodeModifiers;
import io.github.mmm.code.api.modifier.CodeVisibility;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeType;
import io.github.mmm.code.api.type.CodeTypeCategory;
import io.github.mmm.code.api.type.CodeTypeVariables;

/**
 * The default implementation of {@link CodeLanguage} (for Java).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractCodeLanguage implements CodeLanguage {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractCodeLanguage.class);

  @Override
  public String verifyName(CodeItemWithName item, String name) {

    Pattern pattern = getNamePattern(item);
    return verifyName(item, pattern, name);
  }

  @Override
  public String verifySimpleName(CodeItemWithQualifiedName item, String simpleName) {

    Pattern pattern;
    if (item instanceof CodePackage) {
      CodePackage parentPackage = ((CodePackage) item).getParentPackage();
      if (parentPackage == null) {
        if (!"".equals(simpleName)) {
          throw new IllegalArgumentException("Root package name must be empty. It can not be '" + simpleName + "'.");
        }
        return simpleName;
      }
      pattern = getSimpleNamePatternForPackage();
    } else if (item instanceof CodeFile) {
      pattern = getSimpleNamePatternForType();
    } else if (item instanceof CodeType) {
      pattern = getSimpleNamePatternForType();
    } else {
      LOG.debug("Unexepcted item type: {}", item);
      return simpleName;
    }
    return verifyName(item, pattern, simpleName);
  }

  /**
   * @param item the {@link CodeItem} to verify.
   * @param pattern the {@link Pattern} defining valid names.
   * @param name the (simple) name to verify.
   * @return the given {@code name} (or potentially a "normalized" name that is valid).
   */
  protected String verifyName(CodeItem item, Pattern pattern, String name) {

    boolean valid = false;
    if (name != null) {
      if (!isTypeInDefaultPackage(item) && isRevervedKeyword(name, item)) {
        throw new ObjectMismatchException(name, "no reserved keyword");
      }
      if (pattern != null) {
        valid = pattern.matcher(name).matches();
      }
    }
    if (!valid) {
      throw new ObjectMismatchException(name, pattern);
    }
    return name;
  }

  private boolean isTypeInDefaultPackage(CodeItem item) { // e.g. primitive type "void"

    if (item instanceof CodeFile) {
      return (((CodeFile) item).getParentPackage().isRoot());
    }
    return false;
  }

  /**
   * @return the {@link Pattern} that defines valid {@link CodePackage} {@link CodePackage#getSimpleName() names}.
   */
  protected abstract Pattern getSimpleNamePatternForPackage();

  /**
   * @return the {@link Pattern} that defines valid {@link CodeType#getSimpleName() names} for {@link CodeType} and
   *         {@link CodeFile}.
   */
  protected abstract Pattern getSimpleNamePatternForType();

  /**
   * @param item the item to {@link #verifyName(CodeItemWithName, String) verify}. May be ignored if pattern and
   *        validation is independent of the item type.
   * @return the {@link Pattern} that defines valid names.
   */
  protected abstract Pattern getNamePattern(CodeItemWithName item);

  /**
   * @param name the name of the {@link CodeItem} to create.
   * @param item the {@link CodeItem} to verify that defines the given {@code name}. Will most probably be ignored but a
   *        language may allow keywords in specific places and forbid in others.
   * @return {@code true} if the given {@code name} is invalid (for the given {@link CodeItem}) because it is a reserved
   *         keyword.
   */
  protected abstract boolean isRevervedKeyword(String name, CodeItem item);

  /**
   * Writes the given {@link CodeDoc}.
   *
   * @param doc the {@link CodeDoc} to write.
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code to.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  public void writeDoc(CodeDoc doc, Appendable sink, String newline, String defaultIndent, String currentIndent)
      throws IOException {

    if (defaultIndent == null) {
      return;
    }
    doc.write(sink, newline, defaultIndent, currentIndent);
  }

  /**
   * Writes the given {@link CodeComment}.
   *
   * @param comment the {@link CodeComment} to write.
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code to.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  public void writeComment(CodeComment comment, Appendable sink, String newline, String defaultIndent,
      String currentIndent) throws IOException {

    if (comment == null) {
      return;
    }
    if (defaultIndent == null) {
      if ("".equals(newline)) {
        return;
      }
      Iterator<? extends String> lines = comment.iterator();
      if (lines.hasNext()) {
        String separator = "/* ";
        while (lines.hasNext()) {
          String line = lines.next();
          sink.append(separator);
          line = line.trim();
          sink.append(line);
          separator = " ";
        }
        sink.append(" */");
      }
    } else {
      comment.write(sink, newline, defaultIndent, currentIndent, this);
    }
  }

  /**
   * Writes the given {@link CodeAnnotations}.
   *
   * @param annotations the {@link CodeAnnotations} to write.
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code to.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  public void writeAnnotations(CodeAnnotations annotations, Appendable sink, String newline, String defaultIndent,
      String currentIndent) throws IOException {

    annotations.write(sink, newline, defaultIndent, currentIndent, this);
  }

  /**
   * Writes the {@link CodeDoc}, {@link CodeComment}, and {@link CodeAnnotations} of the given {@link CodeElement}.
   *
   * @param element the {@link CodeElement} to write.
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code to.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  public void writeElement(CodeElement element, Appendable sink, String newline, String defaultIndent,
      String currentIndent) throws IOException {

    writeDoc(element.getDoc(), sink, newline, defaultIndent, currentIndent);
    writeComment(element.getComment(), sink, newline, defaultIndent, currentIndent);
    writeAnnotations(element.getAnnotations(), sink, newline, defaultIndent, currentIndent);
  }

  @Override
  public void writeDeclaration(CodeVariable variable, Appendable sink) throws IOException {

    variable.writeReference(sink, false);
    sink.append(' ');
    sink.append(variable.getName());
  }

  @Override
  public void writeDeclaration(CodeType type, Appendable sink, String newline, String defaultIndent,
      String currentIndent) throws IOException {

    sink.append(currentIndent);
    sink.append(getTypeModifiers(type).toString());
    CodeTypeCategory category = type.getCategory();
    sink.append(getKeywordForCategory(category));
    sink.append(' ');
    type.writeReference(sink, true);
    if (category == CodeTypeCategory.RECORD) {
      sink.append('(');
      String separator = "";
      for (CodeField field : type.getFields().getDeclared()) {
        sink.append(separator);
        writeDeclaration(field, sink);
        separator = ", ";
      }
      sink.append(')');
    }
    type.getSuperTypes().write(sink, null, null);
  }

  private CodeModifiers getTypeModifiers(CodeType type) {

    CodeModifiers modifiers = type.getModifiers();
    if (type.isAnnotation() || type.isInterface()) {
      modifiers = modifiers.removeModifier(CodeModifiers.KEY_ABSTRACT);
    }
    return modifiers;
  }

  @Override
  public void writeBody(CodeType type, Appendable sink, String newline, String defaultIndent, String currentIndent)
      throws IOException {

    sink.append(" {");
    sink.append(newline);
    String bodyIndent = currentIndent + defaultIndent;
    CodeTypeCategory category = type.getCategory();
    if (category != CodeTypeCategory.RECORD) {
      type.getFields().write(sink, newline, defaultIndent, bodyIndent);
    }
    CodeBlockInitializer staticInitializer = type.getStaticInitializer();
    if (staticInitializer != null && !staticInitializer.isEmpty()) {
      sink.append(newline);
      sink.append(bodyIndent);
      staticInitializer.write(sink, newline, defaultIndent, bodyIndent, this);
    }
    CodeBlockInitializer nonStaticInitializer = type.getNonStaticInitializer();
    if ((nonStaticInitializer != null) && !nonStaticInitializer.isEmpty()) {
      sink.append(newline);
      sink.append(bodyIndent);
      nonStaticInitializer.write(sink, newline, defaultIndent, bodyIndent, this);
    }
    type.getConstructors().write(sink, newline, defaultIndent, bodyIndent, this);
    type.getMethods().write(sink, newline, defaultIndent, bodyIndent, this);
    type.getNestedTypes().write(sink, newline, defaultIndent, bodyIndent, this);
    sink.append(currentIndent);
    sink.append("}");
    sink.append(newline);
  }

  @Override
  public void writeField(CodeField field, Appendable sink, String newline, String defaultIndent, String currentIndent)
      throws IOException {

    CodeType declaringType = field.getDeclaringType();
    CodeTypeCategory category = declaringType.getCategory();
    if ((category == CodeTypeCategory.RECORD) || (category == CodeTypeCategory.INTERFACE)) {
      return;
    }
    writeElement(field, sink, newline, defaultIndent, currentIndent);
    sink.append(currentIndent);
    sink.append(field.getModifiers().toString());
    writeDeclaration(field, sink);
    CodeExpression initializer = field.getInitializer();
    if (initializer != null) {
      sink.append(" = ");
      initializer.write(sink, "", "");
    }
    sink.append(';');
    sink.append(newline);
  }

  @Override
  public void writeMethod(CodeMethod method, Appendable sink, String newline, String defaultIndent,
      String currentIndent) throws IOException {

    writeOperation(method, getMethodModifiers(method), method.getReturns(), sink, newline, defaultIndent,
        currentIndent);
  }

  /**
   * @param operation the {@link CodeOperation} to write.
   * @param modifiers the {@link CodeModifiers} of the operation, potentially transformed.
   * @param opReturn the {@link CodeReturn} of the operation or {@code null} for {@link CodeConstructor}.
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code to.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected void writeOperation(CodeOperation operation, CodeModifiers modifiers, CodeReturn opReturn, Appendable sink,
      String newline, String defaultIndent, String currentIndent) throws IOException {

    writeElement(operation, sink, newline, defaultIndent, currentIndent);
    sink.append(currentIndent);
    sink.append(modifiers.toString());
    if (opReturn != null) {
      sink.append(getMethodKeyword());
    }
    writeTypeVariables(operation.getTypeParameters(), sink, newline, defaultIndent, currentIndent);
    if (opReturn != null) {
      writeTypeReferenceBegin(opReturn.getType(), sink, newline, defaultIndent, currentIndent);
    }
    sink.append(getOperationName(operation));
    writeParameters(operation.getParameters(), sink, newline, defaultIndent, currentIndent);
    if (opReturn != null) {
      writeTypeReferenceEnd(opReturn.getType(), sink, newline, defaultIndent, currentIndent);
    }
    operation.getExceptions().write(sink, newline, null, null, this);
    writeBody(operation, sink, newline, defaultIndent, currentIndent);
  }

  /**
   * @param operation the {@link CodeOperation}.
   * @return the {@link CodeOperation#getName() name} of the given {@link CodeOperation} to be written as source-code.
   *         May also be "constructor" for {@link CodeConstructor} in languages like TypeScript.
   */
  protected String getOperationName(CodeOperation operation) {

    return operation.getName();
  }

  /**
   * Writes the type reference at the beginning. In languages like Java this is the place where type references are
   * written. However, some other languages write the type reference
   * {@link #writeTypeReferenceEnd(CodeGenericType, Appendable, String, String, String) at the end}. Example: In Java we
   * have the type references at the beginning so this has to be implemented in this method:
   *
   * <pre>
   * abstract String method(long parameter);
   * </pre>
   *
   * In TypeScript however, type references are at the end so this method has to be overridden doing nothing and
   * {@link #writeTypeReferenceEnd(CodeGenericType, Appendable, String, String, String)} has to be overridden to append
   * a colon followed by the type:
   *
   * <pre>
   * abstract method(parameter:number):string;
   * </pre>
   *
   * @param type the {@link CodeGenericType} to write.
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code to.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected void writeTypeReferenceBegin(CodeGenericType type, Appendable sink, String newline, String defaultIndent,
      String currentIndent) throws IOException {

    type.writeReference(sink, false);
    sink.append(' ');
  }

  /**
   * @param type the {@link CodeGenericType} to write.
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code to.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   * @see #writeTypeReferenceBegin(CodeGenericType, Appendable, String, String, String)
   */
  protected void writeTypeReferenceEnd(CodeGenericType type, Appendable sink, String newline, String defaultIndent,
      String currentIndent) throws IOException {

  }

  /**
   * @param method the {@link CodeMethod}.
   * @return the {@link CodeMethod#getModifiers() modifiers} of the given {@link CodeMethod} to write as source-code.
   *         However, with potential transformation for this {@link CodeLanguage}.
   */
  protected CodeModifiers getMethodModifiers(CodeMethod method) {

    CodeType declaringType = method.getDeclaringType();
    CodeModifiers modifiers = method.getModifiers();
    if (declaringType.getCategory().isInterfaceOrAnnotation()) {
      if (modifiers.isPublic()) {
        modifiers = modifiers.changeVisibility(CodeVisibility.DEFAULT);
      }
      if (modifiers.isAbstract()) {
        modifiers = modifiers.removeModifier(CodeModifiers.KEY_ABSTRACT);
      }
    } else {
      if (modifiers.isDefaultModifier()) {
        LOG.warn("Method {}.{}: omitting default modifier that is only allowed in interface.",
            declaringType.getQualifiedName(), method.getName());
        modifiers = modifiers.removeModifier(CodeModifiers.KEY_DEFAULT);
      }
    }
    return modifiers;
  }

  @Override
  public void writeConstructor(CodeConstructor constructor, Appendable sink, String newline, String defaultIndent,
      String currentIndent) throws IOException {

    writeOperation(constructor, constructor.getModifiers(), null, sink, newline, defaultIndent, currentIndent);
  }

  /**
   * Writes the given {@link CodeParameters}.
   *
   * @param parameters the {@link CodeParameters} to write.
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code to.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  public void writeParameters(CodeParameters parameters, Appendable sink, String newline, String defaultIndent,
      String currentIndent) throws IOException {

    sink.append('(');
    parameters.write(sink, newline, null, null, this);
    sink.append(')');
  }

  /**
   * Writes the given {@link CodeTypeVariables}.
   *
   * @param typeVariables the {@link CodeTypeVariables} to write.
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code to.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  public void writeTypeVariables(CodeTypeVariables typeVariables, Appendable sink, String newline, String defaultIndent,
      String currentIndent) throws IOException {

    if (!typeVariables.getDeclared().isEmpty()) {
      typeVariables.write(sink, newline, defaultIndent, currentIndent, this);
      sink.append(' ');
    }
  }

  /**
   * Writes the given {@link CodeOperation}.
   *
   * @param operation the {@link CodeOperation} to write.
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code to.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  public void writeBody(CodeOperation operation, Appendable sink, String newline, String defaultIndent,
      String currentIndent) throws IOException {

    if (!operation.canHaveBody() || (defaultIndent == null)) {
      sink.append(getStatementTerminator());
      sink.append(newline);
    } else {
      sink.append(' ');
      operation.getBody().write(sink, newline, defaultIndent, currentIndent, this);
    }
  }

}
