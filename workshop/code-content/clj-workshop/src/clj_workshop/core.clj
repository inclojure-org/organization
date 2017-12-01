;; lein new clj_workshop
;; and replace core.clj contents with this snippet:

(ns clj-workshop.core
  (:use clojure.repl))

;; We are searching for Earth-like planets
;; cf. https://en.wikipedia.org/wiki/Earth-like_planet
;;
;; Earth-like planet is used as a synonym for any of the following:
;; - Any planet that has about the same mass as Earth. Such planets may or may not have atmospheres.
;; - Earth analog, denoting another world that is very similar to Earth
;;   (a planet or moon with environmental conditions similar to those found on Earth.)
;;   cf.https://en.wikipedia.org/wiki/Earth_analog
;; - Terrestrial planet, denoting a planet that is composed of the same materials as Earth, i.e., primarily of silicate rocks or metals
;;   (a planet that is composed primarily of silicate rocks or metals)
;;   cf.https://en.wikipedia.org/wiki/Terrestrial_planet


;; We are also searching for planets in the Habitable Zone
;; (More  likely to have a rocky composition)
;; - radius less than 1.6 earth radii
;; - mass less than 6 earth masses
;; - orbit in the conservative habitable zone (some criteria)

;; Let's start small

;; Well, if Earth is a reference,
;; let's model the Earth's planetary properties.
;; Let's start small here too... and make whatever
;; assumptions we need, because we don't even know how to really
;; compute this yet.

;; "name" "Earth"
;; "mass" 1
;; "density" 5.5
;; "atmosphere" {"nitrogen" 0.78
;;               "oxygen" 0.2095
;;               "carbon-dioxide" 0.04
;;               "water" 0.01}

;; Ah, looks like a JSON-like thing, made of key-value pairs

;; So, let's make it look like one...
{"name"       "Earth"
 "mass"       1
 "density"    5.5
 "atmosphere" {"nitrogen"       0.78
               "oxygen"         0.20
               "carbon-dioxide" 0.04
               "water"          0.01}}


;; Now, how to access its values?
(let [planet {"name"       "Earth"
              "mass"       1
              "density"    5.5
              "atmosphere" {"nitrogen"       0.78
                            "oxygen"         0.20
                            "carbon-dioxide" 0.04
                            "water"          0.01}}]
  (get planet "name")
  ;; (get planet "mass")
  ;; (get planet "density")
  )
;; notice: return value is the result of evaluating the last thing


;; We can query this thing...
;; but this 'get' business is getting old real quick
;; can we do better?
(let [planet {:name       "Earth"
              :mass       1
              :density    5.5
              :atmosphere {:nitrogen       0.78
                           :oxygen         0.20
                           :carbon-dioxide 0.04
                           :water          0.01}}]

  ;; What's the :name of planet?
  (:name planet)
  (:mass planet)

  ;; What's the planet's :density?
  (planet :density))


;; Ok, fine, but now I want a subset of the planet's properties...
(let [planet {:name       "Earth"
              :mass       1
              :density    5.5
              :atmosphere {:nitrogen       0.78
                           :oxygen         0.20
                           :carbon-dioxide 0.04
                           :water          0.01}}]
  (select-keys planet [:name :mass :density]))

;; select-keys takes a map and a sequence of keys,
;; and returns a map containing corresponding k-v pairs
;; this is an example of a "sequence operation" on a clojure hash-map
;; it is a _pure_, data->data transformation

;; Let's be happy with this for now, more sequence operations later...


;; Why does this fail?!!!
;; (select-keys planet [:name :mass :density])


;; Hold forth on the let form, and introduce lexical scope
;; (see how far we got without def-ing things?)

;; Let's define a global reference 'planet', and perhaps another planet that we can work with.

(def planet
  {:name       "Earth"
   :mass       1
   :density    5.5
   :atmosphere {:nitrogen       0.78
                :oxygen         0.20
                :carbon-dioxide 0.04
                :water          0.01}})

(def some-other-planet
  {:name       "Mercury"
   :mass       0.2
   :density    7.8
   :atmosphere {:nitrogen       0.52
                :oxygen         0.01
                :carbon-dioxide 0.15
                :water          0}})

;; Now the previous expression works, because 'planet' is a global reference... indeed, we can now even remove the 'let' bindings and get results
;; BUT beware of 'def' ... explain why

;; IMPORTANT! the def'd planet is different from each let-bound planet
;; All let-bound 'planet' things are different from each other
;; Because, lexical scope.
;; Spend some time on this --- this will take time to absorb.


;; We want to find out if a planet has plenty of oxygen.
;; How do we get the amount of oxygen a planet has?

;; Evaluate these in the REPL, and build the function up gradually:

(:atmosphere planet)
;; This gives us the atmosphere. We want to pull :oxygen out of it.
(:oxygen (:atmosphere planet))
;; Perfect! A planet is oxygen-rich if it has more than 0.15 oxygen.
(> (:oxygen (:atmosphere planet))
   0.15)
;; We can extract this into a function

(defn oxygen-rich?
  [planet]
  (> 0.15 (:oxygen (:atmosphere planet))))

(doc get-in)

;; Better version, using get-in
(defn oxygen-rich?
  [planet]
  (> (get-in planet [:atmosphere :oxygen])
     0.15))


;; But before we go any further... Here's our dirty secret...
;; 'defn' is actually 'def' in disguise
(def oxygen-rich?
  (fn [planet]
    (> (get-in planet [:atmosphere :oxygen])
       0.15)))

;; Explain anonymous functions

;; EXERCISE: Write a function to find the volume of a planet.
;; The volume is mass divided by density.

;; Solution:
(defn volume [planet]
  (/ (:mass planet)
     (:density planet)))

;; Destructuring is a more elegant way to pull data out of a Clojure data structure.
;; The volume function can also be written like this:

(defn volume [{:keys [mass density]}]
  (/ mass density))

;; Hash-maps are just one kind of "Sequence"
;; Hold forth on "sequences", and a few basic operations on sequences
;; first, last, rest

;; Another kind of sequence is a vector.
;; A simple ordered, indexed collection of values.
;; Thus:
(def my-vector [1 42 "baz" :quux])

;; Since vectors are sequences, all the typical sequence operations work on them.
(first my-vector)
(last my-vector)
(rest my-vector)

;; We can add an element to the end of a vector using conj
(conj my-vector "Jabberwocky")
;; ...but we never mutate the original vector!
my-vector

;; We can have vectors of anything...including planets!

(def planets
  [{:name       "Earth"
    :mass       1
    :density    5.5
    :atmosphere {:nitrogen       0.78
                 :oxygen         0.20
                 :carbon-dioxide 0.04
                 :water          0.01}}
   {:name       "Mercury"
    :mass       0.2
    :density    7.8
    :atmosphere {:nitrogen       0.52
                 :oxygen         0.01
                 :carbon-dioxide 0.15
                 :water          0}}])

(nth planets 0)

;; Which of these planets are oxygen-rich?
;; We already have a function to tell us if a planet is oxygen-rich.
;; How can we apply it to many planets?

(doc filter)

(filter oxygen-rich? planets)

;; Explain "destructuring" a bit.
;; (Shameless plug: I think I wrote pretty neat explanation here:
;; https://stackoverflow.com/a/24483685)

;; NOTE to self:
;; DON'T do map/reduce/filter just yet ... that's later down below,
;; when the problem gets more interesting

;; Hold forth upon vars now, till it's clear to people

;; Oh, also explain anonymous functions

;; GOTCHA: Compilation is single-pass!
;; (but that's quite a nice thing in practice, because,
;; "linear" code is easier to reason about and debug)

;; Moving on ....

;; What if we have many planets?
;; In a JSON file?
;; How to slurp?


;; Use file I/O to demo basic Java interop, FTW.


;; Eh, a sequence of many planets!?

;; How to sequence operations-ing, plz?


;; How to query one property of many planets?
;; How to query many properties of many planets?
;; How to select a planet with
;; - mass conforming to "habitable zone" mass?
;; - radius conforming to habitable zone radius?
;; - both?
;; - at least one?
;; How to select many such planets?

;; Same for each "earth-like" property?
;; - atmosphere?
;; - metals?
;; - one more criteria?

;; How to compose these queries?


;; HIGHER ORDER FUNCTIONS
;; What if I need a general way to filter planets by an arbitrary
;; collection of properties?

;; (defn filter-planets [planets & property-pred-fns] ...)


;; How to spit back files with various results?


;; NOTE: the code in-between 'slurp' and 'spit', is _all_ pure!
;; keep stateful behaviour to the "boundary"

;; etc.
;; etc.
;; etc...

;; SUMMARISE what we learned:
;; - Sequence literals (maps, lists, vectors etc...)
;; - Model domain with data
;; - Use fns to transform data->data
;; - Use higher-order fns to compose complex behaviours
;; - Understand how vars work
;; - Learn to work with single-pass files
;; - Java interop is easy!
;; - etc etc etc...
