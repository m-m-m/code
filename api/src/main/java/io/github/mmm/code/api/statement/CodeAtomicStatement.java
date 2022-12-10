/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.statement;

import io.github.mmm.code.api.block.CodeBlock;

/**
 * An atomic {@link CodeStatement} (unlike a {@link CodeBlock}).
 *
 * @see io.github.mmm.code.api.member.CodeOperation#getBody()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeAtomicStatement extends CodeStatement {

}
