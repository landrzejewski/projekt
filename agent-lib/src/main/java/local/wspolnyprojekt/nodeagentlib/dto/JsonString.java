package local.wspolnyprojekt.nodeagentlib.dto;

import com.google.gson.Gson;

public interface JsonString {

    default String getJsonString() {
        return new Gson().toJson(this);
    }

}
