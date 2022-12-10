/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.type;

import java.io.IOException;
import java.util.Objects;

import io.github.mmm.code.api.CodePackage;
import io.github.mmm.code.api.block.CodeBlockInitializer;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.copy.CodeCopyType;
import io.github.mmm.code.api.element.CodeElement;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.merge.CodeMergeStrategy;
import io.github.mmm.code.api.merge.CodeMergeStrategyDecider;
import io.github.mmm.code.api.modifier.CodeModifiers;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeType;
import io.github.mmm.code.api.type.CodeTypeCategory;
import io.github.mmm.code.api.type.CodeTypeVariable;
import io.github.mmm.code.base.BaseFile;
import io.github.mmm.code.base.BasePackage;
import io.github.mmm.code.base.block.BaseBlockInitializer;
import io.github.mmm.code.base.element.BaseElement;
import io.github.mmm.code.base.member.BaseConstructors;
import io.github.mmm.code.base.member.BaseFields;
import io.github.mmm.code.base.member.BaseMethods;
import io.github.mmm.code.base.member.BaseProperties;

/**
 * Base implementation of {@link CodeType}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseType extends BaseGenericType implements CodeType {

  private final BaseFile file;

  private final BaseType declaringType;

  private final BaseSuperTypes superTypes;

  private final BaseTypeVariables typeVariables;

  private final BaseFields fields;

  private final BaseMethods methods;

  private final BaseConstructors constructors;

  private final BaseProperties properties;

  private final BaseNestedTypes nestedTypes;

  private final Class<?> reflectiveObject;

  private BaseGenericType qualifiedType;

  private String simpleName;

  private String qualifiedName;

  private CodeModifiers modifiers;

  private CodeTypeCategory category;

  private CodeBlockInitializer staticInitializer;

  private CodeBlockInitializer nonStaticInitializer;

  private CodeType sourceCodeObject;

  /**
   * The constructor for a nested type.
   *
   * @param file the {@link #getFile() file}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param declaringType the {@link #getDeclaringType() declaringType}.
   */
  public BaseType(BaseFile file, String simpleName, BaseType declaringType) {

    this(file, simpleName, declaringType, null);
  }

  /**
   * The constructor for a nested type.
   *
   * @param file the {@link #getFile() file}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}.
   */
  public BaseType(BaseFile file, Class<?> reflectiveObject) {

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
  public BaseType(BaseFile file, String simpleName, BaseType declaringType, Class<?> reflectiveObject) {

    super();
    this.file = file;
    this.declaringType = declaringType;
    if (simpleName == null) {
      assert (declaringType == null);
      this.simpleName = null;
    } else {
      this.simpleName = this.file.getLanguage().verifySimpleName(this, simpleName);
    }
    this.reflectiveObject = reflectiveObject;
    this.superTypes = new BaseSuperTypes(this);
    this.nestedTypes = new BaseNestedTypes(this);
    this.typeVariables = new BaseTypeVariables(this);
    this.fields = new BaseFields(this);
    this.methods = new BaseMethods(this);
    this.constructors = new BaseConstructors(this);
    this.properties = new BaseProperties(this);
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
   * @param template the {@link BaseType} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseType(BaseType template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.file = mapper.map(template.file, CodeCopyType.PARENT);
    this.declaringType = mapper.map(template.declaringType, CodeCopyType.PARENT);
    this.simpleName = mapper.mapName(template.simpleName, template);
    this.reflectiveObject = null;
    this.category = template.category;
    this.staticInitializer = template.staticInitializer;
    this.nonStaticInitializer = template.nonStaticInitializer;
    this.modifiers = template.modifiers;
    this.nestedTypes = template.nestedTypes.copy(mapper);
    this.superTypes = template.superTypes.copy(mapper);
    this.typeVariables = template.typeVariables.copy(mapper);
    this.constructors = template.constructors.copy(mapper);
    this.fields = template.fields.copy(mapper);
    this.methods = template.methods.copy(mapper);
    this.properties = template.properties.copy(mapper);
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    if (this.staticInitializer != null) {
      this.staticInitializer.setImmutable();
    }
    if (this.nonStaticInitializer != null) {
      this.nonStaticInitializer.setImmutable();
    }
    this.constructors.setImmutableIfNotSystemImmutable();
    this.fields.setImmutableIfNotSystemImmutable();
    this.methods.setImmutableIfNotSystemImmutable();
    this.nestedTypes.setImmutableIfNotSystemImmutable();
    this.properties.setImmutableIfNotSystemImmutable();
    this.superTypes.setImmutableIfNotSystemImmutable();
    this.typeVariables.setImmutableIfNotSystemImmutable();
  }

  @Override
  public String getSimpleName() {

    if (this.simpleName == null) {
      return getFile().getSimpleName();
    }
    return this.simpleName;
  }

  /**
   * @return the type name what is the {@link #getSimpleName() simple name} in case of a top-level type, and in case of
   *         a nested type the {@link #getTypeName() type name} of the {@link #getDeclaringType() declaring type}
   *         followed by a package separator and the {@link #getSimpleName() simple name}.
   */
  protected String getTypeName() {

    String typeName = getSimpleName();
    if (this.declaringType != null) {
      typeName = this.declaringType.getTypeName() + getLanguage().getPackageSeparator() + typeName;
    }
    return typeName;
  }

  @Override
  public void setSimpleName(String simpleName) {

    if (this.simpleName == null) {
      getFile().setSimpleName(simpleName);
    } else {
      verifyMutalbe();
      if (this.declaringType != null) {
        this.declaringType.getNestedTypes().rename(this, this.simpleName, simpleName, null);
      }
      this.simpleName = getLanguage().verifySimpleName(this, simpleName);
    }
  }

  @Override
  public String getQualifiedName() {

    if (this.qualifiedName != null) {
      return this.qualifiedName;
    }
    BasePackage pkg = getParentPackage();
    String result;
    String typeName = getTypeName();
    if (pkg.isRoot()) {
      result = typeName;
    } else {
      result = pkg.getQualifiedName() + getLanguage().getPackageSeparator() + typeName;
    }
    if (isImmutable()) {
      this.qualifiedName = result;
    }
    return result;
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
  public CodeTypeCategory getCategory() {

    return this.category;
  }

  @Override
  public void setCategory(CodeTypeCategory category) {

    verifyMutalbe();
    this.category = category;
  }

  @Override
  public BaseType getDeclaringType() {

    if (this.declaringType != null) {
      return this.declaringType;
    }
    return this;
  }

  @Override
  public BaseType resolve(CodeGenericType context) {

    return this;
  }

  @Override
  public BaseType getNonPrimitiveType() {

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
  public CodeBlockInitializer getStaticInitializer() {

    if (this.staticInitializer == null) {
      this.staticInitializer = new BaseBlockInitializer(this);
    }
    return this.staticInitializer;
  }

  @Override
  public void setStaticInitializer(CodeBlockInitializer initializer) {

    verifyMutalbe();
    assert (initializer.isStatic());
    this.staticInitializer = initializer;
  }

  @Override
  public CodeBlockInitializer getNonStaticInitializer() {

    if (this.nonStaticInitializer == null) {
      this.nonStaticInitializer = new BaseBlockInitializer(this);
    }
    return this.nonStaticInitializer;
  }

  @Override
  public void setNonStaticInitializer(CodeBlockInitializer initializer) {

    verifyMutalbe();
    assert (!initializer.isStatic());
    this.nonStaticInitializer = initializer;
  }

  @Override
  public BaseType asType() {

    return this;
  }

  @Override
  public BaseElement getParent() {

    if (this.declaringType != null) {
      return this.declaringType;
    }
    return this.file;
  }

  @Override
  public BasePackage getParentPackage() {

    return this.file.getParentPackage();
  }

  @Override
  public void setParentPackage(CodePackage parentPackage) {

    this.file.setParentPackage(parentPackage);
  }

  @Override
  public Class<?> getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  public BaseFile getFile() {

    return this.file;
  }

  @Override
  public BaseSuperTypes getSuperTypes() {

    return this.superTypes;
  }

  @Override
  public BaseFields getFields() {

    return this.fields;
  }

  @Override
  public BaseMethods getMethods() {

    return this.methods;
  }

  @Override
  public BaseConstructors getConstructors() {

    return this.constructors;
  }

  @Override
  public BaseProperties getProperties() {

    return this.properties;
  }

  @Override
  public BaseNestedTypes getNestedTypes() {

    return this.nestedTypes;
  }

  @Override
  public CodeTypeVariable asTypeVariable() {

    return null;
  }

  @Override
  public BaseTypeVariables getTypeParameters() {

    return this.typeVariables;
  }

  @Override
  public BaseParameterizedType createParameterizedType(CodeElement parent) {

    return new BaseParameterizedType((BaseElement) parent, this);
  }

  @Override
  public BaseGenericType getQualifiedType() {

    if (this.qualifiedType == null) {
      if (getParentPackage().isRoot()) {
        this.qualifiedType = this;
      } else {
        this.qualifiedType = BaseTypeProxy.ofQualified(this);
      }
    }
    return this.qualifiedType;
  }

  @Override
  public CodeType merge(CodeType other, CodeMergeStrategyDecider decider, CodeMergeStrategy parentStrategy) {

    CodeMergeStrategy strategy = decider.decide(this, other, parentStrategy);
    if (strategy == CodeMergeStrategy.OVERRIDE) {
      return other;
    }
    if (strategy.isMerge()) {
      doMerge(other, strategy);
      getNestedTypes().merge(other.getNestedTypes(), decider, strategy);
      getFields().merge(other.getFields(), decider, strategy);
      getConstructors().merge(other.getConstructors(), decider, strategy);
      getMethods().merge(other.getMethods(), decider, strategy);
      getTypeParameters().merge(other.getTypeParameters(), strategy);
      getSuperTypes().merge(other.getSuperTypes(), strategy);
    }
    return this;
  }

  @Override
  public BaseType copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseType copy(CodeCopyMapper mapper) {

    return new BaseType(this, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent,
      CodeLanguage language) throws IOException {

    if (defaultIndent == null) {
      writeReference(sink, true);
      return;
    }
    super.doWrite(sink, newline, defaultIndent, currentIndent, language);
    doWriteDeclaration(sink, currentIndent, language);
    doWriteBody(sink, newline, defaultIndent, currentIndent, language);
  }

  void doWriteBody(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language)
      throws IOException {

    sink.append(" {");
    sink.append(newline);
    String bodyIndent = currentIndent + defaultIndent;
    getFields().write(sink, newline, defaultIndent, bodyIndent);
    if (this.staticInitializer != null && !this.staticInitializer.isEmpty()) {
      sink.append(newline);
      sink.append(bodyIndent);
      this.staticInitializer.write(sink, newline, defaultIndent, currentIndent, language);
    }
    if ((this.nonStaticInitializer != null) && !this.nonStaticInitializer.isEmpty()) {
      sink.append(newline);
      sink.append(bodyIndent);
      this.nonStaticInitializer.write(sink, newline, defaultIndent, currentIndent, language);
    }
    getConstructors().write(sink, newline, defaultIndent, bodyIndent, language);
    getMethods().write(sink, newline, defaultIndent, bodyIndent, language);
    getNestedTypes().write(sink, newline, defaultIndent, currentIndent, language);
    sink.append(currentIndent);
    sink.append("}");
    sink.append(newline);
  }

  void doWriteDeclaration(Appendable sink, String currentIndent, CodeLanguage language) throws IOException {

    sink.append(currentIndent);
    sink.append(this.modifiers.toString());
    sink.append(language.getKeywordForCategory(this.category));
    sink.append(' ');
    writeReference(sink, true);
    getSuperTypes().write(sink, null, null);
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration, Boolean qualified) throws IOException {

    if (Boolean.TRUE.equals(qualified) || ((qualified == null) && isQualified())) {
      sink.append(getQualifiedName());
    } else {
      sink.append(getSimpleName());
    }
    if (declaration) {
      getTypeParameters().write(sink, "", null, "");
    }
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
  public CodeType getSourceCodeObject() {

    if (this.sourceCodeObject == null) {
      BaseFile sourceFile = this.file.getSourceCodeObject();
      if (sourceFile != null) {
        this.sourceCodeObject = sourceFile.getType(getSimpleName(), false);
      }
    }
    return this.sourceCodeObject;
  }

  @Override
  public boolean isBoolean() {

    BaseType booleanType = getContext().getBooleanType(false);
    if (booleanType.equals(this)) {
      return true;
    }
    booleanType = getContext().getBooleanType(true);
    if ((booleanType != null) && (booleanType.equals(this))) {
      return true;
    }
    return false;
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

}
