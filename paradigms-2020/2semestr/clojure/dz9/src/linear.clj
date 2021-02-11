;Checks
(defn checkSizes? [objs] (every? (fn [obj] (== (count (first objs)) (count obj))) objs))
(defn checkScalar? [objs] (every? number? objs))
(defn checkVectors? [objs] (every? (fn [obj] (and (vector? obj) (every? number? obj))) objs))
(defn checkMatrices? [objs] (every? (fn [obj] (and (checkVectors? obj) (checkSizes? obj))) objs))
;Vectors
(defn vectorOperation [f & vs]
  {:pre [(and (checkVectors? vs) (checkSizes? vs))]
   }
  (apply mapv f vs))
(defn v+ [& vs] (apply (partial vectorOperation +) vs))
(defn v- [& vs] (apply (partial vectorOperation -) vs))
(defn v* [& vs] (apply (partial vectorOperation *) vs))
(defn v*s [v & scs]
  {:pre [(and (checkVectors? [v]) (checkScalar? scs))]
   }
  (mapv (partial * (reduce * scs)) v))
(defn scalar [& vs]
  {:pre [(and (checkVectors? vs) (checkSizes? vs))]
   }
  (apply + (apply mapv * vs)))
(defn det_2x2 [x y v1 v2] (- (* (nth x v1) (nth y v2)) (* (nth y v1) (nth x v2))))
(defn vect [& vs]
  {:pre [(and (checkVectors? vs) (checkSizes? vs))]
   }
  (reduce (fn [x y] (vector (det_2x2 x y 1 2) (det_2x2 x y 2 0) (det_2x2 x y 0 1))) vs))
;Matrix
(defn matrixOperation [f & ms]
  {:pre [(and (checkMatrices? ms) (checkSizes? ms))]
   }
  (apply mapv f ms))
(defn m+ [& ms] (apply (partial matrixOperation v+) ms))
(defn m- [& ms] (apply (partial matrixOperation v-) ms))
(defn m* [& ms] (apply (partial matrixOperation v*) ms))
(defn m*s [m & scs] (mapv (fn [v] (apply v*s v scs)) m))
(defn m*v [m v] (mapv (partial scalar v) m))
(defn transpose [m] (apply mapv vector m))
(defn m*m [& ms]
  {:pre [(checkMatrices? ms)]
   }
  (reduce (fn [m1 m2] (mapv (fn [v] (m*v (transpose m2) v)) m1)) ms))


(defn shapelessOperation [operation data]
  {:pre [(or (every? number? data) (every? vector? data))]}
  (reduce (fn fun [left right] (cond (vector? left) (mapv fun left right) :else (operation left right))) data)
  )

(defn s+ [& scalars] (shapelessOperation  + scalars))
(defn s- [& scalars] (shapelessOperation - scalars))
(defn s* [& scalars] (shapelessOperation  * scalars))