/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.sf.mmm.code.api.CodeGenericType;
import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.CodeType;
import net.sf.mmm.code.api.CodeTypeCategory;
import net.sf.mmm.code.api.CodeTypeVariable;
import net.sf.mmm.code.api.member.CodeConstructor;
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.member.CodeMemberSelector;
import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.member.CodeProperty;
import net.sf.mmm.code.api.member.CodePropertySelector;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.statement.CodeStaticBlock;

/**
 * Implementation of {@link CodePackage} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaType extends JavaElement implements CodeType {

  private final JavaFile file;

  private String simpleName;

  private CodeModifiers modifiers;

  private CodeTypeCategory category;

  private JavaType declaringType;

  private List<CodeGenericType> superTypes;

  private List<CodeGenericType> typeParameters;

  private List<CodeField> fields;

  private List<CodeMethod> methods;

  private List<CodeConstructor> constructors;

  private List<CodeType> nestedTypes;

  private CodeStaticBlock initializer;

  /**
   * The constructor.
   *
   * @param file the {@link #getFile() file}.
   */
  public JavaType(JavaFile file) {

    this(file, null);
  }

  /**
   * The constructor for a nested type.
   *
   * @param file the {@link #getFile() file}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   */
  public JavaType(JavaFile file, String simpleName) {

    super(file.getContext());
    this.file = file;
    this.simpleName = simpleName;
    this.modifiers = CodeModifiers.MODIFIERS_PUBLIC;
    this.category = CodeTypeCategory.CLASS;
    this.superTypes = new ArrayList<>();
    this.typeParameters = new ArrayList<>();
    this.fields = new ArrayList<>();
    this.methods = new ArrayList<>();
    this.constructors = new ArrayList<>();
    this.nestedTypes = new ArrayList<>();
  }

  @Override
  public JavaPackage getParentPackage() {

    return this.file.getParentPackage();
  }

  @Override
  public void setParentPackage(CodePackage parentPackage) {

    this.file.setParentPackage(parentPackage);
  }

  @Override
  public String getSimpleName() {

    if (this.simpleName == null) {
      return this.file.getSimpleName();
    }
    return this.simpleName;
  }

  @Override
  public void setSimpleName(String simpleName) {

    if (this.simpleName == null) {
      this.file.setSimpleName(simpleName);
    } else {
      verifyMutalbe();
      this.simpleName = simpleName;
    }
  }

  @Override
  public CodeModifiers getModifiers() {

    return this.modifiers;
  }

  @Override
  public void setModifiers(CodeModifiers modifiers) {

    Objects.requireNonNull(modifiers, "modifiers");
    verifyMutalbe();
    this.modifiers = modifiers;
  }

  @Override
  public boolean isAssignableFrom(CodeGenericType type) {

    if (equals(type)) {
      return true;
    }
    return false;
  }

  @Override
  public JavaFile getFile() {

    return this.file;
  }

  @Override
  public CodeTypeCategory getCategory() {

    return this.category;
  }

  @Override
  public void setCategory(CodeTypeCategory category) {

    verifyMutalbe();
    this.category = category;
  }

  @Override
  public List<CodeGenericType> getSuperTypes() {

    return this.superTypes;
  }

  /**
   * @return the (first) {@link #isClass() class} of the {@link #getSuperTypes() super types} or {@code null}
   *         if none exists.
   */
  public CodeGenericType getSuperClass() {

    for (CodeGenericType type : this.superTypes) {
      if (type.getRawType().isClass()) {
        return type;
      }
    }
    return null;
  }

  /**
   * @return the {@link List} of {@link #isInterface() interfaces} of the {@link #getSuperTypes() super
   *         types}. May be {@link List#isEmpty() empty} but is never {@code null}.
   */
  public List<CodeGenericType> getSuperInterfaces() {

    return this.superTypes.stream().filter(x -> x.getRawType().isInterface()).collect(Collectors.toList());
  }

  @Override
  public List<CodeGenericType> getTypeParameters() {

    return this.typeParameters;
  }

  @Override
  public List<CodeField> getFields() {

    return this.fields;
  }

  @Override
  public List<CodeField> getFields(CodeMemberSelector selector) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<CodeMethod> getMethods() {

    return this.methods;
  }

  @Override
  public List<CodeMethod> getMethods(CodeMemberSelector selector) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<CodeConstructor> getConstructors() {

    return this.constructors;
  }

  @Override
  public Map<String, CodeProperty> getProperties(CodeMemberSelector memberSelector, CodePropertySelector propertySelector) {

    if (CodePropertySelector.FIELDS.equals(propertySelector)) {
      List<CodeField> selectedFields = getFields(memberSelector);
      Map<String, CodeProperty> map = new HashMap<>(selectedFields.size());
      for (CodeField field : selectedFields) {
        map.put(field.getName(), field);
      }
      return map;
    }
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JavaType getDeclaringType() {

    return this.declaringType;
  }

  @Override
  public void setDeclaringType(CodeType declaringType) {

    verifyMutalbe();
    if (this.declaringType != null) {
      this.declaringType.getNestedTypes().remove(this);
    }
    if (!declaringType.isImmutable()) {
      declaringType.getNestedTypes().add(this);
    }
    this.declaringType = (JavaType) declaringType;
  }

  @Override
  public List<CodeType> getNestedTypes() {

    return this.nestedTypes;
  }

  @Override
  public CodeType getNonPrimitiveType() {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isPrimitive() {

    if (getParentPackage().isDefault()) {
      String name = getSimpleName();
      if (!name.isEmpty()) {
        char firstChar = name.charAt(0);
        if (Character.isLowerCase(firstChar)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public CodeStaticBlock getStaticInitializer() {

    return this.initializer;
  }

  @Override
  public void setStaticInitializer(CodeStaticBlock initializer) {

    verifyMutalbe();
    this.initializer = initializer;
  }

  @Override
  public boolean isVoid() {

    String name = getSimpleName();
    if ("void".equals(name) && isPrimitive()) {
      return true;
    } else if ("Void".equals(name) && getParentPackage().isJavaLang()) {
      return true;
    }
    return false;
  }

  @Override
  public boolean isBoolean() {

    String name = getSimpleName();
    if ("boolean".equals(name) && isPrimitive()) {
      return true;
    } else if ("Boolean".equals(name) && getParentPackage().isJavaLang()) {
      return true;
    }
    return false;
  }

  private String getJavaCategory() {

    if (isAnnotation()) {
      return "@interface";
    } else if (isEnumeration()) {
      return "enum";
    } else {
      return this.category.toString();
    }
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    super.doWrite(sink, defaultIndent, currentIndent);
    doWriteDeclaration(sink, currentIndent);
    doWriteFields(sink, defaultIndent, currentIndent);
    doWriteConstructors(sink, defaultIndent, currentIndent);
    doWriteMethods(sink, defaultIndent, currentIndent);
    doWriteNestedTypes(sink, defaultIndent, currentIndent);
    sink.append(currentIndent);
    sink.append("}");
    writeNewline(sink);
  }

  private void doWriteDeclaration(Appendable sink, String currentIndent) throws IOException {

    sink.append(currentIndent);
    sink.append(this.modifiers.toString());
    sink.append(getJavaCategory());
    sink.append(' ');
    writeReference(sink, true);
    String keywordInherit;
    if (isInterface()) {
      keywordInherit = " extends ";
    } else {
      CodeGenericType superClass = getSuperClass();
      if (superClass != null) {
        sink.append(" extends ");
        superClass.writeReference(sink, false);
      }
      keywordInherit = " implements ";
    }
    String separator = keywordInherit;
    for (CodeGenericType superType : this.superTypes) {
      assert (superType.isInterface());
      sink.append(separator);
      superType.writeReference(sink, false);
      separator = ", ";
    }
    sink.append(" {");
    writeNewline(sink);
  }

  private void doWriteFields(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    doWriteFields(sink, defaultIndent, currentIndent, f -> f.getModifiers().isStatic());
    doWriteFields(sink, defaultIndent, currentIndent, f -> !f.getModifiers().isStatic());
  }

  private void doWriteFields(Appendable sink, String defaultIndent, String currentIndent, Predicate<CodeField> filter) throws IOException {

    for (CodeField field : this.fields) {
      if (filter.test(field)) {
        field.write(sink, defaultIndent, currentIndent);
        writeNewline(sink);
      }
    }
  }

  private void doWriteConstructors(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    for (CodeConstructor constructor : this.constructors) {
      constructor.write(sink, defaultIndent, currentIndent);
      writeNewline(sink);
    }
  }

  private void doWriteMethods(Appendable sink, String defaultIndent, String currentIndent) {

    // TODO Auto-generated method stub

  }

  private void doWriteNestedTypes(Appendable sink, String defaultIndent, String currentIndent) {

    String childIndent = currentIndent + defaultIndent;
    for (CodeType child : this.nestedTypes) {
      writeNewline(sink);
      child.write(sink, defaultIndent, childIndent);
    }
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration) throws IOException {

    CodeTypeVariable typeVariable = getTypeVariable();
    if (typeVariable == null) {
      CodeType rawType = getRawType();
      if (isQualified()) {
        sink.append(rawType.getQualifiedName());
      } else {
        sink.append(rawType.getSimpleName());
      }
      List<CodeGenericType> parameters = getTypeParameters();
      if (!parameters.isEmpty()) {
        String separator = "<";
        for (CodeGenericType variable : parameters) {
          sink.append(separator);
          typeVariable = variable.getTypeVariable();
          if (declaration) {
            typeVariable.write(sink, "", "");
          } else {
            sink.append(typeVariable.getName());
          }
          separator = ", ";
        }
        sink.append('>');
      }
    } else {
      if (declaration) {
        typeVariable.write(sink, "", "");
      } else {
        sink.append(typeVariable.getName());
      }
    }
  }

}
