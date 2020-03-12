(ns khepucraft.shapes
  (:use
   [khepucraft math]))

                                        ; This triangle is officialy named: Trimester Anglesworth
                                        ; By a friend, Elawn
(def triangle  [-1 -1  0
                 1 -1  0
                 0  1  0])

(def right-triangle [ 1  1  0
                     -1  1  0
                      1 -1  0])

                                        ;This voxel is officialy named: Voxelopoulos Papamichael
                                        ; By a friend, bilkon
(def voxel [ 1  1  0
            -1  1  0
             1 -1  0
            -1 -1  0
             1 -1  0
            -1  1  0
             1  1  1
            -1  1  1
             1 -1  1
            -1 -1  1
             1 -1  1
            -1  1  1
            -1 1 0
            -1 1 1
            -1 -1 1
            -1 -1 1
            -1 1 0
            -1 -1 0
            1 1 1
            1 1 0
            1 -1 1
            1 1 0
            1 -1 1
            1 -1 0
            -1 1 1
            1 1 1
            1 1 0
            1 1 0
            -1 1 1
            -1 1 0
            1 -1 1
            -1 -1 1
            1 -1 0
            -1 -1 1
            1 -1 0
            -1 -1 0])

(defn non-linear-scale
  [f shape]
  (mapv f shape))

(defn scale
  [n shape]
  (non-linear-scale #(* n %) shape))

(defn split-triangles
  [indexes]
  (vec (flatten (partition 3 1 indexes))))

(defn split-vertices
  [vertices]
  (partition 3 vertices))

(defn index-shapes
  [vertices]
  (let [vertices (split-vertices vertices)
        unique-vertices (vec (apply hash-set vertices))
        indexes (split-triangles (range (count unique-vertices)))]

    {:vertices (flatten unique-vertices)
     :indices indexes}))

;; Composite shapes

                                        ;This square is officialy named: Cubulous McQuad
                                        ; By a friend, Elawn
(def square (index-shapes (reduce conj right-triangle (scale -1 right-triangle))))

