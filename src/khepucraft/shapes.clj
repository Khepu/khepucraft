(ns khepucraft.shapes)

(def triangle  [-1 -1  0
                 1 -1  0.
                 0. 1  0.])

(def right-triangle [ 1  1  0
                     -1  1  0
                      1 -1  0])

(defn linear-scale
  [scale shape]
  (non-linear-scale #(* scale %) shape))

(defn non-linear-scale
  [f shape]
  (mapv f shape))

(def square (concat right-triangle (mapv #(* -1 %) right-triangle)))

(defn index-shapes
  [& triangles]
  (let [vertices (partition 3 (reduce concat triangles))
        unique-vertices (apply hash-set vertices)
        indexes (split-triangles (range (count unique-vertices)) (count triangles))]
    {:vertices unique-vertices
     :indexes indexes}))

(defn split-triangles
  "can also be implemented with reduce and cycle but provides different vertex order"
  [indexes n]
  (loop [triangles []
         indexes indexes
         i n]
    (if (zero? i)
      triangles
      (recur (reduce #(conj %1 %2) triangles (take 3 indexes)) (drop 1 indexes) (dec i)))))

