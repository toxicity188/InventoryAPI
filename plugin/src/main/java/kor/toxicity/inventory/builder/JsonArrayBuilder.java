package kor.toxicity.inventory.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class JsonArrayBuilder {
    private final JsonArray array = new JsonArray();


    public JsonArrayBuilder add(Number value) {
        array.add(value);
        return this;
    }
    public JsonArrayBuilder add(Character value) {
        array.add(value);
        return this;
    }
    public JsonArrayBuilder add(String value) {
        array.add(value);
        return this;
    }
    public JsonArrayBuilder add(Boolean value) {
        array.add(value);
        return this;
    }
    public JsonArrayBuilder add(JsonElement value) {
        array.add(value);
        return this;
    }

    public JsonArray build() {
        return array;
    }
}
