# NodeAgent

## REST API - ogólne założenia
Spring Boot słucha na porcie 8000.

Wszystkie endpointy są zdefiniowane poprzez stałe w `local.wspolnyproject.nodeagent.common.RestEndpoints`.
W tych endpointach `{taskid}` oznacza id zadania (może być dowolne, pierwotnie myślałem o czymś w rodzaju UUID, 
aby mieć grawancję unikalności, ale to może być dowolny niepusty String za wyjątkiem `system`, który
jest zastrzeżony na potrzeby endpointów niepowiązanych z żadnym konkretnym taskiem, na przykład dane o obciążeniu dla loadbalancera itp.)

Pole `{*filename}` to nazwa pliku względem katalogu "root" workspace danego taska.

Przyjąłem założenie, że taski są tworzone dynamicznie poprzez jeden z trzech requestów:
1. `GIT_CREDENTIALS` - ustawienie danych uwierzytelniających dla repozytorium Git (nie tworzy folderu dla taska w workspace)
2. `GIT_CLONE` - pobranie plików z repozytorium
3. `FTP_POST_FILE` - wysłanie pliku do workspace taska

Poszczególne endpointy przyjmują/zwracają obiekty klas zdefiniowanych w `common.*` lub w przypadku
endpointa `FTP_*` jako `application/octet-stream` (surowe binarki). Endpoint `DOCKER_LOG` także
zwraca logi w postaci `application/octet-stream`. Nic nie stoi na przeszkodzie, aby przesyłanie
plików binarnych zrealizować jakimś innym `Content-Type` - przyjąłem `application/octetstream` tylko
na potrzeby prototypowania.

Endpointy, które powstaną w przyszłości (na przykład status tasku itp.), także będą zdefiniowane w `common.RestEndpoints`
i będą korzystać z obiektów zdefiniowanych w `common.*`.

## REST API - szczegółowy opis endpointów
### `GIT_CREDENTIALS` - ustawienie danych uwierzytelniających do repozytorium
W body przesyłany obiekt `GitCredentials` który w przypadku autoryzacji user/password
powinien mieć ustawione odpowiednio `username` i `password` a w przypadku uwierzytelniania
za pomocą tokena OAuth należy ten token wstawić w `username` a pole `password` ustawić puste `""` (nie `null`).

W chwili obecnej dane uwierzytelniające są współdzielone przez wszystkie taski, ale w URI jest to przywiązane do konkretnego taska,
bo docelowo będą trzymane razem z taskiem (aby każdy task mógł pochodzić z konta innego użytkownika).

### `GIT_CLONE` i `GIT_PULL` - klonowanie repozytorium lub jego aktualizacja
W chwili obecnej endpoint ma ten sam URI, metoda `POST` wymaga w body danych repozytorium (obiekt `GitResource`)
i klonuje pliki, natomiast metoda `PUT` robi odpowiednik `git pull` czyli aktualizuje pliki jeśli w repo się coś zmieniło.

### `DOCKER_*` - polecenia Dockera
Wszystkie endpointy `DOCKER_*` korzystają z metody `GET`. Odpowiednio:
- `up` - odpowiednik `docker-compose up --build` czyli pobiera/buduje obrazy na podstawie `docker-compose.yml` w rootfolderze taska, a następnie uruchamia zdefiniowane kontenery
- `down` - odpowiednik `docker-compose down` czyli zatrzymuje i usuwa kontenery dockera powiązane z danym taskiem
- `log` - pobiera plik `output.log` z rootfolderu danego taska, w którym to pliku zapisywane są wszystkie dane jakie polecenia wykonywane przez zadania normalnie wypisywałyby na konsolę (w tym logi samego Dockera).
- `cleanup` - czyści dane dockera takie jak utworzone voluminy, pobrane obrazy, cache buildera kontenerów itp. (dany task musi być w stanie `down`, nie wystarczy, że zakończył działanie, musi polecieć request `DOCKER_DOWN` aby Docker usunął utworzone kontenery)

### `FTP_*` - przesyłanie plików
Pliki są przesyłane jako `application/octet-stream`.
URI endpointa jest stałe, rodzaj podejmowanej akcji zależy od zastosowanej metody requestu:
- `POST` - wysłanie pliku
- `GET` - pobranie pliku
- `DELETE` - usunięcie pliku

Na razie nie ma zaimplementowanej obsługi folderów. Trzeba ustalić, czy na przykład request do folderu zwraca
listę plików, czy na przykład pakuje folder do ZIP-a i go przesyła (a możemy zrobić i jedno i drugie, z zachowaniem zależnym od
na przykład parametru `?zip`).

### `SYSTEM_LOAD` - dane dla balancera
Zwraca obiekt klasy `NodeLoad` w którym są podstawowe dane dotyczące obciążenia noda, ilości wątków procesora,
RAM (wolna/całkowita), dysku (tylko tego, na który jest skonfigurowane workspace dla tasków) itp.
Niestety średnie obciążenie z ostatniej minuty nie jest w Windowsie zaimplementowane (zawsze zwracane jest -1.0),
ale mam już swój pomysł jak to obejść :D

`totalTasks/activeTasks` na razie zwraca `null`, bo jeszcze nie mam zaimplementowanego zarządzania stanem zadań, kolejkowania oraz asynchronicznych requestów (na razie wszystkie requesty REST są blokujące).
Docelowo będzie też API do rejestrowania listenerów, ale najpierw muszę zaimplementować obsługę stanów tasków.

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
