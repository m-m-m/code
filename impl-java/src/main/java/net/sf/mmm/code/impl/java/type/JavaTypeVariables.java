/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.api.type.CodeTypeVariable;
import net.sf.mmm.code.api.type.CodeTypeVariables;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.item.JavaItemContainerWithInheritance;
import net.sf.mmm.code.impl.java.member.JavaOperation;
import net.sf.mmm.util.exception.api.DuplicateObjectException;

/**
 * Implementation of {@link CodeTypeVariables} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaTypeVariables extends JavaItemContainerWithInheritance<CodeTypeVariable> implements CodeTypeVariables {

  private final JavaOperation declaringOperation;

  private List<JavaTypeVariable> typeVariables;

  private Map<String, JavaTypeVariable> typeVariableMap;

  /**
   * The constructor.
   *
   * @param context the {@link #getContext() context}.
   */
  public JavaTypeVariables(JavaContext context) {

    super(context);
    this.declaringOperation = null;
    this.typeVariables = Collections.emptyList();
    this.typeVariableMap = Collections.emptyMap();
    setImmutable();
  }

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaTypeVariables(JavaType declaringType) {

    super(declaringType);
    this.typeVariables = new ArrayList<>();
    this.typeVariableMap = new HashMap<>();
    this.declaringOperation = null;
  }

  /**
   * The constructor.
   *
   * @param declaringOperation the {@link #getDeclaringOperation() declaring operation}.
   */
  public JavaTypeVariables(JavaOperation declaringOperation) {

    super(declaringOperation.getDeclaringType());
    this.typeVariables = new ArrayList<>();
    this.typeVariableMap = new HashMap<>();
    this.declaringOperation = declaringOperation;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaTypeVariables} to copy.
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaTypeVariables(JavaTypeVariables template, JavaType declaringType) {

    super(template, declaringType);
    this.typeVariables = doCopy(template.typeVariables, declaringType);
    this.typeVariableMap = new HashMap<>(this.typeVariables.size());
    for (JavaTypeVariable variable : this.typeVariables) {
      this.typeVariableMap.put(variable.getName(), variable);
    }
    this.declaringOperation = null;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaTypeVariables} to copy.
   * @param declaringOperation the {@link #getDeclaringOperation() declaring operation}.
   */
  public JavaTypeVariables(JavaTypeVariables template, JavaOperation declaringOperation) {

    super(template, declaringOperation.getDeclaringType());
    this.declaringOperation = declaringOperation;
    this.typeVariables = doCopy(template.typeVariables, declaringOperation);
    this.typeVariableMap = new HashMap<>(this.typeVariables.size());
    for (JavaTypeVariable variable : this.typeVariables) {
      this.typeVariableMap.put(variable.getName(), variable);
    }
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.typeVariables = Collections.unmodifiableList(this.typeVariables);
    this.typeVariableMap = Collections.unmodifiableMap(this.typeVariableMap);
  }

  @Override
  public List<? extends JavaTypeVariable> getDeclared() {

    return this.typeVariables;
  }

  @Override
  public List<? extends JavaTypeVariable> getAll() {

    return this.typeVariables;
  }

  @Override
  public JavaOperation getDeclaringOperation() {

    return this.declaringOperation;
  }

  @Override
  public CodeTypeVariable getDeclared(String name) {

    return this.typeVariableMap.get(name);
  }

  @Override
  public CodeTypeVariable get(String name) {

    return this.typeVariableMap.get(name);
  }

  @Override
  public JavaTypeVariable add(String name) {

    verifyMutalbe();
    if (this.typeVariableMap.containsKey(name)) {
      throw new DuplicateObjectException(JavaTypeVariable.class, name);
    }
    JavaTypeVariable variable;
    if (this.declaringOperation == null) {
      variable = new JavaTypeVariable(getDeclaringType());
    } else {
      variable = new JavaTypeVariable(this.declaringOperation);
    }
    this.typeVariables.add(variable);
    this.typeVariableMap.put(name, variable);
    return variable;
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    writeReference(sink, true);
  }

  void writeReference(Appendable sink, boolean declaration) throws IOException {

    if (!this.typeVariables.isEmpty()) {
      String prefix = "<";
      for (JavaTypeVariable variable : this.typeVariables) {
        sink.append(prefix);
        variable.write(sink, "", "");
        prefix = ", ";
      }
      sink.append('>');
    }
  }

  @Override
  public JavaTypeVariables copy(CodeType newDeclaringType) {

    return new JavaTypeVariables(this, (JavaType) newDeclaringType);
  }

  @Override
  public CodeTypeVariables copy(CodeOperation newDeclaringOperation) {

    return new JavaTypeVariables(this, (JavaOperation) newDeclaringOperation);
  }

}
