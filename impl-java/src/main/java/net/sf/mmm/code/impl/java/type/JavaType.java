/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.sf.mmm.code.api.CodeGenericType;
import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.member.CodeProperties;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.statement.CodeStaticBlock;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.api.type.CodeTypeCategory;
import net.sf.mmm.code.api.type.CodeTypeVariables;
import net.sf.mmm.code.impl.java.JavaFile;
import net.sf.mmm.code.impl.java.JavaPackage;
import net.sf.mmm.code.impl.java.member.JavaConstructors;
import net.sf.mmm.code.impl.java.member.JavaFields;
import net.sf.mmm.code.impl.java.member.JavaMethods;
import net.sf.mmm.code.impl.java.member.JavaProperties;

/**
 * Implementation of {@link CodeType} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaType extends JavaGenericType implements CodeType {

  private final JavaFile file;

  private String simpleName;

  private CodeModifiers modifiers;

  private CodeTypeCategory category;

  private JavaType declaringType;

  private JavaSuperTypes superTypes;

  private JavaTypeVariables typeVariables;

  private JavaFields fields;

  private JavaMethods methods;

  private JavaConstructors constructors;

  private JavaProperties properties;

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
    this.superTypes = new JavaSuperTypes(this);
    this.typeVariables = new JavaTypeVariables(this);
    this.fields = new JavaFields(this);
    this.methods = new JavaMethods(this);
    this.constructors = new JavaConstructors(this);
    this.nestedTypes = new ArrayList<>();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaType} to copy.
   */
  public JavaType(JavaType template) {

    super(template);
    this.category = template.category;
    this.constructors = copy(template.constructors);
    this.declaringType = template.declaringType;
    this.fields = copy(template.fields);
    this.file = template.file;
    this.initializer = copy(template.initializer);
    this.methods = copy(template.methods);
    this.modifiers = template.modifiers;
    this.nestedTypes = copy(template.nestedTypes);
    this.simpleName = template.simpleName;
    this.superTypes = copy(template.superTypes);
    this.typeVariables = copy(template.typeVariables);
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
    // TODO implement this properly (tricky one)
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
  public JavaSuperTypes getSuperTypes() {

    lazyInit();
    return this.superTypes;
  }

  @Override
  public JavaFields getFields() {

    lazyInit();
    return this.fields;
  }

  @Override
  public JavaMethods getMethods() {

    lazyInit();
    return this.methods;
  }

  @Override
  public JavaConstructors getConstructors() {

    lazyInit();
    return this.constructors;
  }

  @Override
  public CodeProperties getProperties() {

    lazyInit();
    return this.properties;
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
  public JavaType asType() {

    return this;
  }

  @Override
  public JavaTypeVariable asTypeVariable() {

    return null;
  }

  @Override
  public CodeTypeVariables getTypeVariables() {

    return this.typeVariables;
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    super.doWrite(sink, defaultIndent, currentIndent);
    doWriteDeclaration(sink, currentIndent);
    sink.append(" {");
    writeNewline(sink);
    String bodyIndent = currentIndent + defaultIndent;
    this.fields.write(sink, defaultIndent, bodyIndent);
    this.constructors.write(sink, defaultIndent, bodyIndent);
    this.methods.write(sink, defaultIndent, bodyIndent);
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
    getSuperTypes().write(sink, "", "");
  }

  private void doWriteNestedTypes(Appendable sink, String defaultIndent, String currentIndent) {

    String childIndent = currentIndent + defaultIndent;
    for (CodeType child : this.nestedTypes) {
      writeNewline(sink);
      child.write(sink, defaultIndent, childIndent);
    }
  }

}
