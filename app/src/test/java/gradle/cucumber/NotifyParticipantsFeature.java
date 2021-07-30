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
import uc.seng301.eventapp.model.EventStatus;
import uc.seng301.eventapp.model.Participant;
import uc.seng301.eventapp.util.DateUtil;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotifyParticipantsFeature {

    private SessionFactory sessionFactory;
    private EventAccessor eventAccessor;
    private ParticipantAccessor participantAccesor;
    private EventHandler eventHandler;

    private Event event;
    private Participant participant;
    private Long eventId;
    private String eventDate;

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();


    @Before
    public void setup() {
        Configuration configuration = new Configuration();
        configuration.configure();
        System.setOut(new PrintStream(outputStreamCaptor));
        sessionFactory = configuration.buildSessionFactory();
        participantAccesor = new ParticipantAccessor(sessionFactory);
        eventAccessor = new EventAccessor(sessionFactory, participantAccesor);
        eventHandler = new EventHandlerImpl(eventAccessor);
        eventDate = "07/09/2021";
    }

    @Given("There is an event with name {string} with a participant named {string}")
    public void there_is_an_event_with_name_with_a_participant_named(String string, String string2) {
        event = eventHandler.createEvent(string, "some description", eventDate, "some type");
        participant = new Participant(string2);
        event.addParticipant(participant);
        eventId = eventAccessor.persistEventAndParticipants(event);
        Assertions.assertTrue(eventAccessor.eventExistsWithName(string));
        Assertions.assertEquals(participantAccesor.getParticipantByName(string2).getName(), string2);
    }

    @When("I update the status of event {string} to PAST")
    public void i_update_the_status_of_event_to_past(String string) {
        event = eventHandler.updateEventStatus(event, EventStatus.PAST, null);
        Assertions.assertNotNull(event);
    }

    @Then("A notification containing the event name and new status is shown")
    public void a_notification_containing_the_event_name_and_new_status_is_shown() {
        String lines[] = outputStreamCaptor.toString().split("\\r?\\n");
        Assertions.assertEquals("SENG301 Asg 3 updates its status to PAST", lines[0]);
    }



    @Then("A message containing my name, the event name and new status is shown")
    public void a_message_containing_my_name_the_event_name_and_new_status_is_shown() {
        String lines[] = outputStreamCaptor.toString().split("\\r?\\n");
        Assertions.assertEquals("Bob Jones: the event SENG301 Asg 3 has updated its status to PAST", lines[1]);
    }

//    @Then("I can remove myself from the participants list of that event")
//    public void i_can_remove_myself_from_the_participants_list_of_that_event() {
//
//    }

    @Given("There is an event with name {string} with status PAST, with a participant named {string}")
    public void there_is_an_event_with_name_with_status_past_with_a_participant_named(String string, String string2) {
        event = eventHandler.createEvent(string, "some description", eventDate, "some type");
        participant = new Participant(string2);
        event.addParticipant(participant);
        event = eventHandler.updateEventStatus(event, EventStatus.PAST, null);
        eventId = eventAccessor.persistEventAndParticipants(event);
        Assertions.assertTrue(eventAccessor.eventExistsWithName(string));
        Assertions.assertEquals(participantAccesor.getParticipantByName(string2).getName(), string2);
    }

    @When("I update the status of event {string} to ARCHIVED")
    public void i_update_the_status_of_event_to_archived(String string) {
        event = eventHandler.updateEventStatus(event, EventStatus.ARCHIVED, null);
        eventAccessor.persistEventAndParticipants(event);
        Assertions.assertNotNull(event);
    }

    @Then("All participants are removed from that event")
    public void all_participants_are_removed_from_that_event() {
        Assertions.assertEquals(eventAccessor.getEventAndParticipantsById(eventId).getParticipants().size(), 0);
    }

}
