(ns game-of-life.render
  (:use game-of-life.core)
  (:import java.awt.image.BufferedImage java.awt.Color java.io.File javax.imageio.ImageIO)
  (:gen-class))

(def test-grid '[[. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . o . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . o . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . o o o . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . o o . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . o o . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . o o . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . o o . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . o o o . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]
                 [. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .]])

(defn set-color!
  "iterates over an image with a function that takes coordinates and returns colors"
  [image color-chooser]
  (doseq [x (range (.getWidth image)) y (range (.getHeight image))]
    (.setRGB image x y (color-chooser x y)))) 

(defn grid->image [grid]
  (let [height (count (first grid)) width (count grid) image (new BufferedImage height width (. BufferedImage TYPE_INT_RGB))]
    (set-color! image (fn [x y] (.getRGB (if (= (get-tile grid [x y]) 'o) (Color/black) (Color/white)))))
    image))

(defn write-file [image filename]
  (ImageIO/write image "png" (new File filename)))

(defn -main [& args]
  (let [timeline (iterate tick test-grid)]
    (doseq [n (range 100)] (write-file (grid->image (nth timeline n)) (str "out/gen" n ".png")))))
