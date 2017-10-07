/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.expression;

/**
 * {@link CodeVariable} for the surrounding instance. In Java and some various languages called {@code this}.
 * In some other languages called {@code self}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeVariableThis extends CodeVariable {

  /** {@link #getName() Name} {@value}. */
  String NAME_THIS = "this";

  /** {@link #getName() Name} {@value}. */
  String NAME_SELF = "self";

}
