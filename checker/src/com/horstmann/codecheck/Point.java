package com.horstmann.codecheck;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
 
/**
 *
 * @author Minh Dang
 * Annotation for JUnit Test
 */
 
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Point {
    String message();
}

