package com.example.KS_63875_Library_SpringBoot.controllers;


import com.example.KS_63875_Library_SpringBoot.models.Author;
import com.example.KS_63875_Library_SpringBoot.models.Book;
import com.example.KS_63875_Library_SpringBoot.repo.AuthorRepository;
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
public class AuthorsController {

    @Autowired
    AuthorRepository authorRepository;

    @GetMapping("/authors")
    public String getAuthors(Model model){
        Iterable<Author> authors = authorRepository.findAll();
        model.addAttribute("authors", authors);
        return "authors";
    }

    @GetMapping("authors/add")
    public String addGetAuthor(Model model){
        return "add-author";
    }

    @PostMapping("/authors/add")
    public String addPostAuthor(@RequestParam String username, @RequestParam String fullname, @RequestParam int age, Model model){
        Author author = new Author(username, fullname, age);
        authorRepository.save(author);
        return "redirect:/authors";
    }

    @GetMapping("/authors/{id}")
    public String getAuthorDetails(@PathVariable(value = "id") long id, Model model){
        if(!authorRepository.existsById(id)){
            return "redirect:/authors";
        }
        Optional<Author> author = authorRepository.findById(id);
        ArrayList<Author> res = new ArrayList<>();
        author.ifPresent(res::add);
        model.addAttribute("author", res);
        return "author-detail";
    }

    @GetMapping("authors/{id}/update")
    public String getUpdateAuthor(@PathVariable(value = "id") long id, Model model)
    {
        if(!authorRepository.existsById(id)){
            return "redirect:/authors";
        }
        Optional<Author> author = authorRepository.findById(id);
        ArrayList<Author> res = new ArrayList<>();
        author.ifPresent(res::add);
        model.addAttribute("author", res);
        return "update-author";
    }

    @PostMapping("authors/{id}/update")
    public String postUpdateAuthor(@PathVariable(value = "id") long id, @RequestParam String username, @RequestParam String fullname,
                                   @RequestParam int age, Model mode)
    {
        Author author = authorRepository.findById(id).orElseThrow();
        author.setUsername(username);
        author.setFullname(fullname);
        author.setAge(age);
        authorRepository.save(author);
        return "redirect:/authors";
    }

    @PostMapping("/authors/{id}/delete")
    public String deleteAuthor(@PathVariable(value = "id") long id, Model model){
        Author author = authorRepository.findById(id).orElseThrow();
        authorRepository.delete(author);
        return "redirect:/authors";
    }
}
