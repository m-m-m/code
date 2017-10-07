/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.block;

import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeBlock} for initializer code of a {@link net.sf.mmm.code.api.type.CodeType}.
 *
 * @author hohwille
 * @since 1.0.0
 * @see net.sf.mmm.code.api.type.CodeType#getStaticInitializer()
 * @see net.sf.mmm.code.api.type.CodeType#getNonStaticInitializer()
 */
public interface CodeBlockInitializer extends CodeBlock {

  @Override
  CodeType getParent();

}
