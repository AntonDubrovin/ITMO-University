package calculator
import java.io.IOException
import java.io.Reader
typealias Token = Int
const val UNKNOWN_CHAR = -2
open class Lexer(_reader: Reader) {
    private val groupsToTokens: MutableMap<Int, Token> = LinkedHashMap()
    private val tokenStream: Iterator<TokenMatch>

    var token: Token = TOKENS.EOF
        private set

    var tokenValue: String? = null
        private set

    private var position: Int = -1
        private set

    init {
        val text = _reader.readText()
        var grp = 0

        tokenStream = patterns
            .map { (t, r) ->
                groupsToTokens[grp++] = t
                "($r)"
            }.joinToString("|")
            .toRegex().findAll(text)
            .map {
                it.groups.mapIndexedNotNull { i, g ->
                    if (i == 0 || g == null) null else TokenMatch(i, g.range.first, g.value)
                }.singleOrNull() ?: throw Exception("Ambiguous tokens")
            }
            .iterator()
    }

    data class TokenMatch(val groupPos: Int, val strPos: Int, val value: String? = null)

    private fun _next() {
        if (!tokenStream.hasNext()) {
            if (token != TOKENS.EOF) {
                token = TOKENS.EOF
                tokenValue = null
                return
            } else throw IOException("No more tokens!")
        }
        val (g, s, v) = tokenStream.next()
        token = groupsToTokens.getValue(g - 1)
        position = s
        tokenValue = v
        if (token == UNKNOWN_CHAR) throw Exception("Unexpected symbol $v")
    }

    fun next() {
        _next()
    }
}

