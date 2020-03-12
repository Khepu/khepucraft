(defproject khepucraft "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/core.cache "0.8.2"]

                 [uncomplicate/neanderthal "0.26.1"]

                 [org.lwjgl/lwjgl "3.2.3"]
                 [org.lwjgl/lwjgl "3.2.3" :classifier "natives-windows"]
                 [org.lwjgl/lwjgl-opengl "3.2.3"]
                 [org.lwjgl/lwjgl-opengl "3.2.3" :classifier "natives-windows"]
                 [org.lwjgl/lwjgl-glfw "3.2.3"]
                 [org.lwjgl/lwjgl-glfw "3.2.3" :classifier "natives-windows"]
                 ]
  :main ^:skip-aot khepucraft.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

