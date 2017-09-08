/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.source;

import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import net.sf.mmm.code.api.source.CodeSourceDescriptor;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.JavaPackage;
import net.sf.mmm.code.impl.java.JavaProvider;
import net.sf.mmm.code.impl.java.item.JavaReflectiveObject;
import net.sf.mmm.code.impl.java.node.JavaContainer;

/**
 * Implementation of {@link net.sf.mmm.code.api.source.CodeSource} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSource extends JavaProvider implements net.sf.mmm.code.api.source.CodeSource, JavaContainer, JavaReflectiveObject<CodeSource> {

  private final CodeSource reflectiveObject;

  private JavaContext context;

  private JavaSourceDependencies dependencies;

  private JavaSourceDescriptor descriptor;

  private Supplier<JavaSourceDescriptor> lazyDescriptor;

  private URL location;

  private String uri;

  /**
   * The constructor.
   *
   * @param context the owning {@link #getContext() context}.
   * @param uri the {@link #getUri() URI}.
   * @param lazyDescriptor the {@link Supplier} for {@link #getDescriptor() descriptor}.
   */
  public JavaSource(JavaContext context, String uri, Supplier<JavaSourceDescriptor> lazyDescriptor) {

    super();
    Objects.requireNonNull(context, "context");
    this.context = context;
    this.reflectiveObject = null;
    this.uri = uri;
    this.dependencies = new JavaSourceDependencies(this, new ArrayList<>());
    this.lazyDescriptor = lazyDescriptor;
  }

  /**
   * The constructor.
   *
   * @param context the owning {@link #getContext() context}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May not be {@code null}
   *        otherwise use different constructor.
   * @param lazyDependencies the {@link Supplier} for {@link #getDependencies()} dependencies.
   * @param lazyDescriptor the {@link Supplier} for {@link #getDescriptor() descriptor}.
   */
  public JavaSource(JavaContext context, CodeSource reflectiveObject, Supplier<List<JavaSource>> lazyDependencies,
      Supplier<JavaSourceDescriptor> lazyDescriptor) {

    super();
    Objects.requireNonNull(context, "context");
    Objects.requireNonNull(reflectiveObject, "reflectiveObject");
    this.context = context;
    this.reflectiveObject = reflectiveObject;
    this.dependencies = new JavaSourceDependencies(this, lazyDependencies);
    this.lazyDescriptor = lazyDescriptor;
  }

  @Override
  public JavaSourceDependencies getDependencies() {

    return this.dependencies;
  }

  @Override
  public JavaPackage getRootPackage() {

    return this.context.getRootPackage();
  }

  @Override
  public JavaPackage getToplevelPackage() {

    return getContext().getToplevelPackage();
  }

  @Override
  public CodeSource getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  public JavaSource getParent() {

    Iterator<? extends JavaSource> iterator = getDependencies().getAll().iterator();
    if (iterator.hasNext()) {
      return iterator.next();
    }
    return null;
  }

  @Override
  public JavaSource getSource() {

    return this;
  }

  @Override
  public JavaContext getContext() {

    return this.context;
  }

  /**
   * @param context the initial {@link #getContext() context}.
   */
  public void setContext(JavaContext context) {

    if (this.context == null) {
      this.context = context;
    }
    if (this.context != context) {
      throw new IllegalStateException("Already initialized!");
    }
  }

  @Override
  public CodeSourceDescriptor getDescriptor() {

    if (this.descriptor == null) {
      this.descriptor = this.lazyDescriptor.get();
      this.lazyDescriptor = null;
    }
    return this.descriptor;
  }

  @Override
  public URL getLocation() {

    if (this.location == null) {
      this.location = this.reflectiveObject.getLocation();
    }
    return this.location;
  }

  @Override
  public String getUri() {

    if (this.uri == null) {
      this.uri = getLocation().toString();
    }
    return this.uri;
  }

  @Override
  public boolean isSourceCodeAvailable() {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String toString() {

    return getUri();
  }

}
