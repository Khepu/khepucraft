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
