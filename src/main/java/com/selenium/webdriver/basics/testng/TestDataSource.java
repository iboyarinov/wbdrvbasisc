package com.selenium.webdriver.basics.testng;

/**
 * Created with IntelliJ IDEA.
 * User: iboyarinov
 * Date: 2/12/14
 * Time: 9:38 AM
 * To change this template use File | Settings | File Templates.
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestDataSource {

    public String csv() default "";
    public String csvDelimiter() default ",";
    public String csvCommentSymbol() default "#";
}