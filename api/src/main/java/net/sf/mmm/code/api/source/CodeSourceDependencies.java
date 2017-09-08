/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.source;

import net.sf.mmm.code.api.node.CodeNodeContainer;

/**
 * {@link CodeNodeContainer} containing {@link CodeSource}s. It represents the parents
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeSourceDependencies extends CodeNodeContainer<CodeSource> {

  @Override
  CodeSource getParent();

}
