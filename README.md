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
    * Severity  ist nicht zugelassen. Bitte wählen Sie eins der folgenden severities aus: 
    TRACE, DEBUG, INFO, WARNING, ERROR, FATAL
* GET: /logs/{severity}
  * Filtern nach Severity
  * Geforderte Parameter: 
    * severity: String (Erlaubt: TRACE, DEBUG, INFO, WARNING, ERROR; FATAL)
* GET: /logs/message
  * Filtern nach Message
  * Geforderte Parameter:
    * message (Eine Teilsuche der Lognachrichten ist möglich)
  * Mögliche Fehler:
    * Bad request (Parameter "message" wurde nicht mitgegeben)
* GET: /logs/date
  * Filtern nach Range
  * Geforderte Parameter:
    * startDateTime: LocalDateTime (Format: dd.MM.yyyy HH:mm:ss)
    * endDateTime: LocalDateTime (Format: dd.MM.yyyy HH:mm:ss)
  * Mögliche Fehler:
    * Bad Request (Format des Datums war falsch)
    * Bad request (Einer der beiden Parameter wurde nicht mitgegeben)
* GET: /logs/id
  * Filtern nach ID
  * Geforderte Parameter:
    * id: Integer
  * Mögliche Fehler:
    * Bad request (Der Parameter wurde nicht als Integer eingegeben)
    * Bad request (Es wurde kein Wert übergeben)
    * Internal Server Error (Eine ID wird eingegeben, die nicht vergeben wurde)
* DELETE: /logs/delete/{id}
  * Ein Eintrag in der Datenbank wird gelöscht, basierend auf der ID.
  * Geforderte Parameter: 
    * id: Integer
  * Mögliche Fehler:
    * Bad request (Der Parameter wurde nicht als Integer eingegeben)E
    * Internal Server Error (Die gewünschte ID existiert nicht oder es wurde kein Wert übergeben)
* DELETE: /logs/delete/severity
  * Vorhandene Einträge zu der gewählten severity werden gelöscht. 
  * Geforderte Parameter:
    * severity: String
  * Mögliche Fehler
  * DELETE: /logs/delete
    * Alle Logs werden aus der Datenbank gelöscht

### Restschnittstellen User:
* POST: /user
  * Ein User wird hinzugefügt
  * Geforderte Parameter:
    * name: String
    * geburtsdatum: LocalDate
    * gewicht: double
    * groesse: double
    * lieblingsfarbe: Farbe
  * Mögliche Fehler:
    * Bad request (Datumformat falsch oder wenn String statt double übergeben wird)
    * Bad request (Es wird kein Parameter übergeben)
* GET: /users
  * Eine Liste der in der Datenbank vorhandenen User wird ausgegeben
  * Geforderte Parameter:
    * --
  * Mögliche Fehler:
* GET: /user/id
  * Ein User kann mithilfe der ID gefunden werden
  * Geforderte Parameter:
    * id: Integer
  * Mögliche Fehler:
    * Bad request (Es wird nichts übergeben)
    * Bad request (Es wird kein Integer übergeben)
* DELETE: /user/delete/{id}
  * Ein User wird anhand der ID gelöscht
  * Geforderte Parameter:
    * id: Integer
  * Mögliche Fehler:
    * Internal Server Error (ID unbekannt)
* DELETE: /user/delete/name/{name}
  * Ein User wird anhand des Namens gelöscht
  * Geforderte Parameter:
    * name: String
    * actor: String
* DELETE: /user/delete
  * Alle User werden aus der Datenbank gelöscht

###Restschnittstellen BMI
* GET: /bmi
  * Der BMI wird berechnet, ohne das ein User gespeichert wird und eine entsprechende Nachricht wird ausgegeben
  * Geforderte Parameter:
    * geburtsDatum: LocalDate
    * groesse: Double
    * gewicht: Double
  * Mögliche Fehler:
* GET: /bmi/{user}
  * Ein User wird gefunden und sein BMI wird ausgegeben
  * Geforderte Parameter:
    * user: String

### Datenbank
* Der Logmanager besitzt eine H2-Datenbank
* Hierfür ist notwendig unter D: ein Ordner data hinzuzufügen
* Über die URL "http://localhost:8081/h2-console" startet die Datenbank lokal
* Der Username für die Datenbank ist "blogger"