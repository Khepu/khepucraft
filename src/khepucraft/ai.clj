(ns khepucraft.ai)

(defn create-perceptron
  [inputs activation]
  (let [p {:weights (vec (take (inc inputs) (repeatedly #(Math/random))))
           :activation activation}]
    (fn [data]
      data)))
