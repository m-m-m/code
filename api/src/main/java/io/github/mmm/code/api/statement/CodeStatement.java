/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.statement;

import io.github.mmm.code.api.item.CodeItem;
import io.github.mmm.code.api.item.CodeItemWithVariables;

/**
 * {@link CodeItem} with a {@link CodeAtomicStatement single statement} or a complete
 * {@link io.github.mmm.code.api.block.CodeBlockStatement block} of statements. A {@link CodeStatement} is what
 * can be inside a {@link io.github.mmm.code.api.block.CodeBlockBody body}.
 *
 * @see io.github.mmm.code.api.member.CodeOperation#getBody()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeStatement extends CodeItemWithVariables {

}
