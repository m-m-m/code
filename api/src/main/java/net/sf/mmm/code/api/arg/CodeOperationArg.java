/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.arg;

import net.sf.mmm.code.api.CodeElement;
import net.sf.mmm.code.api.CodeItemWithType;
import net.sf.mmm.code.api.member.CodeOperation;

/**
 * A {@link CodeItemWithType} representing a argument of a {@link CodeOperation} such as {@link CodeParameter},
 * {@link CodeException} or {@link CodeReturn}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeOperationArg extends CodeElement, CodeItemWithType {

}
