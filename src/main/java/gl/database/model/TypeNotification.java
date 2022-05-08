package gl.database.model;

public class TypeNotification {

        public final static String TYPE_SMS = "SMS";
        public final static String TYPE_MAIL = "Mail";

        private String id_typeNotification;

        public TypeNotification() {
        }

    public String getId_typeNotification() {
        return id_typeNotification;
    }

    public void setId_typeNotification(String id_typeNotification) {
        this.id_typeNotification = id_typeNotification;
    }

    @Override
        public String toString() {
            return String.format("'%s'", id_typeNotification);
        }
}
