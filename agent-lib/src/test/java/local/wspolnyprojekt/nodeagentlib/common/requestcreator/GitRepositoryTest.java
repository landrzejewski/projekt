package local.wspolnyprojekt.nodeagentlib.common.requestcreator;

import local.wspolnyprojekt.nodeagentlib.common.GitResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GitRepositoryTest {
    GitResource gitResource;
    String repositoryUrl = "repo";
    String branch = "branch";

    @BeforeEach
    void init() {
        gitResource = new GitResource(repositoryUrl, branch);
    }

    @Test
    void isRepositoryUrlOk() {
        assertThat(gitResource.getRepositoryUrl()).isEqualTo(repositoryUrl);
    }

    @Test
    void isBranchOk() {
        assertThat(gitResource.getBranch()).isEqualTo(branch);
    }

    @Test
    void isJsonOk() {
        String expected = String.format("{\"repositoryUrl\":\"%s\",\"branch\":\"%s\"}", repositoryUrl, branch);
        assertThat(gitResource.getJsonString()).isEqualTo(expected);
    }

}
