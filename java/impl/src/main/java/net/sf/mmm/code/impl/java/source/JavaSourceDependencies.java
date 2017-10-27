/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.source;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import net.sf.mmm.code.api.source.CodeSourceDependencies;
import net.sf.mmm.code.base.node.BaseNode;

/**
 * Implementation of {@link CodeSourceDependencies} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSourceDependencies implements CodeSourceDependencies<JavaSource>, BaseNode {

  private final JavaSource parent;

  private List<JavaSource> dependencies;

  private Supplier<List<JavaSource>> lazyInit;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param lazyInit the lazy initializer for the {@link #getDeclared() dependencies}.
   */
  public JavaSourceDependencies(JavaSource parent, Supplier<List<JavaSource>> lazyInit) {

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
  public JavaSourceDependencies(JavaSource parent, List<JavaSource> dependencies) {

    super();
    this.parent = parent;
    this.dependencies = dependencies;
  }

  @Override
  public List<? extends JavaSource> getDeclared() {

    if (this.dependencies == null) {
      if (this.lazyInit == null) {
        this.dependencies = Collections.emptyList();
      } else {
        List<JavaSource> deps = this.lazyInit.get();
        this.dependencies = Collections.unmodifiableList(deps);
        this.lazyInit = null;
      }
    }
    return this.dependencies;
  }

  @Override
  public JavaSource getParent() {

    return this.parent;
  }

  @Override
  public JavaSource getSource() {

    return this.parent;
  }

}
