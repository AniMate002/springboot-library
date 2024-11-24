package com.example.KS_63875_Library_SpringBoot;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.KS_63875_Library_SpringBoot.models.Author;
import com.example.KS_63875_Library_SpringBoot.models.Book;
import com.example.KS_63875_Library_SpringBoot.repo.AuthorRepository;
import com.example.KS_63875_Library_SpringBoot.repo.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class Ks63875LibrarySpringBootApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthorRepository authorRepository;

	@MockBean
	private BookRepository bookRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void checkMainPage() throws Exception {
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("View Books")));
	}

	@Test
	void checkBooksPage() throws Exception {
		this.mockMvc.perform(get("/books")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("Discover Books")));
	}

	@Test
	void testGetAuthors() throws Exception {
		Author author1 = new Author("user1", "Author One", 30);
		Author author2 = new Author("user2", "Author Two", 40);
		Mockito.when(authorRepository.findAll()).thenReturn(Arrays.asList(author1, author2));

		mockMvc.perform(get("/authors"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("authors", hasSize(2)));
	}

	@Test
	void testAddPostAuthor() throws Exception{
		Mockito.when(authorRepository.save(Mockito.any(Author.class))).thenReturn(new Author("user3", "Author Three", 25));

		mockMvc.perform(post("/authors/add")
						.param("username", "user3")
						.param("fullname", "Author Three")
						.param("age", "25")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/authors"));

	}

	@Test
	void testGetAuthorDetails() throws Exception{
		Author author = new Author("user1", "Author One", 30);
		Mockito.when(authorRepository.existsById(1L)).thenReturn(true);
		Mockito.when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

		mockMvc.perform(get("/authors/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("author"))
				.andExpect(model().attribute("author", is(Arrays.asList(author))));
	}

	@Test
	void testUpdateAuthor() throws Exception{
		Author author = new Author("user1", "Author One", 30);
		Mockito.when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
		Mockito.when(authorRepository.save(Mockito.any(Author.class))).thenReturn(author);

		mockMvc.perform(post("/authors/1/update")
				.param("username", "updatedUser")
				.param("fullname", "Updated Author")
				.param("age", "35")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/authors"));
	}


	@Test
	void testDeleteAuthor() throws Exception{
		Author author = new Author("user1", "Author One", 30);
		Mockito.when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
		Mockito.doNothing().when(authorRepository).delete(Mockito.any(Author.class));
		mockMvc.perform(post("/authors/1/delete"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/authors"));

	}


//	{ BOOKS TEST }

	@Test
	void testGetBooks() throws Exception{
		Book book1 = new Book("Book One", "About Book One", "Preview One", 300);
		Book book2 = new Book("Book Two", "About Book Two", "Preview Two", 400);
		Mockito.when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

		mockMvc.perform(get("/books"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("books", hasSize(2)));
	}

	@Test
	void testAddBookPost() throws Exception{
		Author author = new Author("user1", "Author One", 45);
		Mockito.when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
		Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(new Book("Book Three", "About Book Three", "Preview Three", 250));

		mockMvc.perform(post("/addBook")
				.param("name", "Book Three")
				.param("about", "About Book Three")
				.param("preview", "Preview Three")
				.param("pages", "250")
				.param("author_id", "1")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/books"));
	}


	@Test
	void testGetBookDetail() throws Exception {
		Author author = new Author("user1", "Author One", 45);
		Book book = new Book("Book One", "About Book One", "Preview One", 300);
		book.setAuthor(author);

		Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
		Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

		mockMvc.perform(get("/books/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("book"))
				.andExpect(model().attribute("book", is(Arrays.asList(book))));
	}


	@Test
	void testUpdateBookPost() throws Exception{
		Book book = new Book("Book One", "About Book One", "Preview One", 300);
		Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
		Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

		mockMvc.perform(post("/books/1/update")
				.param("name", "Updated Book")
				.param("about", "Updated About Book")
				.param("preview", "Updated Preview")
				.param("pages", "350")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/books"));
	}


	@Test
	void testDeleteBook() throws Exception{
		Book book = new Book("Book One", "About Book One", "Preview One", 300);
		Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
		Mockito.doNothing().when(bookRepository).delete(Mockito.any(Book.class));

		mockMvc.perform(post("/books/1/delete"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/books"));
	}

}