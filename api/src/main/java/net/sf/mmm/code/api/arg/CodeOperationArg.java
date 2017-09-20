/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.arg;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.node.CodeNodeItemWithDeclaringOperation;
import net.sf.mmm.code.api.node.CodeNodeItemWithType;

/**
 * A {@link CodeNodeItemWithType} representing a argument of a {@link CodeOperation} such as
 * {@link CodeParameter}, {@link CodeException} or {@link CodeReturn}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeOperationArg extends CodeElement, CodeNodeItemWithType, CodeNodeItemWithDeclaringOperation {

}
