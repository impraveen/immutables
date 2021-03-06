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

import com.google.common.base.CaseFormat;
import org.immutables.generator.Naming;

public final class Styles {
  private final StyleInfo style;
  private final Scheme scheme;

  Styles(StyleInfo style) {
    this.style = style;
    this.scheme = new Scheme();
  }

  public ValueImmutableInfo defaults() {
    return style.defaults();
  }

  public StyleInfo style() {
    return style;
  }

  public UsingName.TypeNames forType(String name) {
    return new UsingName(name, scheme, "").new TypeNames();
  }

  public UsingName.AttributeNames forAccessorWithRaw(String name, String raw) {
    return new UsingName(name, scheme, raw).new AttributeNames();
  }

  public UsingName.AttributeNames forAccessor(String name) {
    return forAccessorWithRaw(name, "");
  }

  class Scheme {
    Naming[] typeAbstract = Naming.fromAll(style.typeAbstract());
    Naming typeImmutable = Naming.from(style.typeImmutable());
    Naming typeImmutableNested = Naming.from(style.typeImmutableNested());
    Naming typeImmutableEnclosing = Naming.from(style.typeImmutableEnclosing());
    Naming of = Naming.from(style.of());
    Naming copyOf = Naming.from(style.copyOf());
    Naming instance = Naming.from(style.instance());

    Naming typeBuilder = Naming.from(style.typeBuilder());
    Naming from = Naming.from(style.from());
    Naming build = Naming.from(style.build());

    Naming builder() {
      return Naming.from(style.builder());
    }

    Naming newBuilder() {
      return Naming.from(style.newBuilder());
    }

//    Naming typeModifiable = Naming.from(style.typeModifiable());
//    Naming create = Naming.from(style.create());
//    Naming set = Naming.from(style.set());
//    Naming isSet = Naming.from(style.isSet());
//    Naming toImmutable = Naming.from(style.toImmutable());

    Naming[] get = Naming.fromAll(style.get());
    Naming init = Naming.from(style.init());
    Naming with = Naming.from(style.with());

//    Naming unset = Naming.from(style.unset());
//    Naming clear = Naming.from(style.clear());

    Naming add = Naming.from(style.add());
    Naming addAll = Naming.from(style.addAll());
    Naming put = Naming.from(style.put());
    Naming putAll = Naming.from(style.putAll());
  }

  public static class UsingName {
    private final String name;
    private final Scheme scheme;
    private final String forcedRaw;

    private UsingName(String name, Scheme scheme, String forcedRaw) {
      this.name = name;
      this.scheme = scheme;
      this.forcedRaw = forcedRaw;
    }

    private String detectRawFromGet() {
      if (!forcedRaw.isEmpty()) {
        return forcedRaw;
      }
      for (Naming naming : scheme.get) {
        String raw = naming.detect(name);
        if (!raw.isEmpty()) {
          return raw;
        }
      }
      return name;
    }

    private String detectRawFromAbstract() {
      if (!forcedRaw.isEmpty()) {
        return forcedRaw;
      }
      return detectRawFromAbstract(name);
    }

    /** Forced raw will not work if using this method */
    String detectRawFromAbstract(String abstractName) {
      for (Naming naming : scheme.typeAbstract) {
        String raw = naming.detect(abstractName);
        if (!raw.isEmpty()) {
          // TBD is there a way to raise abstraction
          return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, raw);
        }
      }
      return abstractName;
    }

    public class TypeNames {
      public final Scheme namings = scheme;
      public final String raw = detectRawFromAbstract();
      public final String typeAbstract = name;
      public final String typeImmutable = scheme.typeImmutable.apply(raw);
      public final String typeImmutableEnclosing = scheme.typeImmutableEnclosing.apply(raw);
      public final String typeImmutableNested = scheme.typeImmutableNested.apply(raw);
      public final String of = scheme.of.apply(raw);
      public final String instance = scheme.instance.apply(raw);
      // Builder template is being applied programatically in Constitution class
      // public final String typeBuilder = scheme.typeBuilder.apply(raw);
      public final String copyOf = scheme.copyOf.apply(raw);
      public final String from = scheme.from.apply(raw);
      public final String build = scheme.build.apply(raw);

      public final String builder() {
        return scheme.builder().apply(raw);
      }

//      public final String typeModifiable = scheme.typeModifiable.apply(raw);
//      public final String create = scheme.create.apply(raw);
//      public final String toImmutable = scheme.toImmutable.apply(raw);

      String rawFromAbstract(String abstractName) {
        return detectRawFromAbstract(abstractName);
      }
    }

    public class AttributeNames {
      public final String raw = detectRawFromGet();
      public final String get = name;
      public final String init = scheme.init.apply(raw);
      public final String with = scheme.with.apply(raw);
      public final String add = scheme.add.apply(raw);
      public final String addAll = scheme.addAll.apply(raw);
      public final String put = scheme.put.apply(raw);
      public final String putAll = scheme.putAll.apply(raw);
//      public final String set = scheme.set.apply(raw);
//      public final String isSet = scheme.isSet.apply(raw);

//      public final String unset = scheme.unset.apply(raw);
//      public final String clear = scheme.clear.apply(raw);
    }
  }
}
