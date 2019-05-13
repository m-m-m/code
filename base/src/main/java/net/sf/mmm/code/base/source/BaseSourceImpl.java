/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.source;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.source.CodeSourceDescriptor;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.BasePackage;
import net.sf.mmm.code.base.BaseProviderImpl;
import net.sf.mmm.code.base.loader.BaseSourceLoader;
import net.sf.mmm.code.base.loader.BaseSourceLoaderImpl;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.util.component.api.ResourceMissingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link net.sf.mmm.code.api.source.CodeSource} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseSourceImpl extends BaseProviderImpl implements BaseSource {

  private static final Logger LOG = LoggerFactory.getLogger(BaseSourceImpl.class);

  private final CodeSource reflectiveObject;

  private final BasePackage rootPackage;

  private BaseContext context;

  private BaseSourceDependencies dependencies;

  private CodeSourceDescriptor descriptor;

  private File byteCodeLocation;

  private File sourceCodeLocation;

  private BaseSourceLoader loader;

  private String id;

  /**
   * The constructor.
   *
   * @param byteCodeLocation the {@link #getByteCodeLocation() byte code location}.
   * @param sourceCodeLocation the {@link #getSourceCodeLocation() source code location}.
   * @param id the {@link #getId() ID}.
   * @param descriptor the {@link #getDescriptor() descriptor}.
   * @param loader the {@link #getLoader() loader}.
   */
  public BaseSourceImpl(File byteCodeLocation, File sourceCodeLocation, String id, CodeSourceDescriptor descriptor,
      BaseSourceLoader loader) {

    this(null, byteCodeLocation, sourceCodeLocation, id, descriptor, null, loader);
  }

  /**
   * The constructor.
   *
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May not be {@code null} otherwise use
   *        different constructor.
   * @param descriptor the {@link #getDescriptor() descriptor}.
   * @param loader the {@link #getLoader() loader}.
   */
  public BaseSourceImpl(CodeSource reflectiveObject, CodeSourceDescriptor descriptor, BaseSourceLoader loader) {

    this(reflectiveObject, null, null, null, descriptor, null, loader);
    Objects.requireNonNull(reflectiveObject, "reflectiveObject");
  }

  /**
   * The constructor.
   *
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May not be {@code null} otherwise use
   *        different constructor.
   * @param byteCodeLocation the {@link #getByteCodeLocation() byte code location}.
   * @param sourceCodeLocation the {@link #getSourceCodeLocation() source code location}.
   * @param id the {@link #getId() ID}.
   * @param descriptor the {@link #getDescriptor() descriptor}.
   * @param dependencies the {@link #getDependencies()} dependencies.
   * @param loader the {@link #getLoader() loader}.
   */
  public BaseSourceImpl(CodeSource reflectiveObject, File byteCodeLocation, File sourceCodeLocation, String id,
      CodeSourceDescriptor descriptor, List<BaseSource> dependencies, BaseSourceLoader loader) {

    super();
    if ((byteCodeLocation != null) && (id != null)) {
      assert (id.equals(getNormalizedId(byteCodeLocation)));
    }
    if ((byteCodeLocation == null) && (sourceCodeLocation == null) && (id == null) && (reflectiveObject == null)) {
      Objects.requireNonNull(byteCodeLocation, "location||uri||codeSource");
    }
    this.byteCodeLocation = byteCodeLocation;
    this.sourceCodeLocation = sourceCodeLocation;
    if (id != null) {
      this.id = normalizeId(id);
    }
    this.reflectiveObject = reflectiveObject;
    this.rootPackage = new BasePackage(this);
    this.rootPackage.setImmutable();
    if (dependencies != null) {
      this.dependencies = new BaseSourceDependencies(this, dependencies);
    }
    this.descriptor = descriptor;
    if (loader instanceof BaseSourceLoaderImpl) {
      ((BaseSourceLoaderImpl) loader).setSource(this);
    }
    this.loader = loader;
  }

  /**
   * @param id the raw {@link #getId() ID}.
   * @return the normalized {@link #getId() ID}.
   */
  public static String normalizeId(String id) {

    return id.replace('\\', '/');
  }

  /**
   * @param location the {@link File} pointing to the location of the code that shall be used as {@link #getId() ID}.
   * @return the normalized {@link #getId() ID}.
   */
  public static String getNormalizedId(File location) {

    return normalizeId(location.toString());
  }

  /**
   * @param source the {@link CodeSource} with to the location of the code that shall be used as {@link #getId() ID}.
   * @return the normalized {@link #getId() ID}.
   */
  public static String getNormalizedId(CodeSource source) {

    return normalizeId(BaseSourceHelper.asFile(source.getLocation()).toString());
  }

  @Override
  public BaseContext getContext() {

    return this.context;
  }

  /**
   * @param context the initial {@link #getContext() context}.
   */
  public void setContext(BaseContext context) {

    if (this.context == null) {
      this.context = context;
    }
    if (this.context != context) {
      throw new IllegalStateException("Already initialized!");
    }
  }

  @Override
  public BasePackage getRootPackage() {

    return this.rootPackage;
  }

  @Override
  public BaseSourceLoader getLoader() {

    return this.loader;
  }

  @Override
  public CodeSource getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  public BaseSource getParent() {

    Iterator<? extends BaseSource> iterator = getDependencies().iterator();
    if (iterator.hasNext()) {
      return iterator.next();
    }
    return null;
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
  public BaseSourceDependencies getDependencies() {

    if (this.dependencies == null) {
      this.dependencies = createDependencies();
      if (this.dependencies == null) {
        throw new ResourceMissingException("dependencies");
      }
    }
    return this.dependencies;
  }

  /**
   * @return the lazily created {@link BaseSourceDependencies}. Method will be called only once.
   */
  protected BaseSourceDependencies createDependencies() {

    return new BaseSourceDependencies(this, new ArrayList<>());
  }

  @Override
  public File getByteCodeLocation() {

    if (this.byteCodeLocation == null) {
      if (this.reflectiveObject != null) {
        this.byteCodeLocation = BaseSourceHelper.asFile(this.reflectiveObject.getLocation());
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
   * @return the lazily created {@link #getSourceCodeLocation() source code location}. Method will be called only once.
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
      this.id = getNormalizedId(location);
    }
    return this.id;
  }

  @Override
  public BaseType getType(String qualifiedName) {

    return getType(getContext().getType(qualifiedName));
  }

  @Override
  public BaseType getType(CodeName qualifiedName) {

    return getType(getContext().getType(qualifiedName));
  }

  @Override
  public BaseGenericType getType(Class<?> clazz) {

    return getType(getContext().getType(clazz));
  }

  private <T extends BaseGenericType> T getType(T type) {

    BaseSource source = type.getSource();
    if ((type != null) && (source == this)) {
      return type;
    }
    LOG.debug("Ignoring type {} from different source {} in source {}.", type, source, this);
    return null;
  }

  @Override
  public void close() throws Exception {

    if (this.loader != null) {
      this.loader.close();
      this.loader = null;
    }
  }

  @Override
  public String toString() {

    return getId();
  }

  @Override
  public void write(Path targetFolder) {

    getRootPackage().write(targetFolder);
  }

  @Override
  public void write(Path targetFolder, Charset encoding) {

    getRootPackage().write(targetFolder, encoding);
  }

}
