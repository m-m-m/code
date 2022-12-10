package io.github.mmm.code.api.imports;

import io.github.mmm.code.api.CodeFile;
import io.github.mmm.code.api.object.CodeVisitor;
import io.github.mmm.code.api.type.CodeType;

/**
 * TODO hohwille This type ...
 *
 * @since 1.0.0
 */
public class CodeImportGeneratorVisitor extends CodeVisitor {

  private final CodeImports imports;

  /**
   * The constructor.
   *
   * @param imports the {@link CodeImports} to create and auto-generate.
   */
  public CodeImportGeneratorVisitor(CodeImports imports) {

    super();
    this.imports = imports;
  }

  @Override
  protected void visitTypeReference(CodeType type) {

    super.visitTypeReference(type);
    this.imports.add(type);
  }

  /**
   * @param type the {@link CodeType} for which to create the imports.
   */
  public static void createImports(CodeType type) {

    CodeImportGeneratorVisitor visitor = new CodeImportGeneratorVisitor(type.getFile().getImports());
    visitor.visitTypeDeclaration(type);
  }

  /**
   * @param file the {@link CodeFile} for which to create the imports.
   */
  public static void createImports(CodeFile file) {

    CodeImportGeneratorVisitor visitor = new CodeImportGeneratorVisitor(file.getImports());
    visitor.visitFile(file);
  }

  /**
   * @param imports the {@link CodeImports} for which to create the imports.
   */
  public static void createImports(CodeImports imports) {

    CodeImportGeneratorVisitor visitor = new CodeImportGeneratorVisitor(imports);
    visitor.visitFile(imports.getParent());
  }

}
