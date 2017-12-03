/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.io.IOException;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.CodePathElement;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyType;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.language.CodeLanguageJava;
import net.sf.mmm.code.api.node.CodeContainer;
import net.sf.mmm.code.base.source.BaseSource;

/**
 * Base implementation of {@link CodePackage}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class BasePackage extends BasePathElement implements CodePackage {

  /** {@link #getSimpleName() Simple name} of the default (root) package. */
  public static final String NAME_DEFAULT = "";

  /** {@link #getSimpleName() Simple name} {@value}. */
  public static final String NAME_JAVA = "java";

  /** {@link #getSimpleName() Simple name} {@value}. */
  public static final String NAME_LANG = "lang";

  /** {@link #getSimpleName() Simple name} {@value}. */
  public static final String NAME_UTIL = "util";

  private static final Logger LOG = LoggerFactory.getLogger(BasePackage.class);

  private final BaseSource source;

  private final BasePathElements children;

  private final Package reflectiveObject;

  private final boolean systemImmutable;

  private BasePackage sourceCodeObject;

  private Supplier<BasePackage> sourceSupplier;

  /**
   * The constructor for a {@link #isRoot() root} package.
   *
   * @param source the {@link #getSource() source}.
   */
  public BasePackage(BaseSource source) {

    super(null, "");
    this.source = source;
    this.children = new BasePathElements(this);
    this.reflectiveObject = null;
    this.systemImmutable = true; // depend on source?
  }

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   */
  public BasePackage(BasePackage parentPackage, String simpleName) {

    this(parentPackage, simpleName, null, null, false);
  }

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}.
   * @param sourceSupplier the optional {@link Supplier} for lazy-loading of source-code.
   * @param systemImmutable the {@link #isSystemImmutable() system immutable flag}.
   */
  public BasePackage(BasePackage parentPackage, String simpleName, Package reflectiveObject, Supplier<BasePackage> sourceSupplier, boolean systemImmutable) {

    super(parentPackage, simpleName);
    this.source = parentPackage.getSource();
    this.children = new BasePathElements(this);
    Package pkg = reflectiveObject;
    if (pkg == null) {
      if (CodeLanguageJava.LANGUAGE_NAME_JAVA.equals(getLanguage().getLanguageName())) {
        pkg = Package.getPackage(getQualifiedName());
      }
    } else if (!systemImmutable) {
      LOG.warn("System immutable flag needs to be true when reflective package is present.");
    }
    this.reflectiveObject = pkg;
    this.sourceSupplier = sourceSupplier;
    this.systemImmutable = systemImmutable;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BasePackage} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BasePackage(BasePackage template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.source = mapper.map(template.source, CodeCopyType.REFERENCE);
    this.reflectiveObject = null;
    this.children = new BasePathElements(this);
    this.systemImmutable = false;
    for (CodePathElement child : template.children) {
      CodePathElement childCopy = mapper.map(child, CodeCopyType.CHILD);
      if (childCopy != null) {
        this.children.add(childCopy);
      }
    }
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.children.setImmutableIfNotSystemImmutable();
  }

  @Override
  public CodeContainer getParent() {

    BasePackage parent = getParentPackage();
    if (parent != null) {
      return parent;
    }
    return this.source;
  }

  @Override
  public Package getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  public BasePackage getSourceCodeObject() {

    if (this.sourceCodeObject == null) {
      if (this.sourceSupplier != null) {
        this.sourceCodeObject = this.sourceSupplier.get();
      }
    }
    return this.sourceCodeObject;
  }

  @Override
  public BaseContext getContext() {

    if (this.source == null) {
      return null; // only during initialization (e.g. debugging in constructor)
    }
    return this.source.getContext();
  }

  @Override
  public BaseSource getSource() {

    return this.source;
  }

  @Override
  public boolean isFile() {

    return false;
  }

  @Override
  public BasePathElements getChildren() {

    return this.children;
  }

  @Override
  public boolean isRequireImport() {

    return !isRoot() && !isJavaLang();
  }

  /**
   * @return {@code true} if this is the default package, {@code false} otherwise.
   */
  @Override
  public boolean isRoot() {

    if (getParentPackage() == null) {
      String name = getSimpleName();
      if ("".equals(name)) {
        return true;
      }
      LOG.warn("Package has no parent but non-empty name {}.", name);
    }
    return false;
  }

  /**
   * @return {@code true} if this is the "{@code java}" package, {@code false} otherwise.
   */
  public boolean isJava() {

    if (NAME_JAVA.equals(getSimpleName())) {
      BasePackage parent = getParentPackage();
      if ((parent != null) && (parent.isRoot())) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return {@code true} if this is the "{@code java.lang}" package (that requires no import), {@code false} otherwise.
   */
  public boolean isJavaLang() {

    if (NAME_LANG.equals(getSimpleName())) {
      BasePackage parent = getParentPackage();
      if ((parent != null) && (parent.isJava())) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected boolean isSystemImmutable() {

    return this.systemImmutable;
  }

  @Override
  public BasePackage copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BasePackage copy(CodeCopyMapper mapper) {

    return new BasePackage(this, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    if (isRoot()) {
      return;
    }
    super.doWrite(sink, newline, defaultIndent, currentIndent, language);
    if (currentIndent != null) {
      sink.append(currentIndent);
    }
    sink.append("package ");
    sink.append(getQualifiedName());
    sink.append(language.getStatementTerminator());
    sink.append(newline);
    sink.append(newline);
  }

}
