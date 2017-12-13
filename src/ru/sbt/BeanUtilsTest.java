package ru.sbt;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BeanUtilsTest {
    @Test
    public void assign() throws Exception {
        Person a = new Person(true, "Jack");
        Person b = new Person(false, "Jill");
        System.out.println("OLD:\ta.name= " + a.getName());
        System.out.println("OLD:\tb.name= " + b.getName());
        (new BeanUtils()).assign(a, b);
        System.out.println("NEW:\ta.name= " + a.getName());
        System.out.println("NEW:\tb.name= " + b.getName());
        assertEquals(a.getName(), b.getName());
    }

}