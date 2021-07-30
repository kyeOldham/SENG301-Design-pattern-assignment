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

Feature: U1 - Create a new event

  Scenario: AC1 - Create an event with a name, description, type and a date and no cost
    Given There is no event with name "SENG301 Asg 3"
    When I add an event with name "SENG301 Asg 3", description "Let's learn some patterns", type "assignment" and date "07/08/2021"
    Then The event is created with the correct name, description, type and date

  Scenario: AC2 - Create an event with a name, description, type, a date and a cost
    Given There is no event with name "SENG301 Asg 3 (bis)"
    When I add an event with name "SENG301 Asg 3 (bis)", description "Let's learn some more patterns", type "assignment" date "07/08/2021" and cost 0.0
    Then The event is created with the correct name, description, date, type and cost

  Scenario: AC3 - Two events cannot have the same name
    Given There is an event with name "SENG301 Asg 3" and date "07/08/2021"
    When I create a duplicate event with name "SENG301 Asg 3" and date "07/08/2021"
    Then I expect an exception that disallow me to save it

  Scenario: AC 4 - The event date must be in the future, but no later than a year
    Given There is no events with name "Event in the past" and "Event to far in the future"
    When I want to set the first event date to "07/08/2022" and the second to "01/01/2000"
    Then I expect an exception that disallow me to create any of those
