/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.item;

import net.sf.mmm.code.api.item.CodeChildItem;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.source.BaseSource;

/**
 * Base implementation of {@link CodeChildItem}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseChildItem extends BaseMutableItem implements CodeChildItem {

  private final BaseSource source;

  /**
   * The constructor.
   *
   * @param source the {@link #getSource() source}.
   */
  public BaseChildItem(BaseSource source) {

    super();
    this.source = source;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseChildItem} to copy.
   */
  public BaseChildItem(BaseChildItem template) {

    super(template);
    this.source = template.source;
  }

  @Override
  public BaseContext getContext() {

    return this.source.getContext();
  }

  @Override
  public BaseSource getSource() {

    return this.source;
  }

}
