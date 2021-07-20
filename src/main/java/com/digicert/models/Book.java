package com.digicert.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class Book {
    @Id
    private Long id;

    private String name;
    private String author;
    private Date release;

}