/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.lang.reflect.WildcardType;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link JavaConstants}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
@SuppressWarnings("javadoc")
public class JavaConstantsTest extends Assertions {

  @Test
  public void testUnboundedWildcard() {

    // given / when
    WildcardType unboundedWildcard = JavaConstants.UNBOUNDED_WILDCARD;
    // then
    assertThat(unboundedWildcard).isNotNull();
    assertThat(unboundedWildcard.getUpperBounds()).hasSize(1).containsExactly(Object.class);
    assertThat(unboundedWildcard.getLowerBounds()).isEmpty();
    assertThat(unboundedWildcard.getTypeName()).isEqualTo("?");
  }

}
