/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.mmm.code.api.type.CodeTypeVariable;
import net.sf.mmm.code.api.type.CodeTypeVariables;
import net.sf.mmm.code.impl.java.item.JavaItemContainerWithDeclaringType;
import net.sf.mmm.code.impl.java.member.JavaOperation;
import net.sf.mmm.util.exception.api.DuplicateObjectException;

/**
 * Implementation of {@link CodeTypeVariables} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaTypeVariables extends JavaItemContainerWithDeclaringType<CodeTypeVariable> implements CodeTypeVariables {

  private List<JavaTypeVariable> typeVariables;

  private Map<String, JavaTypeVariable> typeVariableMap;

  private JavaOperation declaringOperation;

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
   */
  public JavaTypeVariables(JavaTypeVariables template) {

    super(template);
    this.typeVariables = copy(template.typeVariables);
    this.typeVariableMap = new HashMap<>(this.typeVariables.size());
    for (JavaTypeVariable variable : this.typeVariables) {
      this.typeVariableMap.put(variable.getName(), variable);
    }
    this.declaringOperation = template.declaringOperation;
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
    return null;
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    // TODO Auto-generated method stub

  }

}
