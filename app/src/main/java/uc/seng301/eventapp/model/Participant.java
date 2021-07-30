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

import uc.seng301.eventapp.util.DateUtil;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * This entity represents a participant to an event. Contains a reference to the
 * list of events this participant participates in.
 */
@Entity
@Table(name = "participant")
public class Participant implements PropertyChangeListener  {

  @Id
  @Column(name = "id_participant")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long participantId;

  private String name;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "participant_attends_event", joinColumns = @JoinColumn(name = "id_participant"), inverseJoinColumns = @JoinColumn(name = "id_event"))
  private List<Event> events;

  /**
   * JPA compliant no-args constructor.
   */
  Participant() {
    // needed by JPA
  }

  /**
   * Convenience constructor. Only this constructor should be used.
   * 
   * @param name this participant's name
   */
  public Participant(String name) {
    this.name = name;
  }

//  @Override
//  public void watch(List<Event> events) {
//    for (Event event : events) {
//      event.attach(this);
//    }
//  }

  /**
   * This is the {@link PropertyChangeListener} method being called when a change
   * event is generated from the Subject (of the Observer pattern)
   *
   * Respond to a change in the state of an abductee (Person) that this UFO is
   * notified of.
   *
   * @param event the (Java) event of the generic {@link PropertyChangeEvent}
   *              class allowing to observe other objects. Must be fired from
   *              other places. Only explicitly bound listeners will be notified.
   *
   */
  @Override
  public void propertyChange(PropertyChangeEvent event) {
    Event even = (Event) event.getSource();
    System.out.println(getName() + ": the event " + even.getName() + " has updated its status to " + event.getNewValue());
  }

  /**
   * Get this participant technical id (JPA managed)
   * 
   * @return a unique participant id
   */
  public Long getParticipantId() {
    return this.participantId;
  }

  /**
   * Set this participant id, should not be initialised manually
   * 
   * @param participantId a unique ID
   */
  public void setParticipantId(Long participantId) {
    this.participantId = participantId;
  }

  /**
   * Get this participant's name
   * 
   * @return a name (shouldn't be null or blank)
   */
  public String getName() {
    return this.name;
  }

  /**
   * Set this participant's name
   * 
   * @param name a participant's name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the list of events this participant is enrolled in
   * 
   * @return a (possibly empty) list of events
   */
  public List<Event> getEvents() {
    return null != this.events ? this.events : Collections.emptyList();
  }

  /**
   * Set the list of events this participant is enrolled in
   * 
   * @param events a list of events
   */
  public void setEvents(List<Event> events) {
    this.events = events;
  }

  @Override
  public String toString() {
    // @formatter:off
    return "{" +
      " participantId='" + getParticipantId() + "'" +
      ", name='" + getName() + "'" +
      ", events='" + getEvents() + "'" +
      "}";
      // @formatter:on
  }

}
