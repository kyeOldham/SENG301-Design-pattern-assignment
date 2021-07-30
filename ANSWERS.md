# SENG301 Assignment 1 (2021) - Student answers

Kye Oldham

## Task 1.b - Write acceptance tests for U3 - Add participants to events

### Feature file (Cucumber Scenarios)

**participant.feature**

### Java class implementing the acceptance tests

**AddParticipantFeature.java**

## Task 2 - Identify the patterns in the code

### Pattern 1

#### What pattern is it?

**State Pattern**

#### What is its goal in the code?

**The goal of the state pattern is to encapsulate differing behaviour for an 
Event object based on its status or 'state'. This is more efficient than relying on conditional statements. In this case,
the state interface, Event, declares several state-specific methods that can handle a status change
for an event. The context, EventHandler, and
the concrete states can perform state transitions by replacing the EventHandlers Event object.**

#### Name of UML Class diagram attached:

**StatePatternUML.png**

#### Mapping to GoF pattern elements:

| GoF element | Code element |
| ----------- | ------------ |
|   Context   |     EventHandler    |
|   request() |     updateEventStatus()    |
|   State |     Event    |
|   ConcreteStateA   |   ScheduledEvent      |
|   ConcreteStateB   |   CanceledEvent |
|   ConcreteStateC   |   PastEvent |
|   ConcreteStateD   |   ArchivedEvent |
|   handle()   |   happen(), archive(), cancel(), reschedule() |


#### What pattern is it?

**Factory pattern**

#### What is its goal in the code?

**The factory pattern for this codebase is the EventHandler, which is an
interface for creating new Event objects, and the EventHandlerImpl which
alters the type of ScheduledEvent created based on a set of constraints.**

#### Name of UML Class diagram attached:

**FactoryPatternUML.png**

#### Mapping to GoF pattern elements:

| GoF element | Code element |
| ----------- | ------------ |
| Creator    |   EventHandler  |
|factoryMethod()| createEvent()|
|   ConcreteCreator   |   EventHandlerImpl      |
|   Product   |   Event |
|   ConcreteProduct   |   ScheduledEvent |
|ConcreteCreator -- ConcreteProduct Relationship| EventHandlerImpl -- ScheduledEvent|

[comment]: <> (|doSomething&#40;&#41;| refreshEvents&#40;&#41;|)

## Task 3 - Full UML Class diagram

### Name of file of full UML Class diagram attached

**FullUML.jpeg**

## Task 4 - Implement new feature

### What pattern fulfils the need for the feature?

**Observer Pattern**

### What is its goal and why is it needed here?

**The goal of the observer pattern is to notify 'observers' when
the status of a 'subject' changes. This needed because so participants
can be notified of a change in event status. Event participants act
as the observers. They are notified of a change in status for events
they are attending/participating in. They are then presented with the
opportunity to withdraw from this event**

### Name of UML Class diagram attached:

**ObserverPatternUML.png**

### Mapping to GoF pattern elements:

| GoF element | Code element |
| ----------- | ------------ |
|   Observer    |   Participant  |
| Update() | propertyChange()  |
|Concrete Observer | Participant |
|Subject| Event |
| attach(Observer)|attach(Participant)|
|detach(Observer)|detach(Participant)|
|notify()|notifyParticipants(EventStatus)|
|Concrete Subject| PastEvent, ScheduledEvent, CanceledEvent, ArchivedEvent|
|doSomething()|happen(), archive(), cancel(), reschedule()|
|Subject -- Observer relationship| Event -- Participant|
## Task 5 - BONUS - Acceptance tests for Task 4

### Feature file (Cucumber Scenarios)

**notify.feature**

### Java class implementing the acceptance tests

**NotifyParticipantsFeature.java**
