/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.source;

import io.github.mmm.code.api.node.CodeNodeContainer;

/**
 * {@link CodeNodeContainer} containing {@link CodeSource}s. It represents the parents
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <S> the type of the contained {@link CodeSource}s.
 * @since 1.0.0
 */
public interface CodeSourceDependencies<S extends CodeSource> extends CodeNodeContainer<S> {

  @Override
  CodeSource getParent();

}
