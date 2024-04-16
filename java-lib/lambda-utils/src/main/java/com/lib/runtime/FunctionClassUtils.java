package com.lib.runtime;

import org.springframework.util.ClassUtils;


public final class FunctionClassUtils {

  public static final String MAIN_CLASS = "MAIN_CLASS";

  private FunctionClassUtils() {

  }

  /**
   * Discovers the start class in the currently running application.
   * The discover search order is 'MAIN_CLASS' environment property,
   * 'MAIN_CLASS' system property, META-INF/MANIFEST.MF:'Start-Class' attribute,
   * meta-inf/manifest.mf:'Start-Class' attribute.
   *
   * @return instance of Class which represent the start class of the application.
   */
  public static Class<?> getStartClass() {
    ClassLoader classLoader = FunctionClassUtils.class.getClassLoader();
    return getStartClass(classLoader);
  }

  static Class<?> getStartClass(ClassLoader classLoader) {
    Class<?> mainClass = null;
    if (System.getenv(MAIN_CLASS) != null) {
      mainClass = ClassUtils.resolveClassName(System.getenv(MAIN_CLASS), classLoader);
    } else if (System.getProperty(MAIN_CLASS) != null) {
      mainClass = ClassUtils.resolveClassName(System.getProperty(MAIN_CLASS), classLoader);
    }
    return mainClass;
  }
}
