# SENG301 Assignment 3 (2021)

## Context

This material is part of the SENG301 assignment 3. It contains a series of Java
classes meant to set the framework for students to write acceptance tests,
analyse the presence of design patterns and weave new patterns to fulfil new
features. Please take some to familiarise yourself with the code and its
structure. It is an augmented version of the code base used during the first
term.

This assignment is meant to create an event management system that allows to:

- create events
- retrieve locations from an external API
- add participants to events
- notify participants when the status of events changes

A partial domain model is included below

![Event App domain models](diagrams/eventapp.png)

## Authors

Initial contribution by SENG301 teaching team.

## Content

```
|_ app
  |_src
    |_ main: main source folder
      |_ java: project code to be analysed and extended
      |_ resources: application resource files (e.g., hibernate, logger)
    |_ test: test folder
      |_ java: project tests (cucumber scenario implementation)
      |_ resources: test-specific configuration and Cucumber features
  |_ build.gradle: project dependencies and (build) tasks
|_ ANSWERS.md: the file that will contain your answers to this assignment
|_ gradlew: gradle wrapper (unix)
|_ gradlew.bat: gradle wrapper (windows)
|_ LICENSE.txt: this project license file (i.e. The Unlicense)
|_ README.md: this file
|_ settings.gradle: top project gradle configuration

```

See attached handout for more details on the code.

## Run the project

This project relies on gradle (version 6.8 or later). See `build.gradle` file for
full list of dependencies. You can use the built-in scripts to bootstrap the
project (`gradlew` on Linux/Mac or `gradlew.bat` on Windows).

To build the project, place yourself at the root folder of this project, then,
in a command line:

- On Windows: type `gradlew.bat build`
- On Linux/Mac: type `./gradlew build`

To run the Common Line Interface application, from the root folder:

- On Windows: type `gradlew.bat --console=PLAIN run`
- On Linux/Mac: type `./gradlew --console=PLAIN run`

The option `--console=PLAIN` is passed to suppress part of the coloured output
of gradle that may interfere with the CLI. More details about gradle, see
[Gradle Website](https://gradle.org/).

## Copyright notice

Copyright (c) 2021. University of Canterbury

See LICENSE.txt file for more details.
