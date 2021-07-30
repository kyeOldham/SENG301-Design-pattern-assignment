#
# Created on Wed Apr 09 2021
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

Feature: U3 - Add participant to event

#  AC.1 I can add one existing participant to an event by its name (non-empty).
#  AC.2 I can add one participant that does not exist yet. This participant will be created as a side eect with
#  given non-empty name.
#  AC.3 I cannot add empty participants, or participants with names containing invalid characters (e.g., numbers).

  Scenario: AC1 - I can add one existing participant to an event by its name (non-empty).
    Given There is an event with name "SENG301 Asg 3" and an existing participant named "Bob Jones"
    When I add "Bob Jones" as a participant
    Then The participant has been added to the event

  Scenario: AC2 - I can add one participant that does not exist yet. This participant will be created as a side effect with given non-empty name.
    Given There is an event with name "SENG301 Asg 3" and no participant named "Lisa Smith"
    When I add "Lisa Smith" as a new participant
    Then A participant named "Lisa Smith" is created
    And The brand new participant has been added to the event

  Scenario: AC3 - I cannot add empty participants, or participants with names containing invalid characters (e.g., numbers).
    Given There is an event with name "SENG301 Asg 3"
    When I add "" as a bad participant
    Then I expect an exception that disallows me to add this participant to the event

#  Scenario: AC3 - I cannot add participants with names containing numbers.
#    Given There is an event with name "SENG301 Asg 3"
#    When I add "1234" as a bad participant
#    Then I expect an exception that disallows me to add this participant to the event
#
#  Scenario: AC3 - I cannot add participants with names containing invalid characters
#    Given There is an event with name "SENG301 Asg 3"
#    When I add "%%%^%" as a bad participant
#    Then I expect an exception that disallows me to add this participant to the event




