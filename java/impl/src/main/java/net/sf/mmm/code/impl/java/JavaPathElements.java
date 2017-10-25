/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.IOException;
import java.util.List;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.CodePathElements;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.base.node.AbstractCodeNodeItemContainerFlat;
import net.sf.mmm.code.impl.java.node.JavaNodeItem;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodePathElements} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaPathElements extends AbstractCodeNodeItemContainerFlat<JavaPathElement>
    implements CodePathElements<JavaPathElement>, CodeNodeItemWithGenericParent<JavaPackage, JavaPathElements>, JavaNodeItem {

  private final JavaPackage parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaPathElements(JavaPackage parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaPathElements} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaPathElements(JavaPathElements template, JavaPackage parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  protected boolean isNamed() {

    return true;
  }

  @Override
  public JavaPackage getParent() {

    return this.parent;
  }

  @Override
  public JavaType getDeclaringType() {

    return null;
  }

  @Override
  public JavaPathElement get(String simpleName) {

    return get(simpleName, false);
  }

  @Override
  public JavaPathElement get(String simpleName, boolean withoutSuperLayer) {

    return get(simpleName, withoutSuperLayer, true);
  }

  JavaPathElement get(String simpleName, boolean withoutSuperLayer, boolean init) {

    if (init) {
      initialize();
    }
    JavaPathElement child = getByName(simpleName);
    if ((child == null) && !withoutSuperLayer) {
      JavaPackage superLayerPackage = this.parent.getSuperLayerPackage();
      if (superLayerPackage != null) {
        child = superLayerPackage.getChildren().get(simpleName, withoutSuperLayer);
      }
    }
    return child;
  }

  @Override
  public JavaFile getFile(String simpleName) {

    return getFile(simpleName, false);
  }

  @Override
  public JavaFile getFile(String simpleName, boolean withoutSuperLayer) {

    return getFile(simpleName, withoutSuperLayer, true);
  }

  JavaFile getFile(String simpleName, boolean withoutSuperLayer, boolean init) {

    JavaPathElement child = get(simpleName, withoutSuperLayer, init);
    if ((child != null) && child.isFile()) {
      return (JavaFile) child;
    }
    return null;
  }

  @Override
  public JavaFile getFile(CodeName path) {

    return getFile(path, false);
  }

  @Override
  protected List<JavaPathElement> getList() {

    return super.getList();
  }

  @Override
  public JavaFile getFile(CodeName path, boolean withoutSuperLayer) {

    return getFile(path, withoutSuperLayer, true);
  }

  JavaFile getFile(CodeName path, boolean withoutSuperLayer, boolean init) {

    CodeName parentPath = path.getParent();
    if (parentPath == null) {
      return getFile(path.getSimpleName(), withoutSuperLayer, init);
    }
    JavaPackage pkg = getPackage(parentPath, withoutSuperLayer);
    return pkg.getChildren().getFile(path.getSimpleName(), withoutSuperLayer, init);
  }

  @Override
  public JavaFile addFile(String simpleName) {

    return createFile(simpleName, true);
  }

  @Override
  public JavaFile createFile(String simpleName) {

    return createFile(simpleName, false);
  }

  private JavaFile createFile(String simpleName, boolean add) {

    JavaFile file = new JavaFile(this.parent, simpleName);
    if (add || isMutable()) {
      add(file);
    }
    return file;
  }

  @Override
  protected void addInternal(JavaPathElement item) {

    super.addInternal(item);
  }

  @Override
  public JavaFile getOrCreateFile(String simpleName) {

    JavaFile file = getFile(simpleName);
    if (file == null) {
      file = createFile(simpleName);
    }
    return file;
  }

  @Override
  public JavaPackage getPackage(String simpleName) {

    return getPackage(simpleName, false);
  }

  @Override
  public JavaPackage getPackage(String simpleName, boolean withoutSuperLayer) {

    return getPackage(simpleName, withoutSuperLayer, true);
  }

  JavaPackage getPackage(String simpleName, boolean withoutSuperLayer, boolean init) {

    JavaPathElement child = get(simpleName, withoutSuperLayer, init);
    if ((child != null) && !child.isFile()) {
      return (JavaPackage) child;
    }
    return null;
  }

  @Override
  public JavaPackage getPackage(CodeName path) {

    return getPackage(path, false);
  }

  @Override
  public JavaPackage getPackage(CodeName path, boolean withoutSuperLayer) {

    return getPackage(path, withoutSuperLayer, true);
  }

  JavaPackage getPackage(CodeName path, boolean withoutSuperLayer, boolean init) {

    CodeName parentPath = path.getParent();
    String simpleName = path.getSimpleName();
    if (parentPath == null) {
      if (simpleName.isEmpty()) {
        return this.parent;
      }
      return getPackage(simpleName, withoutSuperLayer, init);
    }
    JavaPackage parentPkg = getPackage(parentPath, withoutSuperLayer, init);
    if (parentPkg != null) {
      return parentPkg.getChildren().getPackage(simpleName, withoutSuperLayer, init);
    }
    return null;
  }

  @Override
  public JavaPackage addPackage(String simpleName) {

    return createPackage(simpleName, true);
  }

  @Override
  public JavaPackage createPackage(String simpleName) {

    return createPackage(simpleName, false);
  }

  private JavaPackage createPackage(String simpleName, boolean add) {

    JavaPackage superLayerPackage = this.parent.getSuperLayerPackage();
    if (superLayerPackage != null) {
      JavaPathElement child = superLayerPackage.getChildren().getByName(simpleName);
      if (!child.isFile()) {
        superLayerPackage = (JavaPackage) child;
      } else {
        superLayerPackage = null;
      }
    }
    JavaPackage file = new JavaPackage(this.parent, simpleName, null, superLayerPackage);
    if (add && isMutable()) {
      add(file);
    }
    return file;
  }

  @Override
  protected void add(JavaPathElement item) {

    super.add(item);
  }

  @Override
  public JavaPackage getOrCreatePackage(String simpleName) {

    JavaPackage pkg = getPackage(simpleName);
    if (pkg == null) {
      pkg = createPackage(simpleName);
    }
    return pkg;
  }

  @Override
  public JavaType getType(String simpleName) {

    return getType(simpleName, false);
  }

  @Override
  public JavaType getType(String simpleName, boolean withoutSuperLayer) {

    return getType(simpleName, withoutSuperLayer, true);
  }

  JavaType getType(String simpleName, boolean withoutSuperLayer, boolean init) {

    JavaFile file = getFile(simpleName, withoutSuperLayer, init);
    if (file != null) {
      return file.getType();
    }
    if (init) {
      initialize();
    }
    for (JavaPathElement child : getList()) {
      if (child.isFile()) {
        JavaType type = ((JavaFile) child).getChildType(simpleName, init);
        if (type != null) {
          return type;
        }
      }
    }
    if (!withoutSuperLayer) {
      JavaPackage superLayerPackage = this.parent.getSuperLayerPackage();
      if (superLayerPackage != null) {
        return superLayerPackage.getChildren().getType(simpleName, withoutSuperLayer);
      }
    }
    return null;
  }

  @Override
  public JavaType addType(String simpleName) {

    return addFile(simpleName).getType();
  }

  @Override
  public JavaType createType(String simpleName) {

    return createFile(simpleName).getType();
  }

  @Override
  public JavaPathElements copy() {

    return copy(this.parent);
  }

  @Override
  public JavaPathElements copy(JavaPackage newParent) {

    return new JavaPathElements(this, newParent);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    // packages write their children to separate files - nothing to do...
  }

}
