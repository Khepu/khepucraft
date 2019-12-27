(ns khepucraft.shapes
  (:gen-class))

                                        ; This triangle is officialy named: Trimester Anglesworth
                                        ; By a friend, Elawn
(def triangle  [-1 -1  0
                 1 -1  0
                 0  1  0])

(def right-triangle [ 1  1  0
                     -1  1  0
                      1 -1  0])

(defn non-linear-scale
  [f shape]
  (mapv f shape))

(defn scale
  [n shape]
  (non-linear-scale #(* n %) shape))

(defn split-triangles
  [indexes]
  (vec (flatten (partition 3 1 indexes))))

(defn index-shapes
  "TODO: Use neanderthal for efficiency"
  ([triangle]
   {:vertices triangle
    :indexes [0 1 2]})
  ([triangle & triangles]
   (let [triangles (conj triangles triangle)
         vertices (partition 3 (reduce concat triangles))
         unique-vertices (vec (apply hash-set vertices))
         indexes (split-triangles (range (count unique-vertices)))]
     {:vertices (vec (flatten unique-vertices))
      :indexes indexes})))

(def rotation-2d
  [angle]
  (let [cos (Math/cos angle)
        sin (Math/sin angle)]
    [cos ]))

;; Composite shapes

(def square (index-shapes right-triangle (scale -1 right-triangle)))

