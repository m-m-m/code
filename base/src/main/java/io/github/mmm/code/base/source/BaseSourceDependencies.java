/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.source;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import io.github.mmm.code.api.source.CodeSourceDependencies;
import io.github.mmm.code.base.BaseContext;

/**
 * Implementation of {@link CodeSourceDependencies} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseSourceDependencies implements CodeSourceDependencies<BaseSource> {

  private final BaseSource parent;

  private List<BaseSource> dependencies;

  private Supplier<List<BaseSource>> lazyInit;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param lazyInit the lazy initializer for the {@link #getDeclared() dependencies}.
   */
  public BaseSourceDependencies(BaseSource parent, Supplier<List<BaseSource>> lazyInit) {

    super();
    this.parent = parent;
    this.lazyInit = lazyInit;
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param dependencies the {@link #getDeclared() dependencies}.
   */
  public BaseSourceDependencies(BaseSource parent, List<BaseSource> dependencies) {

    super();
    this.parent = parent;
    this.dependencies = dependencies;
  }

  @Override
  public List<? extends BaseSource> getDeclared() {

    if (this.dependencies == null) {
      if (this.lazyInit == null) {
        this.dependencies = Collections.emptyList();
      } else {
        List<BaseSource> deps = this.lazyInit.get();
        this.dependencies = Collections.unmodifiableList(deps);
        this.lazyInit = null;
      }
    }
    return this.dependencies;
  }

  @Override
  public BaseSource getParent() {

    return this.parent;
  }

  @Override
  public BaseSource getSource() {

    return this.parent;
  }

  @Override
  public BaseContext getContext() {

    return this.parent.getContext();
  }

}
