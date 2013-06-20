(ns game-of-life.core)

;; utility functions
;; -----------------
(defn valid? 
  "determine if a space is valid"
  [maze [x y]]
  (let [width  (count (first maze))
        height (count maze)]
    (and (>= x 0) (>= y 0) (< x width) (< y height))))

(defn neighbors
  "return the coordinates of all adjacent tiles"
  [maze [x y]]
  (let [delta-spaces [[-1 1] [0 1] [1 1] [-1 0] [1 0] [-1 -1] [0 -1] [1 -1]]
        surroundings (map #(map + [x y] %) delta-spaces)]
    (filter (partial valid? maze) surroundings)))

(defn get-tile 
  "return the value of a square on a maze"
  [maze [x y]]
  (nth (nth maze y) x))

(defn coords
  "return all the possible coordinates with a certain width and height"
  [[width height]]
  (mapcat (fn [x] (map (fn [y] [x y]) (range height))) (range width)))

(defn replace-tile
  "replace a value on a grid with another and return the new grid"
  [grid [x y] value]
  (assoc grid y (assoc (nth grid y) x value)))

;; game of life functions
;; ---------------------
(defn alive? [grid [x y]]
  (let [current (= (get-tile grid [x y]) 'o)
        buddies (count (filter #(= % 'o) (map (partial get-tile grid) (neighbors grid [x y]))))]
    (cond
     (> 2 buddies) false
     (< 3 buddies) false
     (= 3 buddies) true
     true current)))

(defn bool->tile [cell]
  (if cell 'o '.))

(defn tick
  "return the next iteration of the grid"
  [grid]
  (loop [new-grid grid
         coords-left (coords [(count (first grid)) (count grid)])]
    (if (empty? coords-left) new-grid
        (recur (replace-tile new-grid (first coords-left) (bool->tile (alive? grid (first coords-left)))) (rest coords-left)))))
