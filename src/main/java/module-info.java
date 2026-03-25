module com.animal_bite_surveillance_and_public_health_monitoring_system.animal_bite_surveillance_and_public_health_monitoring_system {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;

    opens com.animal_bite_surveillance_and_public_health_monitoring_system.animal_bite_surveillance_and_public_health_monitoring_system to javafx.fxml;
    exports com.animal_bite_surveillance_and_public_health_monitoring_system.animal_bite_surveillance_and_public_health_monitoring_system;
    
    // New packages
    opens com.abms.ui to javafx.fxml;
    opens com.abms.repository to java.sql;
    exports com.abms.ui;
    exports com.abms.repository;
    exports com.abms.service;
    exports com.abms.utils;
}
