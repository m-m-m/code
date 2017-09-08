/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import net.sf.mmm.code.api.node.CodeNodeItemContainerHierarchicalWithName;

/**
 * {@link CodeMembers} as a container for the {@link CodeField}s.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeFields extends CodeMembers<CodeField>, CodeNodeItemContainerHierarchicalWithName<CodeField> {

  @Override
  CodeFields copy();

}
