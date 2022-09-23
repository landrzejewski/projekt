package local.wspolnyprojekt.nodeagentlib.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitCredentials implements JsonString {
    /**
     * Username konta lub token OAuth
     */
    String username;
    /**
     * Password konta lub "" (nie null) jeśli autoryzacja za pomocą tokena OAuth
     */
    String password;

}
