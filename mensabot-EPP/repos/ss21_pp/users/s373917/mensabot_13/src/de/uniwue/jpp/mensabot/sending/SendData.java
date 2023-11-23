package de.uniwue.jpp.mensabot.sending;

import java.util.Optional;

public class SendData implements Sender{
    @Override
    public Optional<String> send(String msg) {
        if (msg == null || msg.isEmpty()) {
            return Optional.of("An error occurred");
        } else {
            System.out.println(msg);
        }
        return Optional.empty();
    }
}
