(ns khepucraft.utils
  (:import
   [org.lwjgl.opengl GL GL46])
  (:require
   [clojure.core.cache.wrapped :as cache])
  (:use
   [khepucraft.shapes])
  (:gen-class))

(def static-draw GL46/GL_STATIC_DRAW)
(def array-buffer GL46/GL_ARRAY_BUFFER)
(def element-array-buffer GL46/GL_ELEMENT_ARRAY_BUFFER)
(def triangles GL46/GL_TRIANGLES)

(defn memoize-vertices
  "Currently not used"
  [f]
  (let [c (cache/fifo-cache-factory {})]
    (fn
      [& args]
      (-> (cache/through-cache c (hash args) (constantly (apply f args)))
          vals
          first))))

(defn load-vertices
  [vertices]
  (let [vbo (GL46/glGenBuffers)
        vao (GL46/glGenVertexArrays)]
                                        ;Bind buffers
    (GL46/glBindVertexArray vao)

    (GL46/glBindBuffer array-buffer vbo)
    (GL46/glBufferData array-buffer (float-array vertices) static-draw)

    (GL46/glVertexAttribPointer 0 3 GL46/GL_FLOAT false 0 0)
    (GL46/glEnableVertexAttribArray 0)

                                        ;Unbind buffers
    #_(GL46/glBindBuffer GL46/GL_ARRAY_BUFFER 0)
    #_(GL46/glBindVertexArray 0)

    {:vao vao
     :vertices (int (/ (count vertices) 3))}))

(defn uniform-location
  [shader uniform-key]
  (GL46/glGetUniformLocation shader (name uniform-key)))

(defn apply-uniforms
  [shader uniforms]
  (let [uni-fs {:f   #(GL46/glUniform1f %1 %2)
                :f3  #(GL46/glUniform3fv %1 %2)
                :mf4 #(GL46/glUniformMatrix4fv %1 false %2)}]
    (run! #((uni-fs (first %))
            (uniform-location shader (second %))
            (last %))
          uniforms)))

(defn draw-triangles
  [buffers shader uniforms]
  (let [{:keys [vao vertices]} buffers]
    (GL46/glBindVertexArray vao)
    (GL46/glUseProgram shader)
    (apply-uniforms shader uniforms)
    (GL46/glDrawArrays triangles 0 vertices)))

(defn load-indexed
  [vertices]
  (let [vao (GL46/glGenBuffers)
        vbo (GL46/glGenBuffers)
        ebo (GL46/glGenBuffers)
        {:keys [vertices indices]} (index-shapes vertices)]
    (GL46/glBindVertexArray vao)

    (GL46/glBindBuffer array-buffer vbo)
    (GL46/glBufferData array-buffer (float-array vertices) static-draw)
    (println "Vertices: " vertices)
    (GL46/glBindBuffer element-array-buffer ebo)
    (GL46/glBufferData element-array-buffer (float-array indices) static-draw)
    (println "Indices: " indices)
    (GL46/glVertexAttribPointer 0 3 GL46/GL_FLOAT false 0 0)
    (GL46/glEnableVertexAttribArray 0)

    {:vao vao
     :indices (count indices)}))

(defn draw-elements
  [buffers shader uniforms]
  (let [{:keys [vao indices]} buffers]
    (GL46/glBindVertexArray vao)
    (GL46/glUseProgram shader)
    (apply-uniforms shader uniforms)
    (GL46/glDrawElements triangles indices GL46/GL_UNSIGNED_BYTE 0)))

