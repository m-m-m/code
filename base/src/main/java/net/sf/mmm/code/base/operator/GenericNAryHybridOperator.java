/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.operator;

import net.sf.mmm.code.api.operator.CodeNAryHybridOperator;

/**
 * Generic implementation of {@link CodeNAryHybridOperator}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class GenericNAryHybridOperator extends GenericNAryOperator implements CodeNAryHybridOperator {

  /** Instance for {@link #NAME_XOR}. */
  public static final GenericNAryHybridOperator XOR = new GenericNAryHybridOperator(NAME_XOR);

  /** Instance for {@link #NAME_BIT_OR}. */
  public static final GenericNAryHybridOperator BIT_OR = new GenericNAryHybridOperator(NAME_BIT_OR);

  /** Instance for {@link #NAME_BIT_AND}. */
  public static final GenericNAryHybridOperator BIT_AND = new GenericNAryHybridOperator(NAME_BIT_AND);

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   */
  public GenericNAryHybridOperator(String name) {

    super(name);
  }

  /**
   * @param name the {@link #getName() name} of the requested {@link CodeNAryHybridOperator}.
   * @return the {@link CodeNAryHybridOperator} or {@code null} if not found.
   */
  public static CodeNAryHybridOperator of(String name) {

    return of(name, CodeNAryHybridOperator.class);
  }

  static void initialize() {

    // ensure class-loading so constant fields are created...
  }
}
