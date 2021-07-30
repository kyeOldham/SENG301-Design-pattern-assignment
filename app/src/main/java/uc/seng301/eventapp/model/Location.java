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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * This entity represents the location linked to an event. This entity only
 * exists linked to a single event (one-to-one). It will be deleted when an
 * event is deleted.
 *
 * In other words, the livecycle of this entity is linked to its parent event
 */
@Entity
@Table(name = "location")
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_location")
  private Long locationId;

  private String name;

  private String latitude;
  private String longitude;

  @OneToOne(mappedBy = "location")
  private Event event;

  /**
   * JPA compliant no-args constructor
   */
  Location() {
    // JPA requirement
  }

  /***
   * Convenience constructor
   *
   * @param name the location name
   */
  public Location(String name) {
    this(name, null, null);
  }

  /**
   * Convenience constructor
   *
   * @param name      the location name
   * @param latitude  its latitude
   * @param longitude its longitude
   */
  public Location(String name, String latitude, String longitude) {
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  /**
   * Get this location unique ID
   *
   * @return a unique id (null if this object is not yet persisted)
   */
  public Long getLocationId() {
    return this.locationId;
  }

  /**
   * Set this location ID (managed by JPA)
   *
   * @param locationId a unique ID
   */
  public void setLocationId(Long locationId) {
    this.locationId = locationId;
  }

  /**
   * Get this location name
   *
   * @return a name (shouldn't be empty or null)
   */
  public String getName() {
    return this.name;
  }

  /**
   * Set this location name
   *
   * @param name either a single name or a full description
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get this location's' latitude
   *
   * @return the latitude, can be null
   */
  public String getLatitude() {
    return this.latitude;
  }

  /**
   * Set this location's latitude
   *
   * @param latitude a latitude (value assumed valid)
   */
  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  /**
   * Get this location's longitude
   *
   * @return the latitude, can be null
   */
  public String getLongitude() {
    return this.longitude;
  }

  /**
   * Set this location's longitude
   *
   * @param longitude a longitude (value assumed valid)
   */
  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  /**
   * Get the event associated to this location (can't be null)
   *
   * @return an Event object
   */
  public Event getEvent() {
    return this.event;
  }

  /**
   * Set the event associated to this location
   *
   * @param event an event object (shouldn't be null)
   */
  public void setEvent(Event event) {
    this.event = event;
  }

  @Override
  public String toString() {
    // @formatter:off
    return "{" +
      " locationId='" + getLocationId() + "'" +
      ", name='" + getName() + "'" +
      ", latitude='" + getLatitude() + "'" +
      ", longitude='" + getLongitude() + "'" +
      "}";
      // @formatter:on
  }
}
