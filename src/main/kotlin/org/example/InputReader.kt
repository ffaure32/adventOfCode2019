import java.io.File

fun readInput(day: Int)
        = readFile("day${day}.txt")

fun lineToLongs(line : String) : List<Long> = Regex("\\d+")
    .findAll(line).map(MatchResult::value).map(String::toLong).toList()

fun lineToInts(line : String) : List<Long> = Regex("\\d+")
    .findAll(line).map(MatchResult::value).map(String::toLong).toList()

fun readFile(fileName: String)
        = File(ClassLoader.getSystemResource(fileName).file).readLines(Charsets.UTF_8)
