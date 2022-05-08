package gl.database.dao;

import gl.database.model.TypeNotification;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeNotificationDAOTest {

    @Test
    public void testGetTypeNotification(){
        List<TypeNotification> listOfTypeNotification = TypeNotificationDAO.getTypeNotification();
        assertThat(listOfTypeNotification)
                .extracting(TypeNotification::getId_typeNotification)
                .contains(TypeNotification.TYPE_MAIL)
                .contains(TypeNotification.TYPE_SMS)
                .hasSize(2);
    }
}
