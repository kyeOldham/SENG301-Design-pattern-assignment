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
 * This class represents an archived event. Used for record keeping and later
 * reference only.
 */
@Entity
@DiscriminatorValue("ARCHIVED")
public class ArchivedEvent extends Event {

  /**
   * JPA compliant no-args constructor.
   */
  ArchivedEvent() {
    // JPA requirement
  }

  /**
   * Copy constructor.
   * 
   * @param event the event to copy into this new archived event
   */
  ArchivedEvent(Event event) {
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
    throw new IllegalStateException();
  }

  @Override
  public Event happen() {
    throw new IllegalStateException();
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
    return "ARCHIVED " + super.toString();
  }

}
