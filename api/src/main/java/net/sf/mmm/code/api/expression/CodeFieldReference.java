/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.expression;

import net.sf.mmm.code.api.member.CodeField;

/**
 * {@link CodeExpression} referencing a {@link CodeField} from an optinal {@link #getExpression() expression}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeFieldReference extends CodeMemberReference {

  @Override
  CodeField getMember();

}
