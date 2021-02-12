(defn proto-get [obj key]                                   ;берет значение из прототипа если он там есть
  (cond
    (contains? obj key) (obj key)
    (contains? obj :prototype) (proto-get (obj :prototype) key)
    :else nil))

(defn proto-call [this key & args]                          ;вызывает функцию и исполняет ее для всех переданных аргументов
  (apply (proto-get this key) this args))

(defn field [key]                                           ;чтоб не писать. функция которая делает прото гет на объект
  (fn [this] (proto-get this key)))

(defn method [key]                                          ;чтоб не писать. функция которая делает прото каал на объект
  (fn [this & args] (apply proto-call this key args)))

(def toString (method :toString))                           ;
(def evaluate (method :evaluate))                           ;
(def diff (method :diff))                                   ;
(def operands (field :operands))                            ;определяем функции просто чтоб вызывать *функция* *объект*

(def Constant)
(def ConstantPrototype
  (let [number (field :value)]
    {:toString (fn [this]
                 (let [value (number this)] (format "%.1f" value))) ;вывод без E + степень
     :evaluate (fn [this _]
                 (number this))
     :diff     (fn [_ _] (Constant 0))}))

(defn Constant [number]
  {:prototype ConstantPrototype
   :value     number})

(def ZERO (Constant 0))
(def ONE (Constant 1))
(def TWO (Constant 2))

(def VariablePrototype
  (let [name (field :value)]
    {:toString (fn [this]
                 (name this))
     :evaluate (fn [this id]
                 (id (name this)))
     :diff     (fn [this id]
                 (if (= (name this) id) (Constant 1) ZERO))}))

(defn Variable [identifier]
  {:prototype VariablePrototype
   :value     identifier})

(def diff-of (fn [var this index] (diff ((operands this) index) var))) ;название переменной объект и индекс и мы берем его производную
(def operand-at (fn [this index] ((operands this) index)))  ;обращаемся к операнду по индекусу
(def d-operands (fn [this var] (map (fn [operand] (diff operand var)) (operands this)))) ;для каждого операнда делаем производную

(def Operation
  (let [operands (field :operands)
        symbol (field :lexeme)
        function (field :action)
        howToDiff (method :howToDiff)]
    {:toString (fn [this]
                 (str "(" (symbol this) " "
                      (clojure.string/join " " (mapv toString (operands this))) ;применяем ко всем жлементам то стринг и ьзаписываем через пробел
                      ")"))
     :evaluate (fn [this vars]
                 (apply (function this)
                        (mapv (fn [operand] (evaluate operand vars))
                              (operands this))))
     :diff     (fn [this var]
                 (howToDiff this var))}))

(defn create-operation
  [lexeme action howToDiff]
  (fn [& operands]
    {:prototype {:prototype Operation
                 :lexeme    lexeme
                 :action    action
                 :howToDiff howToDiff}
     :operands  (vec operands)}))

(def Add
  (create-operation '+ + (fn [this var] (apply Add (d-operands this var)))))

(def Subtract
  (create-operation '- - (fn [this var] (apply Subtract (d-operands this var)))))

(def Multiply
  (create-operation '* * (fn [this var] (Add
                                          (Multiply (operand-at this 0) (diff-of var this 1))
                                          (Multiply (operand-at this 1) (diff-of var this 0))))))
(def Divide
  (create-operation '/ (fn [x y] (/ x (double y)))
                    (fn [this var] (Divide
                                     (Subtract
                                       (Multiply (operand-at this 1) (diff-of var this 0))
                                       (Multiply (operand-at this 0) (diff-of var this 1)))
                                     (Multiply (operand-at this 1) ((operands this) 1))))))
(def Negate
  (create-operation 'negate - (fn [this var] (apply Negate (d-operands this var)))
                    ))

(def Square
  (create-operation 'square (fn [x] (* x x))
                    (fn [this var] (Multiply
                                     (Constant 2)
                                     (operand-at this 0)
                                     (diff-of var this 0)))))

(def Lg
  (create-operation 'lg (fn [x y] (/ (Math/log (Math/abs y)) (Math/log (Math/abs x))))
                    (fn [this var] (Divide
                                     (Subtract
                                       (Multiply
                                         (Lg (Constant Math/E) (operand-at this 0))
                                         (Divide (diff-of var this 1) (operand-at this 1)))
                                       (Multiply
                                         (Lg (Constant Math/E) (operand-at this 1))
                                         (Divide (diff-of var this 0) (operand-at this 0))))
                                     (Square (Lg (Constant Math/E) (operand-at this 0)))))))

(def Pw
  (create-operation 'pw (fn [x y] (Math/pow x y))
                    (fn [this var] (Multiply
                                     (Pw (operand-at this 0) (operand-at this 1))
                                     (Add
                                       (Multiply
                                         (Lg (Constant Math/E) (operand-at this 0))
                                         (diff-of var this 1))
                                       (Multiply
                                         (operand-at this 1)
                                         (diff (Lg (Constant Math/E) (operand-at this 0)) var)))))))


(def objectOperations
  {
   '+      Add
   '-      Subtract
   '*      Multiply
   '/      Divide
   'constant Constant
   'negate Negate
   'lg     Lg
   'pw     Pw
   })

(defn parseObjectExpression [expr]
  (cond
    (seq? expr) (apply (objectOperations (first expr)) (mapv parseObjectExpression (rest expr))) ;берем first от expr - это операция. Вызываем рекурсию от хвоста
    (number? expr) (Constant expr)
    :else (Variable (str expr))))

(def parseObject
  (comp parseObjectExpression read-string))                 ;comp - парименяет все функции для аргументов