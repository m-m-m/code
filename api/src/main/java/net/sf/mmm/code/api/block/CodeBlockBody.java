/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.block;

import net.sf.mmm.code.api.copy.CodeNodeItemCopyable;
import net.sf.mmm.code.api.node.CodeFunction;

/**
 * {@link CodeBlock} for the {@link net.sf.mmm.code.api.node.CodeFunction#getBody() body} of a
 * {@link net.sf.mmm.code.api.node.CodeFunction}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeBlockBody extends CodeBlock, CodeNodeItemCopyable<CodeFunction, CodeBlockBody> {

}
