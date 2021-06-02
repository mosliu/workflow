package net.liuxuan.utils.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import lombok.extern.java.Log;
import net.liuxuan.utils.date.DateUtils;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2019-12-16
 **/
@Log
public class FailsafeDateDeserializer implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        if (!json.isJsonPrimitive()) {
            log.warning("Json Expected String But Got Another");
            return null;
        }
        try {
            String j = json.getAsJsonPrimitive().getAsString();
            return DateUtils.guessDate(j);
        } catch (ClassCastException ex) {
            log.warning("ClassCastException!" + ex.getMessage());
            return null;
        } catch (IllegalStateException ex) {
            log.warning("IllegalStateException!" + ex.getMessage());
            return null;
        }
    }
}
