# Logmanager API

### Service port

* Der Service port ist 8081

### Restschnittstellen Log:

* POST: /log
    * Anlegen eines neuen Logeintrags
    * Geforderte Parameter:
        * severity: String (Erlaubt: TRACE, DEBUG, INFO, WARNING, ERROR; FATAL)
        * message: String
        * nameUser: String
    * Mögliche Fehler:
        * Severity ist nicht zugelassen. Bitte wählen Sie eins der folgenden severities aus:
          TRACE, DEBUG, INFO, WARNING, ERROR, FATAL
        * User nicht gefunden
        * Required String parameter 'message/severity/nameUser' is not present
    * Besonderheiten:
        * Wird die message "Katze" übergeben, so wird diese in "Hund" übersetzt und die Nachricht "Katze wurde in Hund
          übersetzt!
          Es wurde die Nachricht "Hund" als INFO abgespeichert!" wird ausgegeben.
* GET: /logs
    * Es können Logs gefiltert abgerufen werden.
    * Die Felder startDateTime und endDateTime können unabhängig voneinander verwendet werden.
    * Mögliche Filter:
        * severity: String (Erlaubt: TRACE, DEBUG, INFO, WARNING, ERROR; FATAL)
        * message: String (Eine Teilsuche der Lognachrichten ist möglich)
        * startDateTime: LocalDateTime (Format: dd.MM.yyyy HH:mm:ss)
        * endDateTime: LocalDateTime (Format: dd.MM.yyyy HH:mm:ss)
    * Mögliche Fehler:
        * Failed to convert value of type 'java.lang.String' to required type 'java.time.LocalDateTime'
        * Severity ist nicht zugelassen. Bitte wählen Sie eins der folgenden severities aus:" +
          " TRACE, DEBUG, INFO, WARNING, ERROR, FATAL
* GET: /logs/id
    * Filtern nach ID
    * Geforderte Parameter:
        * id: Integer
    * Mögliche Fehler:
        * Required path variable was not found or request param has wrong format! Failed to convert value of type '
          java.lang.String' to required type 'java.lang.Integer'; nested exception is java.lang.NumberFormatException:
          For input string: ""
        * null (wenn die Id nicht gefunden wurde)
* DELETE: /logs/delete/{id}
    * Ein Eintrag in der Datenbank wird gelöscht, basierend auf der ID.
    * Geforderte Parameter:
        * id: Integer
    * Mögliche Fehler:
        * No class project.logManager.model.entity.Log entity with id 1 exists!
        * Required path variable was not found or request param has wrong format! Failed to convert value of type '
          java.lang.String' to required type 'java.lang.Integer'; nested exception is java.lang.NumberFormatException:
          For input string: ""
* DELETE: /logs/delete/severity
    * Vorhandene Einträge zu der gewählten severity werden gelöscht.
    * Geforderte Parameter:
        * severity: String
    * Mögliche Fehler
        * Required String parameter 'severity' is not present
    * DELETE: /logs/delete
        * Alle Logs werden aus der Datenbank gelöscht

### Restschnittstellen User:

* POST: /user
    * Ein User wird hinzugefügt
    * Geforderte Parameter:
        * actor: String
        * name: String
        * birthdate: LocalDate
        * weight: double
        * height: double
        * favouriteColor: Farbe
    * Mögliche Fehler:
        * User kann nicht angelegt werden, da noch keine User in der Datenbank angelegt sind. Erster User muss sich
          selbst anlegen!
        * User (actor) nicht gefunden.
        * Farbe falsch! Wählen Sie eine der folgenden Farben: blau, rot, orange, gelb, schwarz
        * Required path variable was not found or request param has wrong format! Failed to convert value of type '
          java.lang.String' to required type 'java.lang.Integer'; nested exception is java.lang.NumberFormatException:
          For input string: ""
        * User bereits vorhanden
        * Required String/double/LocalDate parameter "actor/name/geburtsdatum/gewicht/groesse/lieblingsfarbe" is not
          present
* GET: /users
    * Eine Liste der in der Datenbank vorhandenen User wird ausgegeben
* GET: /user/id
    * Ein User kann mithilfe der ID gefunden werden
    * Geforderte Parameter:
        * id: Integer
    * Mögliche Fehler:
        * Required Integer parameter 'id' is not present
        * "null", wenn die übergebene ID nicht gefunden wurde
* DELETE: /user/delete/{id}
    * Ein User wird anhand der ID gefunden und gelöscht
    * Geforderte Parameter:
        * id: Integer
        * actor: String
    * Mögliche Fehler:
        * User konnte nicht identifiziert werden!
        * Required String parameter 'actor' is not present
        * User mit der ID[...] konnte nicht gefunden werden
        * Ein User kann sich nicht selbst löschen!
        * User[...] kann nicht gelöscht werden, da er in einer anderen Tabelle referenziert wird!
* DELETE: /user/delete/name/{name}
    * Ein User wird anhand des Namens gelöscht
    * Geforderte Parameter:
        * name: String
        * actor: String
    * Mögliche Fehler:
        * Ein User kann sich nicht selbst löschen!
        * Required String parameter 'actor' is not present
        * User mit dem Namen[(actor)] konnte nicht gefunden werden
        * User mit dem Namen[(name)] konnte nicht gefunden werden
        * Required path variable was not found or request param has wrong format! "
            + "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; "
            + "nested exception is java.lang.NumberFormatException: For input string: \"name\"
        * User[...] kann nicht gelöscht werden, da er in einer anderen Tabelle referenziert wird!
* DELETE: /user/delete
    * Alle User werden aus der Datenbank gelöscht
    * Mögliche Fehler:
        * User können nicht gelöscht werden, da sie in einer anderen Tabelle referenziert werden

### Restschnittstellen BMI

* GET: /bmi
    * Der BMI wird berechnet, ohne das ein User gespeichert wird und eine entsprechende Nachricht wird ausgegeben
    * Geforderte Parameter:
        * birthdate: LocalDate
        * height: Double
        * weight: Double
    * Besonderheiten:
        * Angaben zu Usern unter 18 Jahren können nicht verarbeitet werden
    * Mögliche Fehler:
        * Required double parameter 'gewicht/groesse/geburtsdatum' is not present
        * BMI konnte nicht berechnet werden
        * Infinite or NaN
* GET: /bmi/{user}
    * Ein User wird gefunden und sein BMI wird ausgegeben
    * Geforderte Parameter:
        * user: String
    * Mögliche Fehler:
        * User[...] konnte nicht identifiziert werden!

### Datenbank

* Der Logmanager besitzt eine H2-Datenbank
* Hierfür ist notwendig unter D: ein Ordner data hinzuzufügen
* Über die URL "http://localhost:8081/h2-console" startet die Datenbank lokal
* Der Username für die Datenbank ist "blogger"