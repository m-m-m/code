/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import java.util.List;

import net.sf.mmm.code.api.node.CodeNodeItemContainerFlat;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeNodeItemContainerFlat} containing the {@link CodePathElement}s of a {@link CodePackage}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodePathElements extends CodeNodeItemContainerFlat<CodePathElement> {

  @Override
  CodePackage getParent();

  /**
   * <b>Attention:</b><br>
   * This method can trigger expensive classpath scanning (or filesystem traversal) on its first call.
   */
  @Override
  List<? extends CodePathElement> getAll();

  /**
   * @param simpleName the {@link CodePathElement#getSimpleName() simple name} of the requested
   *        {@link CodePathElement}.
   * @return the {@link CodePathElement} from {@link #getAll() all items} with the given {@code name} or
   *         {@code null} if not found.
   */
  CodePathElement get(String simpleName);

  /**
   * @param simpleName the {@link CodePathElement#getSimpleName() simple name} of the requested
   *        {@link CodePathElement}.
   * @param withoutSuperLayer {@code false} to recursively traverse {@link CodePackage#getSuperLayerPackage()
   *        super layer packages} during the search what is the default if this flag is omitted, {@code true}
   *        to only consider the direct children of the package.
   * @return the {@link CodePathElement} from {@link #getAll() all items} with the given {@code name} or
   *         {@code null} if not found.
   */
  CodePathElement get(String simpleName, boolean withoutSuperLayer);

  /**
   * @param simpleName the {@link CodeFile#getSimpleName() simple name} of the requested {@link CodeFile}.
   * @return the {@link CodeFile} with the given {@code name} or {@code null} if not found.
   */
  CodeFile getFile(String simpleName);

  /**
   * @param simpleName the {@link CodeFile#getSimpleName() simple name} of the requested {@link CodeFile}.
   * @param withoutSuperLayer {@code false} to recursively traverse {@link CodePackage#getSuperLayerPackage()
   *        super layer packages} during the search what is the default if this flag is omitted, {@code true}
   *        to only consider the direct children of the package.
   * @return the {@link CodeFile} with the given {@code name} or {@code null} if not found.
   */
  CodeFile getFile(String simpleName, boolean withoutSuperLayer);

  /**
   * @param path the {@link CodeName} to traverse.
   * @return the traversed {@link CodeFile} or {@code null} if not found.
   */
  CodeFile getFile(CodeName path);

  /**
   * @param path the {@link CodeName} to traverse.
   * @param withoutSuperLayer {@code false} to recursively traverse {@link CodePackage#getSuperLayerPackage()
   *        super layer packages} during the search what is the default if this flag is omitted, {@code true}
   *        to only consider the direct children of the package.
   * @return the traversed {@link CodeFile} or {@code null} if not found.
   */
  CodeFile getFile(CodeName path, boolean withoutSuperLayer);

  /**
   * @param simpleName the {@link CodeFile#getSimpleName() simple name} of the new {@link CodeFile}.
   * @return a new {@link CodeFile} containing an empty top-level {@link CodeType} of the same name that has
   *         been added.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  CodeFile addFile(String simpleName);

  /**
   * @param simpleName the {@link CodeFile#getSimpleName() simple name} of the new {@link CodeFile}.
   * @return a new {@link CodeFile} containing an empty top-level {@link CodeType} of the same name. It was
   *         only added if {@link #isMutable() mutalbe}.
   */
  CodeFile createFile(String simpleName);

  /**
   * @param simpleName the {@link CodeFile#getSimpleName() simple name} of the requested {@link CodeFile}.
   * @return the {@link #getFile(String) existing} or {@link #createFile(String) newly created}
   *         {@link CodeFile}.
   */
  CodeFile getOrCreateFile(String simpleName);

  /**
   * @param simpleName the {@link CodePackage#getSimpleName() simple name} of the requested
   *        {@link CodePackage}.
   * @return the {@link CodePackage} with the given {@code name} or {@code null} if not found.
   */
  CodePackage getPackage(String simpleName);

  /**
   * @param simpleName the {@link CodePackage#getSimpleName() simple name} of the requested
   *        {@link CodePackage}.
   * @param withoutSuperLayer {@code false} to recursively traverse {@link CodePackage#getSuperLayerPackage()
   *        super layer packages} during the search what is the default if this flag is omitted, {@code true}
   *        to only consider the direct children of the package.
   * @return the {@link CodePackage} with the given {@code name} or {@code null} if not found.
   */
  CodePackage getPackage(String simpleName, boolean withoutSuperLayer);

  /**
   * @param path the {@link CodeName} to traverse.
   * @return the traversed {@link CodePackage} or {@code null} if not found.
   */
  CodePackage getPackage(CodeName path);

  /**
   * @param path the {@link CodeName} to traverse.
   * @param withoutSuperLayer {@code false} to recursively traverse {@link CodePackage#getSuperLayerPackage()
   *        super layer packages} during the search what is the default if this flag is omitted, {@code true}
   *        to only consider the direct children of the package.
   * @return the traversed {@link CodePackage} or {@code null} if not found.
   */
  CodePackage getPackage(CodeName path, boolean withoutSuperLayer);

  /**
   * @param simpleName the {@link CodePackage#getSimpleName() simple name} of the new {@link CodePackage}.
   * @return a new empty {@link CodePackage} that has been added.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  CodePackage addPackage(String simpleName);

  /**
   * @param simpleName the {@link CodePackage#getSimpleName() simple name} of the new {@link CodePackage}.
   * @return a new empty {@link CodePackage}. It was only added if {@link #isMutable() mutalbe}.
   */
  CodePackage createPackage(String simpleName);

  /**
   * @param simpleName the {@link CodePackage#getSimpleName() simple name} of the requested
   *        {@link CodePackage}.
   * @return the {@link #getPackage(String) existing} or {@link #createPackage(String) newly created}
   *         {@link CodePackage}.
   */
  CodePackage getOrCreatePackage(String simpleName);

  /**
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the requested {@link CodeType}.
   * @return the {@link CodeType} with the given {@code name} or {@code null} if not found.
   */
  CodeType getType(String simpleName);

  /**
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the requested {@link CodeType}.
   * @param withoutSuperLayer {@code false} to recursively traverse {@link CodePackage#getSuperLayerPackage()
   *        super layer packages} during the search what is the default if this flag is omitted, {@code true}
   *        to only consider the direct children of the package.
   * @return the {@link CodeType} with the given {@code name} or {@code null} if not found.
   */
  CodeType getType(String simpleName, boolean withoutSuperLayer);

  /**
   * @param simpleName the {@link CodeFile#getSimpleName() simple name} of the new {@link CodeType}.
   * @return a new {@link CodeType} together with a new {@link CodeFile} that has been added.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   * @see #addFile(String)
   */
  CodeType addType(String simpleName);

  /**
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the requested {@link CodeType}.
   * @return the top-level {@link CodeType} that has been created together with its {@link CodeFile}. It was
   *         only added if {@link #isMutable() mutalbe}.
   * @see #createFile(String)
   */
  CodeType createType(String simpleName);
}
