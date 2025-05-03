# Übungsprojekt Space Station :rocket:

Dieses Repository enthält das Übungsprojekt für Clean Code Workshops der viadee Unternehmensberatung AG.

## Story
Du bist gerade auf der Raumstation "CCK" eingetroffen.
Da die Besatzung der Raumstation immer wieder "seltsames Verhalten" der Bordsoftware feststellt, wurdest du als Software-Expert:in gerufen, um die Bordsoftware zu analysieren, sie in einen wartbaren Zustand zu versetzen und dabei ggf. Fehler sichtbar zu machen. 
Die Probleme haben in letzter Zeit stark zugenommen - es wird also höchste Zeit für deinen Einsatz!

Du beginnst damit dir einen Überblick zu verschaffen. Nachdem du dir einen Branch vom git master abgezweigt hast,
verschaffst du dir erst mal einen Überblick über die Funktionen der Raumstation, denn die Dokumentation scheint auf kyrillisch verfasst zu sein und ursprünglich von einer anderen Station mit dem Namen "MIR" zu kommen.

## Setup
Um nicht gleich am Produktionscode zu schrauben, startest du dir eine lokale Version der Anwendung.
Die Anwendung ist ein Java Spring Backend. Zur Unterstützung bei der Entwicklung gibt es ein 
virtuelles Cockpit, das stets den aktuellen Zustand der Anwendung zeigt.

### Setup der Entwicklungsumgebung fürs Backend
1. Installiere Git
2. Installiere eine Entwicklungsumgebung wie IntelliJ oder Eclipse
3. Installiere dir ein JDK mindestens in der Version 8 (ggf. mit einer aktuellen IDE bereits erledigt)
4. Installiere Maven, wenn es noch nicht bei deiner Entwicklungsumgebung dabei ist.

### Setup der Raumstation
1. Installiere die Abhängigkeiten des Projektes und baue das Projekt mit `mvn clean package`.
2. Starte die Anwendung SpaceStationApplication über deine Entwicklungsumgebung.
3. Öffne den Browser mit der URL [localhost:8080](localhost:8080)

Das Cockpit der Raumstation sollte geladen werden und sekündlich die aktuellen Werte der Raumstationsmodule anzeigen.
Wenn das Cockpit nicht mit dem Backend reden kann, wird ein _Connection lost_-Fehler angezeigt.


Wir hoffen die Übungen machen euch Spaß und wünschen euch viel Erfolg beim Retten der Raumstation!

Viele Grüße

Euer Clean Code Team
