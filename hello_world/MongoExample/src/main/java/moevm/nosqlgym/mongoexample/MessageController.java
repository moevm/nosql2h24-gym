package moevm.nosqlgym.mongoexample;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageRepository repository;
    public MessageController(MessageRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public void addMessage(@RequestBody String message) {
        repository.save(new Message(message));
    }

    @GetMapping
    public List<Message> getMessage() {
        return repository.findAll();
    }
}
