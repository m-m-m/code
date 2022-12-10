package io.github.mmm.code.api.object;

import io.github.mmm.code.api.CodeFile;
import io.github.mmm.code.api.CodePackage;
import io.github.mmm.code.api.CodePathElement;
import io.github.mmm.code.api.annotation.CodeAnnotation;
import io.github.mmm.code.api.arg.CodeParameter;
import io.github.mmm.code.api.arg.CodeReturn;
import io.github.mmm.code.api.block.CodeBlock;
import io.github.mmm.code.api.block.CodeBlockBody;
import io.github.mmm.code.api.block.CodeBlockFor;
import io.github.mmm.code.api.block.CodeBlockIf;
import io.github.mmm.code.api.block.CodeBlockStatement;
import io.github.mmm.code.api.comment.CodeComment;
import io.github.mmm.code.api.doc.CodeDoc;
import io.github.mmm.code.api.element.CodeElement;
import io.github.mmm.code.api.expression.CodeArrayInstatiation;
import io.github.mmm.code.api.expression.CodeCastExpression;
import io.github.mmm.code.api.expression.CodeCondition;
import io.github.mmm.code.api.expression.CodeConstant;
import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.api.expression.CodeExpressionCondition;
import io.github.mmm.code.api.expression.CodeForEachExpression;
import io.github.mmm.code.api.expression.CodeForExpression;
import io.github.mmm.code.api.expression.CodeForLoopExpression;
import io.github.mmm.code.api.expression.CodeOperationInvocation;
import io.github.mmm.code.api.expression.CodeOperatorExpression;
import io.github.mmm.code.api.expression.CodeTernaryExpression;
import io.github.mmm.code.api.expression.CodeVariable;
import io.github.mmm.code.api.imports.CodeImport;
import io.github.mmm.code.api.imports.CodeImportItem;
import io.github.mmm.code.api.member.CodeConstructor;
import io.github.mmm.code.api.member.CodeField;
import io.github.mmm.code.api.member.CodeMethod;
import io.github.mmm.code.api.member.CodeOperation;
import io.github.mmm.code.api.member.CodeProperty;
import io.github.mmm.code.api.statement.CodeAssignment;
import io.github.mmm.code.api.statement.CodeAtomicStatement;
import io.github.mmm.code.api.statement.CodeLocalVariable;
import io.github.mmm.code.api.statement.CodeReturnStatement;
import io.github.mmm.code.api.statement.CodeStatement;
import io.github.mmm.code.api.type.CodeArrayType;
import io.github.mmm.code.api.type.CodeComposedType;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeParameterizedType;
import io.github.mmm.code.api.type.CodeType;
import io.github.mmm.code.api.type.CodeTypePlaceholder;
import io.github.mmm.code.api.type.CodeTypeVariable;
import io.github.mmm.code.api.type.CodeTypeWildcard;

/**
 * Generic visitor to traverse the code AST (abstract syntax tree). Allows to recursively visit {@link CodeElement}s,
 * {@link io.github.mmm.code.api.node.CodeNode}s, and other related code elements. To visit simply create your own
 * sub-class and call according {@code visit} method (e.g. {@link #visitFile(CodeFile)} on it. You implementation may be
 * stateful and collect data in fields of your implementation. To avoid performance issues some recursive traversals are
 * not enabled by default. You may therefore override methods like {@link #isVisitDocs()}, {@link #isVisitProperties()},
 * {@link #isVisitComments()} to activate deeper and more greedy recursion. Also you may override other methods like
 * {@link #isVisitFields()}, etc. if recursion is not needed in your case.
 *
 * @since 1.0.0
 */
public abstract class CodeVisitor {

  /**
   * @param pkg the {@link CodePackage} to visit.
   */
  public void visitPackage(CodePackage pkg) {

    doVisitElement(pkg);
    for (CodePathElement element : pkg.getChildren().getDeclared()) {
      if (element.isFile()) {
        visitFile((CodeFile) element);
      } else {
        visitPackage((CodePackage) element);
      }
    }
  }

  /**
   * @param file the {@link CodeFile} to visit.
   */
  public void visitFile(CodeFile file) {

    doVisitElement(file);
    if (isVisitImports()) {
      for (CodeImport importStatement : file.getImports()) {
        visitImport(importStatement);
      }
    }
    for (CodeType type : file.getTypes()) {
      visitTypeDeclaration(type);
    }
  }

  /**
   * @return {@code true} to {@link #visitImport(CodeImport) visit import statements} , {@code false} otherwise (ignore
   *         imports).
   */
  protected boolean isVisitImports() {

    return true;
  }

  /**
   * @param importStatement the {@link CodeImport} to visit.
   * @see #isVisitImports()
   */
  protected void visitImport(CodeImport importStatement) {

    for (CodeImportItem item : importStatement.getItems()) {
      visitImportItem(item);
    }
  }

  /**
   * @param item the {@link CodeImportItem} to visit.
   */
  protected void visitImportItem(CodeImportItem item) {

  }

  /**
   * @return {@code true} to {@link #visitComment(CodeComment) visit comments}, {@code false} otherwise (ignore
   *         comments).
   */
  protected boolean isVisitComments() {

    return false;
  }

  /**
   * @param comment the {@link CodeComment} to visit.
   * @see #isVisitComments()
   */
  protected void visitComment(CodeComment comment) {

  }

  /**
   * @return {@code true} to {@link #visitDoc(CodeDoc) visit documentation}, {@code false} otherwise (ignore doc).
   */
  protected boolean isVisitDocs() {

    return false;
  }

  /**
   * @param doc the {@link CodeDoc} to visit.
   * @see #isVisitDocs()
   */
  protected void visitDoc(CodeDoc doc) {

  }

  /**
   * @return {@code true} to {@link #visitAnnotation(CodeAnnotation) visit annotations}, {@code false} otherwise (ignore
   *         annotations).
   */
  protected boolean isVisitAnnotations() {

    return true;
  }

  /**
   * @param annotation the {@link CodeAnnotation} to visit.
   * @see #isVisitAnnotations()
   */
  protected void visitAnnotation(CodeAnnotation annotation) {

    if (annotation == null) {
      return;
    }
    visitGenericType(annotation.getType());
  }

  /**
   * This method is called to visit a type declaration and will recursively traverse the given {@link CodeType}
   * extensively. However, further {@link CodeType} referenced from traversed children will be visited via
   * {@link #visitTypeReference(CodeType)} to avoid infinity loops as types can have cyclic references and visitors may
   * also not expect such behavior.
   *
   * @param type the {@link CodeType} to visit.
   * @see #visitTypeReference(CodeType)
   */
  public void visitTypeDeclaration(CodeType type) {

    doVisitElement(type);
    if (isVisitBlocks()) {
      doVisitBlock(type.getStaticInitializer());
      doVisitBlock(type.getNonStaticInitializer());
    }
    if (isVisitSuperTypes()) {
      for (CodeGenericType superType : type.getSuperTypes().getDeclared()) {
        visitSuperType(superType);
      }
    }
    if (isVisitFields()) {
      for (CodeField field : type.getFields().getDeclared()) {
        visitField(field);
      }
    }
    if (isVisitConstructors()) {
      for (CodeConstructor constructor : type.getConstructors().getDeclared()) {
        visitConstructor(constructor);
      }
    }
    if (isVisitMethods()) {
      for (CodeMethod metohd : type.getMethods()) {
        visitMethod(metohd);
      }
    }
    if (isVisitProperties()) {
      for (CodeProperty property : type.getProperties()) {
        visitProperty(property);
      }
    }
  }

  /**
   * This method will be called for every {@link CodeType} visited from code as reference. It will do nothing by default
   * but can be overridden to collect referenced types.
   *
   * @param type the raw referenced {@link CodeType} to visit.
   * @see #visitTypeDeclaration(CodeType)
   */
  protected void visitTypeReference(CodeType type) {

  }

  /**
   * @return {@code true} to {@link #visitSuperType(CodeGenericType) visit super-types}, {@code false} otherwise (ignore
   *         super-types).
   */
  protected boolean isVisitSuperTypes() {

    return true;
  }

  /**
   * @param superType the {@link CodeType#getSuperTypes() super-type} to visit.
   */
  protected void visitSuperType(CodeGenericType superType) {

    visitGenericType(superType);
  }

  /**
   * @return {@code true} to {@link #visitField(CodeField) visit fields}, {@code false} otherwise (ignore fields).
   */
  protected boolean isVisitFields() {

    return true;
  }

  /**
   * @param field the {@link CodeField} to visit.
   */
  protected void visitField(CodeField field) {

    doVisitElement(field);
    visitGenericType(field.getType());
    CodeExpression initializer = field.getInitializer();
    if (initializer != null) {
      visitExpression(initializer);
    }
    // getter/setter for properties?
  }

  /**
   * @return {@code true} to {@link #visitConstructor(CodeConstructor) visit constructors}, {@code false} otherwise
   *         (ignore constructors).
   */
  protected boolean isVisitConstructors() {

    return true;
  }

  /**
   * @param constructor the {@link CodeConstructor} to visit.
   * @see #isVisitConstructors()
   */
  protected void visitConstructor(CodeConstructor constructor) {

    doVisitOperation(constructor);

  }

  /**
   * @return {@code true} to {@link #visitMethod(CodeMethod) visit methods}, {@code false} otherwise (ignore methods).
   */
  protected boolean isVisitMethods() {

    return true;
  }

  /**
   * @param method the {@link CodeMethod} to visit.
   * @see #isVisitMethods()
   */
  protected void visitMethod(CodeMethod method) {

    doVisitOperation(method);
    visitReturns(method.getReturns());
  }

  /**
   * @param returns the {@link CodeReturn} to visit.
   * @see #visitMethod(CodeMethod)
   */
  protected void visitReturns(CodeReturn returns) {

    doVisitElement(returns);
    CodeGenericType type = returns.getType();
    if (type != null) {
      visitGenericType(type);
    }
  }

  /**
   * @param operation the {@link CodeOperation} to visit.
   * @see #visitMethod(CodeMethod)
   * @see #visitConstructor(CodeConstructor)
   */
  protected void doVisitOperation(CodeOperation operation) {

    if (operation == null) {
      return;
    }
    doVisitElement(operation);
    if (isVisitBodies()) {
      visitBody(operation.getBody());
    }
    if (isVisitParameters()) {
      for (CodeParameter parameter : operation.getParameters().getDeclared()) {
        visitParameter(parameter);
      }
    }
  }

  /**
   * @return {@code true} to {@link #visitProperty(CodeProperty) visit properties}, {@code false} otherwise (ignore
   *         properties).
   */
  protected boolean isVisitProperties() {

    return false;
  }

  /**
   * @param property the {@link CodeProperty} to visit.
   * @see #isVisitProperties()
   */
  protected void visitProperty(CodeProperty property) {

    doVisitElement(property);
  }

  /**
   * @return {@code true} to {@link #visitParameter(CodeParameter) visit parameters}, {@code false} otherwise (ignore
   *         parameters).
   */
  protected boolean isVisitParameters() {

    return true;
  }

  /**
   * @param parameter the {@link CodeParameter} to visit.
   * @see #isVisitParameters()
   */
  protected void visitParameter(CodeParameter parameter) {

    doVisitElement(parameter);
    CodeGenericType type = parameter.getType();
    if (type != null) {
      visitGenericType(type);
    }
  }

  /**
   * @return {@code true} to {@link #visitBody(CodeBlockBody) visit bodies} (implementations), {@code false} otherwise
   *         (ignore bodies).
   */
  protected boolean isVisitBodies() {

    return true;
  }

  /**
   * @param body the {@link CodeBlockBody} to visit.
   * @see #isVisitBodies()
   */
  protected void visitBody(CodeBlockBody body) {

    if (isVisitBlocks()) {
      doVisitBlock(body);
    }
  }

  /**
   * @return {@code true} to {@link #doVisitBlock(CodeBlock) visit blocks} (implementations), {@code false} otherwise
   *         (ignore blocks).
   */
  protected boolean isVisitBlocks() {

    return true;
  }

  /**
   * @param block the {@link CodeBlock} to visit.
   * @see #isVisitBlocks()
   */
  protected void doVisitBlock(CodeBlock block) {

    for (CodeStatement statement : block.getStatements()) {
      visitStatement(statement);
    }
  }

  /**
   * @return {@code true} to {@link #visitStatement(CodeStatement) visit statements}, {@code false} otherwise (ignore
   *         statements).
   */
  protected boolean isVisitStatements() {

    return true;
  }

  /**
   * @param statement the {@link CodeStatement} to visit.
   */
  protected void visitStatement(CodeStatement statement) {

    if (statement instanceof CodeAssignment) {
      visitAssignment((CodeAssignment) statement);
    } else if (statement instanceof CodeReturnStatement) {
      visitReturnStatement((CodeReturnStatement) statement);
    } else if (statement instanceof CodeBlockStatement) {
      if (isVisitBlocks()) {
        visitBlockStatement((CodeBlockStatement) statement);
      }
    }
  }

  /**
   * @param block the {@link CodeBlockStatement} to visit.
   */
  protected void visitBlockStatement(CodeBlockStatement block) {

    if (block instanceof CodeBlockFor) {
      visitBlockFor((CodeBlockFor) block);
    } else if (block instanceof CodeBlockIf) {
      visitBlockIf((CodeBlockIf) block);
    }
  }

  /**
   * @param block the {@link CodeBlockIf} to visit.
   * @see #visitBlockStatement(CodeBlockStatement)
   * @see #doVisitBlock(CodeBlock)
   */
  protected void visitBlockIf(CodeBlockIf block) {

    if (isVisitExpressions()) {
      CodeCondition condition = block.getCondition();
      if (condition != null) {
        visitCondition(condition);
      }
    }
  }

  /**
   * @param block the {@link CodeBlockFor} to visit.
   * @see #visitBlockStatement(CodeBlockStatement)
   * @see #doVisitBlock(CodeBlock)
   */
  protected void visitBlockFor(CodeBlockFor block) {

    if (isVisitExpressions()) {
      CodeForExpression expression = block.getExpression();
      if (expression != null) {
        visitForExpression(expression);
      }
    }
    doVisitBlock(block);
  }

  /**
   * @param statement the {@link CodeReturnStatement} to visit.
   */
  protected void visitReturnStatement(CodeReturnStatement statement) {

    if (isVisitExpressions()) {
      CodeExpression expression = statement.getExpression();
      if (expression != null) {
        visitExpression(expression);
      }
    }
  }

  /**
   * @param statement the {@link CodeAssignment} to visit.
   * @see #visitStatement(CodeStatement)
   */
  protected void visitAssignment(CodeAssignment statement) {

    if (isVisitVariables()) {
      CodeVariable variable = statement.getVariable();
      if (variable instanceof CodeLocalVariable) {
        visitLocalVariable((CodeLocalVariable) variable);
      }
    }
    if (isVisitExpressions()) {
      CodeExpression expression = statement.getExpression();
      if (expression != null) {
        visitExpression(expression);
      }
    }
  }

  /**
   * @return {@code true} to {@link #visitLocalVariable(CodeLocalVariable) visit variables}, {@code false} otherwise
   *         (ignore variables).
   */
  protected boolean isVisitVariables() {

    return true;
  }

  /**
   * @param variable the {@link CodeLocalVariable} to visit.
   * @see #isVisitVariables()
   */
  protected void visitLocalVariable(CodeLocalVariable variable) {

  }

  /**
   * @return {@code true} to {@link #doVisitExpression(CodeExpression) visit expressions}, {@code false} otherwise
   *         (ignore expressions).
   */
  protected boolean isVisitExpressions() {

    return true;
  }

  /**
   * @param condition the {@link CodeCondition} to visit.
   * @see #doVisitExpression(CodeExpression)
   */
  protected void visitCondition(CodeCondition condition) {

    doVisitExpression(condition);
  }

  /**
   * @param expression the {@link CodeExpression} to visit.
   * @see #isVisitExpressions()
   * @see #visitExpression(CodeExpression)
   */
  protected void doVisitExpression(CodeExpression expression) {

  }

  /**
   * @param expression the {@link CodeExpression} to visit.
   * @see #isVisitExpressions()
   * @see #doVisitExpression(CodeExpression)
   */
  protected void visitExpression(CodeExpression expression) {

    if (expression instanceof CodeConstant) {
      visitConstant((CodeConstant) expression);
    } else if (expression instanceof CodeOperatorExpression) {
      visitOperatorExpression((CodeOperatorExpression) expression);
    } else if (expression instanceof CodeOperationInvocation) {
      visitOperationInvocation((CodeOperationInvocation) expression);
    } else if (expression instanceof CodeCondition) {
      visitCondition((CodeCondition) expression);
    } else if (expression instanceof CodeCastExpression) {
      visitCastExpression((CodeCastExpression) expression);
    } else if (expression instanceof CodeExpressionCondition) {
      visitExpressionCondition((CodeExpressionCondition) expression);
    } else if (expression instanceof CodeTernaryExpression) {
      visitTernaryExpression((CodeTernaryExpression) expression);
    } else if (expression instanceof CodeArrayInstatiation) {
      visitArrayInstatiation((CodeArrayInstatiation) expression);
    }
  }

  /**
   * @param expression the {@link CodeArrayInstatiation} to visit.
   * @see #doVisitExpression(CodeExpression)
   */
  protected void visitArrayInstatiation(CodeArrayInstatiation expression) {

    doVisitExpression(expression);
    for (CodeExpression value : expression.getValues()) {
      visitExpression(value);
    }
  }

  /**
   * @param expression the {@link CodeOperationInvocation} to visit.
   * @see #doVisitExpression(CodeExpression)
   */
  protected void visitOperationInvocation(CodeOperationInvocation expression) {

    doVisitExpression(expression);
    for (CodeExpression argument : expression.getArguments()) {
      visitExpression(argument);
    }
    for (CodeGenericType parameter : expression.getTypeParameters()) {
      visitGenericType(parameter);
    }
  }

  /**
   * @param expression the {@link CodeTernaryExpression} to visit.
   * @see #doVisitExpression(CodeExpression)
   */
  protected void visitTernaryExpression(CodeTernaryExpression expression) {

    doVisitExpression(expression);
    CodeCondition condition = expression.getCondition();
    if (condition != null) {
      visitCondition(condition);
    }
    CodeExpression ifArg = expression.getIfArg();
    if (ifArg != null) {
      visitExpression(ifArg);
    }
    CodeExpression elseArg = expression.getElseArg();
    if (elseArg != null) {
      visitExpression(elseArg);
    }
  }

  /**
   * @param expression the {@link CodeExpressionCondition} to visit.
   * @see #doVisitExpression(CodeExpression)
   */
  protected void visitExpressionCondition(CodeExpressionCondition expression) {

    doVisitExpression(expression);
    CodeExpression wrappedExpression = expression.getExpression();
    if (wrappedExpression != null) {
      visitExpression(wrappedExpression);
    }
  }

  /**
   * @param expression the {@link CodeOperatorExpression} to visit.
   * @see #doVisitExpression(CodeExpression)
   */
  protected void visitOperatorExpression(CodeOperatorExpression expression) {

    doVisitExpression(expression);
    for (CodeExpression argument : expression.getArguments()) {
      visitExpression(argument);
    }
  }

  /**
   * @param expression the {@link CodeCastExpression} to visit.
   * @see #doVisitExpression(CodeExpression)
   */
  protected void visitCastExpression(CodeCastExpression expression) {

    doVisitExpression(expression);
    CodeGenericType type = expression.getType();
    if (type != null) {
      visitGenericType(type);
    }
  }

  /**
   * @param constant the {@link CodeConstant} to visit.
   * @see #doVisitExpression(CodeExpression)
   */
  protected void visitConstant(CodeConstant constant) {

    doVisitExpression(constant);
  }

  /**
   * @param expression the {@link CodeForExpression} to visit.
   * @see #isVisitExpressions()
   */
  protected void visitForExpression(CodeForExpression expression) {

    if (expression instanceof CodeForLoopExpression) {
      visitForLoopExpression((CodeForLoopExpression) expression);
    } else if (expression instanceof CodeForEachExpression) {
      visitForEachExpreesion((CodeForEachExpression) expression);
    }
  }

  /**
   * @param expression the {@link CodeForEachExpression} to visit.
   * @see #visitForExpression(CodeForExpression)
   */
  protected void visitForEachExpreesion(CodeForEachExpression expression) {

    if (isVisitVariables()) {
      CodeLocalVariable variable = expression.getVariable();
      if (variable != null) {
        visitLocalVariable(variable);
      }
    }
    if (isVisitExpressions()) {
      CodeExpression iterable = expression.getExpression();
      if (iterable != null) {
        visitExpression(iterable);
      }
    }
  }

  /**
   * @param expression the {@link CodeForLoopExpression} to visit.
   * @see #visitForExpression(CodeForExpression)
   */
  protected void visitForLoopExpression(CodeForLoopExpression expression) {

    if (isVisitStatements()) {
      CodeAtomicStatement statement = expression.getInit();
      if (statement != null) {
        visitStatement(statement);
      }
    }
    if (isVisitExpressions()) {
      CodeCondition condition = expression.getCondition();
      if (condition != null) {
        visitCondition(condition);
      }
    }
    if (isVisitStatements()) {
      CodeAtomicStatement statement = expression.getUpdate();
      if (statement != null) {
        visitStatement(statement);
      }
    }
  }

  /**
   * @param element the {@link CodeElement} to visit.
   */
  protected void doVisitElement(CodeElement element) {

    if (isVisitComments()) {
      visitComment(element.getComment());
    }
    if (isVisitDocs()) {
      visitDoc(element.getDoc());
    }
    if (isVisitAnnotations()) {
      for (CodeAnnotation annotation : element.getAnnotations()) {
        visitAnnotation(annotation);
      }
    }
  }

  /**
   * @param type the {@link CodeGenericType} to visit.
   */
  protected void visitGenericType(CodeGenericType type) {

    if (type instanceof CodeType) {
      visitTypeReference((CodeType) type);
    } else if (type instanceof CodeArrayType) {
      visitArrayType((CodeArrayType) type);
    } else if (type instanceof CodeParameterizedType) {
      visitParameterizedType((CodeParameterizedType) type);
    } else if (type instanceof CodeTypeVariable) {
      visitTypeVariable((CodeTypeVariable) type);
    } else if (type instanceof CodeTypeWildcard) {
      visitTypeWildcard((CodeTypeWildcard) type);
    } else if (type instanceof CodeComposedType) {
      visitComposedType((CodeComposedType) type);
    }
  }

  /**
   * @param type the {@link CodeComposedType} to visit.
   */
  protected void visitComposedType(CodeComposedType type) {

    if (type == null) {
      return;
    }
    doVisitElement(type);
    for (CodeGenericType childType : type.getTypes()) {
      visitGenericType(childType);
    }
  }

  /**
   * @param type the {@link CodeParameterizedType} to visit.
   */
  protected void visitParameterizedType(CodeParameterizedType type) {

    if (type == null) {
      return;
    }
    doVisitElement(type);
    for (CodeGenericType parameter : type.getTypeParameters().getDeclared()) {
      if (parameter != type) {
        visitGenericType(parameter);
      }
    }
  }

  /**
   * @param type the {@link CodeArrayType} to visit.
   */
  protected void visitArrayType(CodeArrayType type) {

    doVisitElement(type);
    CodeGenericType componentType = type.getComponentType();
    if (componentType != null) {
      visitGenericType(componentType);
    }
  }

  /**
   * @param type the {@link CodeTypePlaceholder} to visit.
   */
  protected void doVisitTypePlaceholder(CodeTypePlaceholder type) {

    doVisitElement(type);
    CodeGenericType bound = type.getBound();
    if (bound != null) {
      visitGenericType(bound);
    }
  }

  /**
   * @param type the {@link CodeTypeVariable} to visit.
   */
  protected void visitTypeVariable(CodeTypeVariable type) {

    doVisitTypePlaceholder(type);
  }

  /**
   * @param type the {@link CodeTypeWildcard} to visit.
   */
  protected void visitTypeWildcard(CodeTypeWildcard type) {

    doVisitTypePlaceholder(type);
  }

}
