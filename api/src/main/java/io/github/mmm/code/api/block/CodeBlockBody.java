/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.block;

import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.node.CodeFunction;

/**
 * {@link CodeBlock} for the {@link io.github.mmm.code.api.node.CodeFunction#getBody() body} of a
 * {@link io.github.mmm.code.api.node.CodeFunction}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeBlockBody extends CodeBlock, CodeNodeItemCopyable<CodeFunction, CodeBlockBody> {

  @Override
  CodeBlockBody copy();

}
