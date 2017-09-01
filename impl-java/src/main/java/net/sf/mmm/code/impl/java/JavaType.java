/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.member.CodeMemberSelector;
import net.sf.mmm.code.api.member.CodeProperty;
import net.sf.mmm.code.api.member.CodePropertySelector;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.statement.CodeStaticBlock;
import net.sf.mmm.code.impl.java.arg.JavaReturn;
import net.sf.mmm.code.impl.java.member.JavaConstructor;
import net.sf.mmm.code.impl.java.member.JavaField;
import net.sf.mmm.code.impl.java.member.JavaMethod;
import net.sf.mmm.util.collection.base.AbstractIterator;
import net.sf.mmm.util.exception.api.DuplicateObjectException;

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

  private List<JavaGenericType> superTypes;

  private List<JavaGenericType> typeParameters;

  private List<JavaField> fields;

  private Map<String, JavaField> fieldMap;

  private List<JavaMethod> methods;

  private List<JavaConstructor> constructors;

  private List<CodeType> nestedTypes;

  private CodeStaticBlock initializer;

  private Runnable lazyInit;

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
    this.fieldMap = new HashMap<>();
    this.methods = new ArrayList<>();
    this.constructors = new ArrayList<>();
    this.nestedTypes = new ArrayList<>();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaType} to copy.
   */
  public JavaType(JavaType template) {

    super(template);
    template.lazyInit();
    this.category = template.category;
    this.constructors = copy(template.constructors);
    this.declaringType = template.declaringType;
    this.fields = copy(template.fields);
    this.fieldMap = new HashMap<>(this.fields.size());
    for (JavaField field : this.fields) {
      this.fieldMap.put(field.getName(), field);
    }
    this.file = template.file;
    this.initializer = template.initializer; // TODO copy
    this.methods = copy(template.methods);
    this.modifiers = template.modifiers;
    this.nestedTypes = copy(template.nestedTypes);
    this.simpleName = template.simpleName;
    this.superTypes = new ArrayList<>(template.superTypes);
    this.typeParameters = copy(template.typeParameters);
  }

  /**
   * Runs a potential lazy initializer.
   */
  private void lazyInit() {

    if (this.lazyInit != null) {
      this.lazyInit.run();
      this.lazyInit = null;
    }
  }

  @Override
  public void setImmutable() {

    lazyInit();
    super.setImmutable();
  }

  @Override
  protected void verifyMutalbe() {

    lazyInit();
    super.verifyMutalbe();
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
  public List<? extends JavaGenericType> getSuperTypes() {

    lazyInit();
    return this.superTypes;
  }

  /**
   * @return the (first) {@link #isClass() class} of the {@link #getSuperTypes() super types} or {@code null}
   *         if none exists.
   */
  public JavaGenericType getSuperClass() {

    for (JavaGenericType type : getSuperTypes()) {
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
  public List<? extends JavaGenericType> getSuperInterfaces() {

    return getSuperTypes().stream().filter(x -> x.getRawType().isInterface()).collect(Collectors.toList());
  }

  @Override
  public List<? extends JavaGenericType> getTypeParameters() {

    lazyInit();
    return this.typeParameters;
  }

  @Override
  public JavaField getField(String name) {

    lazyInit();
    JavaField field = this.fieldMap.get(name);
    if (field != null) {
      return field;
    }
    JavaGenericType superClass = getSuperClass();
    if (superClass != null) {
      JavaType rawType = superClass.getRawType();
      field = rawType.getField(name);
      if (field != null) {
        return field;
      }
    }
    return null;
  }

  @Override
  public List<? extends JavaField> getDeclaredFields() {

    lazyInit();
    return this.fields;
  }

  @Override
  public Iterable<? extends JavaField> getFields() {

    return () -> new FieldIterator(this);
  }

  @Override
  public CodeField createField(String name, CodeGenericType type) {

    verifyMutalbe();
    if (this.fieldMap.containsKey(name)) {
      throw new DuplicateObjectException(getSimpleName() + ".fields", name);
    }
    JavaField field = new JavaField(this);
    field.setName(name);
    field.setType(type);
    this.fields.add(field);
    this.fieldMap.put(name, field);
    return field;
  }

  @Override
  public List<? extends JavaMethod> getDeclaredMethods() {

    return this.methods;
  }

  @Override
  public Iterable<? extends JavaMethod> getMethods() {

    return () -> new MethodIterator(this);
  }

  @Override
  public JavaMethod createMethod(String name, CodeGenericType returnType) {

    JavaMethod method = new JavaMethod(this);
    method.setName(name);
    JavaReturn returns = new JavaReturn(method);
    returns.setType(returnType);
    method.setReturns(returns);
    return null;
  }

  @Override
  public List<? extends JavaConstructor> getConstructors() {

    return this.constructors;
  }

  @Override
  public JavaConstructor createConstructor() {

    verifyMutalbe();
    JavaConstructor constructor = new JavaConstructor(this);
    this.constructors.add(constructor);
    return constructor;
  }

  @Override
  public Map<String, CodeProperty> getProperties(CodeMemberSelector memberSelector, CodePropertySelector propertySelector) {

    if (CodePropertySelector.FIELDS.equals(propertySelector)) {
      Iterable<? extends JavaField> selectedFields = getFields();
      Map<String, CodeProperty> map = new HashMap<>();
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
  public JavaType getRawType() {

    return this;
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

    for (JavaConstructor constructor : this.constructors) {
      constructor.write(sink, defaultIndent, currentIndent);
      writeNewline(sink);
    }
  }

  private void doWriteMethods(Appendable sink, String defaultIndent, String currentIndent) {

    for (JavaMethod method : this.methods) {
      method.write(sink, defaultIndent, currentIndent);
      writeNewline(sink);
    }
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
      List<? extends JavaGenericType> parameters = getTypeParameters();
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

  private static class FieldIterator extends AbstractIterator<JavaField> {

    private JavaType type;

    private Iterator<JavaField> currentIterator;

    private FieldIterator(JavaType type) {

      super();
      this.type = type;
      this.currentIterator = type.fields.iterator();
      findFirst();
    }

    @Override
    protected JavaField findNext() {

      if (this.currentIterator == null) {
        return null;
      }
      if (this.currentIterator.hasNext()) {
        return this.currentIterator.next();
      }
      JavaGenericType superClass = this.type.getSuperClass();
      if (superClass == null) {
        return null;
      }
      this.type = superClass.getRawType();
      this.currentIterator = this.type.fields.iterator();
      return findNext();
    }
  }

  private static class MethodIterator extends AbstractIterator<JavaMethod> {

    private JavaType type;

    private Iterator<JavaMethod> currentIterator;

    private MethodIterator(JavaType type) {

      super();
      this.type = type;
      this.currentIterator = type.methods.iterator();
      findFirst();
    }

    @Override
    protected JavaMethod findNext() {

      if (this.currentIterator == null) {
        return null;
      }
      if (this.currentIterator.hasNext()) {
        return this.currentIterator.next();
      }
      JavaGenericType superClass = this.type.getSuperClass();
      if (superClass == null) {
        return null;
      }
      this.type = superClass.getRawType();
      this.currentIterator = this.type.methods.iterator();
      return findNext();
    }
  }

}
