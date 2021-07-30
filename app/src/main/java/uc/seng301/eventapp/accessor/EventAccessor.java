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

package uc.seng301.eventapp.accessor;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import uc.seng301.eventapp.model.Event;
import uc.seng301.eventapp.model.EventStatus;
import uc.seng301.eventapp.model.EventType;
import uc.seng301.eventapp.model.Participant;

/**
 * This class offers helper methods to search for, retrieve or persit
 * {@link Event} and {@EventType} entities
 */
public class EventAccessor {

  private final SessionFactory sessionFactory;
  private final ParticipantAccessor participantAccessor;
  private static final Logger LOGGER = LogManager.getLogger(EventAccessor.class);

  /**
   * Default constructor.
   *
   * @param sessionFactory      the JPA session factory to talk to the persistence
   *                            implementation.
   * @param participantAccessor the accessor class for the participants
   */
  public EventAccessor(SessionFactory sessionFactory, ParticipantAccessor participantAccessor) {
    this.sessionFactory = sessionFactory;
    this.participantAccessor = participantAccessor;
  }

  /**
   * Retrieve an event by its id (without the participants)
   *
   * @param eventId an id to look up in the database for
   * @return the event with given id (without resolving the participants), null if
   *         none could be retrieved
   * @throw IllegalArgumentException if given eventId is null
   */
  public Event getEventById(Long eventId) {
    if (null == eventId) {
      throw new IllegalArgumentException("cannot retrieve event with null id");
    }
    Event event = null;
    try (Session session = sessionFactory.openSession()) {
      LOGGER.info("retrieve the event with id {}", eventId);
      event = session.createQuery("FROM Event WHERE eventId =" + eventId, Event.class).uniqueResult();
    } catch (HibernateException e) {
      LOGGER.error("unable to retrieve event with id:{}", eventId, e);
    }
    return event;
  }

  /**
   * Retrieve an event by its id (resolving the participants)
   *
   * @param eventId an id to look up in the database for
   * @return the event with given id (resolving the links to participants too),
   *         null if none could be retrieved
   * @throw IllegalArgumentException if given eventId is null
   */
  public Event getEventAndParticipantsById(Long eventId) {
    if (null == eventId) {
      throw new IllegalArgumentException("cannot retrieve event with null id");
    }
    Event event = null;
    try (Session session = sessionFactory.openSession()) {
      LOGGER.info("retrieve the event with id {} (with its participants)", eventId);
      event = session.createQuery("FROM Event WHERE eventId =" + eventId, Event.class).uniqueResult();
      if (null != event) {
        Hibernate.initialize(event.getParticipants());
      }
    } catch (HibernateException e) {
      LOGGER.error("unable to retrieve event with id:{}", eventId, e);
    }
    return event;
  }

  /**
   * Retrieve or create an event type with given name.
   *
   * @param name a name (cannot be null)
   * @return either the existing event with given name (exact match), a new type
   *         with given name (if not existing) or null if an error occurred
   * @throws IllegalArgumentException if given name is null or blank
   */
  public EventType getEventTypeFromName(String name) throws HibernateException {
    if (null == name || name.isBlank()) {
      throw new IllegalArgumentException("cannot retrieve event type with null or blank name");
    }
    EventType result = null;
    try (Session session = sessionFactory.openSession()) {
      LOGGER.info("retrieve the event type with name '{}'", name);
      result = session.createQuery("FROM EventType WHERE name = '" + name + "'", EventType.class).uniqueResult();
      if (null == result) {
        Transaction transaction = session.beginTransaction();
        result = new EventType(name);
        Long eventTypeId = (Long) session.save(result);
        LOGGER.info("new event type saved with id:{}", eventTypeId);
        transaction.commit();
        // we need to set the eventTypeId in the object we send back to tell
        // hibernate that it does not need to care about the event type when
        // saving an event that has been propertly saved here
        result.setEventTypeId(eventTypeId);
      }
    } catch (HibernateException e) {
      LOGGER.error("unable to store / retrieve event type with name '{}'", name, e);
    }
    return result;
  }

  /**
   * Retrieve all events with given status. If null status passed, all events are
   * sent back
   * 
   * @param status a status to filter all events on
   * @return the list of events with given status (or all if null status passed)
   */
  public List<Event> getAllEventsWithStatus(EventStatus status) {
    List<Event> results = null;
    try (Session session = sessionFactory.openSession()) {
      LOGGER.info("retrieve all events with status '{}'", null != status ? status.name() : "no status");
      // we use a native SQL query because we cannot discriminate on entity property
      // since the Event's polymorphic @Discriminator is a database column only (not a
      // JPA property)
      results = session.createNativeQuery(
              "select * from Event" + (null != status ? " WHERE event_status = '" + status.name() + "'" : ""), Event.class)
              .list();

    } catch (HibernateException e) {
      LOGGER.error("unable to retrieve all events with status: {}", null != status ? status.name() : "no status", e);
    }
    return null != results ? results : Collections.emptyList();
  }

  /**
   * Checks wether an {@link Event} with given name exists in the persistence
   * layer (perfect match only).
   *
   * @param name an event name to look for
   * @return true if there exists a perfect match with given name
   */
  public boolean eventExistsWithName(String name) {
    boolean result = false;
    try (Session session = sessionFactory.openSession()) {
      LOGGER.info("retrieve the event with name '{}'", name);
      Event retrievedEvent = session.createQuery("FROM Event WHERE name = '" + name + "'", Event.class).uniqueResult();
      result = null != retrievedEvent;
    } catch (HibernateException e) {
      LOGGER.error("unable to check if event with name '{}' exists", name, e);
    }
    return result;
  }

  /**
   * Save given event to the database. No validity checks are performed on the
   * event (rely on proper factory methods).
   * 
   * If given event has an id, it will be updated, otherwise a duplicate name
   * check will be performed.
   *
   * @param event an Event to save (assumed to be semanticall correct and not
   *              null)
   * @return the ID of the persisted Event.
   * @throws IllegalArgumentException if given Event's name already exists in the
   *                                  database and event has no id
   */
  public Long persistEvent(Event event) {
    if (null == event.getEventId() && eventExistsWithName(event.getName())) {
      throw new IllegalArgumentException("event with name " + event.getName() + " already exists");
    }
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();
      LOGGER.info("persist event with name '{}'", event.getName());
      session.saveOrUpdate(event);
      transaction.commit();
    } catch (HibernateException e) {
      LOGGER.error("unable to persist event '{}'.", event.getName(), e);
    }
    return event.getEventId();
  }

  /**
   * Save given event and attached participants to the database. Unresolved
   * participants will be persisted too. No validity checks are performed on
   * either the event or the participant at this level (rely on proper factory
   * methods)
   * 
   * If given event has an id, it will be updated, otherwise a duplicate name
   * check will be performed.
   *
   * @param event an Event to save (assumed to be semanticall correct and not
   *              null)
   * @return the ID of the persisted Event. Linked {@link Participant} are saved
   *         as a side effect.
   * @throws IllegalArgumentException if given Event's name already exists in the
   *                                  database
   */
  public Long persistEventAndParticipants(Event event) {
    if (null == event.getEventId() && eventExistsWithName(event.getName())) {
      throw new IllegalArgumentException("event with name " + event.getName() + " already exists");
    }

    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();
      // if we have participants, we save them too
      event.getParticipants().stream().filter(p -> null == p.getParticipantId())
          .forEach(participantAccessor::persistParticipant);

      LOGGER.info("persist event with name '{}'", event.getName());
      session.saveOrUpdate(event);
      transaction.commit();
    } catch (HibernateException e) {
      LOGGER.error("unable to persist event '{}' with participants", event.getName(), e);
    }
    return event.getEventId();
  }

  public Long persistEventAndParticipants(Event event, EventStatus status) {
    if (null == event.getEventId() && eventExistsWithName(event.getName())) {
      throw new IllegalArgumentException("event with name " + event.getName() + " already exists");
    }

    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();
      // if we have participants, we save them too
      event.getParticipants().stream().filter(p -> null == p.getParticipantId())
              .forEach(participantAccessor::persistParticipant);

      LOGGER.info("persist event with name '{}'", event.getName());
      session.saveOrUpdate(event);
      session.createNativeQuery("update event set event_status = '" + status + "' where id_event = " + event.getEventId()).executeUpdate();
      transaction.commit();
    } catch (HibernateException e) {
      LOGGER.error("unable to persist event '{}' with participants", event.getName(), e);
    }
    return event.getEventId();
  }
}
