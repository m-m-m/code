package net.sf.mmm.code.api.imports;

import java.util.Collection;

import net.sf.mmm.code.api.CodeFile;
import net.sf.mmm.code.api.annotation.CodeAnnotation;
import net.sf.mmm.code.api.arg.CodeParameter;
import net.sf.mmm.code.api.arg.CodeReturn;
import net.sf.mmm.code.api.block.CodeBlock;
import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.expression.CodeVariable;
import net.sf.mmm.code.api.member.CodeConstructor;
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.statement.CodeAssignment;
import net.sf.mmm.code.api.statement.CodeLocalVariable;
import net.sf.mmm.code.api.statement.CodeStatement;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.api.type.CodeTypeVariable;
import net.sf.mmm.code.api.type.CodeTypeVariables;

/**
 * Helper to generate import statements.
 *
 * @since 1.0.0
 */
public class CodeImportHelper {

  private static final CodeImportHelper INSTANCE = new CodeImportHelper();

  protected CodeImportHelper() {

    super();
  }

  public void createImports(CodeImports imports) {

    CodeFile file = imports.getParent();
    for (CodeType type : file.getTypes()) {
      createImports(imports, type);
    }
  }

  public void createImports(CodeFile file) {

    CodeImports imports = file.getImports();
    for (CodeType type : file.getTypes()) {
      createImports(imports, type);
    }
  }

  protected void createImports(CodeImports imports, CodeType type) {

    for (CodeGenericType superType : type.getSuperTypes().getDeclared()) {
      imports.add(superType);
      createImportsForElement(imports, superType);
    }
    for (CodeField field : type.getFields().getDeclared()) {
      createImports(imports, field);
    }
    for (CodeMethod method : type.getMethods().getDeclared()) {
      createImports(imports, method);
    }
    for (CodeConstructor constructor : type.getConstructors().getDeclared()) {
      createImports(imports, constructor);
    }
    createImportsForTypeParameters(imports, type.getTypeParameters());
    createImportsForBlock(imports, type.getNonStaticInitializer());
    createImportsForBlock(imports, type.getStaticInitializer());
    for (CodeType nestedType : type.getNestedTypes()) {
      createImports(imports, nestedType);
    }
  }

  protected void createImportsForTypeParameters(CodeImports imports, CodeTypeVariables typeParameters) {

    for (CodeTypeVariable typeVariable : typeParameters.getDeclared()) {
      createImportsForTypeVariable(imports, typeVariable);
    }
  }

  protected void createImportsForTypeVariable(CodeImports imports, CodeTypeVariable typeVariable) {

    if (typeVariable != null) {
      imports.add(typeVariable.getBound());
    }
  }

  protected void createImportsForBlock(CodeImports imports, CodeBlock block) {

    if (block == null) {
      return;
    }
    for (CodeStatement statement : block.getStatements()) {
      createImportsForStatement(imports, statement);
    }
  }

  protected void createImportsForStatement(CodeImports imports, CodeStatement statement) {

    if (statement == null) {
      return;
    }
    if (statement instanceof CodeAssignment) {
      CodeAssignment assignment = (CodeAssignment) statement;
      createImportsForVariable(imports, assignment.getVariable());
      createImportsForExpression(imports, assignment.getExpression());
    }
  }

  protected void createImportsForVariable(CodeImports imports, CodeVariable variable) {

    if (variable == null) {
      return;
    }
    if (variable instanceof CodeLocalVariable) {
      imports.add(variable.getType());
    }
  }

  protected void createImports(CodeImports imports, CodeConstructor constructor) {

    createImportsForOperation(imports, constructor);
  }

  protected void createImports(CodeImports imports, CodeField field) {

    imports.add(field.getType());
    createImportsForElement(imports, field);
  }

  protected void createImports(CodeImports imports, CodeMethod method) {

    CodeReturn returns = method.getReturns();
    imports.add(returns.getType());
    createImportsForElement(imports, returns);
    createImportsForOperation(imports, method);
  }

  protected void createImportsForOperation(CodeImports imports, CodeOperation operation) {

    createImportsForTypeParameters(imports, operation.getTypeParameters());
    for (CodeParameter parameter : operation.getParameters().getDeclared()) {
      createImports(imports, parameter);
      createImportsForElement(imports, parameter);
    }
    createImportsForElement(imports, operation);
    createImportsForBlock(imports, operation.getBody());
  }

  protected void createImports(CodeImports imports, CodeParameter parameter) {

    imports.add(parameter.getType());
  }

  protected void createImportsForElement(CodeImports imports, CodeElement element) {

    for (CodeAnnotation annotation : element.getAnnotations()) {
      imports.add(annotation.getType());
      // createImportsForExpressions(imports, annotation.getParameters().values());
    }
  }

  protected void createImportsForExpressions(CodeImports imports, Collection<CodeExpression> expressions) {

    for (CodeExpression expression : expressions) {
      createImportsForExpression(imports, expression);
    }
  }

  protected void createImportsForExpression(CodeImports imports, CodeExpression expression) {

    if (expression == null) {
      return;
    }
    // TODO Auto-generated method stub

  }

  /**
   * @return the singleton instance.
   */
  public static CodeImportHelper get() {

    return INSTANCE;
  }

}
