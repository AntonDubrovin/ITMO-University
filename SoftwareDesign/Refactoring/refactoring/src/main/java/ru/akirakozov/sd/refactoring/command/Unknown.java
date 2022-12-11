package ru.akirakozov.sd.refactoring.command;

public class Unknown extends Products {
    private final String command;

    public Unknown(final String command) {
        this.command = command;
    }

    @Override
    public String product_ans() {
        return "Unknown command: " + command;
    }
}
