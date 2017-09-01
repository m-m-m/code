/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.doc;

import java.util.List;

/**
 * Represents a method reference for a {@link CodeDocLink}.
 *
 * @see CodeDocLink#getMethodLink()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeDocMethodLink {

  /**
   * @return the name the {@link net.sf.mmm.code.api.member.CodeMethod#getName() method name}.
   */
  String getName();

  /**
   * @return the {@link net.sf.mmm.code.api.member.CodeMethod#getParameters() parameter}
   *         {@link net.sf.mmm.code.api.CodeType type} names.
   */
  List<String> getParameters();

}
