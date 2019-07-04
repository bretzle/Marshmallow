package marshmallow.debug;

import org.json.JSONObject;

import java.lang.reflect.Field;

public abstract class Evalable {

    public final JSONObject toEvalableString() {
        JSONObject json = new JSONObject();

        for (Field field : getClass().getDeclaredFields()) {
            json.put(field.getName(), getValueFromField(field));
        }

        return json;
    }

    private Object getValueFromField(Field field) {
        try {
            field.setAccessible(true);

            Object value = field.get(this);
            if (value == null) {
                return JSONObject.NULL;
            }

            if (value instanceof Evalable) {
                return ((Evalable) value).toEvalableString();
            }

//            if (value instanceof EvalAudioEventWrapper) {
//                return ((EvalAudioEventWrapper) value).toEvalableString();
//            } todo

            return value;
        } catch (IllegalAccessException e) {
            return JSONObject.NULL;
        }
    }

    @Override
    public String toString() {
        return toEvalableString().toString();
    }
}
