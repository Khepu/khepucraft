(ns khepucraft.shapes
  (:gen-class))

(def triangle  [-1 -1  0
                 1 -1  0
                 0  1  0])

(def right-triangle [ 1  1  0
                     -1  1  0
                      1 -1  0])

(defn non-linear-scale
  [f shape]
  (mapv f shape))

(defn linear-scale
  [scale shape]
  (non-linear-scale #(* scale %) shape))

(defn split-triangles
  [indexes]
  (vec (flatten (map #(take 3 (drop % indexes)) (range (- (count indexes) 2))))))

(defn index-shapes
  "Use neanderthal for efficiency"
  ([triangle]
   {:vertices triangle
    :indexes [0 1 2]})
  ([triangle & triangles]
   (let [triangles (conj triangles triangle)
         vertices (partition 3 (reduce concat triangles))
        unique-vertices (vec (apply hash-set vertices))
        indexes (split-triangles2 (range (count unique-vertices)))]
    {:vertices (vec (flatten unique-vertices))
     :indexes indexes})))

;; Composite shapes

(def square (index-shapes right-triangle (linear-scale -1 right-triangle)))

