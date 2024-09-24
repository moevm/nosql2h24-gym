package moevm.nosqlgym.mongoexample;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection= "messages")
public class Message {
    @Id
    private ObjectId id;
    private String content;

    public Message(String content) {
        this.content = content;
    }

    public ObjectId getId() {
        return this.id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

}
