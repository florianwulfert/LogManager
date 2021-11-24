# Logmanager API

### Restschnittstellen:
* POST: /log
  * Anlegen eines neuen Logeintrags
  * Geforderte Parameter:
    * severity: String (Erlaubt: TRACE, DEBUG, INFO, WARNING, ERROR; FATAL)
    * message: String
  * Mögliche Fehler:
    * Bad request (Ein Parameter wurde nicht mitgegeben)  
    * Severity "severity" is not allowed. Please Choose on these ... (siehe oben) 
* GET: /logs
  * Es können Logs gefiltert abgerufen werden.
  * Die Felder startDateTime und endDateTime können unabhängig voneinander verwendet werden.
  * Mögliche Filter:
    * severity: String (Erlaubt: TRACE, DEBUG, INFO, WARNING, ERROR; FATAL)
    * message: String (Eine Teilsuche der Lognachrichten ist möglich)
    * startDateTime: LocalDateTime (Format: dd.MM.yyyy HH:mm:ss)
    * endDateTime: LocalDateTime (Format: dd.MM.yyyy HH:mm:ss)
  * Mögliche Fehler:
    * Bad Request (Format des Datums war falsch)
* GET: /logs/{severity}
  * Filtern nach Severity
  * Geforderte Parameter: 
    * severity: String (Erlaubt: TRACE, DEBUG, INFO, WARNING, ERROR; FATAL)
* GET: /logs/message
  * Filtern nach Message
  * Geforderte Paramter:
    * message (Eine Teilsuche der Lognachrichten ist möglich)
  * Mögliche Fehler:
    * Bad request (Parameter "message" wurde nicht mitgegeben)
* GET: /logs/date
  * Filtern nach Range
  * Geforder Parameter:
    * startDateTime: LocalDateTime (Format: dd.MM.yyyy HH:mm:ss)
    * endDateTime: LocalDateTime (Format: dd.MM.yyyy HH:mm:ss)
  * Mögliche Fehler:
    * Bad Request (Format des Datums war falsch)
    * Bad request (Einer der beiden Parameter wurde nicht mitgegeben)
  
### Datenbank
* Der Logmanger besitzt eine H2-Datenbank
* Hierfür ist notwendig unter D: ein Ordner data hinzuzufügen
* Der Username für die Datenbank ist "blogger"

### Aufgaben
Aufgabe 1:
  Message = Katze --> Hund in Datenbank.
  Nachricht an User --> Katze wurde in Hund übersetzt.

Aufgabe2:
  Message an User -> Die Nachricht wurde als ... + savarity (z.b. INFO, ERROR, usw.) + abgespeichert

Aufgabe 3:
  Erzeuge die Möglichkeit, dass ein Eintrag anhand der ID aus der Datenbank gelöscht wird.
 * a) Erstelle eine Methode im LogRepository, die einen Log Eintrag anhand der ID aus der Datenbank löscht.
 * b) Erstelle eine Methode im LogService, die die erstellte Methode (aus a)) aus dem Repository aufruft.
   * Erstelle einen Test dazu.
 * c) Erstelle einen Endpoint im LogController (/log/delete/{Id}). Der Endpoint soll mit @PutMapping annotiert werden.
   * Erstelle einen Test dazu.