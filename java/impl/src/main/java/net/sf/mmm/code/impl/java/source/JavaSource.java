/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.source;

import java.io.File;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import net.sf.mmm.code.api.source.CodeSourceDescriptor;
import net.sf.mmm.code.base.source.CodeSourceHelper;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.JavaPackage;
import net.sf.mmm.code.impl.java.JavaProvider;
import net.sf.mmm.code.impl.java.item.JavaReflectiveObject;
import net.sf.mmm.code.impl.java.node.JavaContainer;
import net.sf.mmm.util.component.api.ResourceMissingException;

/**
 * Implementation of {@link net.sf.mmm.code.api.source.CodeSource} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSource extends JavaProvider implements net.sf.mmm.code.api.source.CodeSource, JavaContainer, JavaReflectiveObject<CodeSource> {

  private final CodeSource reflectiveObject;

  private final JavaPackage rootPackage;

  private JavaContext context;

  private JavaSourceDependencies dependencies;

  private CodeSourceDescriptor descriptor;

  private File byteCodeLocation;

  private File sourceCodeLocation;

  private String id;

  /**
   * The constructor.
   *
   * @param byteCodeLocation the {@link #getByteCodeLocation() byte code location}.
   * @param sourceCodeLocation the {@link #getSourceCodeLocation() source code location}.
   * @param id the {@link #getId() ID}.
   * @param descriptor the {@link #getDescriptor() descriptor}.
   */
  public JavaSource(File byteCodeLocation, File sourceCodeLocation, String id, CodeSourceDescriptor descriptor) {

    this(null, byteCodeLocation, sourceCodeLocation, id, descriptor, null, null);
  }

  /**
   * The constructor.
   *
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May not be {@code null}
   *        otherwise use different constructor.
   * @param descriptor the {@link #getDescriptor() descriptor}.
   */
  public JavaSource(CodeSource reflectiveObject, CodeSourceDescriptor descriptor) {

    this(reflectiveObject, null, null, null, descriptor, null, null);
    Objects.requireNonNull(reflectiveObject, "reflectiveObject");
  }

  /**
   * The constructor.
   *
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May not be {@code null}
   *        otherwise use different constructor.
   * @param byteCodeLocation the {@link #getByteCodeLocation() byte code location}.
   * @param sourceCodeLocation the {@link #getSourceCodeLocation() source code location}.
   * @param id the {@link #getId() ID}.
   * @param descriptor the {@link #getDescriptor() descriptor}.
   * @param dependencies the {@link #getDependencies()} dependencies.
   * @param superLayerPackage the {@link JavaPackage#getSuperLayerPackage() super layer package} to inherit
   *        from.
   */
  public JavaSource(CodeSource reflectiveObject, File byteCodeLocation, File sourceCodeLocation, String id, CodeSourceDescriptor descriptor,
      List<JavaSource> dependencies, JavaPackage superLayerPackage) {

    super();
    if ((byteCodeLocation != null) && (id != null)) {
      assert (id.equals(byteCodeLocation.toString()));
    }
    if ((byteCodeLocation == null) && (sourceCodeLocation == null) && (id == null) && (reflectiveObject == null)) {
      Objects.requireNonNull(byteCodeLocation, "location||uri||codeSource");
    }
    this.byteCodeLocation = byteCodeLocation;
    this.id = id;
    this.reflectiveObject = reflectiveObject;
    this.rootPackage = new JavaPackage(this, superLayerPackage);
    if (dependencies != null) {
      this.dependencies = new JavaSourceDependencies(this, dependencies);
    }
    this.descriptor = descriptor;
  }

  @Override
  public JavaPackage getRootPackage() {

    return this.rootPackage;
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
      this.descriptor = createDescriptor();
      if (this.descriptor == null) {
        throw new ResourceMissingException("descriptor");
      }
    }
    return this.descriptor;
  }

  /**
   * @return the lazily created {@link CodeSourceDescriptor}. Method will be called only once.
   */
  protected CodeSourceDescriptor createDescriptor() {

    return null;
  }

  @Override
  public JavaSourceDependencies getDependencies() {

    if (this.dependencies == null) {
      this.dependencies = createDependencies();
      if (this.dependencies == null) {
        throw new ResourceMissingException("dependencies");
      }
    }
    return this.dependencies;
  }

  /**
   * @return the lazily created {@link JavaSourceDependencies}. Method will be called only once.
   */
  protected JavaSourceDependencies createDependencies() {

    return new JavaSourceDependencies(this, new ArrayList<>());
  }

  @Override
  public File getByteCodeLocation() {

    if (this.byteCodeLocation == null) {
      if (this.reflectiveObject != null) {
        this.byteCodeLocation = CodeSourceHelper.asFile(this.reflectiveObject.getLocation());
      }
    }
    return this.byteCodeLocation;
  }

  @Override
  public File getSourceCodeLocation() {

    if (this.sourceCodeLocation == null) {
      this.sourceCodeLocation = createSourceCodeLocation();
    }
    return this.sourceCodeLocation;
  }

  /**
   * @return the lazily created {@link #getSourceCodeLocation() source code location}. Method will be called
   *         only once.
   */
  protected File createSourceCodeLocation() {

    return null;
  }

  @Override
  public String getId() {

    if (this.id == null) {
      File location = getByteCodeLocation();
      if (location == null) {
        location = getSourceCodeLocation();
      }
      this.id = location.toString();
    }
    return this.id;
  }

  @Override
  public String toString() {

    return getId();
  }

}
