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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import uc.seng301.eventapp.model.Participant;

/**
 * This class handles {@link Participant} entities and offers helpers method to
 * save and retrieve participant entities.
 * 
 * Note that no helper methods are defined to construct participants because of
 * their simplicity.
 * 
 * Refer to {@link Participant#Participant(String)}.
 */
public class ParticipantAccessor {

  private static final Logger LOGGER = LogManager.getLogger(ParticipantAccessor.class);
  private final SessionFactory sessionFactory;

  /**
   * Default constructor.
   *
   * @param sessionFactory the JPA session factory to talk to the persistence
   *                       implementation.
   */
  public ParticipantAccessor(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  /**
   * Retrieve a participant with given name (without their events)
   * 
   * @param name a name to look for (can't be null)
   * @return the participant entity with given name (perfect match) if any, null
   *         otherwise (associated events are not resolved)
   * @throws IllegalArgumentException if given name is null or empty
   */
  public Participant getParticipantByName(String name) {
    if (null == name || name.isBlank()) {
      throw new IllegalArgumentException("name '" + name + "' cannot be null or blank");
    }
    Participant participant = null;
    try (Session session = sessionFactory.openSession()) {
      participant = session.createQuery("FROM Participant WHERE name = '" + name + "'", Participant.class)
          .uniqueResult();
    } catch (HibernateException e) {
      LOGGER.error("unable to get participant by name '{}'", name, e);
    }
    return participant;
  }

  /**
   * Retrieve a participant by its id (without the events the participant is
   * enrolled in)
   *
   * @param participantId an id to look up in the database for
   * @return the participant with given id (without resolving the events), null if
   *         none could be retrieved
   * @throws IllegalArgumentException if given id is null
   */
  public Participant getParticipantById(Long participantId) {
    if (null == participantId) {
      throw new IllegalArgumentException("cannot retrieve participant with null id");
    }
    Participant participant = null;
    try (Session session = sessionFactory.openSession()) {
      LOGGER.info("retrieve the participant with id {}", participantId);
      participant = session.createQuery("FROM Participant WHERE participantId =" + participantId, Participant.class)
          .uniqueResult();
    } catch (HibernateException e) {
      LOGGER.error("unable to get participant with id {}", participantId, e);
    }
    return participant;
  }

  /**
   * Save given participant into the database. If participant with same name
   * exists, the existing participant id will be returned.
   * 
   * If given participant has an id, the existing participant will be updated.
   *
   * @param participant a participant to save or update
   * @return the saved participant id
   * @throws IllegalArgumentException if given participant is null or has a null
   *                                  or blank name
   */
  public Long persistParticipant(Participant participant) throws IllegalArgumentException {
    if (null == participant || null == participant.getName() || participant.getName().isBlank()) {
      throw new IllegalArgumentException("cannot save null or blank participant");
    }

    Participant existingParticipant = getParticipantByName(participant.getName());
    LOGGER.debug(participant);
    if (null != existingParticipant) {
      participant.setParticipantId(existingParticipant.getParticipantId());
      return existingParticipant.getParticipantId();
    }

    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();
      LOGGER.info("save participant with name {}", participant.getName());
      session.save(participant);
      transaction.commit();
    } catch (HibernateException e) {
      LOGGER.error("unable to persist participant with name '{}'", participant.getName(), e);
    }
    return participant.getParticipantId();
  }
}
