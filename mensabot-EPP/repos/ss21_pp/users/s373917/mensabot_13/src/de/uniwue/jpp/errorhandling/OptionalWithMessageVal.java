package de.uniwue.jpp.errorhandling;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class OptionalWithMessageVal <T> implements OptionalWithMessage <T>{
    private T val;
    public OptionalWithMessageVal(T val){
        this.val = val;
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public T get() {
        return val;
    }

    @Override
    public T orElse(T def) {
        return val;
    }

    @Override
    public T orElseGet(Supplier<? extends T> supplier) {
        return val;
    }

    @Override
    public String getMessage() {
        throw new NoSuchElementException();
    }

    @Override
    public <S> OptionalWithMessage<S> map(Function<T, S> f) {
        S result = f.apply(val);
        return OptionalWithMessage.of(result);
    }

    @Override
    public <S> OptionalWithMessage<S> flatMap(Function<T, OptionalWithMessage<S>> f) {
        return f.apply(val);
    }

    @Override
    public Optional<String> consume(Consumer<T> c) {
        //Im Fall eines OptionalWithMessageVal, das t enthält, soll c == val(?) mit t ausgeführt werden
        c.accept(val);
        return Optional.empty();
    }

    @Override
    public Optional<String> tryToConsume(Function<T, Optional<String>> c) {
        //c consumes Object with type T;
        //if everything ok (???) -> return Optional<String> == Optional.empty();
        //else return Optional<String> with error message = Optional.of("Fehlermeldung");
        //return Optional.ofNullable(getMessage());
        return c.apply(val);
    }
}
