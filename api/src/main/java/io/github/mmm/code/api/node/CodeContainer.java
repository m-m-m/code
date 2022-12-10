/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.node;

/**
 * CodeNode as common anchestor of {@link io.github.mmm.code.api.CodePackage} and
 * {@link io.github.mmm.code.api.source.CodeSource}.
 *
 * @see io.github.mmm.code.api.CodePackage#getParent()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeContainer extends CodeNode {

}
