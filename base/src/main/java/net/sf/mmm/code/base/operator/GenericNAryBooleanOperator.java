/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.operator;

import net.sf.mmm.code.api.operator.CodeNAryBooleanOperator;

/**
 * Generic implementation of {@link CodeNAryBooleanOperator}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class GenericNAryBooleanOperator extends GenericNAryOperator implements CodeNAryBooleanOperator {

  /** Instance for {@link #NAME_OR}. */
  public static final GenericNAryBooleanOperator OR = new GenericNAryBooleanOperator(NAME_OR);

  /** Instance for {@link #NAME_AND}. */
  public static final GenericNAryBooleanOperator AND = new GenericNAryBooleanOperator(NAME_AND);

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   */
  public GenericNAryBooleanOperator(String name) {

    super(name);
  }

  @Override
  public boolean isNumeric() {

    return false;
  }

  /**
   * @param name the {@link #getName() name} of the requested {@link CodeNAryBooleanOperator}.
   * @return the {@link CodeNAryBooleanOperator} or {@code null} if not found.
   */
  public static CodeNAryBooleanOperator of(String name) {

    return of(name, CodeNAryBooleanOperator.class);
  }

  static void initialize() {

    // ensure class-loading so constant fields are created...
  }
}
