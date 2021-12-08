(ns io.github.marcusdunn 
    "aoc 2021 day 8 solution in clojure"
    (:require [clojure.string :as str])
    (:require [clojure.set :as set]))

(defn words [] (map #(str/split %1 #" ") (str/split-lines (slurp "input.txt"))))

(defn drop-until-pipe [words] (drop 1 (drop-while #(not (= "|" %1)) words)))

(defn after-pipe [] (map drop-until-pipe (words)))

(defn after-pipe-len [] (map #(map count %1) (after-pipe)))

(defn relevant-ones [] (map #(filter #{7,4,2,3} %) (after-pipe-len)))

(defn part-one [] (reduce + (map count (io.github.marcusdunn/relevant-ones))))

(defn words-of-len [size words] (filter #(= size (count %)) words))

(defn find-one [words] (first (words-of-len 2 words)))

(defn find-seven [words] (first (words-of-len 3 words)))

(defn find-four [words] (first (words-of-len 4 words)))

(defn find-a [words] (first (seq (set/difference (set (find-seven words)) (set (find-one words))))))

(defn before-pipe [words] (take-while #(not (= "|" %1)) words))

(defn occurences [words] (core/frequencies (reduce str (before-pipe words))))

(defn find-c [words] (first (seq (disj 
    (set (map key (filter #(= 8 (val %1)) (occurences words))))
    (find-a words)
    ))))

(defn find-by-occurences [num words] (map key 
    (filter #(= num (val %1))
    (occurences words))
    ))

(defn find-b [words] (first (find-by-occurences 6 words)))

(defn find-e [words] (first (find-by-occurences 4 words)))

(defn find-f [words] (first (find-by-occurences 9 words)))

(defn find-d [words] (first (seq (set/difference 
    (set (find-four words)) 
    #{(find-b words) (find-c words) (find-f words)})
)))

(defn find-g [words] (first (seq (disj 
    (set (find-by-occurences 7 words)) 
    (find-d words)
    ))))

(defn create-map [words] {
    (find-a words) \a 
    (find-b words) \b 
    (find-c words) \c 
    (find-d words) \d 
    (find-e words) \e 
    (find-f words) \f 
    (find-g words) \g 
    })

(defn translate-letters [words word] (map (create-map words) (set word)))

(def number-map {
    #{\a \b \c \e \f \g} 0,
    #{\c \f} 1,
    #{\a \c \d \e \g} 2,
    #{\a \c \d \f \g} 3,
    #{\b \c \d \f} 4,
    #{\a \b \d \f \g} 5,
    #{\a \b \d \e \f \g} 6,
    #{\a \c \f} 7,
    #{\a \b \c \d \e \f \g} 8,
    #{\a \b \c \d \f \g} 9,
})

(defn translate-to-number [words word] (number-map (set (translate-letters words word))))

(defn to-numbers [words] (map #(translate-to-number words %1) (drop-until-pipe words)))

(defn to-number [words] (Integer/parseInt (apply str (to-numbers words))))

(defn part2 [] (reduce + (map to-number (words))))