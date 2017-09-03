/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.statement;

import net.sf.mmm.code.api.item.CodeItem;

/**
 * {@link CodeItem} with a statement or a complete {@link CodeBlock block} of statements. A
 * {@link CodeStatement} is what is inside an {@link net.sf.mmm.code.api.member.CodeOperation operation}.
 *
 * @see net.sf.mmm.code.api.member.CodeOperation#getBody()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeStatement extends CodeItem {

}
