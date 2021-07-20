package com.digicert.api;

import com.digicert.models.Book;
import com.digicert.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/books")
public class BookController {

    private BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping(value="/list")
    public ResponseEntity<List<Book>> listBooks() {
        try {
            List<Book> list = bookRepository.findAll();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Book> retrieveBook(@PathVariable("id") Long id) {
        Optional<Book> optBook = bookRepository.findById(id);

        return optBook.map(book -> new ResponseEntity<>(book, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PutMapping()
    public ResponseEntity<Book> createBook(@RequestBody Book newBook) {
        try {
            Book created = bookRepository.saveAndFlush(newBook);
            return new ResponseEntity<>(created, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(newBook, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping()
    public ResponseEntity<Book> updateBook(@RequestBody Book newBook) {
        try {
            if (newBook.getId() == null) throw new Exception("not found");

            Optional<Book> found = bookRepository.findById(newBook.getId());
            if (found.isPresent()) {
                Book existing = found.get();
                existing.setName(newBook.getName());
                existing.setAuthor(newBook.getAuthor());
                existing.setRelease(newBook.getRelease());

                Book updated = bookRepository.saveAndFlush(existing);
                return new ResponseEntity<>(updated, HttpStatus.OK);
            }

            throw new Exception("Book "+newBook.getId()+" not found");
        } catch (Exception ex){
            return new ResponseEntity<>(newBook, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> removeBook(@PathVariable("id") Long id) {
        Optional<Book> optBook = bookRepository.findById(id);

        if (optBook.isPresent()) {
            try {
                bookRepository.deleteById(id);
                return new ResponseEntity<>(true, HttpStatus.OK);
            } catch (Exception ex) {
                return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
            }
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
