/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.java.maven.impl;

import java.io.File;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link MavenLocalRepositoryLocator}.
 */
public class MavenLocalRepositoryLocatorTest extends Assertions {

  /**
   * Test of {@link MavenLocalRepositoryLocator#getDefaultLocalRepository()}
   */
  @Test
  public void testReadModel() {

    // act
    File defaultLocalRepository = MavenLocalRepositoryLocator.getDefaultLocalRepository();

    // assert
    assertThat(defaultLocalRepository).isEqualTo(new File(System.getProperty("user.home"), ".m2/repository"))
        .isDirectory();
  }

}
