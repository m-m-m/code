/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.loader;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.CodeContext;
import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.base.BaseFile;
import net.sf.mmm.code.base.BasePackage;
import net.sf.mmm.code.base.loader.SourceCodeProvider;
import net.sf.mmm.code.base.parser.SourceCodeParser;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.impl.java.parser.JavaSourceCodeParserImpl;

/**
 * Implementation of {@link AbstractJavaCodeLoader}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaLoader extends AbstractJavaCodeLoader {

  private static final Logger LOG = LoggerFactory.getLogger(JavaLoader.class);

  private final ClassLoader classloader;

  private final SourceCodeProvider sourceCodeProvider;

  private SourceCodeParser parser;

  /**
   * The constructor.
   */
  public JavaLoader() {

    this(null);
  }

  /**
   * The constructor.
   *
   * @param sourceCodeProvider the {@link SourceCodeProvider}.
   */
  public JavaLoader(SourceCodeProvider sourceCodeProvider) {

    this(Thread.currentThread().getContextClassLoader(), sourceCodeProvider);
  }

  /**
   * The constructor.
   *
   * @param classloader the underlying {@link ClassLoader} to adopt.
   * @param sourceCodeProvider the {@link SourceCodeProvider}.
   */
  public JavaLoader(ClassLoader classloader, SourceCodeProvider sourceCodeProvider) {

    super();
    if ((classloader == null) && (sourceCodeProvider == null)) {
      Objects.requireNonNull(null, "classloader & sourceCodeProvider");
    }
    this.classloader = classloader;
    this.sourceCodeProvider = sourceCodeProvider;
  }

  /**
   * @return the {@link SourceCodeParser} used to parse source code files.
   */
  public SourceCodeParser getParser() {

    if (this.parser == null) {
      this.parser = JavaSourceCodeParserImpl.get();
    }
    return this.parser;
  }

  /**
   * @param parser the new value of {@link #getParser()}.
   */
  public void setParser(SourceCodeParser parser) {

    if (this.parser == null) {
      this.parser = parser;
    }
    if (this.parser == parser) {
      throw new IllegalStateException("Already initialized!");
    }
  }

  /**
   * @return the classloader
   */
  public ClassLoader getClassloader() {

    return this.classloader;
  }

  /**
   * @return the sourceCodeProvider
   */
  public SourceCodeProvider getSourceCodeProvider() {

    return this.sourceCodeProvider;
  }

  @Override
  public boolean isSupportByteCode() {

    return (this.classloader != null);
  }

  @Override
  public boolean isSupportSourceCode() {

    return (this.sourceCodeProvider != null);
  }

  @Override
  public void scan(BasePackage pkg) {

    if ((this.sourceCodeProvider != null)) {
      try {
        String qualifiedName = pkg.getQualifiedName();
        List<String> simpleNames = this.sourceCodeProvider.scanPackage(qualifiedName);
        CodeContext context = getContext();
        String prefix = qualifiedName + context.getSyntax().getPackageSeparator();
        for (String simpleName : simpleNames) {
          context.getType(prefix + simpleName);
        }
      } catch (IOException e) {
        LOG.debug("Package scan failed: {}", e.getMessage(), e);
      }
    } else {
      // reflective component scan? or scan classes directory?
    }
  }

  @Override
  public BasePackage getPackage(CodeName qualifiedName) {

    return getContext().getRootPackage().getChildren().getOrCreatePackage(qualifiedName, false, this::getSourcePackageSupplier);
  }

  private Supplier<BasePackage> getSourcePackageSupplier(BasePackage parentPackage, String simpleName) {

    if (this.sourceCodeProvider == null) {
      return null;
    }
    return () -> getSourcePackage(parentPackage, simpleName);
  }

  private BasePackage getSourcePackage(BasePackage parentPackage, String simpleName) {

    BasePackage pkg = new BasePackage(parentPackage, simpleName, null, null, null);
    try (Reader reader = this.sourceCodeProvider.openPackage(pkg.getQualifiedName())) {
      if (reader != null) {
        getParser().parsePackage(reader, pkg);
      }
    } catch (IOException e) {
      LOG.debug("Open package failed: {}", e.getMessage(), e);
    }
    return pkg;
  }

  @Override
  public BaseType getType(String qualifiedName) {

    if (this.classloader == null) {
      getType(getContext().parseName(qualifiedName));
    }
    try {
      Class<?> clazz = this.classloader.loadClass(qualifiedName);
      if (clazz.isArray()) {
        throw new IllegalArgumentException(qualifiedName);
      }
      return (BaseType) getContext().getType(clazz);
    } catch (ClassNotFoundException e) {
      LOG.debug("Class {} not found.", qualifiedName, e);
      return null;
    }
  }

  @Override
  public BaseType getType(CodeName qualifiedName) {

    if (this.classloader == null) {
      CodeName parent = qualifiedName.getParent();
      try (Reader reader = this.sourceCodeProvider.openType(qualifiedName.getFullName())) {
        if (reader == null) {
          return getTypeFromSource(parent, qualifiedName.getSimpleName());
        } else {
          BasePackage pkg;
          if (parent == null) {
            pkg = getContext().getRootPackage();
          } else {
            pkg = getPackage(parent.getFullName());
          }
          BaseFile file = getFileFromSource(pkg, qualifiedName.getSimpleName());
          return file.getType();
        }
      } catch (IOException e) {
        LOG.debug("Failed to open type: {}", e.getMessage(), e);
        return null;
      }
    } else {
      return getType(qualifiedName.getFullName());
    }
  }

  @Override
  public Supplier<BaseFile> getSourceFileSupplier(BasePackage pkg, String simpleName) {

    if (this.sourceCodeProvider == null) {
      return null;
    }
    return () -> getFileFromSource(pkg, simpleName);
  }

  private BaseFile getFileFromSource(BasePackage pkg, String simpleName) {

    BaseFile file = pkg.getChildren().createFile(simpleName);
    try (Reader reader = this.sourceCodeProvider.openType(file.getQualifiedName())) {
      if (reader != null) {
        getParser().parseType(reader, file);
        return file;
      }
    } catch (IOException e) {
      LOG.debug("Failed to open type: {}", e.getMessage(), e);
    }
    return null;
  }

  private BaseType getTypeFromSource(CodeName parent, String simpleName) {

    if (parent == null) {
      return null;
    }
    String parentSimpleName = parent.getSimpleName();
    BaseType declaringType;
    if ((parentSimpleName.length() > 0) && Character.isUpperCase(parentSimpleName.charAt(0))) {
      declaringType = getTypeFromSource(parent.getParent(), parentSimpleName);
    } else {
      declaringType = getType(parent);
    }
    if (declaringType == null) {
      return null;
    }
    return declaringType.getNestedTypes().get(simpleName);
  }

}
