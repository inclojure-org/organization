;; lein new clj_workshop
;; and replace core.clj contents with this snippet:

(ns clj-workshop.core)

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
(let [planet {:name    "Earth"
              :mass    1
              :density 5.5
              :atmosphere
                       {:nitrogen           0.78
                        :oxygen             0.20
                        :carbon-dioxide     0.04
                        :water              0.01}}]
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

;; Let's define a global reference 'planet'
(comment                                                    ;; uncomment + eval to def
  (def planet
    {:name       "Earth"
     :mass       1
     :density    5.5
     :atmosphere {:nitrogen       0.78
                  :oxygen         0.20
                  :carbon-dioxide 0.04
                  :water          0.01}}))

;; Now the previous expression works, because 'planet' is a global reference... indeed, we can now even remove the 'let' bindings and get results
;; BUT beware of 'def' ... explain why

;; IMPORTANT! the def'd planet is different from each let-bound planet
;; All let-bound 'planet' things are different from each other
;; Because, lexical scope.
;; Spend some time on this --- this will take time to absorb.

;; What if we want a planet-property-querying-abstraction?
;; Named functions!
;; Also are data->data transformations

(defn planet-props [planet prop-keys]
  ;; Oh we used this before ... nice
  (select-keys planet prop-keys))

;; Another way
(defn planet-props-variadic [planet & prop-keys]
  (select-keys planet prop-keys))

;; Yet another way, with a query map (no-sql DB anyone?)
(defn planet-props-hash [query-map]
  (select-keys (:planet query-map)
               (:prop-keys query-map)))

;; A more convenient way
(defn planet-props-hash-destructure [{:keys [planet prop-keys]}]
  (select-keys planet prop-keys))

;; What if I want the original planet back?
(defn planet-props-plus-original [{:keys [planet prop-keys]
                                   :as   query-map}]
  ;; we could...
  (assoc {:planet planet}
    :props (select-keys planet prop-keys))

  ;; ;; or we could...
  (comment
    (assoc (dissoc query-map :prop-keys)
      :props (select-keys planet prop-keys)))

  ;; but that's just silly (in this case)
  ;; 'cause we could "literally" return a map
  (comment
    {:planet planet
     :props  (select-keys planet prop-keys)})
  )


;; Explain "destructuring" a bit.
;; (Shameless plug: I think I wrote pretty neat explanation here:
;; https://stackoverflow.com/a/24483685)



;; Hash-maps are just one kind of "Sequence"
;; Hold forth on "sequences", and a few basic operations on sequences
;; get, first, last, rest
;; "Oh, by the way, these are all pure functions too!"
;; Remember kids data->data transformations.

;; NOTE to self:
;; DON'T do map/reduce/filter just yet ... that's later down below,
;; when the problem gets more interesting


;; But before we go any further... Here's our dirty secret...
;; 'defn' is actually 'def' in disguise
(def planet-by-def
  (fn [planet prop-keys]
    (select-keys planet prop-keys)))

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
