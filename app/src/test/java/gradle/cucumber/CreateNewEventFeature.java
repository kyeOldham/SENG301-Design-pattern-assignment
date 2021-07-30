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

package gradle.cucumber;

import java.util.Date;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Assertions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uc.seng301.eventapp.accessor.EventAccessor;
import uc.seng301.eventapp.accessor.ParticipantAccessor;
import uc.seng301.eventapp.handler.EventHandler;
import uc.seng301.eventapp.handler.EventHandlerImpl;
import uc.seng301.eventapp.model.Event;
import uc.seng301.eventapp.util.DateUtil;

public class CreateNewEventFeature {

  private SessionFactory sessionFactory;
  private EventAccessor eventAccessor;
  private EventHandler eventHandler;

  private Event firstEvent;
  private Event secondEvent;
  private Long eventId;
  private Date eventDate;
  private Date secondEventDate;
  private String eventType;
  private Double eventCost;

  private String firstEventName;
  private String eventDescription;

  @Before
  public void setup() {
    Configuration configuration = new Configuration();
    configuration.configure();
    sessionFactory = configuration.buildSessionFactory();
    eventAccessor = new EventAccessor(sessionFactory, new ParticipantAccessor(sessionFactory));
    eventHandler = new EventHandlerImpl(eventAccessor);
  }

  //
  // U1 - AC1
  //

  @Given("There is no event with name {string}")
  public void there_is_no_event_with_name(String name) {
    Assertions.assertFalse(eventAccessor.eventExistsWithName(name));
  }

  @When("I add an event with name {string}, description {string}, type {string} and date {string}")
  public void i_add_an_event_with_name_description_type_and_date(String name, String description, String type,
      String date) {
    firstEventName = name;
    eventDescription = description;
    eventDate = DateUtil.getInstance().convertToDate(date);
    eventType = type;
    firstEvent = eventHandler.createEvent(name, description, date, type);
    eventId = eventAccessor.persistEvent(firstEvent);
    Assertions.assertNotNull(firstEvent);
    Assertions.assertNotNull(eventId);
  }

  @Then("The event is created with the correct name, description, type and date")
  public void the_event_is_created_with_the_correct_name_description_type_and_date() {
    Assertions.assertEquals(firstEvent.getName(), firstEventName);
    Assertions.assertEquals(firstEvent.getDescription(), eventDescription);
    Assertions.assertEquals(firstEvent.getDate(), eventDate);
    Assertions.assertEquals(firstEvent.getEventType().getName(), eventType);
  }

  //
  // U1 - AC2
  //

  @When("I add an event with name {string}, description {string}, type {string} date {string} and cost {double}")
  public void i_add_an_event_with_name_description_type_date_and_cost(String name, String description, String type,
      String date, Double cost) {
    firstEventName = name;
    eventDescription = description;
    eventDate = DateUtil.getInstance().convertToDate(date);
    eventType = type;
    eventCost = cost;
    firstEvent = eventHandler.createEvent(name, description, date, type, cost);
    Assertions.assertNotNull(firstEvent);
    eventId = eventAccessor.persistEvent(firstEvent);
    Assertions.assertNotNull(eventId);
    Assertions.assertNotNull(eventAccessor.getEventById(eventId));
  }

  @Then("The event is created with the correct name, description, date, type and cost")
  public void the_event_is_created_with_the_correct_name_description_date_type_and_cost() {
    Assertions.assertEquals(firstEvent.getName(), firstEventName);
    Assertions.assertEquals(firstEvent.getDescription(), eventDescription);
    Assertions.assertEquals(firstEvent.getDate(), eventDate);
    Assertions.assertEquals(firstEvent.getEventType().getName(), eventType);
    Assertions.assertEquals(firstEvent.getCost(), eventCost);
  }

  //
  // U1 - AC3
  //

  @Given("There is an event with name {string} and date {string}")
  public void there_is_an_event_with_name_and_date(String name, String date) {
    firstEvent = eventHandler.createEvent(name, "some description", date, "some type");
    eventAccessor.persistEvent(firstEvent);
    Assertions.assertTrue(eventAccessor.eventExistsWithName(name));
  }

  @When("I create a duplicate event with name {string} and date {string}")
  public void i_create_a_duplicate_event_with_name_and_date(String name, String date) {
    secondEvent = eventHandler.createEvent(name, "some description", date, "some type");
    Assertions.assertNotNull(secondEvent);
  }

  @Then("I expect an exception that disallow me to save it")
  public void i_expect_an_exception_that_disallow_me_to_save_it() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> eventAccessor.persistEvent(secondEvent));
  }

  //
  // U1 - AC4
  //

  @Given("There is no events with name {string} and {string}")
  public void there_is_no_events_with_name_and(String string, String string2) {
    Assertions.assertFalse(eventAccessor.eventExistsWithName(string));
    Assertions.assertFalse(eventAccessor.eventExistsWithName(string2));
  }

  @When("I want to set the first event date to {string} and the second to {string}")
  public void i_want_to_set_the_first_event_date_to_and_the_second_to(String string, String string2) {
    eventDate = DateUtil.getInstance().convertToDate(string);
    secondEventDate = DateUtil.getInstance().convertToDate(string2);
  }

  @Then("I expect an exception that disallow me to create any of those")
  public void i_expect_an_exception_that_disallow_me_to_create_any_of_those() {
    // Write code here that turns the phrase above into concrete actions
    Assertions.assertThrows(IllegalArgumentException.class, () -> eventHandler.createEvent("Event in the past", "some description", eventDate.toString(), "some type"));
    Assertions.assertThrows(IllegalArgumentException.class, () -> eventHandler.createEvent("Event to far in the future", "some description", secondEventDate.toString(), "some type"));
  }


}
