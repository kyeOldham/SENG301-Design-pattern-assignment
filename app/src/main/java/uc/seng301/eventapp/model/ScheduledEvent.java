/*
 * Created on Thu Apr 08 2021
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

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * This class represents a scheduled event that will happen in the future. Can
 * be canceled or happen.
 */
@Entity
@DiscriminatorValue("SCHEDULED")
public class ScheduledEvent extends Event {

  /**
   * JPA compliant no-args constructor.
   */
  ScheduledEvent() {
    // JPA requirement
  }

  /**
   * Default constructor, passing all fields (some may be null). Noe checks are
   * performed here, so values are assumed valid.
   * 
   * @param name        the event name (assumed not null / blank)
   * @param description the event description (assumed not null / blank)
   * @param date        the event date (assumed in the future)
   * @param type        the event type (assumed not null)
   * @param cost        the event cost (assumed positive)
   * @param location    the event location object (can be null)
   */
  public ScheduledEvent(String name, String description, Date date, EventType type, double cost, Location location) {
    setName(name);
    setDescription(description);
    setDate(date);
    setEventType(type);
    setCost(cost);
    setLocation(location);
  }

  /**
   * Copy constructor.
   * 
   * @param event the event to copy into this new scheduled event
   */
  ScheduledEvent(Event event) {
    setEventId(event.getEventId());
    setName(event.getName());
    setCost(event.getCost());
    setDescription(event.getDescription());
    // don't need to do a defensive copy
    setDate(event.getDate());
    setEventType(event.getEventType());
    setParticipants(event.getParticipants());
    setLocation(event.getLocation());
  }

  @Override
  public Event cancel() {
    notifyParticipants(EventStatus.CANCELED);
    return new CanceledEvent(this);
  }

  @Override
  public Event happen() {
    notifyParticipants(EventStatus.PAST);
    return new PastEvent(this);
  }

  @Override
  public Event reschedule(Date date) {
    throw new IllegalStateException();
  }

  @Override
  public Event archive() {
    throw new IllegalStateException();
  }

  @Override
  public String toString() {
    return "SCHEDULED " + super.toString();
  }

}
