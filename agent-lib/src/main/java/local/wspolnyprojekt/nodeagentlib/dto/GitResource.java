package local.wspolnyprojekt.nodeagentlib.dto;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitResource implements JsonString {
    String repositoryUrl;
    String branch;

    public String getJsonString() {
        return new Gson().toJson(this);
    }

}
