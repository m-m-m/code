/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.loader;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.security.CodeSource;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.code.api.CodeContext;
import io.github.mmm.code.api.CodeName;
import io.github.mmm.code.base.BaseFile;
import io.github.mmm.code.base.BasePackage;
import io.github.mmm.code.base.loader.BaseSourceLoaderImpl;
import io.github.mmm.code.base.loader.SourceCodeProvider;
import io.github.mmm.code.base.parser.SourceCodeParser;
import io.github.mmm.code.base.source.BaseSource;
import io.github.mmm.code.base.type.BaseGenericType;
import io.github.mmm.code.base.type.BaseType;
import io.github.mmm.code.impl.java.parser.JavaSourceCodeParserImpl;

/**
 * Implementation of {@link BaseSourceLoaderImpl} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSourceLoader extends BaseSourceLoaderImpl {

  private static final Logger LOG = LoggerFactory.getLogger(JavaSourceLoader.class);

  private SourceCodeProvider sourceCodeProvider;

  private SourceCodeParser parser;

  /**
   * The constructor.
   *
   * @param sourceCodeProvider the {@link SourceCodeProvider}.
   */
  public JavaSourceLoader(SourceCodeProvider sourceCodeProvider) {

    super();
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
   * @return the sourceCodeProvider
   */
  public SourceCodeProvider getSourceCodeProvider() {

    return this.sourceCodeProvider;
  }

  @Override
  public BaseType getType(String qualifiedName) {

    if (this.sourceCodeProvider == null) {
      return null;
    }
    return getType(getSource().parseName(qualifiedName));
  }

  @Override
  public BaseType getType(CodeName qualifiedName) {

    if (this.sourceCodeProvider == null) {
      return null;
    }
    CodeName parent = qualifiedName.getParent();
    try (Reader reader = this.sourceCodeProvider.openType(qualifiedName.getFullName())) {
      if (reader == null) {
        return getTypeFromSource(parent, qualifiedName.getSimpleName());
      } else {
        BasePackage pkg = getPackage(parent);
        BaseFile file = pkg.getChildren().createFile(qualifiedName.getSimpleName());
        getParser().parseType(reader, file);
        return file.getType();
      }
    } catch (IOException e) {
      LOG.debug("Failed to open type: {}", e.getMessage(), e);
      return null;
    }
  }

  @Override
  public BaseGenericType getType(Class<?> clazz) {

    if (clazz.isArray()) {
      BaseGenericType componentType = getType(clazz.getComponentType());
      return componentType.createArray();
    }
    CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
    BaseSource source = getSource();

    if (!Objects.equals(codeSource, source.getReflectiveObject())) {
      URL location = null;
      if (codeSource != null) {
        location = codeSource.getLocation();
      }
      throw new IllegalStateException(source.getId() + " not responsible for location " + location);
    }

    BasePackage parentPackage;
    Package pkg = clazz.getPackage();
    if (pkg == null) {
      parentPackage = getSource().getRootPackage();
    } else {
      String pkgName = pkg.getName();
      parentPackage = getPackage(source.parseName(pkgName));
    }
    return getTypeInternal(clazz, parentPackage);
  }

  private BaseType getTypeInternal(Class<?> clazz, BasePackage pkg) {

    String simpleName = clazz.getSimpleName();
    Class<?> declaringClass = clazz.getDeclaringClass();
    BaseType type = (BaseType) pkg.getChildren().getType(simpleName, false);
    if (type != null) {
      return type;
    }
    BaseType declaringType = null;
    if (declaringClass != null) {
      declaringType = getTypeInternal(declaringClass, pkg);
      BaseFile file = declaringType.getFile();
      type = new BaseType(file, simpleName, declaringType, clazz);
      addContainerItem(declaringType.getNestedTypes(), type);
    } else {
      BaseFile file = new BaseFile(pkg, clazz, getSourceFileSupplier(pkg, clazz.getSimpleName()));
      addPathElementInternal(pkg.getChildren(), file);
      type = file.getType();
    }
    return type;
  }

  private BasePackage getPackage(CodeName qualifiedName) {

    BasePackage pkg = getSource().getRootPackage();
    if (qualifiedName != null) {
      pkg = getPackage(pkg.getChildren(), qualifiedName, false, this::createPackage, true, true);
    }
    return pkg;
  }

  private BasePackage createPackage(BasePackage parentPackage, String simpleName) {

    BasePackage pkg = new BasePackage(parentPackage, simpleName, null,
        () -> getSourcePackage(parentPackage, simpleName), true);
    return pkg;
  }

  private BasePackage getSourcePackage(BasePackage parentPackage, String simpleName) {

    if (this.sourceCodeProvider == null) {
      return null;
    }
    BasePackage pkg = new BasePackage(parentPackage, simpleName, null, null, true);
    try (Reader reader = this.sourceCodeProvider.openPackage(pkg.getQualifiedName())) {
      if (reader != null) {
        getParser().parsePackage(reader, pkg);
      }
    } catch (IOException e) {
      LOG.debug("Open package failed: {}", e.getMessage(), e);
    }
    return pkg;
  }

  private Supplier<BaseFile> getSourceFileSupplier(BasePackage pkg, String simpleName) {

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
    } catch (IOException | RuntimeException e) {
      LOG.warn("Failed to open type: {}", e.getMessage(), e);
    }
    return null;
  }

  private BaseType getTypeFromSource(CodeName parent, String simpleName) {

    if (parent == null) {
      return null;
    }
    String parentSimpleName = parent.getSimpleName();
    if ((parentSimpleName.length() > 0) && Character.isUpperCase(parentSimpleName.charAt(0))) {
      BaseType declaringType = getTypeFromSource(parent.getParent(), parentSimpleName);
      if (declaringType != null) {
        return (BaseType) declaringType.getNestedTypes().get(simpleName);
      }
    }
    return null;
  }

  @Override
  public void scan(BasePackage pkg) {

    if ((this.sourceCodeProvider != null)) {
      try {
        String qualifiedName = pkg.getQualifiedName();
        List<String> simpleNames = this.sourceCodeProvider.scanPackage(qualifiedName);
        CodeContext context = getContext();
        String prefix = qualifiedName + context.getLanguage().getPackageSeparator();
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
  public void close() {

    if (this.sourceCodeProvider != null) {
      this.sourceCodeProvider.close();
      this.sourceCodeProvider = null;
    }
  }

}
