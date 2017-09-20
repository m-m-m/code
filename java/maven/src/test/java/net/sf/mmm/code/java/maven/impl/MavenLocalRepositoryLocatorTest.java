/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.java.maven.impl;

import java.io.File;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Test of {@link MavenLocalRepositoryLocator}.
 */
public class MavenLocalRepositoryLocatorTest extends Assertions {

  /**
   * Test of {@link MavenLocalRepositoryLocator#getDefaultLocalRepository()}
   */
  @Test
  public void testReadModel() {

    // when
    File defaultLocalRepository = MavenLocalRepositoryLocator.getDefaultLocalRepository();

    // then
    assertThat(defaultLocalRepository).isEqualTo(new File(System.getProperty("user.home"), ".m2/repository")).isDirectory();
  }

}
