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
  * Geforderte Parameter:
    * message (Eine Teilsuche der Lognachrichten ist möglich)
  * Mögliche Fehler:
    * Bad request (Parameter "message" wurde nicht mitgegeben)
* GET: /logs/date
  * Filtern nach Range
  * Gefordert Parameter:
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
- Message = Katze --> Hund in Datenbank.
- Nachricht an User --> Katze wurde in Hund übersetzt.

Aufgabe 2:
- Message an User --> Die Nachricht wurde als ... + severity (z.b. INFO, ERROR, usw.) + abgespeichert

Aufgabe 3:
- Erzeuge die Möglichkeit, dass ein Eintrag anhand der ID aus der Datenbank gelöscht wird.
 * a) Erstelle eine Methode im LogRepository, die einen Log Eintrag anhand der ID aus der Datenbank löscht.
 * b) Erstelle eine Methode im LogService, die die erstellte Methode (aus a)) aus dem Repository aufruft.
   * Erstelle einen Test dazu.
 * c) Erstelle einen Endpunkt im LogController (/log/delete/{Id}). Der Endpoint soll mit @DeleteMapping annotiert werden.
   * Erstelle einen Test dazu.

Aufgabe 4:
- Erzeuge die Möglichkeit, dass Einträge anhand der Severity aus der Datenbank gelöscht werden.
 * a) Erstelle eine Methode im LogRepository, die Logeinträge anhand der Severity aus der Datenbank löscht und lass sie von einer Methode aus dem LogService aufrufen.
 * b) Erstelle einen Endpunkt im LogController (/log/delete) mit dem RequestParam severity. Der Endpunkt soll mit @DeleteMapping annotiert werden. Der Endpunkt soll die Methode im LogService aufrufen.
 * c) Im LogService soll implementiert werden, dass alle gelöschten Einträge aus der Datenbank als Liste an den User zurückgegeben werden. Die Ausgabe an den User soll damit wie folgt lauten: Es wurden die Einträge mit der ID .., .., .. aus der Datenbank gelöscht.
 
Aufgabe 5:
- Erzeuge eine neue Tabelle User in der Datenbank und gebe die Möglichkeit einen User zu erzeugen.
* a) Die Entity User soll wie folgt aussehen:
    * id, name, geburtsdatum, gewicht, groesse, lieblingsfarbe
* b) Im UserService soll eine Methode saveUser(User) aufgerufen werden, die aus dem Repository kommt.
* c) Erstelle einen Endpunkt im UserController, der zuvor erzeugt werden muss, mit dem ein User angelegt werden kann.

Aufgabe 6:
- Erweitere die Dokumentation um die hinzugefügten Endpunkte. Achte dabei auf die Unterscheidung zwischen Log und User.

Aufgabe 7:
- Dokumentiere das Hinzufügen eines Users, indem du die Tabelle Log verwendest.
* a) Im UserService soll in der Methode addLog der LogService aufgerufen werden. Dabei soll die Methode addLog verwendet werden.
* b) Es sollen die Nachricht "Der User <username> wurde angelegt" und die Severity INFO hinterlegt werden.
* c) Erweitere die Methode im UserService um ein try/catch und fange die Exception ab.
* d) Konnte der User nicht angelegt werden und du landest in dem catch, dann soll die Nachricht "Der User <username> konnte nicht angelegt werden. Grund: <exception>" mit der Severity "ERROR" abgespeichert werden.

Aufgabe 8:
- Erzeuge die Möglichkeit nach Nutzern zu suchen und sie zu löschen.
* a) erweitere UserService und UserController mit den entsprechenden Methoden. Verwende die Interfaces von denen das UserRepository erbt.
* b) schreibe Tests
* c) erweitere die Dokumentation um die hinzugefügten Endpunkte.