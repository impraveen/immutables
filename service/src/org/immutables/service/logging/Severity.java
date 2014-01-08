/*
    Copyright 2013-2014 Immutables.org authors

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
package org.immutables.service.logging;

/**
 * The severity of log event.
 */
public enum Severity {

  /**
   * System encountered error and may work improperly and may be in undefined state, administrator
   * monitoring or intervention required.
   */
  ERROR(1),

  /**
   * System automatically reacted to some events and considered to work stable, but warns and
   * suggests administrator to review system.
   */
  WARNING(2),

  /**
   * Information events that tell about important system state changes or actions.
   */
  INFO(3);

  private final int value;

  Severity(int value) {
    this.value = value;
  }

  /**
   * Value.
   * @return the int
   */
  public int value() {
    return value;
  }
}