#
# Created on Wed Apr 07 2021
#
# The Unlicense
# This is free and unencumbered software released into the public domain.
#
# Anyone is free to copy, modify, publish, use, compile, sell, or distribute
# this software, either in source code form or as a compiled binary, for any
# purpose, commercial or non-commercial, and by any means.
#
# In jurisdictions that recognize copyright laws, the author or authors of this
# software dedicate any and all copyright interest in the software to the public
# domain. We make this dedication for the benefit of the public at large and to
# the detriment of our heirs and successors. We intend this dedication to be an
# overt act of relinquishment in perpetuity of all present and future rights to
# this software under copyright law.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
# ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
# WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
#
# For more information, please refer to <https://unlicense.org>
#

Feature: U4 - Notify participant of event status change

#  AC.1 When the status of an event I am attending changes, I receive a notification containing the event name
#  and the new status of this event.
#  AC.2 When being notified of a status change, I print a message containing my name, the event name and the
#  new status of the event.
#  AC.3 When receiving the notification, I can remove myself from the participants list of that event.
#  AC.4 If an event is “archived”, all participants are removed from that event.

  Scenario: AC1 - When the status of an event I am attending changes, I receive a notification containing the event name and the new status of this event.
    Given There is an event with name "SENG301 Asg 3" with a participant named "Bob Jones"
    When I update the status of event "SENG301 Asg 3" to PAST
    Then A notification containing the event name and new status is shown

  Scenario: AC2 - When being notified of a status change, I print a message containing my name, the event name and the new status of the event.
    Given There is an event with name "SENG301 Asg 3" with a participant named "Bob Jones"
    When I update the status of event "SENG301 Asg 3" to PAST
    Then A message containing my name, the event name and new status is shown

#  Scenario: AC3 - When receiving the notification, I can remove myself from the participants list of that event.
#    Given There is an event with name "SENG301 Asg 3" with a participant named "Bob Jones"
#    When I update the status of event "SENG301 Asg 3" to PAST
#    Then I can remove myself from the participants list of that event

  Scenario: AC4 - If an event is “archived”, all participants are removed from that event.
    Given There is an event with name "SENG301 Asg 3" with status PAST, with a participant named "Bob Jones"
    When I update the status of event "SENG301 Asg 3" to ARCHIVED
    Then All participants are removed from that event