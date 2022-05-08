package gl.database.dao;

import gl.database.model.EtatBorne;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public class EtatBorneDAOTest {

    @Test
    public void testGetEtatBorne(){
        List<EtatBorne> listOfEtatBorne = EtatBorneDAO.getEtatBorne();
        assertThat(listOfEtatBorne)
                .extracting(EtatBorne::getId_etatBorne)
                .contains(EtatBorne.STATE_AVAILABLE)
                .contains(EtatBorne.STATE_UNAVAILABLE)
                .contains(EtatBorne.STATE_RESERVED)
                .contains(EtatBorne.STATE_BUSY)
                .hasSize(4);
    }
}
