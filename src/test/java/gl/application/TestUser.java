package gl.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User")
public class TestUser {

    @Test
    public void testNewState(){
        User user = new User(Application.STATE_CONNECTED);
        assertThat(user.getState()).isEqualTo(Application.STATE_CONNECTED);
    }
    
    @Test
    public void testSetState(){
        User user = new User(Application.STATE_ANONYMOUS);
        user.setState(Application.STATE_MANAGER);
        assertThat(user.getState()).isEqualTo(Application.STATE_MANAGER);
    }
}
