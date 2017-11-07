/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import java.util.List;

import net.sf.mmm.code.api.node.CodeNodeItemContainerFlat;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeNodeItemContainerFlat} containing the {@link CodePathElement}s of a {@link CodePackage}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <P> the type of the contained {@link CodePathElement}s.
 * @since 1.0.0
 */
public interface CodePathElements<P extends CodePathElement> extends CodeNodeItemContainerFlat<P> {

  @Override
  CodePackage getParent();

  /**
   * <b>Attention:</b><br>
   * This method can trigger expensive classpath scanning (or filesystem traversal) on its first call.
   */
  @Override
  List<? extends P> getDeclared();

  /**
   * @param simpleName the {@link CodePathElement#getSimpleName() simple name} of the requested
   *        {@link CodePathElement}.
   * @return the {@link CodePathElement} from {@link #getDeclared() all items} with the given {@code name} or
   *         {@code null} if not found.
   */
  P get(String simpleName);

  /**
   * @param simpleName the {@link CodeFile#getSimpleName() simple name} of the requested {@link CodeFile}.
   * @return the {@link CodeFile} with the given {@code name} or {@code null} if not found.
   */
  CodeFile getFile(String simpleName);

  /**
   * @param path the {@link CodeName} to traverse.
   * @return the traversed {@link CodeFile} or {@code null} if not found.
   */
  CodeFile getFile(CodeName path);

  /**
   * @param simpleName the {@link CodeFile#getSimpleName() simple name} of the new {@link CodeFile}.
   * @return a new {@link CodeFile} containing an empty top-level {@link CodeType} of the same name.
   */
  CodeFile createFile(String simpleName);

  /**
   * @param path the {@link CodeName} leading to the requested child relative to the {@link #getParent()
   *        package of this children}. Will typically be invoked on the {@link CodePackage#getChildren()
   *        children} of a {@link net.sf.mmm.code.api.source.CodeSource#getRootPackage() root package} with a
   *        {@link net.sf.mmm.code.api.item.CodeItemWithQualifiedName#getQualifiedName() qualified name}
   *        parsed via {@link net.sf.mmm.code.api.source.CodeSource#parseName(String)}.
   * @param add - {@code true} to add a newly created {@link CodeFile}, {@code false} otherwise.
   * @return the {@link #getFile(String) existing} or {@link #createFile(String) newly created}
   *         {@link CodeFile}.
   */
  CodeFile getOrCreateFile(CodeName path, boolean add);

  /**
   * @param simpleName the {@link CodePackage#getSimpleName() simple name} of the requested
   *        {@link CodePackage}.
   * @return the {@link CodePackage} with the given {@code name} or {@code null} if not found.
   */
  CodePackage getPackage(String simpleName);

  /**
   * @param path the {@link CodeName} leading to the requested child relative to the {@link #getParent()
   *        package of this children}. Will typically be invoked on the {@link CodePackage#getChildren()
   *        children} of a {@link net.sf.mmm.code.api.source.CodeSource#getRootPackage() root package} with a
   *        {@link net.sf.mmm.code.api.item.CodeItemWithQualifiedName#getQualifiedName() qualified name}
   *        parsed via {@link net.sf.mmm.code.api.source.CodeSource#parseName(String)}.
   * @return the traversed {@link CodePackage} or {@code null} if not found.
   */
  CodePackage getPackage(CodeName path);

  /**
   * @param path the {@link CodeName} leading to the requested child relative to the {@link #getParent()
   *        package of this children}. Will typically be invoked on the {@link CodePackage#getChildren()
   *        children} of a {@link net.sf.mmm.code.api.source.CodeSource#getRootPackage() root package} with a
   *        {@link net.sf.mmm.code.api.item.CodeItemWithQualifiedName#getQualifiedName() qualified name}
   *        parsed via {@link net.sf.mmm.code.api.source.CodeSource#parseName(String)}.
   * @param add - {@code true} to add newly created packages, {@code false} otherwise.
   * @return the traversed {@link CodePackage}. Has been created if it did not already exist.
   */
  CodePackage getOrCreatePackage(CodeName path, boolean add);

  /**
   * @param simpleName the {@link CodePackage#getSimpleName() simple name} of the new {@link CodePackage}.
   * @return a new empty {@link CodePackage}.
   */
  CodePackage createPackage(String simpleName);

  /**
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the requested {@link CodeType}.
   * @return the {@link CodeType} with the given {@code name} or {@code null} if not found.
   */
  CodeType getType(String simpleName);

  /**
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the requested {@link CodeType}.
   * @return the top-level {@link CodeType} that has been created together with its {@link CodeFile}.
   * @see #createFile(String)
   */
  CodeType createType(String simpleName);

}
