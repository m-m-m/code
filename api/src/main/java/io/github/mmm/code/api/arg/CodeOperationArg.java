/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.arg;

import io.github.mmm.code.api.element.CodeElementWithDeclaringType;
import io.github.mmm.code.api.item.CodeMutableItemWithType;
import io.github.mmm.code.api.member.CodeOperation;
import io.github.mmm.code.api.node.CodeNodeItemWithDeclaringOperation;

/**
 * A {@link CodeMutableItemWithType} representing a argument of a {@link CodeOperation} such as
 * {@link CodeParameter}, {@link CodeException} or {@link CodeReturn}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeOperationArg extends CodeElementWithDeclaringType, CodeMutableItemWithType, CodeNodeItemWithDeclaringOperation {

}
