# Goal

Given natural, physiological, and currently-stocked-kitchen constraints, help the user compose meals for the day that will exactly meet his or her nutritional demands (according to the best data available).

## Installation

Requirements:

- [Postgresql 8.4](http://www.postgresql.org/)
- [Leiningen](https://github.com/technomancy/leiningen)

Setup db:

    $> createdb nutrition
    $> createuser --pwprompt test  # make password "test"
    $> lein run -m nutrition.refresh-db  # to populate database

## Usage

To run:

    $> lein run

To run tests:

    $> lein test

## License

Copyright (C) 2011 thanthese productions
