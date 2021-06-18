(defn evaluate [expression varsValue] ((.evaluate expression) varsValue))
(defn toStringSuffix [expression] (.toStringSuffix expression))
(defn toStringInfix [expression] (.toStringInfix expression))

(definterface IOperation
  (evaluate [])
  (toStringSuffix [])
  (toStringInfix []))

(deftype ConstantOperation [constValue]
  IOperation
  (evaluate [this] (fn [_] constValue))
  (toStringSuffix [this] (format "%.1f" (double constValue)))
  (toStringInfix [this] (format "%.1f" (double constValue))))

(defn Constant [constValue] (ConstantOperation. constValue))

(deftype VariableOperation [varName]
  IOperation
  (evaluate [this] (fn [varsValue] (varsValue varName)))
  (toStringSuffix [this] varName)
  (toStringInfix [this] varName))

(defn Variable [varName] (VariableOperation. varName))

(deftype CommonOperation [comparator stringOperation expressions]
  IOperation
  (evaluate [this] (fn [varsValue] (apply comparator (map (fn [expression] (evaluate expression varsValue)) expressions))))
  (toStringSuffix [this] (str "(" (clojure.string/join " " (map (fn [expression] (toStringSuffix expression)) expressions)) " " stringOperation ")"))
  (toStringInfix [this]
    (if  (= (count expressions) 1) (str stringOperation "(" (toStringInfix (nth expressions 0)) ")") (reduce #(if (= %1 "") (toStringInfix %2) (str "(" %1 " " stringOperation " " (toStringInfix %2) ")")) "" expressions) )))

(defn Add [& expressions] (CommonOperation.
                            +
                            "+"
                            expressions))

(defn Subtract [& expressions] (CommonOperation.
                                 -
                                 "-"
                                 expressions))

(defn Multiply [& expressions] (CommonOperation.
                                 *
                                 "*"
                                 expressions))


(defn Divide [& expressions] (CommonOperation.
                               (fn [a b] (/ (double a) (double b)))
                               "/"
                               expressions))

(defn Negate [expression] (CommonOperation.
                            -
                            "negate"
                            (list expression)))

(def getOperation {
                   '+ Add '- Subtract '* Multiply '/ Divide 'negate Negate
                   })

(defn parse [expression]
  (cond
    (seq? expression) (apply (getOperation (first expression)) (map parse (rest expression)))

    (number? expression) (Constant expression)

    :else (Variable (str expression))
    ))

;-----------------------------------

(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)

(defn _empty [value] (partial -return value))

(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c)) (-return c cs))))

(defn _map [f]
  (fn [result]
    (if (-valid? result)
      (-return (f (-value result)) (-tail result)))))

(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        ((_map (partial f (-value ar)))
         ((force b) (-tail ar)))))))

(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))

(defn _parser [p]
  (fn [input]
    (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))

(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (_map f) parser))
(def +parser _parser)
(def +ignore (partial +map (constantly 'ignore)))

(defn iconj [coll value]
  (if (= value 'ignore) coll (conj coll value)))
(defn +seq [& ps]
  (reduce (partial _combine iconj) (_empty []) ps))
(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))

(defn +or [p & ps]
  (reduce (partial _either) p ps))
(defn +opt [p]
  (+or p (_empty nil)))
(defn +star [p]
  (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
(defn +plus [p] (+seqf cons p (+star p)))
(defn +str [p] (+map (partial apply str) p))

;-----------------------------------

(defn *word [word] (apply +seq (reduce #(conj %1 (+char (str %2))) [] word)))

(def digit-symbols "0123456789")
(def operation-symbols "+-*/")
(def special-symbols #{\u0000})
(def registred-symbols (into (set (str digit-symbols operation-symbols)) special-symbols))

(def *variables (+char "xyz"))

(def *negate (+map (partial apply str) (*word "negate")))

(def *digit (+char digit-symbols))
(def *number (+map read-string (+str (+seqf #(cons %1 (into (vec %2) (vec %3))) (+opt (+char "-+")) (+plus *digit) (+opt (+seqf #(cons %1 %2) (+char ".") (+plus *digit)))))))
(def *space (+char " \t\n\r"))
(def *spaces (+plus *space))
(def *symbol (+char-not registred-symbols))
(def *symbols (+plus *symbol))
(def *whitespaces (+ignore (+star *space)))
(def *operation (+char operation-symbols))

(declare *suffix)

(defn *seqSuffix [begin p end]
  (+seqn 1 (+char begin) *whitespaces (+opt (+seqf #(into (vec (cons %1 %2)) (vector %3)) p (+star (+seqn 0 *whitespaces p)) *whitespaces (+or *operation *negate) )) *whitespaces (+char end)))
(defn *innerSuffix [begin p end] (+map read-string (+map #(str "(" % ")") (+map (partial clojure.string/join " ") (+map #(cons (last %) (drop-last %)) (*seqSuffix begin p end))))))
(def *suffix (+or (*innerSuffix "(" (delay *suffix) ")") *number *variables))

(defn parseObjectSuffix [expression] (parse ((+parser (+seqn 0 *whitespaces *suffix *whitespaces)) expression)))

(defn *seqInfix [p op] (+seqf cons *whitespaces p (+plus (+seq *whitespaces op *whitespaces p)) *whitespaces))
(defn *innerInfix [p op] (+map read-string (+map (fn [x] (reduce #(if (= "" %1) (str %2) (str "(" (nth %2 0) " " %1 " " (nth %2 1) ")")) "" x)) (*seqInfix p op))))

(defn *seqBracketInfix [p op] (+seqn 1 (+char "(") (+seqf cons *whitespaces p (+plus (+seq *whitespaces op *whitespaces p)) *whitespaces) (+char ")")))
(defn *innerBracketInfix [p op] (+map read-string (+map (fn [x] (reduce #(if (= "" %1) (str %2) (str "(" (nth %2 0) " " %1 " " (nth %2 1) ")")) "" x)) (*seqBracketInfix p op))))

(declare *plus_minus)
(declare *plus_minus_brackets)
(declare *multiply_divide)
(declare *multiply_divide_brackets)
(declare *negate_)
(declare *negate_brackets)

(defn *brackets_or_not [p] (+or (+seqn 1 (+char "(") *whitespaces p *whitespaces (+char ")")) p))

(def *plus_minus_rules (+or (delay *multiply_divide) (delay *multiply_divide_brackets) (delay *plus_minus_brackets) (delay *negate_) (delay *negate_brackets) *number *variables))
(def *multiply_divide_rules (+or (delay *plus_minus_brackets) (delay *multiply_divide_brackets) (delay *negate_) (delay *negate_brackets) *number *variables))
(def *negate_rules (+or (delay (*brackets_or_not *plus_minus_brackets)) (delay (*brackets_or_not *multiply_divide_brackets)) (delay *negate_) (delay (*brackets_or_not *negate_brackets)) (*brackets_or_not *number) (*brackets_or_not *variables)))

(def *plus_minus (*innerInfix *plus_minus_rules (+char "+-")))
(def *plus_minus_brackets (*innerBracketInfix *plus_minus_rules (+char "+-")))
(def *multiply_divide (*innerInfix *multiply_divide_rules (+char "*/")))
(def *multiply_divide_brackets (*innerBracketInfix *multiply_divide_rules (+char "*/")))

(def *negate_  (+map read-string (+map #(str "(" % ")") (+map #(clojure.string/join " " %) (+seq *whitespaces *negate *whitespaces *negate_rules *whitespaces)))))
(def *negate_brackets (+map read-string (+map #(str "(" % ")") (+map #(clojure.string/join " " %) (+seqn 1 *whitespaces (+char "(") *whitespaces (+seq *negate *whitespaces *negate_rules) *whitespaces (+char ")") *whitespaces)))))

(def *infix (+or *plus_minus *multiply_divide *negate_ *plus_minus_brackets *multiply_divide_brackets *negate_brackets  *number *variables))

(defn parseObjectInfix [expression] (parse ((+parser (+seqn 0 *whitespaces *infix *whitespaces)) expression)))