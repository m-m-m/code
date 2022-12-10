/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.java.parser.base;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import io.github.mmm.code.java.parser.base.JavaSourceCodeParserBaseListener;
import io.github.mmm.code.java.parser.base.JavaSourceCodeParser.*;

/**
 * Implementation of {@link JavaSourceCodeParserBaseListener}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSourceCodeParserListenerImpl extends JavaSourceCodeParserBaseListener {

  @Override
  public void enterCompilationUnit(CompilationUnitContext ctx) {

    System.out.print("CompilationUnit: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitCompilationUnit(CompilationUnitContext ctx) {

    // TODO Auto-generated method stub
    super.exitCompilationUnit(ctx);
  }

  @Override
  public void enterPackageDeclaration(PackageDeclarationContext ctx) {

    System.out.print("PackageDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitPackageDeclaration(PackageDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitPackageDeclaration(ctx);
  }

  @Override
  public void enterImportDeclaration(ImportDeclarationContext ctx) {

    System.out.print("ImportDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitImportDeclaration(ImportDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitImportDeclaration(ctx);
  }

  @Override
  public void enterTypeDeclaration(TypeDeclarationContext ctx) {

    System.out.print("TypeDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitTypeDeclaration(TypeDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitTypeDeclaration(ctx);
  }

  @Override
  public void enterModifier(ModifierContext ctx) {

    System.out.print("Modifier: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitModifier(ModifierContext ctx) {

    // TODO Auto-generated method stub
    super.exitModifier(ctx);
  }

  @Override
  public void enterClassOrInterfaceModifier(ClassOrInterfaceModifierContext ctx) {

    System.out.print("ClassOrInterfaceModifier: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitClassOrInterfaceModifier(ClassOrInterfaceModifierContext ctx) {

    // TODO Auto-generated method stub
    super.exitClassOrInterfaceModifier(ctx);
  }

  @Override
  public void enterVariableModifier(VariableModifierContext ctx) {

    System.out.print("VariableModifier: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitVariableModifier(VariableModifierContext ctx) {

    // TODO Auto-generated method stub
    super.exitVariableModifier(ctx);
  }

  @Override
  public void enterClassDeclaration(ClassDeclarationContext ctx) {

    System.out.print("ClassDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitClassDeclaration(ClassDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitClassDeclaration(ctx);
  }

  @Override
  public void enterTypeParameters(TypeParametersContext ctx) {

    System.out.print("TypeParameters: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitTypeParameters(TypeParametersContext ctx) {

    // TODO Auto-generated method stub
    super.exitTypeParameters(ctx);
  }

  @Override
  public void enterTypeParameter(TypeParameterContext ctx) {

    System.out.print("TypeParameter: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitTypeParameter(TypeParameterContext ctx) {

    // TODO Auto-generated method stub
    super.exitTypeParameter(ctx);
  }

  @Override
  public void enterTypeBound(TypeBoundContext ctx) {

    System.out.print("TypeBound: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitTypeBound(TypeBoundContext ctx) {

    // TODO Auto-generated method stub
    super.exitTypeBound(ctx);
  }

  @Override
  public void enterEnumDeclaration(EnumDeclarationContext ctx) {

    System.out.print("EnumDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitEnumDeclaration(EnumDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitEnumDeclaration(ctx);
  }

  @Override
  public void enterEnumConstants(EnumConstantsContext ctx) {

    System.out.print("EnumConstants: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitEnumConstants(EnumConstantsContext ctx) {

    // TODO Auto-generated method stub
    super.exitEnumConstants(ctx);
  }

  @Override
  public void enterEnumConstant(EnumConstantContext ctx) {

    System.out.print("EnumConstant: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitEnumConstant(EnumConstantContext ctx) {

    // TODO Auto-generated method stub
    super.exitEnumConstant(ctx);
  }

  @Override
  public void enterEnumBodyDeclarations(EnumBodyDeclarationsContext ctx) {

    System.out.print("EnumBodyDeclarations: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitEnumBodyDeclarations(EnumBodyDeclarationsContext ctx) {

    // TODO Auto-generated method stub
    super.exitEnumBodyDeclarations(ctx);
  }

  @Override
  public void enterInterfaceDeclaration(InterfaceDeclarationContext ctx) {

    System.out.print("InterfaceDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitInterfaceDeclaration(InterfaceDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitInterfaceDeclaration(ctx);
  }

  @Override
  public void enterClassBody(ClassBodyContext ctx) {

    System.out.print("ClassBody: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitClassBody(ClassBodyContext ctx) {

    // TODO Auto-generated method stub
    super.exitClassBody(ctx);
  }

  @Override
  public void enterInterfaceBody(InterfaceBodyContext ctx) {

    System.out.print("InterfaceBody: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitInterfaceBody(InterfaceBodyContext ctx) {

    // TODO Auto-generated method stub
    super.exitInterfaceBody(ctx);
  }

  @Override
  public void enterClassBodyDeclaration(ClassBodyDeclarationContext ctx) {

    System.out.print("ClassBodyDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitClassBodyDeclaration(ClassBodyDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitClassBodyDeclaration(ctx);
  }

  @Override
  public void enterMemberDeclaration(MemberDeclarationContext ctx) {

    System.out.print("MemberDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitMemberDeclaration(MemberDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitMemberDeclaration(ctx);
  }

  @Override
  public void enterMethodDeclaration(MethodDeclarationContext ctx) {

    System.out.print("MethodDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitMethodDeclaration(MethodDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitMethodDeclaration(ctx);
  }

  @Override
  public void enterMethodBody(MethodBodyContext ctx) {

    System.out.print("MethodBody: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitMethodBody(MethodBodyContext ctx) {

    // TODO Auto-generated method stub
    super.exitMethodBody(ctx);
  }

  @Override
  public void enterTypeTypeOrVoid(TypeTypeOrVoidContext ctx) {

    System.out.print("TypeTypeOrVoid: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitTypeTypeOrVoid(TypeTypeOrVoidContext ctx) {

    // TODO Auto-generated method stub
    super.exitTypeTypeOrVoid(ctx);
  }

  @Override
  public void enterGenericMethodDeclaration(GenericMethodDeclarationContext ctx) {

    System.out.print("GenericMethodDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitGenericMethodDeclaration(GenericMethodDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitGenericMethodDeclaration(ctx);
  }

  @Override
  public void enterGenericConstructorDeclaration(GenericConstructorDeclarationContext ctx) {

    System.out.print("GenericConstructorDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitGenericConstructorDeclaration(GenericConstructorDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitGenericConstructorDeclaration(ctx);
  }

  @Override
  public void enterConstructorDeclaration(ConstructorDeclarationContext ctx) {

    System.out.print("ConstructorDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitConstructorDeclaration(ConstructorDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitConstructorDeclaration(ctx);
  }

  @Override
  public void enterFieldDeclaration(FieldDeclarationContext ctx) {

    System.out.print("FieldDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitFieldDeclaration(FieldDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitFieldDeclaration(ctx);
  }

  @Override
  public void enterInterfaceBodyDeclaration(InterfaceBodyDeclarationContext ctx) {

    System.out.print("InterfaceBodyDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitInterfaceBodyDeclaration(InterfaceBodyDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitInterfaceBodyDeclaration(ctx);
  }

  @Override
  public void enterInterfaceMemberDeclaration(InterfaceMemberDeclarationContext ctx) {

    System.out.print("InterfaceMemberDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitInterfaceMemberDeclaration(InterfaceMemberDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitInterfaceMemberDeclaration(ctx);
  }

  @Override
  public void enterConstDeclaration(ConstDeclarationContext ctx) {

    System.out.print("ConstDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitConstDeclaration(ConstDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitConstDeclaration(ctx);
  }

  @Override
  public void enterConstantDeclarator(ConstantDeclaratorContext ctx) {

    System.out.print("ConstantDeclarator: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitConstantDeclarator(ConstantDeclaratorContext ctx) {

    // TODO Auto-generated method stub
    super.exitConstantDeclarator(ctx);
  }

  @Override
  public void enterInterfaceMethodDeclaration(InterfaceMethodDeclarationContext ctx) {

    System.out.print("InterfaceMethodDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitInterfaceMethodDeclaration(InterfaceMethodDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitInterfaceMethodDeclaration(ctx);
  }

  @Override
  public void enterInterfaceMethodModifier(InterfaceMethodModifierContext ctx) {

    System.out.print("InterfaceMethodModifier: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitInterfaceMethodModifier(InterfaceMethodModifierContext ctx) {

    // TODO Auto-generated method stub
    super.exitInterfaceMethodModifier(ctx);
  }

  @Override
  public void enterGenericInterfaceMethodDeclaration(GenericInterfaceMethodDeclarationContext ctx) {

    System.out.print("GenericInterfaceMethodDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitGenericInterfaceMethodDeclaration(GenericInterfaceMethodDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitGenericInterfaceMethodDeclaration(ctx);
  }

  @Override
  public void enterVariableDeclarators(VariableDeclaratorsContext ctx) {

    System.out.print("VariableDeclarators: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitVariableDeclarators(VariableDeclaratorsContext ctx) {

    // TODO Auto-generated method stub
    super.exitVariableDeclarators(ctx);
  }

  @Override
  public void enterVariableDeclarator(VariableDeclaratorContext ctx) {

    System.out.print("VariableDeclarator: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitVariableDeclarator(VariableDeclaratorContext ctx) {

    // TODO Auto-generated method stub
    super.exitVariableDeclarator(ctx);
  }

  @Override
  public void enterVariableDeclaratorId(VariableDeclaratorIdContext ctx) {

    System.out.print("VariableDeclaratorId: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitVariableDeclaratorId(VariableDeclaratorIdContext ctx) {

    // TODO Auto-generated method stub
    super.exitVariableDeclaratorId(ctx);
  }

  @Override
  public void enterVariableInitializer(VariableInitializerContext ctx) {

    System.out.print("VariableInitializer: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitVariableInitializer(VariableInitializerContext ctx) {

    // TODO Auto-generated method stub
    super.exitVariableInitializer(ctx);
  }

  @Override
  public void enterArrayInitializer(ArrayInitializerContext ctx) {

    System.out.print("ArrayInitializer: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitArrayInitializer(ArrayInitializerContext ctx) {

    // TODO Auto-generated method stub
    super.exitArrayInitializer(ctx);
  }

  @Override
  public void enterClassOrInterfaceType(ClassOrInterfaceTypeContext ctx) {

    System.out.print("ClassOrInterfaceType: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitClassOrInterfaceType(ClassOrInterfaceTypeContext ctx) {

    // TODO Auto-generated method stub
    super.exitClassOrInterfaceType(ctx);
  }

  @Override
  public void enterTypeArgument(TypeArgumentContext ctx) {

    System.out.print("TypeArgument: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitTypeArgument(TypeArgumentContext ctx) {

    // TODO Auto-generated method stub
    super.exitTypeArgument(ctx);
  }

  @Override
  public void enterQualifiedNameList(QualifiedNameListContext ctx) {

    System.out.print("QualifiedNameList: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitQualifiedNameList(QualifiedNameListContext ctx) {

    // TODO Auto-generated method stub
    super.exitQualifiedNameList(ctx);
  }

  @Override
  public void enterFormalParameters(FormalParametersContext ctx) {

    System.out.print("FormalParameters: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitFormalParameters(FormalParametersContext ctx) {

    // TODO Auto-generated method stub
    super.exitFormalParameters(ctx);
  }

  @Override
  public void enterFormalParameterList(FormalParameterListContext ctx) {

    System.out.print("FormalParameterList: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitFormalParameterList(FormalParameterListContext ctx) {

    // TODO Auto-generated method stub
    super.exitFormalParameterList(ctx);
  }

  @Override
  public void enterFormalParameter(FormalParameterContext ctx) {

    System.out.print("FormalParameter: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitFormalParameter(FormalParameterContext ctx) {

    // TODO Auto-generated method stub
    super.exitFormalParameter(ctx);
  }

  @Override
  public void enterLastFormalParameter(LastFormalParameterContext ctx) {

    System.out.print("LastFormalParameter: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitLastFormalParameter(LastFormalParameterContext ctx) {

    // TODO Auto-generated method stub
    super.exitLastFormalParameter(ctx);
  }

  @Override
  public void enterQualifiedName(QualifiedNameContext ctx) {

    System.out.print("QualifiedName: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitQualifiedName(QualifiedNameContext ctx) {

    // TODO Auto-generated method stub
    super.exitQualifiedName(ctx);
  }

  @Override
  public void enterLiteral(LiteralContext ctx) {

    System.out.print("Literal: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitLiteral(LiteralContext ctx) {

    // TODO Auto-generated method stub
    super.exitLiteral(ctx);
  }

  @Override
  public void enterIntegerLiteral(IntegerLiteralContext ctx) {

    System.out.print("IntegerLiteral: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitIntegerLiteral(IntegerLiteralContext ctx) {

    // TODO Auto-generated method stub
    super.exitIntegerLiteral(ctx);
  }

  @Override
  public void enterAnnotation(AnnotationContext ctx) {

    System.out.print("Annotation: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitAnnotation(AnnotationContext ctx) {

    // TODO Auto-generated method stub
    super.exitAnnotation(ctx);
  }

  @Override
  public void enterElementValuePairs(ElementValuePairsContext ctx) {

    System.out.print("ElementValuePairs: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitElementValuePairs(ElementValuePairsContext ctx) {

    // TODO Auto-generated method stub
    super.exitElementValuePairs(ctx);
  }

  @Override
  public void enterElementValuePair(ElementValuePairContext ctx) {

    System.out.print("ElementValuePair: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitElementValuePair(ElementValuePairContext ctx) {

    // TODO Auto-generated method stub
    super.exitElementValuePair(ctx);
  }

  @Override
  public void enterElementValue(ElementValueContext ctx) {

    System.out.print("ElementValue: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitElementValue(ElementValueContext ctx) {

    // TODO Auto-generated method stub
    super.exitElementValue(ctx);
  }

  @Override
  public void enterElementValueArrayInitializer(ElementValueArrayInitializerContext ctx) {

    System.out.print("ElementValueArrayInitializer: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitElementValueArrayInitializer(ElementValueArrayInitializerContext ctx) {

    // TODO Auto-generated method stub
    super.exitElementValueArrayInitializer(ctx);
  }

  @Override
  public void enterAnnotationTypeDeclaration(AnnotationTypeDeclarationContext ctx) {

    System.out.print("AnnotationTypeDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitAnnotationTypeDeclaration(AnnotationTypeDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitAnnotationTypeDeclaration(ctx);
  }

  @Override
  public void enterAnnotationTypeBody(AnnotationTypeBodyContext ctx) {

    System.out.print("AnnotationTypeBody: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitAnnotationTypeBody(AnnotationTypeBodyContext ctx) {

    // TODO Auto-generated method stub
    super.exitAnnotationTypeBody(ctx);
  }

  @Override
  public void enterAnnotationTypeElementDeclaration(AnnotationTypeElementDeclarationContext ctx) {

    System.out.print("AnnotationTypeElementDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitAnnotationTypeElementDeclaration(AnnotationTypeElementDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitAnnotationTypeElementDeclaration(ctx);
  }

  @Override
  public void enterAnnotationTypeElementRest(AnnotationTypeElementRestContext ctx) {

    System.out.print("AnnotationTypeElementRest: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitAnnotationTypeElementRest(AnnotationTypeElementRestContext ctx) {

    // TODO Auto-generated method stub
    super.exitAnnotationTypeElementRest(ctx);
  }

  @Override
  public void enterAnnotationMethodOrConstantRest(AnnotationMethodOrConstantRestContext ctx) {

    System.out.print("AnnotationMethodOrConstantRest: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitAnnotationMethodOrConstantRest(AnnotationMethodOrConstantRestContext ctx) {

    // TODO Auto-generated method stub
    super.exitAnnotationMethodOrConstantRest(ctx);
  }

  @Override
  public void enterAnnotationMethodRest(AnnotationMethodRestContext ctx) {

    System.out.print("AnnotationMethodRest: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitAnnotationMethodRest(AnnotationMethodRestContext ctx) {

    // TODO Auto-generated method stub
    super.exitAnnotationMethodRest(ctx);
  }

  @Override
  public void enterAnnotationConstantRest(AnnotationConstantRestContext ctx) {

    System.out.print("AnnotationConstantRest: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitAnnotationConstantRest(AnnotationConstantRestContext ctx) {

    // TODO Auto-generated method stub
    super.exitAnnotationConstantRest(ctx);
  }

  @Override
  public void enterDefaultValue(DefaultValueContext ctx) {

    System.out.print("DefaultValue: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitDefaultValue(DefaultValueContext ctx) {

    // TODO Auto-generated method stub
    super.exitDefaultValue(ctx);
  }

  @Override
  public void enterBlock(BlockContext ctx) {

    System.out.print("Block: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitBlock(BlockContext ctx) {

    // TODO Auto-generated method stub
    super.exitBlock(ctx);
  }

  @Override
  public void enterBlockStatement(BlockStatementContext ctx) {

    System.out.print("BlockStatement: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitBlockStatement(BlockStatementContext ctx) {

    // TODO Auto-generated method stub
    super.exitBlockStatement(ctx);
  }

  @Override
  public void enterLocalVariableDeclaration(LocalVariableDeclarationContext ctx) {

    System.out.print("LocalVariableDeclaration: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitLocalVariableDeclaration(LocalVariableDeclarationContext ctx) {

    // TODO Auto-generated method stub
    super.exitLocalVariableDeclaration(ctx);
  }

  @Override
  public void enterStatement(StatementContext ctx) {

    System.out.print("Statement: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitStatement(StatementContext ctx) {

    // TODO Auto-generated method stub
    super.exitStatement(ctx);
  }

  @Override
  public void enterCatchClause(CatchClauseContext ctx) {

    System.out.print("CatchClause: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitCatchClause(CatchClauseContext ctx) {

    // TODO Auto-generated method stub
    super.exitCatchClause(ctx);
  }

  @Override
  public void enterCatchType(CatchTypeContext ctx) {

    System.out.print("CatchType: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitCatchType(CatchTypeContext ctx) {

    // TODO Auto-generated method stub
    super.exitCatchType(ctx);
  }

  @Override
  public void enterFinallyBlock(FinallyBlockContext ctx) {

    System.out.print("FinallyBlock: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitFinallyBlock(FinallyBlockContext ctx) {

    // TODO Auto-generated method stub
    super.exitFinallyBlock(ctx);
  }

  @Override
  public void enterResourceSpecification(ResourceSpecificationContext ctx) {

    System.out.print("ResourceSpecification: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitResourceSpecification(ResourceSpecificationContext ctx) {

    // TODO Auto-generated method stub
    super.exitResourceSpecification(ctx);
  }

  @Override
  public void enterResources(ResourcesContext ctx) {

    System.out.print("Resources: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitResources(ResourcesContext ctx) {

    // TODO Auto-generated method stub
    super.exitResources(ctx);
  }

  @Override
  public void enterResource(ResourceContext ctx) {

    System.out.print("Resource: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitResource(ResourceContext ctx) {

    // TODO Auto-generated method stub
    super.exitResource(ctx);
  }

  @Override
  public void enterSwitchBlockStatementGroup(SwitchBlockStatementGroupContext ctx) {

    System.out.print("SwitchBlockStatementGroup: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitSwitchBlockStatementGroup(SwitchBlockStatementGroupContext ctx) {

    // TODO Auto-generated method stub
    super.exitSwitchBlockStatementGroup(ctx);
  }

  @Override
  public void enterSwitchLabel(SwitchLabelContext ctx) {

    System.out.print("SwitchLabel: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitSwitchLabel(SwitchLabelContext ctx) {

    // TODO Auto-generated method stub
    super.exitSwitchLabel(ctx);
  }

  @Override
  public void enterForControl(ForControlContext ctx) {

    System.out.print("ForControl: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitForControl(ForControlContext ctx) {

    // TODO Auto-generated method stub
    super.exitForControl(ctx);
  }

  @Override
  public void enterForInit(ForInitContext ctx) {

    System.out.print("ForInit: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitForInit(ForInitContext ctx) {

    // TODO Auto-generated method stub
    super.exitForInit(ctx);
  }

  @Override
  public void enterEnhancedForControl(EnhancedForControlContext ctx) {

    System.out.print("EnhancedForControl: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitEnhancedForControl(EnhancedForControlContext ctx) {

    // TODO Auto-generated method stub
    super.exitEnhancedForControl(ctx);
  }

  @Override
  public void enterParExpression(ParExpressionContext ctx) {

    System.out.print("ParExpression: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitParExpression(ParExpressionContext ctx) {

    // TODO Auto-generated method stub
    super.exitParExpression(ctx);
  }

  @Override
  public void enterExpressionList(ExpressionListContext ctx) {

    System.out.print("ExpressionList: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitExpressionList(ExpressionListContext ctx) {

    // TODO Auto-generated method stub
    super.exitExpressionList(ctx);
  }

  @Override
  public void enterExpression(ExpressionContext ctx) {

    System.out.print("Expression: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitExpression(ExpressionContext ctx) {

    // TODO Auto-generated method stub
    super.exitExpression(ctx);
  }

  @Override
  public void enterLambdaExpression(LambdaExpressionContext ctx) {

    System.out.print("LambdaExpression: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitLambdaExpression(LambdaExpressionContext ctx) {

    // TODO Auto-generated method stub
    super.exitLambdaExpression(ctx);
  }

  @Override
  public void enterLambdaParameters(LambdaParametersContext ctx) {

    System.out.print("LambdaParameters: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitLambdaParameters(LambdaParametersContext ctx) {

    // TODO Auto-generated method stub
    super.exitLambdaParameters(ctx);
  }

  @Override
  public void enterLambdaBody(LambdaBodyContext ctx) {

    System.out.print("LambdaBody: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitLambdaBody(LambdaBodyContext ctx) {

    // TODO Auto-generated method stub
    super.exitLambdaBody(ctx);
  }

  @Override
  public void enterPrimary(PrimaryContext ctx) {

    System.out.print("Primary: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitPrimary(PrimaryContext ctx) {

    // TODO Auto-generated method stub
    super.exitPrimary(ctx);
  }

  @Override
  public void enterMethodReference(MethodReferenceContext ctx) {

    System.out.print("MethodReference: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitMethodReference(MethodReferenceContext ctx) {

    // TODO Auto-generated method stub
    super.exitMethodReference(ctx);
  }

  @Override
  public void enterClassType(ClassTypeContext ctx) {

    System.out.print("ClassType: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitClassType(ClassTypeContext ctx) {

    // TODO Auto-generated method stub
    super.exitClassType(ctx);
  }

  @Override
  public void enterCreator(CreatorContext ctx) {

    System.out.print("Creator: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitCreator(CreatorContext ctx) {

    // TODO Auto-generated method stub
    super.exitCreator(ctx);
  }

  @Override
  public void enterCreatedName(CreatedNameContext ctx) {

    System.out.print("CreatedName: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitCreatedName(CreatedNameContext ctx) {

    // TODO Auto-generated method stub
    super.exitCreatedName(ctx);
  }

  @Override
  public void enterInnerCreator(InnerCreatorContext ctx) {

    System.out.print("InnerCreator: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitInnerCreator(InnerCreatorContext ctx) {

    // TODO Auto-generated method stub
    super.exitInnerCreator(ctx);
  }

  @Override
  public void enterArrayCreatorRest(ArrayCreatorRestContext ctx) {

    System.out.print("ArrayCreatorRest: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitArrayCreatorRest(ArrayCreatorRestContext ctx) {

    // TODO Auto-generated method stub
    super.exitArrayCreatorRest(ctx);
  }

  @Override
  public void enterClassCreatorRest(ClassCreatorRestContext ctx) {

    System.out.print("ClassCreatorRest: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitClassCreatorRest(ClassCreatorRestContext ctx) {

    // TODO Auto-generated method stub
    super.exitClassCreatorRest(ctx);
  }

  @Override
  public void enterExplicitGenericInvocation(ExplicitGenericInvocationContext ctx) {

    System.out.print("ExplicitGenericInvocation: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitExplicitGenericInvocation(ExplicitGenericInvocationContext ctx) {

    // TODO Auto-generated method stub
    super.exitExplicitGenericInvocation(ctx);
  }

  @Override
  public void enterTypeArgumentsOrDiamond(TypeArgumentsOrDiamondContext ctx) {

    System.out.print("TypeArgumentsOrDiamond: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitTypeArgumentsOrDiamond(TypeArgumentsOrDiamondContext ctx) {

    // TODO Auto-generated method stub
    super.exitTypeArgumentsOrDiamond(ctx);
  }

  @Override
  public void enterNonWildcardTypeArgumentsOrDiamond(NonWildcardTypeArgumentsOrDiamondContext ctx) {

    System.out.print("NonWildcardTypeArgumentsOrDiamond: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitNonWildcardTypeArgumentsOrDiamond(NonWildcardTypeArgumentsOrDiamondContext ctx) {

    // TODO Auto-generated method stub
    super.exitNonWildcardTypeArgumentsOrDiamond(ctx);
  }

  @Override
  public void enterNonWildcardTypeArguments(NonWildcardTypeArgumentsContext ctx) {

    System.out.print("NonWildcardTypeArguments: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitNonWildcardTypeArguments(NonWildcardTypeArgumentsContext ctx) {

    // TODO Auto-generated method stub
    super.exitNonWildcardTypeArguments(ctx);
  }

  @Override
  public void enterTypeList(TypeListContext ctx) {

    System.out.print("TypeList: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitTypeList(TypeListContext ctx) {

    // TODO Auto-generated method stub
    super.exitTypeList(ctx);
  }

  @Override
  public void enterTypeType(TypeTypeContext ctx) {

    System.out.print("TypeType: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitTypeType(TypeTypeContext ctx) {

    // TODO Auto-generated method stub
    super.exitTypeType(ctx);
  }

  @Override
  public void enterPrimitiveType(PrimitiveTypeContext ctx) {

    System.out.print("PrimitiveType: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitPrimitiveType(PrimitiveTypeContext ctx) {

    // TODO Auto-generated method stub
    super.exitPrimitiveType(ctx);
  }

  @Override
  public void enterTypeArguments(TypeArgumentsContext ctx) {

    System.out.print("TypeArguments: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitTypeArguments(TypeArgumentsContext ctx) {

    // TODO Auto-generated method stub
    super.exitTypeArguments(ctx);
  }

  @Override
  public void enterSuperSuffix(SuperSuffixContext ctx) {

    System.out.print("SuperSuffix: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitSuperSuffix(SuperSuffixContext ctx) {

    // TODO Auto-generated method stub
    super.exitSuperSuffix(ctx);
  }

  @Override
  public void enterExplicitGenericInvocationSuffix(ExplicitGenericInvocationSuffixContext ctx) {

    System.out.print("ExplicitGenericInvocationSuffix: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitExplicitGenericInvocationSuffix(ExplicitGenericInvocationSuffixContext ctx) {

    // TODO Auto-generated method stub
    super.exitExplicitGenericInvocationSuffix(ctx);
  }

  @Override
  public void enterArguments(ArgumentsContext ctx) {

    System.out.print("Arguments: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitArguments(ArgumentsContext ctx) {

    // TODO Auto-generated method stub
    super.exitArguments(ctx);
  }

  @Override
  public void enterDocumentation(DocumentationContext ctx) {

    System.out.print("Documentation: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitDocumentation(DocumentationContext ctx) {

    // TODO Auto-generated method stub
    super.exitDocumentation(ctx);
  }

  @Override
  public void enterDocumentationContent(DocumentationContentContext ctx) {

    System.out.print("DocumentationContent: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitDocumentationContent(DocumentationContentContext ctx) {

    // TODO Auto-generated method stub
    super.exitDocumentationContent(ctx);
  }

  @Override
  public void enterSkipWhitespace(SkipWhitespaceContext ctx) {

    System.out.print("SkipWhitespace: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitSkipWhitespace(SkipWhitespaceContext ctx) {

    // TODO Auto-generated method stub
    super.exitSkipWhitespace(ctx);
  }

  @Override
  public void enterDescription(DescriptionContext ctx) {

    System.out.print("Description: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitDescription(DescriptionContext ctx) {

    // TODO Auto-generated method stub
    super.exitDescription(ctx);
  }

  @Override
  public void enterDescriptionLine(DescriptionLineContext ctx) {

    System.out.print("DescriptionLine: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitDescriptionLine(DescriptionLineContext ctx) {

    // TODO Auto-generated method stub
    super.exitDescriptionLine(ctx);
  }

  @Override
  public void enterDescriptionLineStart(DescriptionLineStartContext ctx) {

    System.out.print("DescriptionLineStart: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitDescriptionLineStart(DescriptionLineStartContext ctx) {

    // TODO Auto-generated method stub
    super.exitDescriptionLineStart(ctx);
  }

  @Override
  public void enterDescriptionLineNoSpaceNoAt(DescriptionLineNoSpaceNoAtContext ctx) {

    System.out.print("DescriptionLineNoSpaceNoAt: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitDescriptionLineNoSpaceNoAt(DescriptionLineNoSpaceNoAtContext ctx) {

    // TODO Auto-generated method stub
    super.exitDescriptionLineNoSpaceNoAt(ctx);
  }

  @Override
  public void enterDescriptionLineElement(DescriptionLineElementContext ctx) {

    System.out.print("DescriptionLineElement: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitDescriptionLineElement(DescriptionLineElementContext ctx) {

    // TODO Auto-generated method stub
    super.exitDescriptionLineElement(ctx);
  }

  @Override
  public void enterDescriptionLineText(DescriptionLineTextContext ctx) {

    System.out.print("DescriptionLineText: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitDescriptionLineText(DescriptionLineTextContext ctx) {

    // TODO Auto-generated method stub
    super.exitDescriptionLineText(ctx);
  }

  @Override
  public void enterDescriptionNewline(DescriptionNewlineContext ctx) {

    System.out.print("DescriptionNewline: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitDescriptionNewline(DescriptionNewlineContext ctx) {

    // TODO Auto-generated method stub
    super.exitDescriptionNewline(ctx);
  }

  @Override
  public void enterTagSection(TagSectionContext ctx) {

    System.out.print("TagSection: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitTagSection(TagSectionContext ctx) {

    // TODO Auto-generated method stub
    super.exitTagSection(ctx);
  }

  @Override
  public void enterBlockTag(BlockTagContext ctx) {

    System.out.print("BlockTag: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitBlockTag(BlockTagContext ctx) {

    // TODO Auto-generated method stub
    super.exitBlockTag(ctx);
  }

  @Override
  public void enterBlockTagName(BlockTagNameContext ctx) {

    System.out.print("BlockTagName: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitBlockTagName(BlockTagNameContext ctx) {

    // TODO Auto-generated method stub
    super.exitBlockTagName(ctx);
  }

  @Override
  public void enterBlockTagContent(BlockTagContentContext ctx) {

    System.out.print("BlockTagContent: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitBlockTagContent(BlockTagContentContext ctx) {

    // TODO Auto-generated method stub
    super.exitBlockTagContent(ctx);
  }

  @Override
  public void enterBlockTagText(BlockTagTextContext ctx) {

    System.out.print("BlockTagText: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitBlockTagText(BlockTagTextContext ctx) {

    // TODO Auto-generated method stub
    super.exitBlockTagText(ctx);
  }

  @Override
  public void enterBlockTagTextElement(BlockTagTextElementContext ctx) {

    System.out.print("BlockTagTextElement: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitBlockTagTextElement(BlockTagTextElementContext ctx) {

    // TODO Auto-generated method stub
    super.exitBlockTagTextElement(ctx);
  }

  @Override
  public void enterInlineTag(InlineTagContext ctx) {

    System.out.print("InlineTag: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitInlineTag(InlineTagContext ctx) {

    // TODO Auto-generated method stub
    super.exitInlineTag(ctx);
  }

  @Override
  public void enterInlineTagName(InlineTagNameContext ctx) {

    System.out.print("InlineTagName: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitInlineTagName(InlineTagNameContext ctx) {

    // TODO Auto-generated method stub
    super.exitInlineTagName(ctx);
  }

  @Override
  public void enterInlineTagContent(InlineTagContentContext ctx) {

    System.out.print("InlineTagContent: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitInlineTagContent(InlineTagContentContext ctx) {

    // TODO Auto-generated method stub
    super.exitInlineTagContent(ctx);
  }

  @Override
  public void enterBraceExpression(BraceExpressionContext ctx) {

    System.out.print("BraceExpression: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitBraceExpression(BraceExpressionContext ctx) {

    // TODO Auto-generated method stub
    super.exitBraceExpression(ctx);
  }

  @Override
  public void enterBraceContent(BraceContentContext ctx) {

    System.out.print("BraceContent: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitBraceContent(BraceContentContext ctx) {

    // TODO Auto-generated method stub
    super.exitBraceContent(ctx);
  }

  @Override
  public void enterBraceText(BraceTextContext ctx) {

    System.out.print("BraceText: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitBraceText(BraceTextContext ctx) {

    // TODO Auto-generated method stub
    super.exitBraceText(ctx);
  }

  @Override
  public void enterEveryRule(ParserRuleContext ctx) {

    System.out.print("ParserRule: ");
    System.out.println(ctx.getText());
  }

  @Override
  public void exitEveryRule(ParserRuleContext ctx) {

    // TODO Auto-generated method stub
    super.exitEveryRule(ctx);
  }

  @Override
  public void visitTerminal(TerminalNode node) {

    // TODO Auto-generated method stub
    super.visitTerminal(node);
  }

  @Override
  public void visitErrorNode(ErrorNode node) {

    // TODO Auto-generated method stub
    super.visitErrorNode(node);
  }

}
