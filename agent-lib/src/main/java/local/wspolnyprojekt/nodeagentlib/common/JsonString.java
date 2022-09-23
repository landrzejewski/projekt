package local.wspolnyprojekt.nodeagentlib.common;

import com.google.gson.Gson;

public interface JsonString {

    default String getJsonString() {
        return new Gson().toJson(this);
    }

}
