(ns khepucraft.camera
  (:use
   [khepucraft.math]))

(defn look
  [pos target init-up]
  (let [direction (normalize (mapv - pos target))
        right (normalize (cross init-up direction))
        up (cross direction right)]
    [(float-array
      [(nth right 0)      (nth right 1)      (nth right 2)      0
       (nth up 0)         (nth up 1)         (nth up 2)         0
       (nth direction 0)  (nth direction 1)  (nth direction 2)  0
       0                  0                  0                  1])

     (float-array
      [1  0  0  (- (nth pos 0))
       0  1  0  (- (nth pos 1))
       0  0  1  (- (nth pos 2))
       0  0  0  1])]))

