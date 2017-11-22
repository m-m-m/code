/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.expression;

import net.sf.mmm.code.api.member.CodeMember;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeExpression} referencing a {@link CodeMember} from an optional {@link #getExpression()
 * expression}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeMemberReference extends CodeExpression {

  /**
   * @return the optional {@link CodeExpression} where the {@link #getMember() member} is referenced from. May
   *         be {@code null} for an implicit reference based on the surrounding
   *         {@link net.sf.mmm.code.api.type.CodeType type}.
   */
  CodeExpression getExpression();

  /**
   * @return the optional {@link CodeGenericType} (typically {@link CodeType} but may be
   *         {@link CodeType#isQualified() qualified}) from which the {@link #getMember() member} shall be
   *         referenced statically. Should be {@code null} if {@link #getExpression() expression} is not
   *         {@code null} or if {@link #getMember() member} is not {@link CodeModifiers#isStatic() static}.
   */
  CodeGenericType getType();

  /**
   * @return the referenced {@link CodeMember}. May not be {@code null}.
   */
  CodeMember getMember();

}
