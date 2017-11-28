/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.operator;

import net.sf.mmm.code.api.operator.CodeNAryArbitraryOperator;

/**
 * Base implementation of {@link CodeNAryArbitraryOperator}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class BaseNAryArbitraryOperator extends BaseNAryOperator implements CodeNAryArbitraryOperator {

  /** Instance for {@link #NAME_ADD}. */
  public static final BaseNAryArbitraryOperator ADD = new BaseNAryArbitraryOperator(NAME_ADD);

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   */
  public BaseNAryArbitraryOperator(String name) {

    super(name);
  }

  @Override
  public boolean isBoolean() {

    return false;
  }

  /**
   * @param name the {@link #getName() name} of the requested {@link CodeNAryArbitraryOperator}.
   * @return the {@link CodeNAryArbitraryOperator} or {@code null} if not found.
   */
  public static CodeNAryArbitraryOperator of(String name) {

    return of(name, CodeNAryArbitraryOperator.class);
  }

  static void initialize() {

    // ensure class-loading so constant fields are created...
  }
}
