(ns khepucraft.math
  (:use
   [uncomplicate.neanderthal core linalg native math]))

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
    (float-array
     [cos-theta       0   sin-theta   0
      0               1   0           0
      (- sin-theta)   0   cos-theta   0
      0               0   0           1])))

(defn ^doubles rotate-z
  [^double theta]
  (let [cos-theta (cos theta)
        sin-theta (sin theta)]
    (float-array
     [cos-theta   (- sin-theta)   0   0
      sin-theta   cos-theta       0   0
      0           0               1   0
      0           0               0   1])))

(defn plus-over-minus
  [a b]
  (/ (+ a b) (- a b)))

(defn ^floats frustum
  [^Double l ^Double r ^Double b ^Double t ^Double n ^Double f]
  (float-array
   [(/ (* 2 n) (- r l))  0                    (plus-over-minus r l)      0
    0                    (/ (* 2 n) (- t b))  (plus-over-minus t b)      0
    0                    0                    (- (plus-over-minus f n))  (- (/ (* 2 f n) (- f n)))
    0                    0                    -1                         0]))

(defn ^floats perspective
  [^long fov ^double aspect ^double near ^double far]
  (let [top (* near (tan (/ (Math/toRadians fov) 2)))
        bottom (- top)
        right (* top aspect)
        left (- right)]
    (frustum left right bottom top near far)))

(defn ^floats transform
  "Translation and Scaling"
  [^Double x ^Double y ^Double z
   ^Double sx ^Double sy ^Double sz]
  (float-array [sx   0   0  x
                0   sy   0  y
                0    0  sz  z
                0    0   0  1]))

(defn ^floats viewport
  []
  (float-array
   [1  0  0  0
    0  1  0  0
    0  0  1  0
    0  0  0  1]))

(defn cross
  [x y]
  (let [[x0 x1 x2] x
        [y0 y1 y2] y]
    [(- (* x1 y2) (* y1 x2))
     (- (* x2 y0) (* y2 x0))
     (- (* x0 y1) (* y0 x1))]))

(defn norm
  [x]
  (sqrt (reduce + (map #(* % %) x))))

(defn normalize
  [x]
  (let [|x| (norm x)]
    (if (= |x| 0)
      x
      (mapv #(/ % |x|) x))))

