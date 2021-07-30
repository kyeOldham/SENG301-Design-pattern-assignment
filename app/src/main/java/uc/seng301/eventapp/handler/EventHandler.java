package uc.seng301.eventapp.handler;

import java.util.Date;
import java.util.List;

import uc.seng301.eventapp.model.Event;
import uc.seng301.eventapp.model.EventStatus;
import uc.seng301.eventapp.model.Location;
import uc.seng301.eventapp.model.Participant;

/**
 * This interface abstract the handling of {@link Event} entities.
 */
public interface EventHandler {

  /**
   * Add given list of participants to given event (duplicates will be ignored)
   * 
   * @param event        an event to update (assume participants have been
   *                     resolved)
   * @param participants a list of participants
   * @throw IllegalArgumentException if any of the passed arguments is null
   */
  void addParticipants(Event event, List<Participant> participants);

  /**
   * Create an {@link Event} object with given parameters (event cost will be set
   * to 0).
   *
   * @param name        the event name (not null and non empty)
   * @param description a description (not null and non empty)
   * @param date        a DD/MM/YYYY formatted string (not null and non empty)
   * @param type        an event type (cannot be empty or null)
   * @return the Event object with given parameters
   * @throws IllegalArgumentException if any of the above preconditions for input
   *                                  arguments is violated
   */
  Event createEvent(String name, String description, String date, String type) throws IllegalArgumentException;

  /**
   * Create an {@link Event} object with given parameters
   *
   * @param name        the event name (not null and non empty)
   * @param description a description (not null and non empty)
   * @param date        a DD/MM/YYYY formatted string (not null and non empty)
   * @param type        an event type (cannot be empty or null)
   * @param cost        an event cost (must be between 0 -inclusive-and 999
   *                    -inclusive-)
   * @return the Event object with given parameters
   * @throws IllegalArgumentException if any of the above preconditions for input
   *                                  arguments is violated
   */
  Event createEvent(String name, String description, String date, String type, Double cost)
      throws IllegalArgumentException;

  /**
   * Create an {@link Event} object with given parameters.
   *
   * @param name        the event name (not null and non empty)
   * @param description a description (not null and non empty)
   * @param date        a DD/MM/YYYY formatted string (not null and non empty)
   * @param type        an event type (cannot be empty or null)
   * @param cost        an event cost (must be between 0 -inclusive-and 999
   *                    -inclusive-)
   * @param location    a location for this event (can be null), but if not, its
   *                    name must be filled
   * @return the Event object with given parameters
   * @throws IllegalArgumentException if any of the above preconditions for input
   *                                  arguments is violated
   */
  Event createEvent(String name, String description, String date, String type, Double cost, Location location)
      throws IllegalArgumentException;

  /**
   * Look for and refresh the status of events when the calendar day changes.
   */
  void refreshEvents();

  /**
   * Update the status of given event to a new status
   * 
   * @param event     an event to change the status of
   * @param newStatus the new status to use
   * @param date      new date to be used in case of a rescheduled event only
   *                  (ignored in other cases)
   * @return the updated event to the new status
   * @throws IllegalArgumentException if any of the required fields is null
   * @throws IllegalStateException    if the change of status is not permitted
   */
  Event updateEventStatus(Event event, EventStatus newStatus, Date date)
      throws IllegalArgumentException, IllegalStateException;
}
