(ns khepucraft.core
  (:import
   [org.lwjgl.glfw GLFW GLFWErrorCallback Callbacks]
   [org.lwjgl.opengl GL GL46])
  (:use
   [uncomplicate.neanderthal core native]
   [khepucraft shaders shapes math])
  (:gen-class))

(defn process-input
  [window]
  (GLFW/glfwSetWindowShouldClose window
                                 (= GLFW/GLFW_PRESS
                                    (GLFW/glfwGetKey window GLFW/GLFW_KEY_ESCAPE))))

(defn -init
  [width height]
  "Initializes GLFW window"
  (.set (GLFWErrorCallback/createPrint System/err))

  (when-not (GLFW/glfwInit)
    (throw (IllegalStateException. "Unable to initialize GLFW")))

  (GLFW/glfwDefaultWindowHints)
  (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE 0)
  (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE 1)
  (let [window (GLFW/glfwCreateWindow width height "Khepucraft" 0 0)]
    (when (nil? window)
      (throw (RuntimeException. "Failed to create the GLFW window")))

    (GLFW/glfwMakeContextCurrent window)
    (GLFW/glfwSwapInterval 1) ;v-sync
    (GLFW/glfwShowWindow window)
    (GL/createCapabilities)
    (GL46/glViewport 0 0 width height)
    (GL46/glClearColor 0.3 0.3 0.3 1.)
    (print window)
    window))

(defn uniform-locations
  [shader uniform-names]
  (mapv #(GL46/glGetUniformLocation shader %) uniform-names))

(defn gl-load
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

    (let [#_[time-loc
           move-loc
           rotation-loc] #_(uniform-locations shader-program (keys uniforms))
          #_{:keys [t move rotation]} #_uniforms]
      (GL46/glUniform1f (GL46/glGetUniformLocation shader "time") (uniforms "time"))
      (GL46/glUniform3fv (GL46/glGetUniformLocation shader "move") (float-array (uniforms "move")))
      (GL46/glUniformMatrix4fv (GL46/glGetUniformLocation shader "rotationz") false (uniforms "rotationz"))
      (GL46/glUniformMatrix4fv (GL46/glGetUniformLocation shader "rotationx") false (uniforms "rotationx"))
      (GL46/glUniformMatrix4fv (GL46/glGetUniformLocation shader "rotationy") false (uniforms "rotationy")))


    (GL46/glDrawArrays GL46/GL_TRIANGLES 0 (int (/ (count vertices) 3)))
    vbo))

(defn -loop
  [window]
  (loop [time 0]
    (process-input window)
    (GL46/glClear (bit-or GL46/GL_COLOR_BUFFER_BIT
                          GL46/GL_DEPTH_BUFFER_BIT))

                                        ; Rendering commands

                                        ;TODO: shaders should only be compiled once in shaders and linked
    (let [vs (compile-shader "./resources/shaders/triangle.vert" :vertex)
          fs (compile-shader "./resources/shaders/triangle.frag" :fragment)
          t (/ time 100)
          uniforms {"time" time
                    "move" [0 0 0]
                    "rotationx" (-> (rotate-x t)
                                    flatten
                                    float-array)
                    "rotationz" (-> (rotate-z t)
                                   flatten
                                   float-array)
                    "rotationy" (-> (rotate-y t)
                                    flatten
                                    float-array)}]

      #_(print "VS: " vs)
      #_(print "FS: " fs)
      (gl-load (scale 0.5 voxel)
               (link-shaders vs fs)
               uniforms))

                                        ; Check events and swap buffers
    (GLFW/glfwSwapBuffers window)
    (GLFW/glfwPollEvents)

    ;might cause issues since it renders once after i press ESCAPE
    (if (GLFW/glfwWindowShouldClose window)
      window
      (recur (inc time)))))

(defn -destruct
  [window]
  (Callbacks/glfwFreeCallbacks window)
  (GLFW/glfwDestroyWindow window)
  (GLFW/glfwTerminate)
  (.free (GLFW/glfwSetErrorCallback nil)))

(defn -run
  []
  ((comp -destruct -loop) (-init 900 900)))

(defn -main
  "Entry point"
  [& args]
  (-run))

