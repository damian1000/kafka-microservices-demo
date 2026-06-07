package io.github.damian1000.kafkademo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation read by JaCoCo (>= 0.8.2) to skip the annotated method
 * during coverage analysis. Used for SpringApplication entry points that
 * cannot be exercised without bringing up the full Spring context plus a
 * Kafka broker.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Generated {
}
