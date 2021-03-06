/*
    Copyright 2014 Immutables Authors and Contributors

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.immutables.value.processor.meta;

import com.google.common.base.Optional;
import java.util.Set;
import javax.lang.model.element.TypeElement;
import com.google.common.collect.Lists;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.List;
import javax.annotation.Nullable;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import org.immutables.generator.AnnotationMirrors;

final class Annotations {
  private Annotations() {}

  private static final String PREFIX_JAVA_LANG = "java.lang.";
  private static final String PREFIX_ORG_IMMUTABLES = "org.immutables.";

  static final String NULLABLE_SIMPLE_NAME = "Nullable";

  static List<CharSequence> getAnnotationLines(
      Element element,
      Optional<Set<String>> includeAnnotations,
      ElementType elementType) {
    List<CharSequence> lines = Lists.newArrayList();

    for (AnnotationMirror annotation : element.getAnnotationMirrors()) {
      TypeElement annotationElement = (TypeElement) annotation.getAnnotationType().asElement();

      if (annotationMatchesName(annotationElement, includeAnnotations)
          && annotationMatchesTarget(annotationElement, elementType)) {
        lines.add(AnnotationMirrors.toCharSequence(annotation));
      }
    }
    return lines;
  }

  private static boolean annotationMatchesName(
      TypeElement annotationElement,
      Optional<Set<String>> includeAnnotations) {
    String qualifiedName = annotationElement.getQualifiedName().toString();

    if (includeAnnotations.isPresent()) {
      return includeAnnotations.get().contains(qualifiedName);
    }

    if (qualifiedName.startsWith(PREFIX_ORG_IMMUTABLES)
        || qualifiedName.startsWith(PREFIX_JAVA_LANG)
        || qualifiedName.equals(JsonPropertyMirror.QUALIFIED_NAME)) {
      // skip immutables and core java annotations (like Override etc)
      // Also skip JsonProperty annotation as we will add it separately
      return false;
    }

    if (annotationElement.getSimpleName().contentEquals(NULLABLE_SIMPLE_NAME)) {
      // we expect to propagate nullability separately
      return false;
    }

    return true;
  }

  static boolean annotationMatchesTarget(Element annotationElement, ElementType elementType) {
    @Nullable
    Target target = annotationElement.getAnnotation(Target.class);
    if (target != null) {
      ElementType[] targetTypes = target.value();
      if (targetTypes.length == 0) {
        return false;
      }
      boolean found = false;
      for (ElementType t : targetTypes) {
        if (t == elementType) {
          found = true;
        }
      }
      if (!found) {
        return false;
      }
    }
    return true;
  }
}
