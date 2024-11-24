package com.example.KS_63875_Library_SpringBoot.controllers;


import com.example.KS_63875_Library_SpringBoot.models.Author;
import com.example.KS_63875_Library_SpringBoot.models.Book;
import com.example.KS_63875_Library_SpringBoot.repo.AuthorRepository;
import com.example.KS_63875_Library_SpringBoot.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BooksController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping("/books")
    public String getBooks(Model model){
        Iterable<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/addBook")
    public String addBook(Model model){
        return "add-book";
    }

    @PostMapping("/addBook")
    public String addBookPost(@RequestParam String name, @RequestParam String about, @RequestParam String preview,
                              @RequestParam int pages, @RequestParam long author_id, Model model){
        Optional<Author> optionalAuthor = authorRepository.findById(author_id);
        if(optionalAuthor.isEmpty()){
            return "redirect:/books";
        }
        Author author = optionalAuthor.get();
        Book book = new Book(name, about, preview, pages);
        book.setAuthor(author);
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/books/{id}")
    public String getBookDetail(@PathVariable(value = "id") long id, Model model){
        if(!bookRepository.existsById(id)){
            return "redirect:/books";
        }

        Optional<Book> book = bookRepository.findById(id);
        ArrayList<Book> res = new ArrayList<>();
        book.ifPresent(res::add);
        model.addAttribute("book", res);
        return "book-detail";
    }

    @GetMapping("books/{id}/update")
    public String getUpdateBook(@PathVariable(value = "id") long id, Model model){
        if(!bookRepository.existsById(id)){
            return "redirect:/books";
        }

        Optional<Book> book = bookRepository.findById(id);
        ArrayList<Book> res = new ArrayList<>();
        book.ifPresent(res::add);
        model.addAttribute("book", res);
        return "update-book";
    }

    @PostMapping("/books/{id}/update")
    public String getUpdateBook(@PathVariable(value = "id") long id,@RequestParam String name, @RequestParam String about, @RequestParam String preview,
                                @RequestParam int pages, Model model)
    {
        Book book = bookRepository.findById(id).orElseThrow();
        book.setName(name);
        book.setAbout(about);
        book.setPreview(preview);
        book.setPages(pages);
        bookRepository.save(book);
        return "redirect:/books";
    }


    @PostMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable(value = "id") long id, Model model){
        Book book = bookRepository.findById(id).orElseThrow();
        bookRepository.delete(book);
        return "redirect:/books";
    }
}
