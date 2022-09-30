# NodeAgent

## REST API - ogólne założenia
Spring Boot słucha na porcie 8000.

Wszystko przeniesione do biblioteki agent-lib. Nie trzeba robić `mvn install` biblioteki, wystarczy dodać zależność do `pom.xml` (serwerowego! nie w roocie) na przykład w postaci:
```
<dependency>
	<groupId>local.wspolnyprojekt</groupId>
	<artifactId>agent-lib</artifactId>
	<version>1.0</version>
</dependency>
```

Metody statyczne klasy `AgentRestRequestDetails` zwracają obiekt `RequestDetails`, który zawiera dane takie jak typ requestu (klasy `RequestMethod`),
"doklejkę" do URL-a z odpowiednim endpointem i ustawionymi pathvariable oraz gotowy payload (JSON jako String) do wrzucenia w body requestu.

## Przykłady użycia
W folderze `TestoweRequestyRest` są przykłady użycia (używane przeze mnie do testowania).

Przykładowy projekt używany przeze mnie do testów jest pod adresem [https://github.com/baranosiu/testproject.git] (branch `master`) w którym jest pokazane:
- jak w `docker-compose.yml` tworzyć voluminy zarówno mapowane na folder (`./out` - tu program sobie tworzy testowy plik wynikowy, który potem można pobrać przez `FTP_GET`) jak i przechowywane wewnętrznie przez Dockera
(`mavenrepo` - cache dla Mavena żeby za każdym razem nie musiał pobierać zależności)
- jak za pomocą `Dockerfile` budować własne obrazy z wrzuconym do wnętrza folderem (w przykładzie `./inside_container`) ustawiać katalog roboczy wewnątrz Dockera i definiować polecenie, które ma się uruchomić automatycznie po podniesieniu kontenera na podstawie zbudowanego obrazu.
- jak za pomocą `.gitattributes` ustawiać odpowiednie mapowania końców linii w plikach tekstowych aby git zrobił odpowiednią konwersję automatycznie (we wnętrzu kontenera Dockera jest środowisko Linuxowe, w którym jest stosowane inne kodowanie końców linii, dla plików źródłowych Javy nie ma to większego znaczenia, ale dla skryptów sh/bash już tak).

## Konfiguracja
Na razie konfiguracja jest w enumie `Configuration.java`, w którym jest ustawiony główny foplder workspace (workspace poszczególnych tasków to podfoldery tego folderu o nazwach takich, jak `taskid`
więc `taskid` trzeba nadawać na razie takie, aby było poprawną nazwą folderu w Windowsie).

## Uwagi
Brakuje jeszcze handlerów do obsługi wielu błędów, ale one pojawią się dopiero jak będę miał obsługę stanów danego taska (na razie nie implementowałem, aby nie tracić czasu na pisanie czegoś, co i tak docelowo poleci do kosza).
