/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import net.sf.mmm.code.api.expression.CodeVariable;

/**
 * {@link CodeItem} that may {@link #getVariable(String) contain} {@link CodeVariable}s.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeItemWithVariables extends CodeItem {

  /**
   * @param name the {@link CodeVariable#getName() name} of the requested {@link CodeVariable}.
   * @return the {@link CodeVariable} with the given {@link CodeVariable#getName() name} or {@code null} if
   *         not found.
   */
  CodeVariable getVariable(String name);

}
