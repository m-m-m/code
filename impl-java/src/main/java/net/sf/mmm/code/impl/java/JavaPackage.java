/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.impl.java.item.JavaReflectiveObject;
import net.sf.mmm.code.impl.java.node.JavaContainer;
import net.sf.mmm.code.impl.java.node.JavaNode;
import net.sf.mmm.code.impl.java.source.JavaSource;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodePackage} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class JavaPackage extends JavaPathElement
    implements CodePackage, JavaContainer, CodeNodeItemWithGenericParent<JavaNode, JavaPackage>, JavaReflectiveObject<Package> {

  /** {@link #getSimpleName() Simple name} of the default (root) package. */
  public static final String NAME_DEFAULT = "";

  /** {@link #getSimpleName() Simple name} {@value}. */
  public static final String NAME_JAVA = "java";

  /** {@link #getSimpleName() Simple name} {@value}. */
  public static final String NAME_LANG = "lang";

  /** {@link #getSimpleName() Simple name} {@value}. */
  public static final String NAME_UTIL = "util";

  private static final Logger LOG = LoggerFactory.getLogger(JavaPackage.class);

  private final JavaSource source;

  private final JavaPackage superLayerPackage;

  private final JavaPathElements children;

  private final Package reflectiveObject;

  /**
   * The constructor for a {@link JavaContext#getRootPackage() root-package}.
   *
   * @param superPackage the super package to inherit from.
   * @param source the {@link #getSource() source}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}.
   */
  JavaPackage(JavaSource source, JavaPackage superPackage, Package reflectiveObject) {

    super(null, "");
    this.source = source;
    this.superLayerPackage = superPackage;
    this.children = new JavaPathElements(this);
    this.reflectiveObject = reflectiveObject;
    setImmutable();
  }

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}.
   */
  public JavaPackage(JavaPackage parentPackage, String simpleName, Package reflectiveObject) {

    super(parentPackage, simpleName);
    this.source = parentPackage.getSource();
    this.superLayerPackage = null;
    this.children = new JavaPathElements(this);
    this.reflectiveObject = reflectiveObject;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaPackage} to copy.
   * @param parentPackage the {@link #getParentPackage() parent package}.
   */
  public JavaPackage(JavaPackage template, JavaPackage parentPackage) {

    super(template, parentPackage);
    this.superLayerPackage = template;
    this.source = parentPackage.source;
    this.reflectiveObject = template.reflectiveObject;
    this.children = new JavaPathElements(this);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaPackage} to copy.
   * @param source the {@link #getSource() source}.
   */
  public JavaPackage(JavaPackage template, JavaSource source) {

    super(template, parentCopy(template, source));
    this.superLayerPackage = template;
    this.source = source;
    this.reflectiveObject = template.reflectiveObject;
    this.children = new JavaPathElements(this);
  }

  private static JavaPackage parentCopy(JavaPackage template, JavaSource source) {

    if (template.isRoot()) {
      return null;
    }
    return new JavaPackage(template.getParentPackage(), source);
  }

  @Override
  public JavaPackage getSuperLayerPackage() {

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
  public JavaPackage getParentPackage() {

    JavaPackage parentPackage = super.getParentPackage();
    if ((parentPackage == null) && (this.superLayerPackage != null)) {
      return this.superLayerPackage.getParentPackage();
    }
    return parentPackage;
  }

  @Override
  public JavaContainer getParent() {

    JavaPackage parent = getParentPackage();
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
  public JavaContext getContext() {

    return this.source.getContext();
  }

  @Override
  public JavaSource getSource() {

    return this.source;
  }

  @Override
  public boolean isFile() {

    return false;
  }

  @Override
  public JavaPathElements getChildren() {

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
      JavaPackage parent = getParentPackage();
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
      JavaPackage parent = getParentPackage();
      if ((parent != null) && (parent.isJava())) {
        return true;
      }
    }
    return false;
  }

  /**
   * @deprecated a {@link CodePackage} contains {@link CodeType}s and not vice versa. Therefore this method
   *             will always return {@code null} here.
   */
  @Deprecated
  @Override
  public JavaType getDeclaringType() {

    return null;
  }

  @Override
  public JavaPackage copy() {

    return copy(getParent());
  }

  @Override
  public JavaPackage copy(JavaNode newParent) {

    if (newParent instanceof JavaPackage) {
      return new JavaPackage(this, (JavaPackage) newParent);
    } else if (newParent instanceof JavaSource) {
      return new JavaPackage(this, (JavaSource) newParent);
    } else {
      throw new IllegalArgumentException("" + newParent);
    }
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    if (isRoot()) {
      return;
    }
    super.doWrite(sink, newline, defaultIndent, currentIndent);
    if (currentIndent != null) {
      sink.append(currentIndent);
    }
    sink.append("package ");
    sink.append(getQualifiedName());
    sink.append(';');
    sink.append(newline);
  }

}
