/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.io.IOException;
import java.util.function.BiFunction;

import net.sf.mmm.code.api.CodeFile;
import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.CodePathElement;
import net.sf.mmm.code.api.CodePathElements;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyType;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.node.BaseNodeItemContainerFlat;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodePathElements}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BasePathElements extends BaseNodeItemContainerFlat<CodePathElement> implements CodePathElements {

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
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BasePathElements(BasePathElements template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
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
  public CodePathElement get(String simpleName) {

    return get(simpleName, true);
  }

  CodePathElement get(String simpleName, boolean init) {

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
   * @param init - {@code true} to {@link #initialize() initialize}, {@code false} otherwise.
   * @return the {@link CodeFile} with the given {@code name} or {@code null} if not found.
   */
  public BaseFile getFile(String simpleName, boolean init) {

    CodePathElement child = get(simpleName, init);
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

  @Override
  public void add(CodePathElement item) {

    super.add(item);
  }

  @Override
  protected void addInternal(CodePathElement item) {

    super.addInternal(item);
  }

  @Override
  protected CodePathElement ensureParent(CodePathElement item) {

    if (item.getParent() != this.parent) {
      return doCopyNodeUnsafe(item, this.parent);
    }
    return item;
  }

  @Override
  public BasePackage getPackage(String simpleName) {

    return getPackage(simpleName, true);
  }

  /**
   * @param simpleName the {@link CodePackage#getSimpleName() simple name} of the requested {@link CodePackage}.
   * @param init - {@code true} to {@link #initialize() initialize}, {@code false} otherwise.
   * @return the {@link BasePackage} with the given {@code name} or {@code null} if not found.
   */
  public BasePackage getPackage(String simpleName, boolean init) {

    CodePathElement child = get(simpleName, init);
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
   * @param path the {@link CodeName} leading to the requested child. See {@link #getPackage(CodeName, boolean)}.
   * @param init - {@code true} to {@link #initialize() initialize}, {@code false} otherwise.
   * @return the traversed {@link BasePackage} or {@code null} if not found.
   */
  public BasePackage getPackage(CodeName path, boolean init) {

    return getPackage(path, init, null, false, false);
  }

  /**
   * @param path the {@link CodeName} leading to the requested child. See {@link #getPackage(CodeName, boolean)}.
   * @param init - {@code true} to {@link #initialize() initialize}, {@code false} otherwise.
   * @param factory the {@link BiFunction} used as factory to create missing packages.
   * @param add - {@code true} to {@link #add(CodePathElement) add} new packages created by the given {@code factory},
   *        {@code false} otherwise.
   * @return the traversed {@link BasePackage}. Has been created if it did not already exist and was produced by the
   *         given {@code factory}.
   */
  public BasePackage getPackage(CodeName path, boolean init, BiFunction<BasePackage, String, BasePackage> factory, boolean add) {

    return getPackage(path, init, factory, add, false);
  }

  /**
   * <b>Attention:</b> This is an internal API that should not be used from outside. Use
   * {@link #getPackage(CodeName, boolean, BiFunction, boolean)} instead.
   *
   * @param path the {@link CodeName} to traverse.
   * @param init - {@code true} to {@link #initialize() initialize}, {@code false} otherwise.
   * @param factory the {@link BiFunction} used as factory to create missing packages.
   * @param add - {@code true} to {@link #add(CodePathElement) add} new packages created by the given {@code factory},
   *        {@code false} otherwise.
   * @param forceAdd - {@code true} to force adding (if {@code add} is {@code true} but {@link #isImmutable() is
   *        immutable}), {@code false} otherwise.
   * @return the traversed {@link BasePackage}. Has been created if it did not already exist and was produced by the
   *         given {@code factory}.
   */
  protected BasePackage getPackage(CodeName path, boolean init, BiFunction<BasePackage, String, BasePackage> factory, boolean add, boolean forceAdd) {

    CodeName parentPath = path.getParent();
    String simpleName = path.getSimpleName();
    BasePathElements parentPathElements;
    if (parentPath == null) {
      if (simpleName.isEmpty()) {
        return this.parent;
      }
      parentPathElements = this;
    } else {
      BasePackage parentPkg = getPackage(parentPath, init, factory, add, forceAdd);
      if (parentPkg == null) {
        return null;
      }
      parentPathElements = parentPkg.getChildren();
    }
    BasePackage pkg = parentPathElements.getPackage(simpleName, init);
    if ((pkg == null) && (factory != null)) {
      pkg = factory.apply(parentPathElements.parent, simpleName);
      if (pkg == null) {
        return null;
      }
      if ((pkg.getParent() != parentPathElements.parent) || !simpleName.equals(pkg.getSimpleName())) {
        throw new IllegalStateException("Invalid factory: " + factory.getClass().toGenericString());
      }
      if (add) {
        if (forceAdd) {
          parentPathElements.addInternal(pkg);
        } else {
          parentPathElements.add(pkg);
        }
      }
    }
    return pkg;
  }

  @Override
  public CodePackage getOrCreatePackage(CodeName path, boolean add) {

    return getOrCreatePackage(path, add, true);
  }

  /**
   * @param path the {@link CodeName} leading to the requested child. See
   *        {@link #getOrCreatePackage(CodeName, boolean)}.
   * @param add - {@code true} to {@link #add(CodePathElement) add} newly created packages, {@code false} otherwise.
   * @param init - {@code true} to {@link #initialize() initialize}, {@code false} otherwise.
   * @return the traversed {@link BasePackage}. Has been created if it did not already exist.
   */
  public BasePackage getOrCreatePackage(CodeName path, boolean add, boolean init) {

    return getOrCreatePackage(path, add, init, false);
  }

  /**
   * @param path the {@link CodeName} leading to the requested child. See
   *        {@link #getOrCreatePackage(CodeName, boolean)}.
   * @param add - {@code true} to {@link #add(CodePathElement) add} newly created packages, {@code false} otherwise.
   * @param init - {@code true} to {@link #initialize() initialize}, {@code false} otherwise.
   * @param forceAdd - {@code true} to force adding (if {@code add} is {@code true} but {@link #isImmutable() is
   *        immutable}), {@code false} otherwise.
   * @return the traversed {@link BasePackage}. Has been created if it did not already exist.
   */
  protected BasePackage getOrCreatePackage(CodeName path, boolean add, boolean init, boolean forceAdd) {

    return getPackage(path, init, (parentPkg, simpleName) -> new BasePackage(parentPkg, simpleName), add, forceAdd);
  }

  @Override
  public BasePackage createPackage(String simpleName) {

    return new BasePackage(this.parent, simpleName);
  }

  @Override
  public BaseFile getOrCreateFile(CodeName path, boolean add) {

    return getOrCreateFile(path, add, true);
  }

  /**
   * @param path the {@link CodeName} leading to the requested child. See {@link #getOrCreateFile(CodeName, boolean)}.
   * @param add - {@code true} to add a newly created {@link CodeFile}, {@code false} otherwise.
   * @param init - {@code true} to {@link #initialize() initialize}, {@code false} otherwise.
   * @return the {@link #getFile(String) existing} or {@link #createFile(String) newly created} {@link CodeFile}.
   */
  public BaseFile getOrCreateFile(CodeName path, boolean add, boolean init) {

    return getOrCreateFile(path, add, init, false);
  }

  /**
   * @param path the {@link CodeName} leading to the requested child. See {@link #getOrCreateFile(CodeName, boolean)}.
   * @param add - {@code true} to add a newly created {@link CodeFile}, {@code false} otherwise.
   * @param init - {@code true} to {@link #initialize() initialize}, {@code false} otherwise.
   * @param forceAdd - {@code true} to force adding (if {@code add} is {@code true} but {@link #isImmutable() is
   *        immutable}), {@code false} otherwise.
   * @return the {@link #getFile(String) existing} or {@link #createFile(String) newly created} {@link CodeFile}.
   */
  protected BaseFile getOrCreateFile(CodeName path, boolean add, boolean init, boolean forceAdd) {

    CodeName parentPath = path.getParent();
    BasePackage pkg;
    if (parentPath == null) {
      pkg = this.parent;
    } else {
      pkg = getOrCreatePackage(parentPath, add, init, forceAdd);
    }
    String simpleName = path.getSimpleName();
    BaseFile file = pkg.getChildren().getFile(simpleName);
    if (file == null) {
      file = createFile(simpleName, add, forceAdd);
    }
    return file;
  }

  @Override
  public BaseFile createFile(String simpleName) {

    return createFile(simpleName, false, false);
  }

  private BaseFile createFile(String simpleName, boolean add, boolean forceAdd) {

    BaseFile file = new BaseFile(this.parent, simpleName);
    if (add) {
      if (forceAdd) {
        addInternal(file);
      } else {
        add(file);
      }
    }
    return file;
  }

  @Override
  public CodeType getType(String simpleName) {

    return getType(simpleName, true);
  }

  /**
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the requested {@link CodeType}.
   * @param init - {@code true} to {@link #initialize() initialize}, {@code false} otherwise.
   * @return the {@link CodeType} with the given {@code name} or {@code null} if not found.
   */
  public CodeType getType(String simpleName, boolean init) {

    BaseFile file = getFile(simpleName, init);
    if (file != null) {
      return file.getType();
    }
    if (init) {
      initialize();
    }
    for (CodePathElement child : getList()) {
      if (child.isFile()) {
        CodeType type = ((BaseFile) child).getType(simpleName, init);
        if (type != null) {
          return type;
        }
      }
    }
    return null;
  }

  @Override
  public BaseType createType(String simpleName) {

    return createFile(simpleName).getType();
  }

  @Override
  public boolean containsPackage(CodePackage child) {

    if (child == null) {
      return false;
    }
    CodePackage anchestor = child;
    while (anchestor != null) {
      if (anchestor == this.parent) {
        return true;
      }
      anchestor = anchestor.getParentPackage();
    }
    return false;
  }

  @Override
  public boolean containsSubPackage(CodePackage child) {

    if (child == null) {
      return false;
    }
    return containsPackage(child.getParentPackage());
  }

  @Override
  public BasePathElements copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BasePathElements copy(CodeCopyMapper mapper) {

    return new BasePathElements(this, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    // packages write their children to separate files - nothing to do...
  }

  @Override
  public String toString() {

    String pkg = this.parent.getQualifiedName();
    if (pkg.isEmpty()) {
      pkg = "«root»";
    }
    return pkg + ".getChildren()";
  }

}
