/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.mmm.code.api.type.CodeNestedTypes;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.api.type.CodeTypeVariables;
import net.sf.mmm.code.impl.java.item.JavaItemContainerWithInheritance;
import net.sf.mmm.util.exception.api.DuplicateObjectException;

/**
 * Implementation of {@link CodeTypeVariables} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaNestedTypes extends JavaItemContainerWithInheritance<CodeType> implements CodeNestedTypes {

  private List<JavaType> nestedTypes;

  private Map<String, JavaType> nestedTypeMap;

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaNestedTypes(JavaType declaringType) {

    super(declaringType);
    this.nestedTypes = new ArrayList<>();
    this.nestedTypeMap = new HashMap<>();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaNestedTypes} to copy.
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaNestedTypes(JavaNestedTypes template, JavaType declaringType) {

    super(template, declaringType);
    this.nestedTypes = doCopy(template.nestedTypes, declaringType);
    this.nestedTypeMap = new HashMap<>(this.nestedTypes.size());
    for (JavaType nestedType : this.nestedTypes) {
      this.nestedTypeMap.put(nestedType.getSimpleName(), nestedType);
    }
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.nestedTypes = Collections.unmodifiableList(this.nestedTypes);
    this.nestedTypeMap = Collections.unmodifiableMap(this.nestedTypeMap);
  }

  @Override
  public List<? extends JavaType> getDeclared() {

    return this.nestedTypes;
  }

  @Override
  public Iterable<? extends JavaType> getAll() {

    List<JavaType> list = new ArrayList<>(this.nestedTypes);
    collectNestedTypes(list);
    return list;
  }

  private void collectNestedTypes(List<JavaType> list) {

    for (JavaType nested : this.nestedTypes) {
      JavaNestedTypes nestedContainer = nested.getNestedTypes();
      list.addAll(nestedContainer.nestedTypes);
      nestedContainer.collectNestedTypes(list);
    }
  }

  @Override
  public JavaType get(String name) {

    JavaType nestedType = this.nestedTypeMap.get(name);
    if (nestedType != null) {
      return nestedType;
    }
    for (JavaType nested : this.nestedTypes) {
      nestedType = nested.getNestedTypes().get(name);
      if (nestedType != null) {
        return nestedType;
      }
    }
    return null;
  }

  @Override
  public CodeType getDeclared(String name) {

    return this.nestedTypeMap.get(name);
  }

  @Override
  public JavaType add(String name) {

    verifyMutalbe();
    if (this.nestedTypeMap.containsKey(name)) {
      throw new DuplicateObjectException(JavaTypeVariable.class, name);
    }
    JavaType nestedType = new JavaType(getDeclaringType().getFile(), name, getDeclaringType());
    this.nestedTypes.add(nestedType);
    this.nestedTypeMap.put(name, nestedType);
    return nestedType;
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    if (this.nestedTypes.isEmpty()) {
      return;
    }
    String childIndent = currentIndent + defaultIndent;
    for (JavaType nestedType : this.nestedTypes) {
      writeNewline(sink);
      nestedType.write(sink, defaultIndent, childIndent);
    }
  }

  @Override
  public JavaNestedTypes copy(CodeType newDeclaringType) {

    return new JavaNestedTypes(this, (JavaType) newDeclaringType);
  }

}
