/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.member.CodeProperties;
import net.sf.mmm.code.api.member.CodeProperty;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeProperties} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaProperties extends JavaMembers<CodeProperty> implements CodeProperties {

  private List<JavaProperty> properties;

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaProperties(JavaType declaringType) {

    super(declaringType);
    this.properties = new ArrayList<>();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaProperties} to copy.
   */
  public JavaProperties(JavaProperties template) {

    super(template);
  }

  @Override
  public List<? extends JavaProperty> getDeclared() {

    return this.properties;
  }

  @Override
  public Iterable<? extends JavaProperty> getInherited() {

    JavaGenericType superClass = getDeclaringType().getSuperTypes().getSuperClass();
    if (superClass != null) {

    }
    return null;
  }

  @Override
  public Iterable<? extends JavaProperty> getAll() {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CodeProperty getProperty(String name) {

    // TODO Auto-generated method stub
    return null;
  }

}
