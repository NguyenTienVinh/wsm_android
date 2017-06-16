Feature: Login
#     Perform search on keyword and limit number are inputted

  Scenario Outline: Input email and password in wrong format
    Given I have a Login Screen
    When I input email <email>
    And I input password <password>
    Then I should see error on the <view>

    Examples:
      | email         | password | view  |
      | vinh          | vinh     | email |

  Scenario Outline: Input email and password in correct format
    Given I have a Login Screen
    When I input email <email>
    And I input password <password>
    Then I should see error on the <view>

    Examples:
      | email         | password | view  |
      | 123@gmail.com | 123      | email |