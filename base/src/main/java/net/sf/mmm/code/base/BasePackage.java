/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.io.IOException;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.syntax.CodeSyntaxJava;
import net.sf.mmm.code.base.node.BaseContainer;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodePackage}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class BasePackage extends BasePathElement implements CodePackage, CodeNodeItemWithGenericParent<CodeNode, BasePackage>, BaseContainer {

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

  private final BasePackage superLayerPackage;

  private final BasePathElements children;

  private final Package reflectiveObject;

  private BasePackage sourceCodeObject;

  private Supplier<BasePackage> sourceSupplier;

  /**
   * The constructor for a {@link #isRoot() root} package.
   *
   * @param source the {@link #getSource() source}.
   * @param superLayerPackage the {@link #getSuperLayerPackage() super layer package} to inherit from. May be
   *        {@code null}.
   */
  public BasePackage(BaseSource source, BasePackage superLayerPackage) {

    super(null, "");
    this.source = source;
    this.superLayerPackage = superLayerPackage;
    this.children = new BasePathElements(this);
    this.reflectiveObject = null;
  }

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}.
   * @param superLayerPackage the {@link #getSuperLayerPackage() super layer package} to inherit from. May be
   *        {@code null}.
   * @param sourceSupplier the optional {@link Supplier} for lazy-loading of source-code.
   */
  public BasePackage(BasePackage parentPackage, String simpleName, Package reflectiveObject, BasePackage superLayerPackage,
      Supplier<BasePackage> sourceSupplier) {

    super(parentPackage, simpleName);
    this.source = parentPackage.getSource();
    this.superLayerPackage = superLayerPackage;
    this.children = new BasePathElements(this);
    Package pkg = reflectiveObject;
    if (pkg == null) {
      if (superLayerPackage != null) {
        pkg = superLayerPackage.reflectiveObject;
      }
      if (pkg == null) {
        if (CodeSyntaxJava.LANGUAGE_NAME_JAVA.equals(getContext().getSyntax().getLanguageName())) {
          pkg = Package.getPackage(getQualifiedName());
        }
      }
    }
    this.reflectiveObject = pkg;
    this.sourceSupplier = sourceSupplier;
    if (parentPackage.isImmutable()) {
      setImmutable();
    }
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BasePackage} to copy.
   * @param parentPackage the {@link #getParentPackage() parent package}.
   */
  public BasePackage(BasePackage template, BasePackage parentPackage) {

    super(template, parentPackage);
    this.superLayerPackage = template;
    this.source = parentPackage.source;
    this.reflectiveObject = template.reflectiveObject;
    this.children = new BasePathElements(this);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BasePackage} to copy.
   * @param source the {@link #getSource() source}.
   */
  public BasePackage(BasePackage template, BaseSource source) {

    super(template, parentCopy(template, source));
    this.superLayerPackage = template;
    this.source = source;
    this.reflectiveObject = template.reflectiveObject;
    this.children = new BasePathElements(this);
  }

  private static BasePackage parentCopy(BasePackage template, BaseSource source) {

    if (template.isRoot()) {
      return null;
    }
    return new BasePackage(template.getParentPackage(), source);
  }

  @Override
  public BasePackage getSuperLayerPackage() {

    return this.superLayerPackage;
  }

  @Override
  public String getSimpleName() {

    String simpleName = super.getSimpleName();
    if ((simpleName == null) && (this.superLayerPackage != null)) {
      simpleName = this.superLayerPackage.getSimpleName();
    }
    return simpleName;
  }

  @Override
  public BasePackage getParentPackage() {

    BasePackage parentPackage = super.getParentPackage();
    if ((parentPackage == null) && (this.superLayerPackage != null)) {
      return this.superLayerPackage.getParentPackage();
    }
    return parentPackage;
  }

  @Override
  public BaseContainer getParent() {

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

    return this.source.getContext();
  }

  @Override
  public BaseSource getSource() {

    return this.source;
  }

  @Override
  public BaseType getDeclaringType() {

    return null;
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
   * @return {@code true} if this is the "{@code java.lang}" package (that requires no import), {@code false}
   *         otherwise.
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
  public BasePackage copy() {

    return copy(getParent());
  }

  @Override
  public BasePackage copy(CodeNode newParent) {

    if (newParent instanceof BasePackage) {
      return new BasePackage(this, (BasePackage) newParent);
    } else if (newParent instanceof BaseSource) {
      return new BasePackage(this, (BaseSource) newParent);
    } else {
      throw new IllegalArgumentException("" + newParent);
    }
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    if (isRoot()) {
      return;
    }
    super.doWrite(sink, newline, defaultIndent, currentIndent, syntax);
    if (currentIndent != null) {
      sink.append(currentIndent);
    }
    sink.append("package ");
    sink.append(getQualifiedName());
    sink.append(';');
    sink.append(newline);
  }

}
