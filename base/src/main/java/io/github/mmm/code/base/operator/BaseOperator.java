/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.operator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.base.exception.ObjectMismatchException;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.operator.CodeOperator;
import io.github.mmm.code.base.item.BaseItem;

/**
 * Base implementation of {@link CodeOperator}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseOperator extends BaseItem implements CodeOperator {

  private static final Map<String, BaseOperator> MAP = new HashMap<>();

  private final String name;

  static {

    BaseComparisonOperator.init();
    BaseUnaryOperator.init();
    BaseNAryOperator.init();
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   */
  protected BaseOperator(String name) {

    super();
    Objects.requireNonNull(name, "name");
    assert (!name.contains(" "));
    this.name = name;
    BaseOperator existing = MAP.get(name);
    if (existing != null) {
      throw new DuplicateObjectException(this, name, existing);
    }
    MAP.put(name, this);
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent,
      CodeLanguage language) throws IOException {

    sink.append(this.name);
  }

  @Override
  public String toString() {

    return this.name;
  }

  /**
   * @param name the {@link #getName() name} of the requested {@link CodeOperator}.
   * @return the {@link CodeOperator} or {@code null} if not found.
   */
  public static CodeOperator of(String name) {

    return MAP.get(name);
  }

  /**
   * @param <T> type of the {@link CodeOperator}.
   * @param name the {@link #getName() name} of the requested {@link CodeOperator}.
   * @param type the {@link Class} reflecting the type of the requested {@link CodeOperator}.
   * @return the {@link CodeOperator} or {@code null} if not found.
   */
  protected static <T extends CodeOperator> T of(String name, Class<T> type) {

    BaseOperator operator = MAP.get(name);
    if (operator == null) {
      return null;
    }
    try {
      return type.cast(operator);
    } catch (ClassCastException e) {
      throw new ObjectMismatchException(operator, type, e);
    }
  }
}
