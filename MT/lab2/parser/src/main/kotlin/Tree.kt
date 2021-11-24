class Tree(
    private val node: String,
    private vararg val children: Tree
) {
    fun toExpression(): String {
        if (children.isEmpty()) {
            return node
        }

        val result = mutableListOf<String>()
        children.forEach {
            if (it.toExpression() != "") {
                result += it.toExpression()
            }
        }
        return result.joinToString(" ")
    }
}