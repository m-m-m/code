/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.java.maven.impl;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;

import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.java.maven.api.MavenConstants;

/**
 * Utility to auto-magically detect the local maven repository (in development situations).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class MavenLocalRepositoryLocator implements MavenConstants {

  private static final Logger LOG = LoggerFactory.getLogger(MavenLocalRepositoryLocator.class);

  /**
   * @return the {@link File#isDirectory() directory} where the maven local repository is located by default.
   */
  public static File getDefaultLocalRepository() {

    File home = new File(System.getProperty("user.home"));
    File m2 = new File(home, CONFIG_FOLDER);
    File localRepo = new File(m2, "repository");
    return localRepo;
  }

  /**
   * @param pomReader the {@link MavenBridgeImpl} to be used as fallback for odd configuration situations.
   * @return the {@link File#isDirectory() directory} pointing to the local maven repository.
   */
  public static File getLocalRepository(MavenBridgeImpl pomReader) {

    CodeSource codeSource = Model.class.getProtectionDomain().getCodeSource();
    if (codeSource != null) {
      URL location = codeSource.getLocation();
      try {
        File source = new File(location.toURI());
        File versionFolder = source.getParentFile();
        File localRepo = findLocalRepository(versionFolder);
        if ((localRepo == null) && source.isFile()) {
          File pomFile = pomReader.findPom(source);
          if (pomFile != null) {
            Model model = pomReader.readModel(pomFile);
            String groupId = getGroupId(model);
            if (groupId != null) {
              File artifactFolder = versionFolder.getParentFile();
              int dotIndex = 0;
              File parent = artifactFolder;
              while (dotIndex >= 0) {
                parent = parent.getParentFile();
                dotIndex = groupId.indexOf('.', dotIndex + 1);
              }
              return parent;
            }
          }
        }
        if (localRepo == null) {
          localRepo = getDefaultLocalRepository();
        }
        return localRepo;
      } catch (URISyntaxException e) {
        LOG.warn("Illegal code source location {}.", location, e);
      }
    }
    return getDefaultLocalRepository();
  }

  private static String getGroupId(Model model) {

    String groupId = model.getGroupId();
    if (groupId == null) {
      Parent parent = model.getParent();
      if (parent != null) {
        groupId = parent.getGroupId();
      }
    }
    return groupId;
  }

  private static File findLocalRepository(File folder) {

    if (folder == null) {
      return null;
    }
    File parent = folder.getParentFile();
    if (folder.getName().equals(REPOSITORY_FOLDER)) {
      if (parent.getName().equals(CONFIG_FOLDER)) {
        return folder;
      } else if (!folder.getPath().replace('\\', '/').contains("/repository/")) {
        return folder;
      }
    }
    return findLocalRepository(parent);
  }

}
