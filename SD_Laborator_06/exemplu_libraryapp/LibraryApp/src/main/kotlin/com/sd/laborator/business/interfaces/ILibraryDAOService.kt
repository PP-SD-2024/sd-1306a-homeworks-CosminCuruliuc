package com.sd.laborator.business.interfaces

import com.sd.laborator.business.models.Book

interface ILibraryDAOService {
    fun createBookTable()
    fun getBooks(): List<Book>
    fun addBook(book: Book)
    fun findAllByAuthor(author: String): List<Book>
    fun findAllByTitle(title: String): List<Book>
    fun findAllByPublisher(publisher: String): List<Book>
}