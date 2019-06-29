package net.sf.mmm.code.impl.java.source.maven;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Customized class loader that we need in order to load all dependencies from a Maven project
 */
public class MavenClassLoader extends URLClassLoader {

  /**
   * The constructor.
   *
   * @param parent classloader parent
   * @param urls urls to load on this class loader
   */
  public MavenClassLoader(ClassLoader parent, URL... urls) {

    super(urls, parent);
  }

}