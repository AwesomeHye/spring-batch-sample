package dev.hyein.springbatchsample.lecture.chunk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Customer {

    @Id
    private Long id;
    private String name;
    private int age;
    private String year;

    public Customer(String name) {
        this.name = name;
    }
}
