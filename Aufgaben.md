

### Aufgaben
Aufgabe 1:
- [x] Message = Katze --> Hund in Datenbank.
- [x] Nachricht an User --> Katze wurde in Hund übersetzt.

Aufgabe 2:
- [x] Message an User --> Die Nachricht wurde als ... + severity (z.b. INFO, ERROR, usw.) + abgespeichert

Aufgabe 3:
- [x] Erzeuge die Möglichkeit, dass ein Eintrag anhand der ID aus der Datenbank gelöscht wird.
* [x] a) Erstelle eine Methode im LogRepository, die einen Log Eintrag anhand der ID aus der Datenbank löscht.
* [x] b) Erstelle eine Methode im LogService, die die erstellte Methode (aus a)) aus dem Repository aufruft.
    * Erstelle einen Test dazu.
* [x] c) Erstelle einen Endpunkt im LogController (/log/delete/{Id}). Der Endpoint soll mit @DeleteMapping annotiert werden.
    * Erstelle einen Test dazu.

Aufgabe 4:
- [x] Erzeuge die Möglichkeit, dass Einträge anhand der Severity aus der Datenbank gelöscht werden.
* [x] a) Erstelle eine Methode im LogRepository, die Logeinträge anhand der Severity aus der Datenbank löscht und lass sie von einer Methode aus dem LogService aufrufen.
* [x] b) Erstelle einen Endpunkt im LogController (/log/delete) mit dem RequestParam severity. Der Endpunkt soll mit @DeleteMapping annotiert werden. Der Endpunkt soll die Methode im LogService aufrufen.
* [x] c) Im LogService soll implementiert werden, dass alle gelöschten Einträge aus der Datenbank als Liste an den User zurückgegeben werden. Die Ausgabe an den User soll damit wie folgt lauten: Es wurden die Einträge mit der ID .., .., .. aus der Datenbank gelöscht.

Aufgabe 5:
- [x] Erzeuge eine neue Tabelle User in der Datenbank und gebe die Möglichkeit einen User zu erzeugen.
* [x] a) Die Entity User soll wie folgt aussehen:
    * id, name, geburtsdatum, gewicht, groesse, lieblingsfarbe
* [x] b) Im UserService soll eine Methode saveUser(User) aufgerufen werden, die aus dem Repository kommt.
* [x] c) Erstelle einen Endpunkt im UserController, der zuvor erzeugt werden muss, mit dem ein User angelegt werden kann.

Aufgabe 6:
- [x] Erweitere die Dokumentation um die hinzugefügten Endpunkte. Achte dabei auf die Unterscheidung zwischen Log und User.

Aufgabe 7:
- [x] Dokumentiere das Hinzufügen eines Users, indem du die Tabelle Log verwendest.
* [x] a) Im UserService soll in der Methode addUser der LogService aufgerufen werden. Dabei soll die Methode addLog verwendet werden.
* [x] b) Es sollen die Nachricht "Der User <username> wurde angelegt" und die Severity INFO hinterlegt werden.
* [x] c) Erweitere die Methode im UserService um ein try/catch und fange die Exception ab (catch (RuntimeException ex) {...}).
* [x] d) Konnte der User nicht angelegt werden und du landest in dem catch, dann soll die Nachricht "Der User <username> konnte nicht angelegt werden. Grund: <exception>" mit der Severity "ERROR" abgespeichert werden.

Aufgabe 8:
- [x] Erzeuge die Möglichkeit nach Nutzern zu suchen und sie zu löschen.
* [x] a) erweitere UserService und UserController mit den entsprechenden Methoden. Verwende die Interfaces von denen das UserRepository erbt.
* [x] b) schreibe Tests
* [x] c) erweitere die Dokumentation um die hinzugefügten Endpunkte.

Aufgabe 9:
- [x] Erzeuge die Möglichkeit als User manuelle Logs anzulegen.
* [x] a) Erweitere die Entity Log um ein User-Attribut.
* [x] b) Der User soll in der Methode addLog im LogController namentlich mit übergeben werden.
    * Im UserService soll der User gefunden werden.
    * Anschließend soll der User mit übergeben werden, um einen Logeintrag anzulegen.
* [x] c) Durch diese Änderung wird die Methode addUser nicht mehr funktionieren, da nach Aufgabe 7 im UserService die Methode addLog aufgerufen wird. Hierfür ist es notwendig auch den Endpunkt addUser um einen User zu erweitern.

Aufgabe 10:
- [x] Refactoring - Optimierung des bereits vorhandenen Codes
* [x] a) LogService - DeleteBySeverity - Rückgabe bei einer leeren Liste: "Keine Einträge gefunden"
* [x] b) LogService - DeleteBySeverity - Die Strings sollen mit einem StringBuilder verkettet werden.
* [x] c) UserService - AddLog - Stringformatter verwenden (der user wurde angelegt)
* [x] d) LogRepository leeren, aus anderen Repositories erben
* [x] e) Verwende im LogDTOMapper Mapstruct - im POM hinzufügen
* [x] f) Enum Farben erstellen (User darf nur diese Farben benutzen)

Aufgabe 11:
- [ ] BMI Rechner erstellen
* [x] a) BMI ausrechnen im UserService
* [x] b) anhand des Geburtsdatums Alter berechnen und zurückgeben
* [x] c) mit switch case: übergewichtig, untergewichtig, normalgewichtig
* [x] d) Endpoint erstellen und dokumentieren
* [x] c) Tests schreiben
* Kriterien
  * 3 verschiedene Arten den BMI zu berechnen
    1. Bei addUser Nachricht: der User wurde angelegt + laut BMI ist er unter/normal/übergewichtig
    2. User beim Namen finden und seinen BMI berechnen
    3. Gar kein User, nur Angaben zu Alter, Gewicht und Größe - kein User wird gespeichert, nur der BMI wird berechnet.
  4. Eine Methode im UserService erstellen dafür
* [x] Entity um bmi erweitern
* [x] Log Ausgaben auf Deutsch übersetzen
* [x] In postman user erstellt + bmi
* [x] bmi wurde berechnet + wert in postman konsole und lognachricht dazu in intellij
* [x] bmi service erstellen und bmicontroller, bmi service vom bmiController aufrufen
* [x] Datum in bmiwithmessage anpassen
* [x] finuserandcalculatebmi anpassen
* [x] nachrichten wergen gewicht in variable auslagern
* [ ] Doku schreiben
* [x] Alle Logs/alle User auf einmal löschen einbauen
* [x] Logger.info aus userService löschen
* [x] testIfUserIsNull im LogServiceTest anpassen
* [x] in LogServiceTest User in eigene Methode auslagern
* [ ] Meldungen erstellen, falls in Postman falsches Format eingegeben wurde 
(getUserId, deleteLogId, deleteUserId, getLogId, postUser - gewicht, groesse, datum, getUserBmi)
* [ ] MR erstellen
* [ ] Integration Tests: webmvc-Tests, SpringBoot-Tests
* [ ] Swagger aufsetzen
