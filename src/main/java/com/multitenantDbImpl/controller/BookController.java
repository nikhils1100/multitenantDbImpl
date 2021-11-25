package com.multitenantDbImpl.controller;

import com.multitenantDbImpl.repository.BookRepository;
import com.multitenantDbImpl.entity.Book;
import com.multitenantDbImpl.masterTenant.config.DbContextHolder;
import com.multitenantDbImpl.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {
    private BookRepository bookDao;
    private BookService bookService;

    public BookController(){}

    public BookController(BookRepository inputBookDao, BookService inputBookService){
        this.bookDao = inputBookDao;
        this.bookService = inputBookService;
    }

    @GetMapping("/{env}")
    public String connectToDb(@RequestHeader("tenant-id") String tenantName) { //  See Headers
        try {
            String tenantStr = "tenant_book_" + tenantName;
            DbContextHolder.setCurrentDb(tenantStr);

            StringBuilder responseStr = new StringBuilder();
            this.bookService = new BookService();

            for(Book b :bookService.getAllBooks()){
                responseStr.append(b.bookId + " |" + b.bookName + " |" +b.author + System.lineSeparator());
            }
            System.out.println(responseStr.toString());

            return responseStr.toString();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return "Failure";
        }
    }
}
