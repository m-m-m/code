/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import io.github.mmm.code.api.CodeContext;
import io.github.mmm.code.api.CodeFile;
import io.github.mmm.code.api.CodePackage;
import io.github.mmm.code.api.node.CodeNodeWithFileWriting;
import io.github.mmm.code.api.type.CodeType;

/**
 * Test of {@link AbstractBaseContext} via {@link TestContext}.
 */
class AbstractBaseContextTest extends BaseContextTest {

  /**
   * Test of {@link CodeNodeWithFileWriting#write(Path)}.
   *
   * @throws Exception on error.
   */
  @Test
  void testWrite() throws Exception {

    // arrange
    CodeContext context = createContext();

    // act
    CodePackage rootPkg = context.getSource().getRootPackage();
    CodePackage fooPkg = rootPkg.getChildren().createPackage("foo");
    CodeFile fooFile = fooPkg.getChildren().getOrCreateFile("Foo");
    CodeType fooType = fooFile.getType();
    CodePackage barPkg = fooPkg.getChildren().getOrCreatePackage("bar");
    barPkg.getDoc().add("This is the bar package.");
    barPkg.getAnnotations().add(context.getType(Deprecated.class).asType());
    CodeFile barFile = barPkg.getChildren().getOrCreateFile("Bar");
    CodeType barType = barFile.getType();
    barType.getSuperTypes().add(fooType);
    barFile.getImports().add(fooType);

    Path targetDir = Files.createTempDirectory("BaseContextImplTest");
    targetDir.toFile().deleteOnExit();
    fooPkg.write(targetDir);

    // assert
    int childCount = 0;
    Iterator<Path> childIterator = Files.list(targetDir).iterator();
    while (childIterator.hasNext()) {
      Path child = childIterator.next();
      if (Files.isRegularFile(child)) {
        assertThat(child).isRegularFile().hasFileName("Foo.java").hasContent("package foo;\n\n" //
            + "public class Foo {\n" //
            + "}\n");
      } else {
        assertThat(child).isDirectory().hasFileName("bar");
        Iterator<Path> subChildIterator = Files.list(child).iterator();
        while (subChildIterator.hasNext()) {
          Path subChild = subChildIterator.next();
          assertThat(subChild).isRegularFile();
          if (subChild.getFileName().toString().equals("package-info.java")) {
            assertThat(subChild).hasContent("/** This is the bar package. */\n" //
                + "@Deprecated\n" //
                + "package foo.bar;\n");
          } else {
            assertThat(subChild).hasFileName("Bar.java").hasContent("package foo.bar;\n\n" //
                + "import foo.Foo;\n\n" //
                + "public class Bar extends Foo {\n"//
                + "}\n");
          }
          childCount++;
        }
      }
      childCount++;
    }
    assertThat(childCount).isEqualTo(4);
  }

}
