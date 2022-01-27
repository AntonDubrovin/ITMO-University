class LL {
    fun checkLL(first: Map<String, Set<String>>, follow: Map<String, Set<String>>): Boolean {
        follow.forEach { (name, follows) ->
            if (first[name]!!.any {
                    follows.contains(it)
                }) {
                return false
            }
        }
        return true
    }
}