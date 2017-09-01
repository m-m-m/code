/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.doc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO: this class ...
 *
 * @author hohwille
 * @since 1.0.0
 */
public class XmlTag implements Tag {

  private static final Pattern PATTERN_ATTRIBUTE = Pattern.compile("([a-zA-Z_][a-zA-Z0-9:._-]*)(='[^']*'|=\"[^\"]*\"|)");

  private final String name;

  private final boolean opening;

  private final boolean closing;

  private final String attributes;

  private final Tag parent;

  private Map<String, String> attributesMap;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name} (will be converted to lower case).
   * @param opening the {@link #isOpening() opening} flag.
   * @param closing the {@link #isClosing() closing} flag.
   * @param attributes the optional {@link #getAttributes() attributes}.
   * @param parent the optional {@link #getParent() parent}.
   */
  public XmlTag(String name, boolean opening, boolean closing, String attributes, Tag parent) {

    super();
    this.name = name;
    this.opening = opening;
    this.closing = closing;
    if (attributes == null) {
      this.attributes = "";
    } else {
      this.attributes = attributes.trim();
    }
    this.parent = parent;
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public boolean isOpening() {

    return this.opening;
  }

  @Override
  public boolean isClosing() {

    return this.closing;
  }

  @Override
  public Tag getParent() {

    return this.parent;
  }

  @Override
  public String getAttributes() {

    return this.attributes;
  }

  @Override
  public Map<String, String> getAttributesAsMap() {

    if (this.attributesMap == null) {
      if (this.attributes.isEmpty()) {
        this.attributesMap = Collections.emptyMap();
      } else {
        Map<String, String> map = new HashMap<>();
        Matcher matcher = PATTERN_ATTRIBUTE.matcher(this.attributes);
        while (matcher.find()) {
          String key = matcher.group(1);
          String value = matcher.group(2);
          // TODO resolve & in value
          map.put(key, value);
        }
        this.attributesMap = map;
      }
    }
    return this.attributesMap;
  }

  @Override
  public String toString() {

    StringBuilder buffer = new StringBuilder();
    buffer.append('<');
    if (isClosing() && !isOpening()) {
      buffer.append('/');
    }
    buffer.append(this.name);
    if (!this.attributes.isEmpty()) {
      buffer.append(' ');
      buffer.append(this.attributes);
    }
    if (isClosing() && isOpening()) {
      buffer.append('/');
    }
    buffer.append('>');
    return buffer.toString();
  }

}
