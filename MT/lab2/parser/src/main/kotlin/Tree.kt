class Tree(
    private val node: String,
    private vararg val children: Tree
) {
    fun show(): String {
        if (children.isEmpty()) {
            return node
        }

        val result = mutableListOf<String>()
        children.forEach {
            if (it.show() != "") {
                result += it.show()
            }
        }
        return result.joinToString(" ")
    }
}