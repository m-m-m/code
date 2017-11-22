/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.doc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.arg.CodeException;
import net.sf.mmm.code.api.arg.CodeParameter;
import net.sf.mmm.code.api.doc.CodeDoc;
import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.element.CodeElementWithTypeVariables;
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.api.type.CodeTypeVariable;

/**
 * Parser to parse and apply {@link CodeDoc} read as plain lines of {@link String}s from the source code to
 * {@link net.sf.mmm.code.api.element.CodeElement}s.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseDocParser {

  private static final Logger LOG = LoggerFactory.getLogger(BaseDocParser.class);

  private static final String TAG_PARAM = "@" + CodeDoc.TAG_PARAM;

  private static final int TAG_PARAM_LENGTH = TAG_PARAM.length();

  private static final String TAG_RETURN = "@" + CodeDoc.TAG_RETURN;

  private static final int TAG_RETURN_LENGTH = TAG_RETURN.length();

  private static final String TAG_THROWS = "@" + CodeDoc.TAG_THROWS;

  private static final int TAG_THROWS_LENGTH = TAG_THROWS.length();

  private static final String TAG_EXCEPTION = "@exception";

  private static final int TAG_EXCEPTION_LENGTH = TAG_EXCEPTION.length();

  private final Map<String, BaseDocTag> argMap;

  private final Map<String, BaseDocTag> genericMap;

  private final Map<String, BaseDocTag> exceptionMap;

  private BaseDocTag returns;

  /**
   * The constructor.
   */
  public BaseDocParser() {

    super();
    this.argMap = new HashMap<>();
    this.genericMap = new HashMap<>();
    this.exceptionMap = new HashMap<>();
  }

  private void parseDocForElement(CodeElement element, List<String> javaDocLines) {

    clear();
    if ((javaDocLines == null) || (javaDocLines.isEmpty())) {
      return;
    }
    List<String> lines = element.getDoc().getLines();
    BaseDocTag tag = null;
    int i = 0;
    int size = javaDocLines.size();
    while (i < size) {
      String line = javaDocLines.get(i++);
      BaseDocTag oldTag = tag;
      if (!line.isEmpty()) {
        if (line.charAt(0) == '@') {
          if (line.startsWith(TAG_PARAM)) {
            tag = parseParamTag(line);
          } else if (line.startsWith(TAG_RETURN)) {
            parseReturnTag(line);
            tag = this.returns;
          } else if (line.startsWith(TAG_THROWS)) {
            tag = parseThrowsTag(line, TAG_THROWS_LENGTH);
          } else if (line.startsWith(TAG_EXCEPTION)) {
            tag = parseThrowsTag(line, TAG_EXCEPTION_LENGTH);
          }
        }
      }
      if (tag == oldTag) {
        if (tag == null) {
          lines.add(line);
        } else {
          tag.add(line);
        }
      }
    }
  }

  private BaseDocTag parseThrowsTag(String line, int tagLength) {

    int start = tagLength;
    int length = line.length();
    char c = ' ';
    while (start < length) {
      c = line.charAt(start);
      if (c == ' ') {
        start++;
      } else {
        break;
      }
    }
    if (c == ' ') {
      return null;
    }
    int end = line.indexOf(' ', start);
    if (end < start) {
      end = length;
    }
    return parseTag(line, start, end, length, this.exceptionMap, "Invalid JavaDoc with duplicated throws: {}");
  }

  private BaseDocTag parseTag(String line, int start, int end, int length, Map<String, BaseDocTag> map, String duplicationLog) {

    String name = line.substring(start, end);
    int index = end + 1;
    while (index < length) {
      if (line.charAt(index) == ' ') {
        index++;
      } else {
        break;
      }
    }
    String tagDoc = null;
    if (index < length) {
      tagDoc = line.substring(index);
    }
    BaseDocTag tag = new BaseDocTag(tagDoc);
    BaseDocTag existing = map.put(name, tag);
    if (existing != null) {
      LOG.warn(duplicationLog, line);
    }
    return tag;
  }

  private BaseDocTag parseParamTag(String line) {

    int length = line.length();
    int start = TAG_PARAM_LENGTH;
    char c = ' ';
    while (start < length) {
      c = line.charAt(start);
      if (c == ' ') {
        start++;
      } else {
        break;
      }
    }
    String duplicationLog = "Invalid JavaDoc with duplicated param: {}";
    if (c == ' ') {
      return null;
    } else if (c == '<') {
      int end = line.indexOf('>', start);
      return parseTag(line, start, end, length, this.genericMap, duplicationLog);
    } else {
      int end = line.indexOf(' ', start);
      return parseTag(line, start, end, length, this.argMap, duplicationLog);
    }
  }

  private void parseReturnTag(String line) {

    int length = line.length();
    int start;
    start = TAG_RETURN_LENGTH;
    if (start < length) {
      if (line.charAt(start) == 's') {
        start++;
      }
      while (start < length) {
        if (line.charAt(start) == ' ') {
          start++;
        } else {
          break;
        }
      }
    }
    String returnDoc = line.substring(start).trim();
    if (this.returns == null) {
      this.returns = new BaseDocTag(returnDoc);
    } else {
      LOG.warn("Invalid JavaDoc with duplicated return: {}", line);
      this.returns.add(returnDoc);
    }
  }

  private void clear() {

    this.argMap.clear();
    this.exceptionMap.clear();
    this.genericMap.clear();
    this.returns = null;
  }

  /**
   * @param operation the {@link CodeOperation} that has already been created with its entire signature.
   * @param javaDocLines the {@link List} of plain {@link CodeDoc} {@link CodeDoc#getLines() lines} to parse
   *        and apply to the given {@link CodeOperation}.
   */
  public void parseDoc(CodeOperation operation, List<String> javaDocLines) {

    parseDocForElement(operation, javaDocLines);
    for (CodeParameter arg : operation.getParameters()) {
      BaseDocTag argDoc = getArgumentDoc(arg.getName());
      if (argDoc != null) {
        argDoc.put(arg.getDoc());
      }
    }
    for (CodeException exception : operation.getExceptions()) {
      BaseDocTag exceptionDoc = getExceptionDoc(exception.getType());
      if (exceptionDoc != null) {
        exceptionDoc.put(exception.getDoc());
      }
    }
    applyTypeVariablesDoc(operation);
    if ((this.returns != null) && (operation instanceof CodeMethod)) {
      this.returns.put(((CodeMethod) operation).getReturns().getDoc());
    }
  }

  private void applyTypeVariablesDoc(CodeElementWithTypeVariables element) {

    for (CodeTypeVariable typeParam : element.getTypeParameters()) {
      BaseDocTag typeParamDoc = getTypeParameterDoc(typeParam.getName());
      if (typeParamDoc != null) {
        typeParamDoc.put(typeParam.getDoc());
      }
    }
  }

  /**
   * @param type the {@link CodeType} that has already been created with type variables.
   * @param javaDocLines the {@link List} of plain {@link CodeDoc} {@link CodeDoc#getLines() lines} to parse
   *        and apply to the given {@link CodeType}.
   */
  public void parseDoc(CodeType type, List<String> javaDocLines) {

    parseDocForElement(type, javaDocLines);
    applyTypeVariablesDoc(type);
  }

  /**
   * @param field the {@link CodeField}.
   * @param javaDocLines the {@link List} of plain {@link CodeDoc} {@link CodeDoc#getLines() lines} to parse
   *        and apply to the given {@link CodeField}.
   */
  public void parseDoc(CodeField field, List<String> javaDocLines) {

    parseDocForElement(field, javaDocLines);
  }

  private BaseDocTag getArgumentDoc(String name) {

    return this.argMap.get(name);
  }

  private BaseDocTag getTypeParameterDoc(String name) {

    return this.genericMap.get(name);
  }

  private BaseDocTag getExceptionDoc(CodeGenericType exception) {

    BaseDocTag exceptionTag = this.exceptionMap.get(exception.getSimpleName());
    if (exceptionTag == null) {
      exceptionTag = this.exceptionMap.get(exception.getQualifiedName());
    }
    return exceptionTag;
  }

}
