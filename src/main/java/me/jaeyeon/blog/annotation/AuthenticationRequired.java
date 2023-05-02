package me.jaeyeon.blog.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({METHOD})
@Retention(RUNTIME)
@Documented
public @interface AuthenticationRequired {
}
