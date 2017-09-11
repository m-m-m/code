/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.util.Objects;

import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.statement.CodeStaticBlock;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.api.type.CodeTypeCategory;
import net.sf.mmm.code.impl.java.JavaFile;
import net.sf.mmm.code.impl.java.JavaPackage;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.element.JavaElementWithTypeVariables;
import net.sf.mmm.code.impl.java.item.JavaReflectiveObject;
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
public class JavaType extends JavaGenericType
    implements CodeType, JavaElementWithTypeVariables, CodeNodeItemWithGenericParent<JavaElement, JavaType>, JavaReflectiveObject<Class<?>> {

  private final JavaFile file;

  private final JavaType declaringType;

  private final JavaSuperTypes superTypes;

  private final JavaTypeVariables typeVariables;

  private final JavaFields fields;

  private final JavaMethods methods;

  private final JavaConstructors constructors;

  private final JavaProperties properties;

  private final JavaNestedTypes nestedTypes;

  private final Class<?> reflectiveObject;

  private String simpleName;

  private CodeModifiers modifiers;

  private CodeTypeCategory category;

  private CodeStaticBlock initializer;

  /**
   * The constructor.
   *
   * @param file the {@link #getFile() file}.
   */
  public JavaType(JavaFile file) {

    this(file, null, null, null);
  }

  /**
   * The constructor for a nested type.
   *
   * @param file the {@link #getFile() file}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}.
   */
  public JavaType(JavaFile file, Class<?> reflectiveObject) {

    this(file, null, null, reflectiveObject);
  }

  /**
   * The constructor for a nested type.
   *
   * @param file the {@link #getFile() file}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param declaringType the {@link #getDeclaringType() declaringType}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}.
   */
  public JavaType(JavaFile file, String simpleName, JavaType declaringType, Class<?> reflectiveObject) {

    super();
    this.file = file;
    this.declaringType = declaringType;
    this.simpleName = simpleName;
    this.reflectiveObject = reflectiveObject;
    this.superTypes = new JavaSuperTypes(this);
    this.nestedTypes = new JavaNestedTypes(this);
    this.typeVariables = new JavaTypeVariables(this);
    this.fields = new JavaFields(this);
    this.methods = new JavaMethods(this);
    this.constructors = new JavaConstructors(this);
    this.properties = new JavaProperties(this);
    if (this.reflectiveObject != null) {
      int mods = this.reflectiveObject.getModifiers();
      this.modifiers = CodeModifiers.of(mods);
      this.category = getCategory(this.reflectiveObject);
    } else {
      this.modifiers = CodeModifiers.MODIFIERS_PUBLIC;
      this.category = CodeTypeCategory.CLASS;
    }
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaType} to copy.
   * @param file the {@link #getFile() file}.
   * @param declaringType the {@link #getDeclaringType() declaringType}.
   */
  public JavaType(JavaType template, JavaFile file, JavaType declaringType) {

    super(template);
    this.file = file;
    this.declaringType = declaringType;
    this.simpleName = template.simpleName;
    this.reflectiveObject = null;
    this.category = template.category;
    this.initializer = template.initializer;
    this.modifiers = template.modifiers;
    this.nestedTypes = template.nestedTypes.copy(this);
    this.superTypes = template.superTypes.copy(this);
    this.typeVariables = template.typeVariables.copy(this);
    this.constructors = template.constructors.copy(this);
    this.fields = template.fields.copy(this);
    this.methods = template.methods.copy(this);
    this.properties = template.properties.copy(this);
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.constructors.setImmutable();
    this.fields.setImmutable();
    this.methods.setImmutable();
    this.nestedTypes.setImmutable();
    this.properties.setImmutable();
    this.superTypes.setImmutable();
    this.typeVariables.setImmutable();
  }

  static CodeTypeCategory getCategory(Class<?> clazz) {

    CodeTypeCategory category;
    if (clazz.isInterface()) {
      category = CodeTypeCategory.INTERFACE;
    } else if (clazz.isEnum()) {
      category = CodeTypeCategory.ENUMERAION;
    } else if (clazz.isAnnotation()) {
      category = CodeTypeCategory.ANNOTATION;
    } else {
      category = CodeTypeCategory.CLASS;
    }
    return category;
  }

  @Override
  public JavaElement getParent() {

    if (this.declaringType != null) {
      return this.declaringType;
    }
    return this.file;
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
      if (this.declaringType != null) {
        this.declaringType.getNestedTypes().rename(this, this.simpleName, simpleName, null);
      }
      this.simpleName = simpleName;
    }
  }

  @Override
  public Class<?> getReflectiveObject() {

    return this.reflectiveObject;
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

    return this.superTypes;
  }

  @Override
  public JavaFields getFields() {

    return this.fields;
  }

  @Override
  public JavaMethods getMethods() {

    return this.methods;
  }

  @Override
  public JavaConstructors getConstructors() {

    return this.constructors;
  }

  @Override
  public JavaProperties getProperties() {

    initialize();
    return this.properties;
  }

  @Override
  public JavaType getDeclaringType() {

    return this.declaringType;
  }

  @Override
  public JavaNestedTypes getNestedTypes() {

    initialize();
    return this.nestedTypes;
  }

  @Override
  public JavaType resolve(CodeGenericType context) {

    return this;
  }

  @Override
  public JavaType getNonPrimitiveType() {

    return getContext().getNonPrimitiveType(this);
  }

  @Override
  public boolean isPrimitive() {

    if (getParentPackage().isRoot()) {
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
  public boolean isException() {

    return getContext().getRootExceptionType().isAssignableFrom(this);
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
  public JavaTypeVariables getTypeVariables() {

    return this.typeVariables;
  }

  @Override
  public JavaType copy() {

    return copy(getParent());
  }

  @Override
  public JavaType copy(JavaElement newParent) {

    if (newParent instanceof JavaFile) {
      assert (this.declaringType == null);
      return new JavaType(this, (JavaFile) newParent, null);
    } else if (newParent instanceof JavaType) {
      JavaType parentType = (JavaType) newParent;
      return new JavaType(this, parentType.file, parentType);
    } else {
      throw new IllegalArgumentException("" + newParent);
    }
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    if (currentIndent == null) {
      writeReference(sink, true);
      return;
    }
    super.doWrite(sink, newline, defaultIndent, currentIndent);
    doWriteDeclaration(sink, currentIndent);
    sink.append(" {");
    sink.append(newline);
    String bodyIndent = currentIndent + defaultIndent;
    this.fields.write(sink, newline, defaultIndent, bodyIndent);
    this.constructors.write(sink, newline, defaultIndent, bodyIndent);
    this.methods.write(sink, newline, defaultIndent, bodyIndent);
    this.nestedTypes.write(sink, newline, defaultIndent, currentIndent);
    sink.append(currentIndent);
    sink.append("}");
    sink.append(newline);
  }

  private void doWriteDeclaration(Appendable sink, String currentIndent) throws IOException {

    sink.append(currentIndent);
    sink.append(this.modifiers.toString());
    sink.append(getJavaCategory());
    sink.append(' ');
    writeReference(sink, true);
    getSuperTypes().write(sink, null, null);
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration) throws IOException {

    if (isQualified()) {
      sink.append(getQualifiedName());
    } else {
      sink.append(getSimpleName());
    }
    if (declaration) {
      getTypeVariables().write(sink, DEFAULT_NEWLINE, null, null);
    }
  }

  @Override
  public boolean equals(Object obj) {

    if (obj == this) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    JavaType other = (JavaType) obj;
    if (!Objects.equals(this.simpleName, other.simpleName)) {
      return false;
    }
    if (!Objects.equals(this.category, other.category)) {
      return false;
    }
    if (!Objects.equals(this.modifiers, other.modifiers)) {
      return false;
    }
    if (!Objects.equals(this.file, other.file)) {
      return false;
    }
    if (!Objects.equals(this.declaringType, other.declaringType)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {

    // TODO Auto-generated method stub
    return super.hashCode();
  }

}
