/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeWildcard;

/**
 * Base implementation of {@link BaseContext}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractBaseContext extends AbstractBaseProvider implements BaseContext {

  private BaseSourceImpl source;

  /**
   * The constructor.
   *
   * @param source the {@link #getSource() source}.
   */
  public AbstractBaseContext(BaseSourceImpl source) {

    super();
    this.source = source;
    this.source.setContext(this);
  }

  @Override
  public BaseContext getContext() {

    return this;
  }

  @Override
  public BaseSource getSource() {

    return this.source;
  }

  @Override
  public BaseSource getSource(String id) {

    if (this.source.getId().equals(id)) {
      return this.source;
    }
    BaseContext parent = getParent();
    if (parent != null) {
      return parent.getSource(id);
    }
    return null;
  }

  @Override
  public CodeLanguage getLanguage() {

    return getParent().getLanguage();
  }

  @Override
  public BaseFactory getFactory() {

    return getParent().getFactory();
  }

  @Override
  public BaseTypeWildcard getUnboundedWildcard() {

    return getParent().getUnboundedWildcard();
  }

  @Override
  public BaseType getRootType() {

    return getParent().getRootType();
  }

  @Override
  public BaseType getRootEnumerationType() {

    return getParent().getRootEnumerationType();
  }

  @Override
  public BaseType getVoidType() {

    return getParent().getVoidType();
  }

  @Override
  public BaseType getRootExceptionType() {

    return getParent().getRootExceptionType();
  }

  @Override
  public BaseType getBooleanType(boolean primitive) {

    return getParent().getBooleanType(primitive);
  }

  @Override
  public BaseType getNonPrimitiveType(BaseType javaType) {

    return getParent().getNonPrimitiveType(javaType);
  }

  @Override
  public String getQualifiedNameForStandardType(String simpleName, boolean omitStandardPackages) {

    return getParent().getQualifiedNameForStandardType(simpleName, omitStandardPackages);
  }

  @Override
  public BaseContext createChildContext() {

    return new BaseContextChild(this);
  }

  @Override
  public void close() throws Exception {

    this.source.close();
    this.source = null;
  }

}
