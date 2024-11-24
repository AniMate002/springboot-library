package com.example.KS_63875_Library_SpringBoot.repo;

import com.example.KS_63875_Library_SpringBoot.models.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> { }
