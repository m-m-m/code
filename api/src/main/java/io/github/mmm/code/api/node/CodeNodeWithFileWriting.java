/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.node;

import java.nio.charset.Charset;
import java.nio.file.Path;

import io.github.mmm.code.api.CodePackage;
import io.github.mmm.code.api.item.CodeItem;

/**
 * {@link CodeItem} with {@link #write(Path) file writing support}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeNodeWithFileWriting extends CodeNode {

  /**
   * Recursively writes the {@link io.github.mmm.code.api.item.CodeItem#getSourceCode() source code} of this item
   * recursively with all its children to the folder identified by the given {@link Path}.
   *
   * @param targetFolder the {@link Path} pointing to the folder where to write the content of the given
   *        {@link CodePackage}.
   */
  void write(Path targetFolder);

  /**
   * Recursively writes the {@link io.github.mmm.code.api.item.CodeItem#getSourceCode() source code} of this item
   * recursively with all its children to the folder identified by the given {@link Path}.
   *
   * @param targetFolder the {@link Path} pointing to the folder where to write the content of the given
   *        {@link CodePackage}.
   * @param encoding the {@link Charset} used to encode the characters in the file.
   */
  void write(Path targetFolder, Charset encoding);

}
