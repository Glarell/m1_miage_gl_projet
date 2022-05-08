package gl.database.dao;

import gl.database.model.Borne;
import gl.database.model.EtatBorne;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class BorneDAOTest {

    @Test
    public void testGetAllBorne() {
        List<Borne> listOfBorne = BorneDAO.getAllBorne();
        assertThat(listOfBorne)
                .hasSize(5);
    }

    @Test
    public void testGetAllBorneFromEachState() {
        List<Borne> listOfAvailable = BorneDAO.getAllBorneFromState(EtatBorne.STATE_AVAILABLE);
        List<Borne> listOfUnavailble = BorneDAO.getAllBorneFromState(EtatBorne.STATE_UNAVAILABLE);
        List<Borne> listOfReserved = BorneDAO.getAllBorneFromState(EtatBorne.STATE_RESERVED);
        List<Borne> listOfBusy = BorneDAO.getAllBorneFromState(EtatBorne.STATE_BUSY);

        assertThat(listOfAvailable.size() + listOfUnavailble.size() + listOfReserved.size() + listOfBusy.size())
                .isEqualTo(5);
    }

    @Test
    public void testGetAllBorneFromUnknowState() {
        assertThat(BorneDAO.getAllBorneFromState("unknow"))
                .hasSize(0);
    }

    @Test
    public void testUpdateStateOfBorne() throws SQLException {
        Borne borne = BorneDAO.getAllBorne().get(0);
        String id_etatBorneSave = borne.getId_etatBorne();

        //Test sur le changement du status
        BorneDAO.updateStateOfBorne(borne, EtatBorne.STATE_BUSY);
        assertThat(BorneDAO.getAllBorne().get(0).toString())
                .isEqualTo(borne.toString());

        //Restoration du status de base
        BorneDAO.updateStateOfBorne(borne, id_etatBorneSave);
        assertThat(BorneDAO.getAllBorne().get(0).toString())
                .isEqualTo(borne.toString());
    }

    @Test
    public void testUpdateBorneWithUnknowState() throws SQLException {
        assertThatThrownBy(() -> {
            Borne borne = BorneDAO.getAllBorne().get(0);
            BorneDAO.updateStateOfBorne(borne, "Unknow");
        }).isInstanceOf(PSQLException.class);
    }
}
