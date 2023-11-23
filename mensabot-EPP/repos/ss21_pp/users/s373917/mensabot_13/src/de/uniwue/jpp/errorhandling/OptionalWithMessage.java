package de.uniwue.jpp.errorhandling;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface OptionalWithMessage <T>{
    boolean isPresent();
    boolean isEmpty();
    T get();
    T orElse(T def);
    T orElseGet(Supplier<? extends T> supplier);
    String getMessage();
    <S> OptionalWithMessage<S> map(Function<T, S> f);
    <S> OptionalWithMessage<S> flatMap(Function<T, OptionalWithMessage<S>> f);
    Optional<String> consume(Consumer<T> c);
    Optional<String> tryToConsume(Function<T, Optional<String>> c);

    static <T> OptionalWithMessage<List<T>> sequence(List<OptionalWithMessage<T>> list) {
        String result = list.stream().filter(e -> e.isEmpty()).map(e -> e.getMessage()).collect(Collectors.joining(System.lineSeparator()));
        if (result != null && !result.isEmpty()) {
            return ofMsg(result);
        }
        return of(list.stream().map(e -> e.get()).collect(Collectors.toList()));
    }

    static <T> OptionalWithMessage<T> of(T val) {
        return new OptionalWithMessageVal<>(val);
        //Erstellt und gibt ein OptionalWithMessageVal mit val zurück
    }

    static <T> OptionalWithMessage<T> ofMsg(String msg) {
        return new OptionalWithMessageMsg(msg);
        //Erstellt und gibt ein OptionalWithMsg mit der Fehlermeldung msg zurück
    }

    static <T> OptionalWithMessage<T> ofNullable(T val, String msg) {
        if (val == null) {
            return ofMsg(msg);
        }
        //return new OptionalWithMessageVal(val); ternärer Operator für inline;
        return of(val);
        //Falls val null ist, soll ein OptionalWithMessageMsg mit der Fehlermeldung msg zurückgegeben werden
        //Ansonsten soll ein OptionalWithMessageVal mit val zurückgegeben werden
    }

    static <T> OptionalWithMessage<T> ofOptional(Optional<T> opt, String msg) {
        if (opt.isEmpty())
            return ofMsg(msg);
        return of(opt.get());


        //Falls opt leer ist, soll ein OptionalWithMessageMsg mit der Fehlermeldung msg zurückgegeben werden.
        //Ansonsten soll ein OptionalWithMessageVal mit dem T-Wert aus opt zurückgegeben werden.
    }
}


