package gl.database.dao;

import gl.database.model.Borne;
import gl.database.model.EtatBorne;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public void testUpdateBorneWithUnknowState() {
        assertThatThrownBy(() -> {
            Borne borne = BorneDAO.getAllBorne().get(0);
            BorneDAO.updateStateOfBorne(borne, "Unknow");
        }).isInstanceOf(PSQLException.class);
    }

    @Test
    public void testgetAllBorneFromDateDispo() throws ParseException {
        Date date = new Date(new SimpleDateFormat("dd-MM-yyyy").parse("14-05-2022").getTime());
        Time int1 = new Time(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("14-05-2022 11:00").getTime());
        Time int2 = new Time(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("14-05-2022 15:00").getTime());
        assertThat(BorneDAO.getAllBorneFromDateDispo(date, int1, int2)).isNotNull();
    }
}
