(ns khepucraft.shapes
  (:use
   [khepucraft math]))

;;This triangle is officialy named: Trimester Anglesworth
;;By a friend, Elawn
(def triangle  [-1 -1  0
                 1 -1  0
                 0  1  0])

(def right-triangle [ 1  1  0
                     -1  1  0
                      1 -1  0])

;;This voxel is officialy named: Voxelopoulos Papamichael
;;By a friend, bilkon
(def voxel [[-1.0 -1.0  1.0
               1.0 -1.0  1.0
               1.0  1.0  1.0
              -1.0  1.0  1.0
              -1.0 -1.0 -1.0
               1.0 -1.0 -1.0
               1.0  1.0 -1.0
              -1.0  1.0 -1.0]
             [0 1 2
		          2 3 0
		          ;; right
		          1 5 6
		          6 2 1
		          ;; back
		          7 6 5
		          5 4 7
		          ;; left
		          4 0 3
		          3 7 4
		          ;; bottom
		          4 5 1
		          1 0 4
		          ;; top
		          3 2 6
		          6 7 3]])

(def voxelb[ 1  1 -1
            -1  1 -1
             1 -1 -1
            -1 -1 -1
             1 -1 -1
            -1  1 -1
             1  1  1
            -1  1  1
             1 -1  1
            -1 -1  1
             1 -1  1
            -1  1  1
            -1  1 -1
            -1  1  1
            -1 -1  1
            -1 -1  1
            -1  1 -1
            -1 -1 -1
             1  1  1
             1  1 -1
             1 -1  1
             1  1 -1
             1 -1  1
             1 -1 -1
            -1  1  1
             1  1  1
             1  1 -1
             1  1 -1
            -1  1  1
            -1  1 -1
             1 -1  1
            -1 -1  1
             1 -1 -1
            -1 -1  1
             1 -1 -1
            -1 -1 -1])

(defn non-linear-scale
  [f shape]
  (mapv f shape))

(defn scale
  [n shape]
  (non-linear-scale #(* n %) shape))

(defn index-shape
  [vertices]
  (let [grouped-vertices (partition 3 vertices)
        unique-vertices (vec (apply hash-set grouped-vertices))
        indexed-vertices (zipmap unique-vertices (range (count unique-vertices)))
        vertex-map (reduce #(assoc %1 (first %2) (second %2)) indexed-vertices {})]
    ;;[vertices indices]
    [(flatten unique-vertices)
     (map indexed-vertices grouped-vertices)]))

(defn combine-shapes
  [& shapes]
  (apply concat shapes))
                                        ; Composite shapes

;;This square is officialy named: Cubulous McQuad
;;By a friend, Elawn
(def square (index-shape (combine-shapes right-triangle (scale -1 right-triangle))))

