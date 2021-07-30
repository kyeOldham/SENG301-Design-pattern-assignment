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

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uc.seng301.eventapp.accessor.EventAccessor;
import uc.seng301.eventapp.accessor.ParticipantAccessor;
import uc.seng301.eventapp.handler.EventHandler;
import uc.seng301.eventapp.handler.EventHandlerImpl;
import uc.seng301.eventapp.location.LocationService;
import uc.seng301.eventapp.location.LocationServiceResult;
import uc.seng301.eventapp.location.NominatimResult;
import uc.seng301.eventapp.model.Event;
import uc.seng301.eventapp.model.Location;

public class AddLocationFeature {

  private Event event;
  private Long eventId;
  private String location;
  private String latitude;
  private String longitude;
  private SessionFactory sessionFactory;
  private EventAccessor eventAccessor;
  private EventHandler eventHandler;
  private LocationService mockNominatimQuery;

  @Before
  public void setup() {
    Configuration configuration = new Configuration();
    configuration.configure();
    sessionFactory = configuration.buildSessionFactory();
    eventAccessor = new EventAccessor(sessionFactory, new ParticipantAccessor(sessionFactory));
    eventHandler = new EventHandlerImpl(eventAccessor);

    mockNominatimQuery = Mockito.mock(LocationService.class);

    // Note that we use a concrete implementation here to access setters, but the
    // interface object disallow that (i.e. create "externally" immutable object)
    // still, what is supplied (stubbed) is a concrete object, but will be retrieved
    // as an implementation (abstract, immutable) one
    NominatimResult locationResult = new NominatimResult();
    locationResult.setDisplayName("Christchurch, Christchurch City, Canterbury, New Zealand/Aotearoa");
    locationResult.setLatitude("-43.530955");
    locationResult.setLongitude("172.6366455");
    Mockito.when(mockNominatimQuery.getCityFromString("Christchurch")).thenReturn(locationResult);
  }

  //
  // U2 - AC1
  //

  @Given("There is an event with name {string}, description {string}, type {string} and date {string}")
  public void there_is_an_event_with_name_description_type_and_date(String name, String description, String type,
      String date) {

    event = eventHandler.createEvent(name, description, date, type);
    eventId = eventAccessor.persistEvent(event);
    event = eventAccessor.getEventById(eventId);
    Assertions.assertNotNull(event);
  }

  @When("I add a location with name {string}")
  public void i_add_a_location_with_name(String location) {
    this.location = location;
    event.setLocation(new Location(location));
    Assertions.assertEquals(event.getLocation().getName(), location);
  }

  @Then("The event has a location that is persisted when saving the event")
  public void the_event_has_a_location_that_is_persisted_when_saving_the_event() {
    eventAccessor.persistEvent(event);
    Assertions.assertEquals(location, eventAccessor.getEventById(eventId).getLocation().getName());
  }

  //
  // U2 - AC2
  //

  @When("I retrieve the full description and geolocalisation coordinates for location {string} from an external API")
  public void i_retrieve_the_full_description_and_geolocalisation_coordinates_for_location_from_an_external_api(
      String name) {
    // use immutable interface here
    LocationServiceResult result = mockNominatimQuery.getCityFromString(name);
    Assertions.assertNotNull(result);
    location = result.getName();
    latitude = result.getLatitude();
    longitude = result.getLongitude();
  }

  @Then("The retrieved description, latitude and longitude are added to the location")
  public void the_retrieved_description_latitude_and_longitude_are_added_to_the_location() {
    event.setLocation(new Location(location, latitude, longitude));
    Assertions.assertNotNull(event.getLocation());
  }

  @Then("The event is persisted with the updated location")
  public void the_event_is_persisted_with_the_updated_location() {
    eventAccessor.persistEvent(event);
    Event retrievedEvent = eventAccessor.getEventById(eventId);
    Assertions.assertNotNull(retrievedEvent.getLocation());
    Assertions.assertEquals(retrievedEvent.getLocation().getName(), location);
    Assertions.assertEquals(retrievedEvent.getLocation().getLatitude(), latitude);
    Assertions.assertEquals(retrievedEvent.getLocation().getLongitude(), longitude);
  }
}
