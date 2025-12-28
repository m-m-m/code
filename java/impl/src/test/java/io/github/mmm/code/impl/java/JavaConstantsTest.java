/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java;

import java.lang.reflect.WildcardType;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link JavaConstants}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
@SuppressWarnings("javadoc")
class JavaConstantsTest extends Assertions {

  @Test
  void testUnboundedWildcard() {

    // arrange + act
    WildcardType unboundedWildcard = JavaConstants.UNBOUNDED_WILDCARD;
    // assert
    assertThat(unboundedWildcard).isNotNull();
    assertThat(unboundedWildcard.getUpperBounds()).hasSize(1).containsExactly(Object.class);
    assertThat(unboundedWildcard.getLowerBounds()).isEmpty();
    assertThat(unboundedWildcard.getTypeName()).isEqualTo("?");
  }

}
