/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.language;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.exception.ObjectMismatchException;
import io.github.mmm.code.api.CodeFile;
import io.github.mmm.code.api.CodePackage;
import io.github.mmm.code.api.item.CodeItem;
import io.github.mmm.code.api.item.CodeItemWithName;
import io.github.mmm.code.api.item.CodeItemWithQualifiedName;
import io.github.mmm.code.api.type.CodeType;

/**
 * The default implementation of {@link CodeLanguage} (for Java).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractCodeLanguage implements CodeLanguage {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractCodeLanguage.class);

  @Override
  public String verifyName(CodeItemWithName item, String name) {

    Pattern pattern = getNamePattern(item);
    return verifyName(item, pattern, name);
  }

  @Override
  public String verifySimpleName(CodeItemWithQualifiedName item, String simpleName) {

    Pattern pattern;
    if (item instanceof CodePackage) {
      CodePackage parentPackage = ((CodePackage) item).getParentPackage();
      if (parentPackage == null) {
        if (!"".equals(simpleName)) {
          throw new IllegalArgumentException("Root package name must be empty. It can not be '" + simpleName + "'.");
        }
        return simpleName;
      }
      pattern = getSimpleNamePatternForPackage();
    } else if (item instanceof CodeFile) {
      pattern = getSimpleNamePatternForType();
    } else if (item instanceof CodeType) {
      pattern = getSimpleNamePatternForType();
    } else {
      LOG.debug("Unexepcted item type: {}", item);
      return simpleName;
    }
    return verifyName(item, pattern, simpleName);
  }

  /**
   * @param item the {@link CodeItem} to verify.
   * @param pattern the {@link Pattern} defining valid names.
   * @param name the (simple) name to verify.
   * @return the given {@code name} (or potentially a "normalized" name that is valid).
   */
  protected String verifyName(CodeItem item, Pattern pattern, String name) {

    boolean valid = false;
    if (name != null) {
      if (!isTypeInDefaultPackage(item) && isRevervedKeyword(name, item)) {
        throw new ObjectMismatchException(name, "no reserved keyword");
      }
      if (pattern != null) {
        valid = pattern.matcher(name).matches();
      }
    }
    if (!valid) {
      throw new ObjectMismatchException(name, pattern);
    }
    return name;
  }

  private boolean isTypeInDefaultPackage(CodeItem item) { // e.g. primitive type "void"

    if (item instanceof CodeFile) {
      return (((CodeFile) item).getParentPackage().isRoot());
    }
    return false;
  }

  /**
   * @return the {@link Pattern} that defines valid {@link CodePackage} {@link CodePackage#getSimpleName() names}.
   */
  protected abstract Pattern getSimpleNamePatternForPackage();

  /**
   * @return the {@link Pattern} that defines valid {@link CodeType#getSimpleName() names} for {@link CodeType} and
   *         {@link CodeFile}.
   */
  protected abstract Pattern getSimpleNamePatternForType();

  /**
   * @param item the item to {@link #verifyName(CodeItemWithName, String) verify}. May be ignored if pattern and
   *        validation is independent of the item type.
   * @return the {@link Pattern} that defines valid names.
   */
  protected abstract Pattern getNamePattern(CodeItemWithName item);

  /**
   * @param name the name of the {@link CodeItem} to create.
   * @param item the {@link CodeItem} to verify that defines the given {@code name}. Will most probably be ignored but a
   *        language may allow keywords in specific places and forbid in others.
   * @return {@code true} if the given {@code name} is invalid (for the given {@link CodeItem}) because it is a reserved
   *         keyword.
   */
  protected abstract boolean isRevervedKeyword(String name, CodeItem item);

}
