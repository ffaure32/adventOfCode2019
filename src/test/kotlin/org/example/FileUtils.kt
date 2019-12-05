package org.example



fun String.loadFromFile(): String {
    return FileUtils::class.java.getResource(this).readText()
}

class FileUtils {

}
