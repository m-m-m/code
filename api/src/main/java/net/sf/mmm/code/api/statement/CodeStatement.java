/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.statement;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.item.CodeItemWithVariables;

/**
 * {@link CodeItem} with a {@link CodeAtomicStatement single statement} or a complete
 * {@link net.sf.mmm.code.api.block.CodeBlockStatement block} of statements. A {@link CodeStatement} is what
 * can be inside a {@link net.sf.mmm.code.api.block.CodeBlockBody body}.
 *
 * @see net.sf.mmm.code.api.member.CodeOperation#getBody()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeStatement extends CodeItemWithVariables {

}
