# Goal

Given natural, physiological, and currently-stocked-kitchen constraints, help the user compose meals for the day that will exactly meet his or her nutritional demands (according to the best data available).

## Installation

Requiremenst:

- [Postgresql 8.4](http://www.postgresql.org/)
- [Leiningen](https://github.com/technomancy/leiningen)

Setup db:

    $> createdb nutrition
    $> createuser --pwprompt test  # make password "test"
    $> lein run -m nutrition.refresh-db  # populate database

## Usage

    $> lein run

## License

Copyright (C) 2011 thanthese productions
