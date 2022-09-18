package local.wspolnyprojekt.nodeagent.common;

import lombok.Data;

@Data
public class GitCredentials {
    /** Username konta lub token OAuth */
    String username;
    /** Password konta lub "" (nie null) jeśli autoryzacja za pomocą tokena OAuth */
    String password;
}
