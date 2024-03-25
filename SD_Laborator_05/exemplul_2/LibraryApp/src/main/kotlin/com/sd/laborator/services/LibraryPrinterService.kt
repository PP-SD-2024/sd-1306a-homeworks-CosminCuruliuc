package com.sd.laborator.services

import com.sd.laborator.interfaces.LibraryPrinter
import com.sd.laborator.model.Book
import org.springframework.stereotype.Service

@Service
class LibraryPrinterService: LibraryPrinter {
    private fun wrapWithHeaderAndFooter(content: String, format: String): String {
        val header = when(format) {
            "html" -> "<header><h1>Welcome to Our Enhanced Library</h1></header>"
            "json" -> "\"header\": \"Welcome to Our Enhanced Library\","
            "raw" -> "***** Welcome to Our Enhanced Library *****\n\n"
            else -> ""
        }

        val footer = when(format) {
            "html" -> "<footer><p>Thank you for visiting our library</p></footer>"
            "json" -> ",\"footer\": \"Thank you for visiting our library\""
            "raw" -> "\n\n***** Thank you for visiting our library *****"
            else -> ""
        }

        return when(format) {
            "html" -> "$header\n$content\n$footer"
            "json" -> "{\n$header\n$content\n$footer\n}"
            "raw" -> "$header$content$footer"
            else -> content
        }
    }

    override fun printHTML(books: Set<Book>): String {
        var content = "<body>"
        books.forEach {
            content += "<p><h3>${it.name}</h3><h4>${it.author}</h4><h5>${it.publisher}</h5>${it.content}</p><br/>"
        }
        content += "</body>"
        return wrapWithHeaderAndFooter(content, "html")
    }

    override fun printJSON(books: Set<Book>): String {
        var content = ""
        books.forEachIndexed { index, it ->
            content += if (index < books.size - 1)
                "    {\"Titlu\": \"${it.name}\", \"Autor\":\"${it.author}\", \"Editura\":\"${it.publisher}\", \"Text\":\"${it.content}\"},\n"
            else
                "    {\"Titlu\": \"${it.name}\", \"Autor\":\"${it.author}\", \"Editura\":\"${it.publisher}\", \"Text\":\"${it.content}\"}\n"
        }
        return wrapWithHeaderAndFooter(content, "json")
    }

    override fun printRaw(books: Set<Book>): String {
        var content = ""
        books.forEach {
            content += "${it.name}\n${it.author}\n${it.publisher}\n${it.content}\n\n"
        }
        return wrapWithHeaderAndFooter(content, "raw")
    }
}
