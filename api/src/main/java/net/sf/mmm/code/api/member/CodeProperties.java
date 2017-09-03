/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

/**
 * {@link CodeMembers} as a container for the {@link CodeProperty}s. <br>
 * <b>Attention:</b><br>
 * The {@link CodeProperty properties} are calculated on the fly and the operations may be expensive. Avoid
 * subsequent calls to the same method if possible.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeProperties extends CodeMembers<CodeProperty> {

  /**
   * @param name the {@link CodeField#getName() name} of the requested {@link CodeField}.
   * @return the requested {@link CodeField} (may be inherited from super-types) or {@code null} if no such
   *         field exists.
   */
  CodeProperty getProperty(String name);

}
