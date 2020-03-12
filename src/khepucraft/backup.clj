(ns khepucraft.backup
  (:import
   [org.lwjgl Version BufferUtils]
   [org.lwjgl.glfw Callbacks GLFW GLFWKeyCallbackI GLFWErrorCallback]
   [org.lwjgl.opengl GL GL46 GL15]
   [org.lwjgl.system MemoryStack MemoryUtil])
    (:use
     [uncomplicate.neanderthal core native])
    (:gen-class))

(defn vertex-buffer
  [vertices]
  (let [length (count vertices)]
    (doto (BufferUtils/createFloatBuffer length)
      (.put (float-array vertices) 0 length)
      (.flip))))



#_(let [vs (compile-shader "./resources/shaders/line.vert" :vertex)
        fs (compile-shader "./resources/shaders/line.frag" :fragment)
        r (range -1 1 0.005)
        data1 (generate-data #(+ (* % %) #_(/ time 100)) r)
        data2 (generate-data (fn [n] (Math/sin (+ (* n 2 Math/PI) (/ time 10)))) r)
        x-axis (generate-data (fn [_] 0) r)
        y-axis (vec (flatten (map (fn [[a b c]] [b a c]) (partition 3 x-axis))))

        sp (link-shaders vs fs)]
    (gl-graph data2 sp {"color" [1 0 0]})
    (gl-graph x-axis sp {"color" [0 0 0]})
    (gl-graph y-axis sp {"color" [0 0 0]})
    #_(gl-graph data2 sp {"color" [0 1 0]}))


(defn gl-graph
  [points shader-program uniforms]
  (let [vbo (GL46/glGenBuffers)
        vao (GL46/glGenVertexArrays)]
    (GL46/glBindVertexArray vao)

    (GL46/glBindBuffer GL46/GL_ARRAY_BUFFER vbo)
    (GL46/glBufferData GL46/GL_ARRAY_BUFFER (float-array points) GL46/GL_STATIC_DRAW)

    (GL46/glVertexAttribPointer 0 3 GL46/GL_FLOAT false 0 0)
    (GL46/glEnableVertexAttribArray 0)

    (GL46/glUseProgram shader-program)

                                        ;Uniforms

    (GL46/glUniform3fv (GL46/glGetUniformLocation shader-program "color") (float-array (uniforms "color")))

    (GL46/glDrawArrays GL46/GL_LINES 0 (int (/ (count points) 3)))
    vbo))



#_(defn uniform-locations
  [shader uniform-keys]
  (mapv #(GL46/glGetUniformLocation shader (name %)) uniform-keys))

#_(defn gl-load
  "Currently this is called in the render loop, this means the vertices are sent to the GPU
  at every loop, should only be sent once"
  [vertices shader uniforms]

  (let [vbo (GL46/glGenBuffers)
        vao (GL46/glGenVertexArrays)]
    (GL46/glBindVertexArray vao)

    (GL46/glBindBuffer GL46/GL_ARRAY_BUFFER vbo)
    (GL46/glBufferData GL46/GL_ARRAY_BUFFER (float-array vertices) GL46/GL_STATIC_DRAW)

    (GL46/glVertexAttribPointer 0 3 GL46/GL_FLOAT false 0 0)
    (GL46/glEnableVertexAttribArray 0)

    (GL46/glUseProgram shader)

                                        ;Uniforms

    (let [[time-loc
           move-loc
           rotx-loc
           roty-loc
           rotz-loc] (uniform-locations shader (keys uniforms))
          {:keys [time move rotx roty rotz]} uniforms]

      (GL46/glUniform1f time-loc time)
      (GL46/glUniform3fv move-loc move)
      (GL46/glUniformMatrix4fv rotx-loc false rotx)
      (GL46/glUniformMatrix4fv roty-loc false roty)
      (GL46/glUniformMatrix4fv rotz-loc false rotz))

    (GL46/glDrawArrays GL46/GL_TRIANGLES 0 (int (/ (count vertices) 3)))
    vbo))
