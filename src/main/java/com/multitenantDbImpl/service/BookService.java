package com.multitenantDbImpl.service;

import com.multitenantDbImpl.repository.BookRepository;
import com.multitenantDbImpl.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BookService {
    @Autowired
    BookRepository bookDao;

    public List<Book> getAllBooks() { return bookDao.findAll(); }
}
