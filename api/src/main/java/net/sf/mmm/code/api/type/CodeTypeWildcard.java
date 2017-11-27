/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import java.lang.reflect.WildcardType;

import net.sf.mmm.code.api.copy.CodeNodeItemCopyable;
import net.sf.mmm.code.api.node.CodeNodeItem;

/**
 * {@link CodeGenericType} representing a type variable. It is a variable as a placeholder for a {@link CodeGenericType
 * generic} {@link CodeType type}.
 *
 * @see java.lang.reflect.WildcardType
 * @see CodeParameterizedType#getTypeParameters()
 * @see CodeTypeParameters#getDeclared()
 * @see CodeTypeVariable
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeTypeWildcard extends CodeTypePlaceholder, CodeNodeItemCopyable<CodeNodeItem, CodeTypeWildcard> {

  @Override
  default String getName() {

    return NAME_WILDCARD;
  }

  @Override
  default boolean isWildcard() {

    return true;
  }

  @Override
  default CodeTypeWildcard asTypeWildcard() {

    return this;
  }

  @Override
  WildcardType getReflectiveObject();

  @Override
  CodeTypeWildcard copy();

}
