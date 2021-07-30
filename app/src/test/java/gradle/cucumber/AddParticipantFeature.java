package gradle.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.core.util.Assert;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Assertions;
import uc.seng301.eventapp.accessor.EventAccessor;
import uc.seng301.eventapp.accessor.ParticipantAccessor;
import uc.seng301.eventapp.handler.EventHandler;
import uc.seng301.eventapp.handler.EventHandlerImpl;
import uc.seng301.eventapp.model.Event;
import uc.seng301.eventapp.model.Participant;
import uc.seng301.eventapp.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddParticipantFeature {

    private SessionFactory sessionFactory;
    private EventAccessor eventAccessor;
    private ParticipantAccessor participantAccesor;
    private EventHandler eventHandler;

    private Event event;
    private Participant participant;
    private Long eventId;
    private String eventDate;

    @Before
    public void setup() {
        Configuration configuration = new Configuration();
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
        participantAccesor = new ParticipantAccessor(sessionFactory);
        eventAccessor = new EventAccessor(sessionFactory, participantAccesor);
        eventHandler = new EventHandlerImpl(eventAccessor);
        eventDate = "07/09/2021";
    }

    //
    // U1 - AC1
    //

    @Given("There is an event with name {string} and an existing participant named {string}")
    public void there_is_an_event_with_name_and_an_existing_participant_named(String string, String string2) {
        event = eventHandler.createEvent(string, "some description", eventDate, "some type");
        eventId = eventAccessor.persistEvent(event);
        Assertions.assertTrue(eventAccessor.eventExistsWithName(string));

        participant = new Participant(string2);
        participantAccesor.persistParticipant(participant);
        Assertions.assertEquals(participantAccesor.getParticipantByName(string2).getName(), string2);
    }

    @When("I add {string} as a participant")
    public void i_add_as_a_participant(String string) {
        event.addParticipant(participant);
        eventAccessor.persistEvent(event);
    }

    @Then("The participant has been added to the event")
    public void the_event_now_has_a_participant_named() {
        Assertions.assertEquals(eventAccessor.getEventAndParticipantsById(eventId).getParticipants().get(0).getName(), participant.getName());
    }

    //
    // U1 - AC2
    //
//
    @Given("There is an event with name {string} and no participant named {string}")
    public void there_is_an_event_with_name_and_no_participant_named(String string, String string2) {
        event = eventHandler.createEvent(string, "some description", eventDate, "some type");
        eventId = eventAccessor.persistEvent(event);
        Assertions.assertTrue(eventAccessor.eventExistsWithName(string));
        Assertions.assertNull(participantAccesor.getParticipantByName(string2));
    }
//
    @When("I add {string} as a new participant")
    public void i_add_as_a_new_participant(String string) {
        participant = new Participant(string);
        event.addParticipant(participant);
        Long updated = eventAccessor.persistEventAndParticipants(event);
        Assertions.assertNotNull(updated);
    }
//
    @Then("A participant named {string} is created")
    public void a_participant_named_is_created(String string) {
        Assertions.assertEquals(participantAccesor.getParticipantByName(string).getName(), string);
    }

    @And("The brand new participant has been added to the event")
    public void the_event_now_has_a_brand_new_participant_named(){
        Assertions.assertEquals(eventAccessor.getEventAndParticipantsById(eventId).getParticipants().get(0).getName(), participant.getName());
    }

    //
    // U1 - AC3
    //

    @Given("There is an event with name {string}")
    public void there_is_an_event_with_name_and_a_participant_with_name(String string) {
        event = eventHandler.createEvent(string, "some description", eventDate, "some type");
        eventId = eventAccessor.persistEvent(event);
        Assertions.assertTrue(eventAccessor.eventExistsWithName(string));
    }

    @When("I add {string} as a bad participant")
    public void i_add_as_a_bad_particpant(String string) {
        participant = new Participant(string);
        event.addParticipant(participant);
    }

    @Then("I expect an exception that disallows me to add this participant to the event")
    public void i_expect_an_exception_that_disallows_me_to_add_this_participant_to_the_event() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> eventAccessor.persistEventAndParticipants(event));
    }

}
