/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.arg;

import java.io.IOException;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.Consumer;

import io.github.mmm.code.api.arg.CodeParameter;
import io.github.mmm.code.api.arg.CodeParameters;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.member.CodeOperation;
import io.github.mmm.code.api.merge.CodeMergeStrategy;
import io.github.mmm.code.base.member.BaseOperation;

/**
 * Base implementation of {@link CodeParameters}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseParameters extends BaseOperationArgs<CodeParameter> implements CodeParameters {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseParameters(BaseOperation parent) {

    super(parent);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseParameters} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseParameters(BaseParameters template, CodeCopyMapper mapper) {

    super(template, mapper);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    Executable reflectiveObject = getParent().getReflectiveObject();
    List<? extends CodeParameter> sourceParams = null;
    int sourceParamsCount = 0;
    CodeParameters sourceParameters = getSourceCodeObject();
    if (sourceParameters != null) {
      sourceParams = sourceParameters.getDeclared();
      sourceParamsCount = sourceParams.size();
    }
    if (reflectiveObject != null) {
      List<CodeParameter> list = getList();
      int i = 0;
      for (Parameter param : reflectiveObject.getParameters()) {
        String name = null;
        CodeParameter baseParameter = null;
        if ((i < sourceParamsCount) && (sourceParams != null)) {
          baseParameter = sourceParams.get(i++);
          name = baseParameter.getName();
        }
        if (name == null) {
          name = param.getName();
        }
        BaseParameter parameter = new BaseParameter(this, name, param, baseParameter);
        list.add(parameter);
      }
    }
  }

  @Override
  public CodeParameter getDeclared(String name) {

    initialize();
    return getByName(name);
  }

  @Override
  public CodeParameter add(String name) {

    BaseParameter parameter = new BaseParameter(this, name);
    add(parameter);
    return parameter;
  }

  @Override
  public CodeParameters getSourceCodeObject() {

    CodeOperation sourceOperation = getParent().getSourceCodeObject();
    if (sourceOperation != null) {
      return sourceOperation.getParameters();
    }
    return null;
  }

  @Override
  protected void rename(CodeParameter child, String oldName, String newName, Consumer<String> renamer) {

    super.rename(child, oldName, newName, renamer);
  }

  @Override
  public CodeParameters merge(CodeParameters o, CodeMergeStrategy strategy) {

    if (strategy == CodeMergeStrategy.KEEP) {
      return this;
    }
    BaseParameters other = (BaseParameters) o;
    List<? extends CodeParameter> otherParameters = other.getDeclared();
    if (strategy == CodeMergeStrategy.OVERRIDE) {
      clear();
      for (CodeParameter otherParameter : otherParameters) {
        CodeParameter copyParameter = doCopyNode(otherParameter, this);
        add(copyParameter);
      }
    } else {
      List<? extends CodeParameter> myParameters = getDeclared();
      int i = 0;
      int len = myParameters.size();
      assert (len == otherParameters.size());
      for (CodeParameter otherParameter : otherParameters) {
        CodeParameter myParameter = null;
        if (i < len) {
          myParameter = myParameters.get(i++); // merging via index as by name could cause errors on mismatch
        }
        if (myParameter == null) {
          CodeParameter copyParameter = doCopyNode(otherParameter, this);
          add(copyParameter);
        } else {
          myParameter.merge(otherParameter, strategy);
        }
      }
    }
    return this;
  }

  @Override
  public BaseParameters copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseParameters copy(CodeCopyMapper mapper) {

    return new BaseParameters(this, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    writeReference(sink, newline, true);
  }

  void writeReference(Appendable sink, String newline, boolean declaration) throws IOException {

    String prefix = "";
    for (CodeParameter parameter : getList()) {
      sink.append(prefix);
      parameter.write(sink, newline, null, null);
      prefix = ", ";
    }
  }

}
