/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.io.IOException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import net.sf.mmm.code.api.CodeFile;
import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.CodePathElements;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.node.BaseNodeItemContainerFlat;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodePathElements}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BasePathElements extends BaseNodeItemContainerFlat<BasePathElement>
    implements CodePathElements<BasePathElement>, CodeNodeItemWithGenericParent<BasePackage, BasePathElements> {

  private final BasePackage parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BasePathElements(BasePackage parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BasePathElements} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public BasePathElements(BasePathElements template, BasePackage parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  protected boolean isKeepListView() {

    return true;
  }

  @Override
  protected boolean isNamed() {

    return true;
  }

  @Override
  public BasePackage getParent() {

    return this.parent;
  }

  @Override
  public BaseType getDeclaringType() {

    return null;
  }

  @Override
  public BasePathElement get(String simpleName) {

    return get(simpleName, false);
  }

  @Override
  public BasePathElement get(String simpleName, boolean withoutSuperLayer) {

    return get(simpleName, withoutSuperLayer, true);
  }

  BasePathElement get(String simpleName, boolean withoutSuperLayer, boolean init) {

    if (init) {
      initialize();
    }
    BasePathElement child = getByName(simpleName);
    if ((child == null) && !withoutSuperLayer) {
      BasePackage superLayerPackage = this.parent.getSuperLayerPackage();
      if (superLayerPackage != null) {
        child = superLayerPackage.getChildren().get(simpleName, withoutSuperLayer);
      }
    }
    return child;
  }

  @Override
  public BaseFile getFile(String simpleName) {

    return getFile(simpleName, false);
  }

  @Override
  public BaseFile getFile(String simpleName, boolean withoutSuperLayer) {

    return getFile(simpleName, withoutSuperLayer, true);
  }

  /**
   * @param simpleName the {@link CodeFile#getSimpleName() simple name} of the requested {@link CodeFile}.
   * @param withoutSuperLayer {@code false} to recursively traverse {@link CodePackage#getSuperLayerPackage()
   *        super layer packages} during the search what is the default if this flag is omitted, {@code true}
   *        to only consider the direct children of the package.
   * @param init - {@code true} to initialize, {@code false} otherwise.
   * @return the {@link CodeFile} with the given {@code name} or {@code null} if not found.
   */
  public BaseFile getFile(String simpleName, boolean withoutSuperLayer, boolean init) {

    BasePathElement child = get(simpleName, withoutSuperLayer, init);
    if ((child != null) && child.isFile()) {
      return (BaseFile) child;
    }
    return null;
  }

  @Override
  public BaseFile getFile(CodeName path) {

    return getFile(path, false);
  }

  /**
   * @return the internal {@link List} without initialization.
   * @see #getDeclared()
   */
  public List<BasePathElement> getInternalList() {

    return getList();
  }

  @Override
  public BaseFile getFile(CodeName path, boolean withoutSuperLayer) {

    return getFile(path, withoutSuperLayer, true);
  }

  BaseFile getFile(CodeName path, boolean withoutSuperLayer, boolean init) {

    CodeName parentPath = path.getParent();
    if (parentPath == null) {
      return getFile(path.getSimpleName(), withoutSuperLayer, init);
    }
    BasePackage pkg = getPackage(parentPath, withoutSuperLayer);
    return pkg.getChildren().getFile(path.getSimpleName(), withoutSuperLayer, init);
  }

  @Override
  public BaseFile addFile(String simpleName) {

    return createFile(simpleName, true);
  }

  @Override
  public BaseFile createFile(String simpleName) {

    return createFile(simpleName, false);
  }

  private BaseFile createFile(String simpleName, boolean add) {

    BaseFile file = new BaseFile(this.parent, simpleName);
    if (add || isMutable()) {
      add(file);
    }
    return file;
  }

  @Override
  protected void addInternal(BasePathElement item) {

    super.addInternal(item);
  }

  @Override
  public BaseFile getOrCreateFile(String simpleName) {

    BaseFile file = getFile(simpleName);
    if (file == null) {
      file = createFile(simpleName);
    }
    return file;
  }

  @Override
  public BasePackage getPackage(String simpleName) {

    return getPackage(simpleName, false);
  }

  @Override
  public BasePackage getPackage(String simpleName, boolean withoutSuperLayer) {

    return getPackage(simpleName, withoutSuperLayer, true);
  }

  /**
   * @param simpleName the {@link CodePackage#getSimpleName() simple name} of the requested
   *        {@link CodePackage}.
   * @param withoutSuperLayer {@code false} to recursively traverse {@link CodePackage#getSuperLayerPackage()
   *        super layer packages} during the search what is the default if this flag is omitted, {@code true}
   *        to only consider the direct children of the package.
   * @param init - {@code true} to initialize, {@code false} otherwise.
   * @return the {@link CodePackage} with the given {@code name} or {@code null} if not found.
   */
  public BasePackage getPackage(String simpleName, boolean withoutSuperLayer, boolean init) {

    BasePathElement child = get(simpleName, withoutSuperLayer, init);
    if ((child != null) && !child.isFile()) {
      return (BasePackage) child;
    }
    return null;
  }

  @Override
  public BasePackage getPackage(CodeName path) {

    return getPackage(path, false);
  }

  @Override
  public BasePackage getPackage(CodeName path, boolean withoutSuperLayer) {

    return getPackage(path, withoutSuperLayer, true);
  }

  /**
   * @param path the {@link CodeName} to traverse.
   * @param withoutSuperLayer {@code false} to recursively traverse {@link CodePackage#getSuperLayerPackage()
   *        super layer packages} during the search what is the default if this flag is omitted, {@code true}
   *        to only consider the direct children of the package.
   * @param init - {@code true} to initialize, {@code false} otherwise.
   * @return the traversed {@link CodePackage} or {@code null} if not found.
   */
  public BasePackage getPackage(CodeName path, boolean withoutSuperLayer, boolean init) {

    CodeName parentPath = path.getParent();
    String simpleName = path.getSimpleName();
    if (parentPath == null) {
      if (simpleName.isEmpty()) {
        return this.parent;
      }
      return getPackage(simpleName, withoutSuperLayer, init);
    }
    BasePackage parentPkg = getPackage(parentPath, withoutSuperLayer, init);
    if (parentPkg != null) {
      return parentPkg.getChildren().getPackage(simpleName, withoutSuperLayer, init);
    }
    return null;
  }

  /**
   * @param path the {@link CodeName} to traverse.
   * @param withoutSuperLayer {@code false} to recursively traverse {@link CodePackage#getSuperLayerPackage()
   *        super layer packages} during the search what is the default if this flag is omitted, {@code true}
   *        to only consider the direct children of the package.
   * @return the traversed {@link CodePackage}. Has been created if it did not already exist.
   */
  public BasePackage getOrCreatePackage(CodeName path, boolean withoutSuperLayer) {

    return getOrCreatePackage(path, withoutSuperLayer, null);
  }

  /**
   * <b>Attention:</b> This is an internal API that should not be used from outside. Use
   * {@link #getOrCreatePackage(CodeName, boolean)} instead.
   *
   * @param path the {@link CodeName} to traverse.
   * @param withoutSuperLayer {@code false} to recursively traverse {@link CodePackage#getSuperLayerPackage()
   *        super layer packages} during the search what is the default if this flag is omitted, {@code true}
   *        to only consider the direct children of the package.
   * @param sourceSupplierFunction the {@link BiFunction} for lazy loading of source-code.
   * @return the traversed {@link CodePackage}. Has been created if it did not already exist.
   */
  public BasePackage getOrCreatePackage(CodeName path, boolean withoutSuperLayer,
      BiFunction<BasePackage, String, Supplier<BasePackage>> sourceSupplierFunction) {

    CodeName parentPath = path.getParent();
    String simpleName = path.getSimpleName();
    BasePathElements parentPathElements;
    if (parentPath == null) {
      if (simpleName.isEmpty()) {
        return this.parent;
      }
      parentPathElements = this;
    } else {
      parentPathElements = getOrCreatePackage(parentPath, withoutSuperLayer).getChildren();
    }
    BasePackage pkg = parentPathElements.getPackage(simpleName, withoutSuperLayer, false);
    if (pkg == null) {
      boolean addRegular = true;
      Supplier<BasePackage> sourceSupplier = null;
      if (sourceSupplierFunction != null) {
        addRegular = false;
        sourceSupplier = sourceSupplierFunction.apply(parentPathElements.parent, simpleName);
      }
      pkg = parentPathElements.createPackage(simpleName, addRegular, sourceSupplier);
      if (!addRegular) {
        parentPathElements.addInternal(pkg);
      }
    }
    return pkg;
  }

  @Override
  public BasePackage addPackage(String simpleName) {

    return createPackage(simpleName, true, null);
  }

  @Override
  public BasePackage createPackage(String simpleName) {

    return createPackage(simpleName, false, null);
  }

  private BasePackage createPackage(String simpleName, boolean add, Supplier<BasePackage> sourceSupplier) {

    BasePackage superLayerPackage = this.parent.getSuperLayerPackage();
    if (superLayerPackage != null) {
      BasePathElement child = superLayerPackage.getChildren().getByName(simpleName);
      if (!child.isFile()) {
        superLayerPackage = (BasePackage) child;
      } else {
        superLayerPackage = null;
      }
    }
    BasePackage file = new BasePackage(this.parent, simpleName, null, superLayerPackage, sourceSupplier);
    if (add && isMutable()) {
      add(file);
    }
    return file;
  }

  @Override
  protected void add(BasePathElement item) {

    super.add(item);
  }

  @Override
  public BasePackage getOrCreatePackage(String simpleName) {

    BasePackage pkg = getPackage(simpleName);
    if (pkg == null) {
      pkg = createPackage(simpleName);
    }
    return pkg;
  }

  @Override
  public BaseType getType(String simpleName) {

    return getType(simpleName, false);
  }

  @Override
  public BaseType getType(String simpleName, boolean withoutSuperLayer) {

    return getType(simpleName, withoutSuperLayer, true);
  }

  /**
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the requested {@link CodeType}.
   * @param withoutSuperLayer {@code false} to recursively traverse {@link CodePackage#getSuperLayerPackage()
   *        super layer packages} during the search what is the default if this flag is omitted, {@code true}
   *        to only consider the direct children of the package.
   * @param init - {@code true} to initialize, {@code false} otherwise.
   * @return the {@link CodeType} with the given {@code name} or {@code null} if not found.
   */
  public BaseType getType(String simpleName, boolean withoutSuperLayer, boolean init) {

    BaseFile file = getFile(simpleName, withoutSuperLayer, init);
    if (file != null) {
      return file.getType();
    }
    if (init) {
      initialize();
    }
    for (BasePathElement child : getList()) {
      if (child.isFile()) {
        BaseType type = ((BaseFile) child).getChildType(simpleName, init);
        if (type != null) {
          return type;
        }
      }
    }
    if (!withoutSuperLayer) {
      BasePackage superLayerPackage = this.parent.getSuperLayerPackage();
      if (superLayerPackage != null) {
        return superLayerPackage.getChildren().getType(simpleName, withoutSuperLayer);
      }
    }
    return null;
  }

  @Override
  public BaseType addType(String simpleName) {

    return addFile(simpleName).getType();
  }

  @Override
  public BaseType createType(String simpleName) {

    return createFile(simpleName).getType();
  }

  @Override
  public BasePathElements copy() {

    return copy(this.parent);
  }

  @Override
  public BasePathElements copy(BasePackage newParent) {

    return new BasePathElements(this, newParent);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    // packages write their children to separate files - nothing to do...
  }

}
