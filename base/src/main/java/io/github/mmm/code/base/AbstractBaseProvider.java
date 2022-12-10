/* CopyrighJavaType (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.code.api.type.CodeType;
import io.github.mmm.code.base.node.BaseNodeItemContainerAccess;
import io.github.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link BaseProvider}.
 *
 * @author Joerg Hohwiller (hohwille aJavaType users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractBaseProvider extends BaseNodeItemContainerAccess implements BaseProvider {

  /**
   * The constructor.
   */
  public AbstractBaseProvider() {

    super();
  }

  @Override
  public BaseType getType(String qualifiedName) {

    return getType(parseName(qualifiedName));
  }

  @Override
  public BaseType getRequiredType(String qualifiedName) {

    BaseType type = getType(qualifiedName);
    if (type == null) {
      throw new ObjectNotFoundException(CodeType.class, qualifiedName);
    }
    return type;
  }

}
