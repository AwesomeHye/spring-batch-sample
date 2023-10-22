package dev.hyein.springbatchsample.lecture.chunk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {

    private String name;
    private int age;
    private String year;

    public Customer(String name) {
        this.name = name;
    }
}
