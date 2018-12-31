package net.karlshaffer.q.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.karlshaffer.q.model.User;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class UserSerializer extends JsonSerializer<User>{

    @Override
    public void serialize(User value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("username", value.getUsername());
        gen.writeEndObject();
    }

}