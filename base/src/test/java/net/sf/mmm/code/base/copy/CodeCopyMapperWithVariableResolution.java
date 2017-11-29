/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.copy;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.CodePathElement;
import net.sf.mmm.code.api.copy.AbstractCodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyType;
import net.sf.mmm.code.api.item.CodeMutableItem;
import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.util.lang.api.CaseSyntax;

/**
 * Implementation of {@link AbstractCodeCopyMapper} that resolves variables.
 */
public class CodeCopyMapperWithVariableResolution extends AbstractCodeCopyMapper {

  private static final Logger LOG = LoggerFactory.getLogger(CodeCopyMapperWithVariableResolution.class);

  private static final Pattern VARIABLE_PATTERN = Pattern.compile("[xX]_((\\pL|\\p{Nd}|[_-])+)_[xX]");

  private final Map<String, String> variables;

  private final Map<String, String> resolutions;

  /**
   * The constructor.
   */
  public CodeCopyMapperWithVariableResolution() {

    super();
    this.variables = new HashMap<>();
    this.resolutions = new HashMap<>();
  }

  /**
   * @param key the key (name) of the variable.
   * @param value the value of the variable.
   */
  public void setVariable(String key, String value) {

    this.variables.put(key.toLowerCase(Locale.US), value);
  }

  private String resolveVariables(String name) {

    if (name == null) {
      return null;
    }
    String result = this.resolutions.get(name);
    if (result != null) {
      return result;
    }
    Matcher matcher = VARIABLE_PATTERN.matcher(name);
    if (matcher.find()) {
      StringBuffer buffer = new StringBuffer(name.length());
      do {
        String variable = matcher.group(1);
        String key = variable.replaceAll("[_-]", "").toLowerCase();
        String value = this.variables.get(key);
        if (value == null) {
          LOG.warn("Unresolved variable {} - could not resolve {}", name, key);
          return name;
        }
        if (!value.isEmpty() && (value.indexOf('.') < 0)) {
          CaseSyntax caseSyntax = CaseSyntax.ofExample(variable, true);
          value = caseSyntax.convert(value);
        }
        matcher.appendReplacement(buffer, value);
      } while (matcher.find());
      matcher.appendTail(buffer);
      result = buffer.toString();
      this.resolutions.put(name, result);
      return result;
    } else {
      return name;
    }
  }

  @Override
  public String mapName(String name, CodeNode node) {

    return resolveVariables(name);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected <N extends CodeNode> N doMap(N node, CodeCopyType type) {

    CodeNode result = null;
    if (type == CodeCopyType.CHILD) {
      if (node instanceof CodePackage) {
        result = doMapPackage((CodePackage) node);
      } else if (node instanceof CodeMutableItem) {
        result = (CodeNode) ((CodeMutableItem) node).copy(this);
      }
    } else if (node instanceof CodeGenericType) {
      CodeGenericType genericType = (CodeGenericType) node;
      CodeType nodeType = genericType.asType();
      CodePackage parentPkgMapped = getMapping(nodeType.getParentPackage());
      if (parentPkgMapped != null) {
        result = genericType.copy(this);
      }
    }
    if (result != null) {
      return (N) result;
    }
    return node;
  }

  private CodePackage doMapPackage(CodePackage pkg) {

    String simpleName = pkg.getSimpleName();
    String resolvedName = resolveVariables(simpleName);
    CodePackage resolvedPkg = null;
    if (!resolvedName.equals(simpleName)) {
      if (resolvedName.isEmpty()) {
        resolvedPkg = resolveParentPackage(pkg);
      } else if (resolvedName.indexOf('.') > 0) {
        CodePackage parentPkg = resolveParentPackage(pkg);
        CodeName resolvedPath = pkg.getContext().parseName(resolvedName);
        resolvedPkg = parentPkg.getChildren().getOrCreatePackage(resolvedPath, !parentPkg.isImmutable());
      }
    }
    if (resolvedPkg == null) {
      resolvedPkg = pkg.copy(this);
    } else {
      registerMapping(pkg, resolvedPkg);
      for (CodePathElement child : pkg.getChildren()) {
        CodePathElement childCopy = map(child, CodeCopyType.CHILD);
        resolvedPkg.getChildren().add(childCopy);
      }
    }
    return resolvedPkg;
  }

  private CodePackage resolveParentPackage(CodePackage pkg) {

    CodePackage copy;
    CodePackage parentPkg = pkg.getParentPackage();
    copy = getMapping(parentPkg);
    if (copy == null) {
      copy = parentPkg;
    }
    return copy;
  }

}
