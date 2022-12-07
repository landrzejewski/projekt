# NodeAgent

## REST API - ogólne założenia

API (endpointy, DTO, utilsy) przeniesione do biblioteki agent-lib. Nie trzeba robić `mvn install` biblioteki, wystarczy dodać zależność do `pom.xml` (serwerowego! nie w roocie) na przykład w postaci:
```
<dependency>
	<groupId>local.wspolnyprojekt</groupId>
	<artifactId>agent-lib</artifactId>
	<version>1.0</version>
</dependency>
```

Metody statyczne klasy `AgentRestRequestDetails` zwracają obiekt `RequestDetails`, który zawiera dane takie jak typ requestu (klasy `NodeHttpRequestMethod`),
"doklejkę" do URL-a z odpowiednim endpointem i ustawionymi pathvariable oraz gotowy payload (JSON jako String) do wrzucenia w body requestu.

## Przykłady użycia
W folderze `TestoweRequestyRest` są przykłady użycia (używane przeze mnie do testowania).

Przykładowy projekt używany przeze mnie do testów jest pod adresem [https://github.com/baranosiu/testproject.git] (branch `master`) w którym jest pokazane:
- jak w `docker-compose.yml` tworzyć voluminy zarówno mapowane na folder (`./out` - tu program sobie tworzy testowy plik wynikowy, który potem można pobrać przez `FTP_GET`) jak i przechowywane wewnętrznie przez Dockera
(`mavenrepo` - cache dla Mavena żeby za każdym razem nie musiał pobierać zależności)
- jak za pomocą `Dockerfile` budować własne obrazy z wrzuconym do wnętrza folderem (w przykładzie `./inside_container`) ustawiać katalog roboczy wewnątrz Dockera i definiować polecenie, które ma się uruchomić automatycznie po podniesieniu kontenera na podstawie zbudowanego obrazu.
- jak za pomocą `.gitattributes` ustawiać odpowiednie mapowania końców linii w plikach tekstowych aby git zrobił odpowiednią konwersję automatycznie (we wnętrzu kontenera Dockera jest środowisko Linuxowe, w którym jest stosowane inne kodowanie końców linii, dla plików źródłowych Javy nie ma to większego znaczenia, ale dla skryptów sh/bash już tak).

## Konfiguracja
Plik `application.properties`, w którym jest ustawiony główny folder workspace (workspace poszczególnych tasków to podfoldery tego folderu o nazwach takich, jak `taskid`
więc `taskid` trzeba nadawać takie, aby było poprawną nazwą folderu w Windowsie).

Klucz `server.port` to port na jakim agent nasłuchuje requestów z serwera (nazwa niefortunna, ale taka jest domyślna w SpringBoot i nie zmieniałem)

Klucz `agent.workspace.dir` to katalog główny workspace (workspace poszczególnych tasków to podfoldery tego folderu).

Klucz `agent.task.autorun` (wartości true/false jako String) decyduje o tym, czy task ma automatycznie startować po zakończeniu pobierania zadania przez git (true), czy czekać na polecenie `TASK_COMMAND_START` (false).

Klucz `agent.configuration.persistence.file` - nazwa pliku (w folderze `agent.workspace.dir`) w którym agent przechowuje dane, które mają przetrwać ewentualny restart noda (w tym momencie tylko nodeId)

Klucz `agent.configuration.id.key` - jego wartość, to klucz pod jakim agent zapisuje swoje id w "persistence.file"

Klucze `server.rest.*` to odpowiednio:

`ip` i `port` - na to ip:port agent wysyła requesty REST

`register` - endpoint na który agent zgłasza serwerowi swoje istnienie (id agenta oraz ip:port na którym przyjmuje polecenia REST) `{nodeid}` jest rozwijane w ścieżce na id noda

`tasklog` - endpoint na który agent wysyła bieżące logi taska; w ścieżce może być zawarty string `{taskid}` (nawiasy klamrowe trzeba maskować przez `\\`) który jest podmieniany na id taska

`taskstatus` - endpoint na który są wysyłane zmiany statusu taska (też może zawierać `{taskid}`)

### Przykładowy plik konfiguracyjny `application.properties`
```
server.port=8000
agent.workspace.dir=C:\\tmp\\workspace
agent.task.autorun=true

agent.configuration.persistence.file=configuration.properties
agent.configuration.id.key=agent.id

server.rest.ip=127.0.0.1
server.rest.port=8080
server.rest.register=/register
server.rest.tasklog=/tasklog/\\{taskid\\}
server.rest.taskstatus=/taskstatus/\\{taskid\\}
```

### Skrypt `simple-http-request-viewer.py`
To prosty skrypt w Pythonie3 słuchający na porcie 8080 i wypisujący na konsoli wysyłane do niego requesty (zawsze odpowiada 200/OK).
Był użyty na potrzeby debugowania/testowania agenta (rejestracja noda, statusy tasków, logi) gdy jeszcze właściwy serwer był w powijakach.

## Skrypt `create-installer.cmd`
Wymaga WiX Toolset (https://wixtoolset.org/) i JDK-17 (tej, która będzie docelowo służyła do uruchamiania agenta).
Skrypt tworzy instalator, który pozwala na szybką instalację agenta na nodzie (instaluje też lokalnie dla siebie JRE-17, więc nie ma problemu jeśli w systemie już jest zainstalowana jakaś inna Java w innej wersji).
Oczywiście potrzebne jest jeszcze zainstalowanie Dockera aby wszystko działało.

## Katalog `TestoweRequestyRest`
Zawiera przykładowe requesty REST zarówno bezpośrednio do agenta jak i do serwera.