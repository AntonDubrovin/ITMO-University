(defn operation [f]
  (fn [& operands]
    (fn [args] (apply f ((apply juxt operands) args)))
    )
  )

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(defn divide [a b] (fn [args] (/ (double (a args)) (double (b args)))))
(def negate subtract)


(def min (operation (fn [& x] (reduce (fn [f s] (Math/min f s)) (first x) (rest x)))))
(def max (operation (fn [& x] (reduce (fn [f s] (Math/max f s)) (first x) (rest x)))))

(defn constant [x] (fn [args] x))
(defn variable [name] (fn [args] (get args name)))


(def opers {'+ add '- subtract '* multiply '/ divide
            'negate negate
            'sqrt sqrt 'square square
            'sinh sinh 'cosh cosh
            'sin sin 'cos cos 'min min 'max max})

(defn parseRecur [getOperation, getOperands, curNode]
  (cond
    (number? curNode) (constant curNode)
    (symbol? curNode) (variable (str curNode))
    (list? curNode) (apply (getOperation curNode) (getOperands curNode getOperation getOperands))
    )
  )

(defn parseFunction [str]  ( parseRecur (fn [list] (get opers (first list)))
                                        (fn [list, fop, fargs] (mapv (partial parseRecur fop fargs) (rest list)))
                                        (read-string str)))
