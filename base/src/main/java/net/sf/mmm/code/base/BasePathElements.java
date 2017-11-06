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

    return get(simpleName, true);
  }

  BasePathElement get(String simpleName, boolean init) {

    if (init) {
      initialize();
    }
    return getByName(simpleName);
  }

  @Override
  public BaseFile getFile(String simpleName) {

    return getFile(simpleName, true);
  }

  /**
   * @param simpleName the {@link CodeFile#getSimpleName() simple name} of the requested {@link CodeFile}.
   * @param init - {@code true} to initialize, {@code false} otherwise.
   * @return the {@link CodeFile} with the given {@code name} or {@code null} if not found.
   */
  public BaseFile getFile(String simpleName, boolean init) {

    BasePathElement child = get(simpleName, init);
    if ((child != null) && child.isFile()) {
      return (BaseFile) child;
    }
    return null;
  }

  @Override
  public BaseFile getFile(CodeName path) {

    return getFile(path, false);
  }

  BaseFile getFile(CodeName path, boolean init) {

    CodeName parentPath = path.getParent();
    if (parentPath == null) {
      return getFile(path.getSimpleName(), init);
    }
    BasePackage pkg = getPackage(parentPath, init);
    return pkg.getChildren().getFile(path.getSimpleName(), init);
  }

  /**
   * @return the internal {@link List} without initialization.
   * @see #getDeclared()
   */
  public List<BasePathElement> getInternalList() {

    return getList();
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

    return getPackage(simpleName, true);
  }

  /**
   * @param simpleName the {@link CodePackage#getSimpleName() simple name} of the requested
   *        {@link CodePackage}.
   * @param init - {@code true} to initialize, {@code false} otherwise.
   * @return the {@link CodePackage} with the given {@code name} or {@code null} if not found.
   */
  public BasePackage getPackage(String simpleName, boolean init) {

    BasePathElement child = get(simpleName, init);
    if ((child != null) && !child.isFile()) {
      return (BasePackage) child;
    }
    return null;
  }

  @Override
  public BasePackage getPackage(CodeName path) {

    return getPackage(path, true);
  }

  /**
   * @param path the {@link CodeName} to traverse.
   * @param init - {@code true} to initialize, {@code false} otherwise.
   * @return the traversed {@link CodePackage} or {@code null} if not found.
   */
  public BasePackage getPackage(CodeName path, boolean init) {

    CodeName parentPath = path.getParent();
    String simpleName = path.getSimpleName();
    if (parentPath == null) {
      if (simpleName.isEmpty()) {
        return this.parent;
      }
      return getPackage(simpleName, init);
    }
    BasePackage parentPkg = getPackage(parentPath, init);
    if (parentPkg != null) {
      return parentPkg.getChildren().getPackage(simpleName, init);
    }
    return null;
  }

  /**
   * @param path the {@link CodeName} to traverse.
   * @return the traversed {@link CodePackage}. Has been created if it did not already exist.
   */
  public BasePackage getOrCreatePackage(CodeName path) {

    return getOrCreatePackage(path, null);
  }

  /**
   * <b>Attention:</b> This is an internal API that should not be used from outside. Use
   * {@link #getOrCreatePackage(CodeName)} instead.
   *
   * @param path the {@link CodeName} to traverse.
   * @param sourceSupplierFunction the {@link BiFunction} for lazy loading of source-code.
   * @return the traversed {@link CodePackage}. Has been created if it did not already exist.
   */
  public BasePackage getOrCreatePackage(CodeName path, BiFunction<BasePackage, String, Supplier<BasePackage>> sourceSupplierFunction) {

    CodeName parentPath = path.getParent();
    String simpleName = path.getSimpleName();
    BasePathElements parentPathElements;
    if (parentPath == null) {
      if (simpleName.isEmpty()) {
        return this.parent;
      }
      parentPathElements = this;
    } else {
      parentPathElements = getOrCreatePackage(parentPath, sourceSupplierFunction).getChildren();
    }
    BasePackage pkg = parentPathElements.getPackage(simpleName, false);
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

    BasePackage pkg = new BasePackage(this.parent, simpleName, null, sourceSupplier);
    if (add && isMutable()) {
      add(pkg);
    }
    return pkg;
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

    return getType(simpleName, true);
  }

  /**
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the requested {@link CodeType}.
   * @param init - {@code true} to initialize, {@code false} otherwise.
   * @return the {@link CodeType} with the given {@code name} or {@code null} if not found.
   */
  public BaseType getType(String simpleName, boolean init) {

    BaseFile file = getFile(simpleName, init);
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
