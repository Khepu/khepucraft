(ns khepucraft.shaders
  (:import
   [org.lwjgl.opengl GL GL46]))

(def shader-types {:vertex GL46/GL_VERTEX_SHADER
                   :fragment GL46/GL_FRAGMENT_SHADER})

(defn compile-shader
  [file type]
  (let [shader-code (slurp file)
        shader (GL46/glCreateShader (type shader-types))]
    (GL46/glShaderSource shader shader-code)
    (GL46/glCompileShader shader)
    (if (GL46/glGetShaderi shader GL46/GL_COMPILE_STATUS)
      shader
                                        ;might want to log the error sometime
      nil)))

(defn link-shaders
  "Can use more error checking https://learnopengl.com/Getting-started/Hello-Triangle"
  [& shaders]
  (let [shader-program (GL46/glCreateProgram)
        attach-shader (fn [s] (GL46/glAttachShader shader-program s) s)
        unload-shader (fn [s] (GL46/glDeleteShader s) s)]
    (run! (comp unload-shader attach-shader) shaders)
    (GL46/glLinkProgram shader-program)
    #_(run! unload-shader shaders)
    #_(print (GL46/glGetProgrami shader-program GL46/GL_LINK_STATUS))
    #_(if (not (zero? (GL46/glGetProgrami shader-program GL46/GL_LINK_STATUS)))
      shader-program
      nil)
    shader-program))

