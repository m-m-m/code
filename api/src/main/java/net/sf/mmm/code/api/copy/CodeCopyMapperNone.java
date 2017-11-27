/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.copy;

import net.sf.mmm.code.api.node.CodeNode;

/**
 * Implementation of {@link CodeCopyMapper} for identity mapping.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class CodeCopyMapperNone implements CodeCopyMapper {

  /** The singleton instance of this class. */
  public static final CodeCopyMapperNone INSTANCE = new CodeCopyMapperNone();

  private CodeCopyMapperNone() {

    super();
  }

  @Override
  public <N extends CodeNode> N map(N node) {

    return node;
  }

}
