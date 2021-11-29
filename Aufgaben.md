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
* a) Im UserService soll in der Methode addUser der LogService aufgerufen werden. Dabei soll die Methode addLog verwendet werden.
* b) Es sollen die Nachricht "Der User <username> wurde angelegt" und die Severity INFO hinterlegt werden.
* c) Erweitere die Methode im UserService um ein try/catch und fange die Exception ab (catch (RuntimeException ex) {...}).
* d) Konnte der User nicht angelegt werden und du landest in dem catch, dann soll die Nachricht "Der User <username> konnte nicht angelegt werden. Grund: <exception>" mit der Severity "ERROR" abgespeichert werden.

Aufgabe 8:
- Erzeuge die Möglichkeit nach Nutzern zu suchen und sie zu löschen.
* a) erweitere UserService und UserController mit den entsprechenden Methoden. Verwende die Interfaces von denen das UserRepository erbt.
* b) schreibe Tests
* c) erweitere die Dokumentation um die hinzugefügten Endpunkte.

Aufgabe 9:
- Erzeuge die Möglichkeit als User manuelle Logs anzulegen.
* a) Erweitere die Entity Log um ein User-Attribut.
* b) Der User soll in der Methode addLog im LogController namentlich mit übergeben werden.
    * Im UserService soll der User gefunden werden.
    * Anschließend soll der User mit übergeben werden, um einen Logeintrag anzulegen.
* c) Durch diese Änderung wird die Methode addUser nicht mehr funktionieren, da nach Aufgabe 7 im UserService die Methode addLog aufgerufen wird. Hierfür ist es notwendig auch den Endpunkt addUser um einen User zu erweitern.