module com.ventasropa {
    requires javafx.controls;
    requires javafx.fxml;

    requires static lombok;
    requires jakarta.validation;
    requires org.hibernate.validator;

    opens com.ventasropa.controller to javafx.fxml;

    opens com.ventasropa.model to javafx.base, org.hibernate.validator;
    opens com.ventasropa.dto to javafx.base, org.hibernate.validator;

    exports com.ventasropa;
}