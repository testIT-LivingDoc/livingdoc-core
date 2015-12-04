package info.novatec.testit.livingdoc.reflect;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


public class InvocationMessage extends Message {

    private final List<Message> messages = new ArrayList<Message>();

    public void addMessage(Message message) {
        messages.add(message);
    }

    @Override
    public int getArity() {
        if (messages.isEmpty()) {
            return 0;
        }

        return messages.get(0).getArity();
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    @Override
    public Object send(String... args) throws InvocationTargetException, IllegalArgumentException, IllegalAccessException,
        SystemUnderDevelopmentException {
        for (Message message : messages) {
            if (message.getArity() == args.length) {
                return message.send(args);
            }
        }
        throw new IllegalArgumentException(String.format("No such method with %d arguments", args.length));
    }

}
