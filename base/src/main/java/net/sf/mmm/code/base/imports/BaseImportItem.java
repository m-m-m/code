/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.imports;

import java.io.IOException;
import java.util.Objects;

import net.sf.mmm.code.api.imports.CodeImportItem;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.base.item.BaseItem;

/**
 * Base implementation of {@link CodeImportItem} (actually specific for TypeScript).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseImportItem extends BaseItem implements CodeImportItem {

  private final String item;

  private final String alias;

  /**
   * The constructor.
   *
   * @param item the {@link #getItem() item}.
   */
  public BaseImportItem(String item) {

    this(item, null);
  }

  /**
   * The constructor.
   *
   * @param item the {@link #getItem() item}.
   * @param alias the {@link #getAlias() alias}.
   */
  public BaseImportItem(String item, String alias) {

    super();
    Objects.requireNonNull(item, "item");
    this.item = item;
    this.alias = alias;
  }

  @Override
  public String getItem() {

    return this.item;
  }

  @Override
  public String getAlias() {

    return this.alias;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    sink.append(this.item);
    if (this.alias != null) {
      sink.append(" as ");
      sink.append(this.alias);
    }
  }

}
