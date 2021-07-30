/*
 * Created on Wed Apr 07 2021
 *
 * The Unlicense
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute
 * this software, either in source code form or as a compiled binary, for any
 * purpose, commercial or non-commercial, and by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the public
 * domain. We make this dedication for the benefit of the public at large and to
 * the detriment of our heirs and successors. We intend this dedication to be an
 * overt act of relinquishment in perpetuity of all present and future rights to
 * this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */

package uc.seng301.eventapp.model;

import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * This entity class types one or multiple {@link Event}. The event's name
 * should be unique. Contains references to all events typed by this type.
 */
@Entity
public class EventType {

  @Id
  @Column(name = "id_event_type")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long eventTypeId;

  private String name;

  @OneToMany(mappedBy = "eventType")
  private List<Event> events;

  /**
   * JPA compliant no-args constructor.
   */
  EventType() {
    // JPA requirement
  }

  /**
   * Convenience constructor.
   *
   * @param name a (non-empty and non null) name
   * @throws IllegalArgumentException if given name is empty or null
   */
  public EventType(String name) throws IllegalArgumentException {
    if (null == name || name.isEmpty()) {
      throw new IllegalArgumentException("Event name cannot be null or empty");
    }
    this.name = name;
  }

  /**
   * Get this event's technical id
   *
   * @return a unique id
   */
  public Long getEventTypeId() {
    return this.eventTypeId;
  }

  /**
   * Set the event's technical id (should never be used programmatically)
   *
   * @param eventTypeId a unique id (managed by JPA)
   */
  public void setEventTypeId(Long eventTypeId) {
    this.eventTypeId = eventTypeId;
  }

  /**
   * Get all events with this type
   *
   * @return a (possibly empty) list of events
   */
  public List<Event> getEvents() {
    return null != this.events ? this.events : Collections.emptyList();
  }

  /**
   * Set the list of events with this type (should never be called, JPA used)
   *
   * @param events a set of events associated with this type
   */
  public void setEvents(List<Event> events) {
    this.events = events;
  }

  /**
   * Get this event's name
   *
   * @return this (non empty and non null) name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Set this type's name (should be unique)
   *
   * @param name a non-null and non empty name
   */
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return ("{" + " eventTypeId='" + getEventTypeId() + "'" + ", name='" + getName() + "'" + "}");
  }
}
