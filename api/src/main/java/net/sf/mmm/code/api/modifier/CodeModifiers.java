/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.modifier;

import java.beans.Visibility;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.util.io.api.IoMode;
import net.sf.mmm.util.io.api.RuntimeIoException;

/**
 * Represents the visibility of a {@link CodeMethod}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class CodeModifiers {

  /** The {@link #getModifiers() modifier} {@value}. */
  public static final String KEY_STATIC = "static";

  /** The {@link #getModifiers() modifier} {@value}. */
  public static final String KEY_FINAL = "final";

  /** The {@link #getModifiers() modifier} {@value}. */
  public static final String KEY_ABSTRACT = "abstract";

  /** The {@link #getModifiers() modifier} {@value}. */
  public static final String KEY_VOLATILE = "volatile";

  /** The {@link #getModifiers() modifier} {@value}. */
  public static final String KEY_TRANSIENT = "transient";

  /** The {@link #getModifiers() modifier} {@value}. */
  public static final String KEY_NATIVE = "native";

  /** The {@link #getModifiers() modifier} {@value}. */
  public static final String KEY_SYNCHRONIZED = "synchronized";

  /** The {@link #getModifiers() modifier} {@value}. */
  private static final String KEY_STRICTFP = "strictfp";

  /**
   * The {@link #getModifiers() modifier} {@value}.
   *
   * @see java.lang.reflect.Method#isDefault()
   */
  public static final String KEY_DEFAULT = "default";

  /** {@link CodeModifiers} for {@code public}. */
  public static final CodeModifiers MODIFIERS_PUBLIC = new CodeModifiers(CodeVisibility.PUBLIC);

  /** {@link CodeModifiers} for {@code public static}. */
  public static final CodeModifiers MODIFIERS_PUBLIC_STATIC = new CodeModifiers(CodeVisibility.PUBLIC, KEY_STATIC);

  /** {@link CodeModifiers} for {@code public static final}. */
  public static final CodeModifiers MODIFIERS_PUBLIC_STATIC_FINAL = new CodeModifiers(CodeVisibility.PUBLIC, KEY_STATIC, KEY_FINAL);

  /** {@link CodeModifiers} for {@code public final}. */
  public static final CodeModifiers MODIFIERS_PUBLIC_FINAL = new CodeModifiers(CodeVisibility.PUBLIC, KEY_FINAL);

  /** {@link CodeModifiers} for {@code private}. */
  public static final CodeModifiers MODIFIERS_PRIVATE = new CodeModifiers(CodeVisibility.PRIVATE);

  /** {@link CodeModifiers} for {@code private static}. */
  public static final CodeModifiers MODIFIERS_PRIVATE_STATIC = new CodeModifiers(CodeVisibility.PRIVATE, KEY_STATIC);

  /** {@link CodeModifiers} for {@code private static final}. */
  public static final CodeModifiers MODIFIERS_PRIVATE_STATIC_FINAL = new CodeModifiers(CodeVisibility.PRIVATE, KEY_STATIC, KEY_FINAL);

  /** {@link CodeModifiers} for {@code private final}. */
  public static final CodeModifiers MODIFIERS_PRIVATE_FINAL = new CodeModifiers(CodeVisibility.PRIVATE, KEY_FINAL);

  /** {@link CodeModifiers} for {@code protected}. */
  public static final CodeModifiers MODIFIERS_PROTECTED = new CodeModifiers(CodeVisibility.PROTECTED);

  /** {@link CodeModifiers} for {@code protected static}. */
  public static final CodeModifiers MODIFIERS_PROTECTED_STATIC = new CodeModifiers(CodeVisibility.PROTECTED, KEY_STATIC);

  /** {@link CodeModifiers} for {@code protected static final}. */
  public static final CodeModifiers MODIFIERS_PROTECTED_STATIC_FINAL = new CodeModifiers(CodeVisibility.PROTECTED, KEY_STATIC, KEY_FINAL);

  /** {@link CodeModifiers} for {@code protected final}. */
  public static final CodeModifiers MODIFIERS_PROTECTED_FINAL = new CodeModifiers(CodeVisibility.PROTECTED, KEY_FINAL);

  /** {@link CodeModifiers} that is empty (no modifiers). */
  public static final CodeModifiers MODIFIERS = new CodeModifiers(CodeVisibility.DEFAULT);

  /** {@link CodeModifiers} for {@code static}. */
  public static final CodeModifiers MODIFIERS_STATIC = new CodeModifiers(CodeVisibility.DEFAULT, KEY_STATIC);

  /** {@link CodeModifiers} for {@code static final}. */
  public static final CodeModifiers MODIFIERS_STATIC_FINAL = new CodeModifiers(CodeVisibility.DEFAULT, KEY_STATIC, KEY_FINAL);

  /** {@link CodeModifiers} for {@code final}. */
  public static final CodeModifiers MODIFIERS_FINAL = new CodeModifiers(CodeVisibility.DEFAULT, KEY_FINAL);

  /** {@link CodeModifiers} for {@code default}. */
  public static final CodeModifiers MODIFIERS_DEFAULT = new CodeModifiers(CodeVisibility.PUBLIC, KEY_DEFAULT);

  private final CodeVisibility visibility;

  private final Set<String> modifiers;

  /**
   * The constructor.
   *
   * @param visibility the {@link Visibility}.
   * @param modifiers the additional modifiers.
   */
  public CodeModifiers(CodeVisibility visibility, String... modifiers) {

    this(visibility, Arrays.asList(modifiers));
  }

  /**
   * The constructor.
   *
   * @param visibility the {@link CodeVisibility}.
   * @param modifiers the {@link #getModifiers() modifiers} excluding the {@link CodeVisibility}.
   */
  public CodeModifiers(CodeVisibility visibility, Collection<String> modifiers) {

    super();
    this.visibility = visibility;
    Set<String> set = new HashSet<>(modifiers);
    for (String modifier : set) {
      verifyModifier(modifier);
    }
    this.modifiers = Collections.unmodifiableSet(set);
  }

  private static void verifyModifier(String modifier) {

    Objects.requireNonNull(modifier, "modifier");
    if (modifier.isEmpty()) {
      throw new IllegalArgumentException(modifier);
    }
    if (CodeVisibility.of(modifier) != null) {
      throw new IllegalArgumentException(modifier);
    }
  }

  /**
   * @return the {@link CodeVisibility}.
   */
  public CodeVisibility getVisibility() {

    return this.visibility;
  }

  /**
   * @return the {@link Set} with all modifiers (not including the {@link #getVisibility() visibility}).
   */
  public Set<String> getModifiers() {

    return this.modifiers;
  }

  /**
   * @param modifier the {@link #getModifiers() modifier} to add.
   * @return this {@link CodeModifiers} if the given {@code modifier} is already {@link #getModifiers() contained} or a
   *         new instance of {@link CodeModifiers} with the given modifier.
   */
  public CodeModifiers addModifier(String modifier) {

    verifyModifier(modifier);
    if (this.modifiers.contains(modifier)) {
      return this;
    }
    Set<String> newModifiers = new HashSet<>(this.modifiers);
    newModifiers.add(modifier);
    return new CodeModifiers(this.visibility, newModifiers);
  }

  /**
   * @param modifier the {@link #getModifiers() modifier} to remove.
   * @return this {@link CodeModifiers} if the given {@code modifier} is not {@link #getModifiers() contained} or a new
   *         instance of {@link CodeModifiers} without the given modifier.
   */
  public CodeModifiers removeModifier(String modifier) {

    verifyModifier(modifier);
    if (!this.modifiers.contains(modifier)) {
      return this;
    }
    Set<String> newModifiers = new HashSet<>(this.modifiers);
    newModifiers.remove(modifier);
    return new CodeModifiers(this.visibility, newModifiers);
  }

  /**
   * @param newVisibility the new {@link #getVisibility() visibility}
   * @return this {@link CodeModifiers} if it already {@link #getVisibility() has} the given {@link CodeVisibility} or a
   *         new instance of {@link CodeModifiers} with the given {@link CodeVisibility}.
   */
  public CodeModifiers changeVisibility(CodeVisibility newVisibility) {

    if (this.visibility.equals(newVisibility)) {
      return this;
    }
    Set<String> newModifiers = new HashSet<>(this.modifiers);
    newModifiers.remove(this.visibility.toString());
    if (!CodeVisibility.DEFAULT.equals(newVisibility)) {
      newModifiers.add(newVisibility.toString());
    }
    return new CodeModifiers(newVisibility, newModifiers);
  }

  /**
   * @return {@code true} if {@link #KEY_ABSTRACT abstract}.
   */
  public boolean isAbstract() {

    return this.modifiers.contains(KEY_ABSTRACT);
  }

  /**
   * @return {@code true} if {@link #KEY_STATIC static}.
   */
  public boolean isStatic() {

    return this.modifiers.contains(KEY_STATIC);
  }

  /**
   * @return {@code true} if {@link #KEY_FINAL final}.
   */
  public boolean isFinal() {

    return this.modifiers.contains(KEY_FINAL);
  }

  /**
   * @return {@code true} if {@link #KEY_DEFAULT default}.
   */
  public boolean isDefault() {

    return this.modifiers.contains(KEY_DEFAULT);
  }

  @Override
  public int hashCode() {

    return Objects.hashCode(this.modifiers);
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }
    CodeModifiers other = (CodeModifiers) obj;
    if (!Objects.equals(this.modifiers, other.modifiers)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {

    StringBuilder buffer = new StringBuilder(32);
    String visibilityString = this.visibility.toString();
    if (!visibilityString.isEmpty()) {
      buffer.append(visibilityString);
      buffer.append(' ');
    }
    formatModifiers(buffer);
    return buffer.toString();
  }

  /**
   * @param buffer the {@link Appendable} where to append the {@link Appendable}.
   */
  public void formatModifiers(Appendable buffer) {

    try {
      if (isDefault()) {
        appendModifier(buffer, KEY_DEFAULT);
      }
      if (isAbstract()) {
        appendModifier(buffer, KEY_ABSTRACT);
      }
      if (isStatic()) {
        appendModifier(buffer, KEY_STATIC);
      }
      if (isFinal()) {
        appendModifier(buffer, KEY_FINAL);
      }
      for (String modifier : this.modifiers) {
        if (!KEY_DEFAULT.equals(modifier) && !KEY_ABSTRACT.equals(modifier) && !KEY_STATIC.equals(modifier) && !KEY_FINAL.equals(modifier)) {
          appendModifier(buffer, modifier);
        }
      }
    } catch (IOException e) {
      throw new RuntimeIoException(e, IoMode.WRITE);
    }
  }

  private static void appendModifier(Appendable buffer, String modifier) throws IOException {

    buffer.append(modifier);
    buffer.append(' ');
  }

  /**
   * @param javaModifiers the Java {@link Modifier} mask.
   * @return the given {@link Modifier} mask as {@link CodeModifiers}.
   */
  public static CodeModifiers of(int javaModifiers) {

    return of(javaModifiers, false);
  }

  /**
   * @param javaModifiers the Java {@link Modifier} mask.
   * @param defaultMethod - {@code true} for {@link java.lang.reflect.Method#isDefault() default method}, {@code false}
   *        otherwise.
   * @return the given {@link Modifier} mask as {@link CodeModifiers}.
   */
  public static CodeModifiers of(int javaModifiers, boolean defaultMethod) {

    List<String> modifiers = new ArrayList<>();
    if (Modifier.isAbstract(javaModifiers)) {
      modifiers.add(KEY_ABSTRACT);
    }
    if (Modifier.isStatic(javaModifiers)) {
      modifiers.add(KEY_STATIC);
    }
    if (Modifier.isFinal(javaModifiers)) {
      modifiers.add(KEY_FINAL);
    }
    if (Modifier.isNative(javaModifiers)) {
      modifiers.add(KEY_NATIVE);
    }
    if (Modifier.isSynchronized(javaModifiers)) {
      modifiers.add(KEY_SYNCHRONIZED);
    }
    if (Modifier.isTransient(javaModifiers)) {
      modifiers.add(KEY_TRANSIENT);
    }
    if (Modifier.isVolatile(javaModifiers)) {
      modifiers.add(KEY_VOLATILE);
    }
    if (Modifier.isStrict(javaModifiers)) {
      modifiers.add(KEY_STRICTFP);
    }
    String[] modifierArray = modifiers.toArray(new String[modifiers.size()]);
    return new CodeModifiers(CodeVisibility.of(javaModifiers), modifierArray);
  }
}
