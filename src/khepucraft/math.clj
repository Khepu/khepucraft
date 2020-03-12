(ns khepucraft.math
  (:use
   [uncomplicate.neanderthal core native math]))

(defn ^doubles rotate-x
  [^double theta]
  (let [cos-theta (cos theta)
        sin-theta (sin theta)]
    (->
     [[1   0           0               0]
      [0   cos-theta   (- sin-theta)   0]
      [0   sin-theta   cos-theta       0]
      [0   0           0               1]]
     flatten
     float-array)))

(defn ^doubles rotate-y
  [^double theta]
  (let [cos-theta (cos theta)
        sin-theta (sin theta)]
    (->
     [[cos-theta       0   sin-theta   0]
      [0               1   0           0]
      [(- sin-theta)   0   cos-theta   0]
      [0               0   0           1]]
     flatten
     float-array)))

(defn ^doubles rotate-z
  [^double theta]
  (let [cos-theta (cos theta)
        sin-theta (sin theta)]
    (->
     [[cos-theta   (- sin-theta)   0   0]
      [sin-theta   cos-theta       0   0]
      [0           0               1   0]
      [0           0               0   1]]
     flatten
     float-array)))

(defn determinant [] nil)
