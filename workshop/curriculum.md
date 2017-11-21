# Workshop Curriculum

### Clojure Basics
* Writing simple expressions; prefix notation; introduce the idea of forms/s-expressions
* primitives: numbers, keywords, strings, booleans, nil
* Collection literals: lists, vectors, maps, sets
* Name things using def
* The idea of 'var's - and the idea of interning, namespaces and bindings
 - And why 'def' is to be used with care, and at the top level (conventionally)
* Define basic functions with defn
* conditionals with if; truthiness and falsiness
* bindings with let
* collections: vectors, maps, sets. Introduce the ideas of immutability and persistent data structures
* destructuring with let

### Working with functions
* Defining and calling functions
* multi-arity functions
* varargs
* destructuring
* pre/post conditions
* operating on functions: partial, comp, juxt
* anonymous functions and closures
* higher-order functions: map, filter, reduce

### Project Management
* namespaces and organising clojure code
* introduce leiningen
* setting up a project with lein new
* adding a dependency using lein, and requiring it in a namespace

### Sample project
* Given a Twitter handle, find out if the user has ever tweeted about Clojure
* Basically, search for "clojure" in all their tweets
