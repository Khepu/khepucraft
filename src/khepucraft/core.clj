(ns khepucraft.core
  (:import
   [org.lwjgl.glfw GLFW GLFWErrorCallback Callbacks]
   [org.lwjgl.opengl GL GL46])
  (:use
   [uncomplicate.neanderthal core native]
   [khepucraft shaders shapes math utils])
  (:gen-class))

(defn process-input
  [window]
  (GLFW/glfwSetWindowShouldClose window
                                 (= GLFW/GLFW_PRESS
                                    (GLFW/glfwGetKey window GLFW/GLFW_KEY_ESCAPE))))

(defn -init
  "Initializes GLFW window"
  [width height]
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
    (GL46/glEnable GL46/GL_DEPTH_TEST)
    (GL46/glEnable GL46/GL_CULL_FACE)
    (println window)
    window))

(defn -loop
  [window]
  (let [vs (compile-shader "./resources/shaders/triangle.vert" :vertex)
        fs (compile-shader "./resources/shaders/triangle.frag" :fragment)
        shader (link-shaders vs fs)
        tri (load-indexed voxel)]
    (print (GL46/glGetError))
    (loop [time 0]
      (process-input window)
      (GL46/glClear (bit-or GL46/GL_COLOR_BUFFER_BIT
                            GL46/GL_DEPTH_BUFFER_BIT))

      ;;Rendering commands
      (let [t (/ time 100)
            uniforms `[(:f :time ~time)
                       (:f3 :move ~(float-array [0 0 0]))
                       (:mf4 :rotx ~(rotate-x t))
                       (:mf4 :roty ~(rotate-y t))
                       (:mf4 :rotz ~(rotate-z t))
                       (:mf4 :perspective ~(perspective 90 1. 0.05 100.))
                       (:mf4 :view ~(viewport))
                       (:mf4 :transform ~(transform 0 0 -4 0.5 0.5 0.5))]]
        (draw-elements tri shader uniforms))

      ;;Check events and swap buffers
    (GLFW/glfwSwapBuffers window)
    (GLFW/glfwPollEvents)

    ;;might cause issues since it renders once after i press ESCAPE
    (if (GLFW/glfwWindowShouldClose window)
      window
      (recur (inc time))))))

(defn -destruct
  [window]
  (Callbacks/glfwFreeCallbacks window)
  (GLFW/glfwDestroyWindow window)
  (GLFW/glfwTerminate)
  (.free (GLFW/glfwSetErrorCallback nil))
  window)

(defn -run
  []
  ((comp -destruct -loop) (-init 900 900)))

(defn -main
  "Entry point"
  [& args]
  (-run))

