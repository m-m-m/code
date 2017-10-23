/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.lang.reflect.WildcardType;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Test of {@link JavaWildcardType}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
@SuppressWarnings("javadoc")
public class JavaWildcardTypeTest extends Assertions {

  @Test
  public void testUnboundedWildcard() {

    // given / when
    WildcardType unboundedWildcard = JavaWildcardType.UNBOUNDED_WILDCARD;
    // then
    assertThat(unboundedWildcard).isNotNull();
    assertThat(unboundedWildcard.getUpperBounds()).hasSize(1).containsExactly(Object.class);
    assertThat(unboundedWildcard.getLowerBounds()).isEmpty();
    assertThat(unboundedWildcard.getTypeName()).isEqualTo("?");
  }

}
