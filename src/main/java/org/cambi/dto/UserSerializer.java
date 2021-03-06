/**
 *
 */
package org.cambi.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.cambi.model.UserTweet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author luca
 *
 */
public class UserSerializer extends StdSerializer<UserTweet> {

    private DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);

    public UserSerializer() {
        this(null);
    }

    public UserSerializer(Class<UserTweet> t) {
        super(t);
    }

    @Override
    public void serialize(UserTweet value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId().getUserId().intValue());
        jgen.writeStringField("name", value.getUserName());
        jgen.writeStringField("screen_name", value.getUserScreenName());
        jgen.writeStringField("created_at", dateFormat.format(value.getCreationDate()));

        jgen.writeEndObject();
    }
}
