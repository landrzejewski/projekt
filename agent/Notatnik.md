# Agent

## Do zrobienia
- [ ] Dokumentacja do API, aby pozostali mogli już testować na "żywym organizmie" co najmniej w podstawowym zakresie
- [ ] **TESTY** :D
- [ ] Przedyskutować zasady obsługi niepowodzenia wykonania poleceń i obsługi nieprzewidywalnych błędów
- [ ] Pomyśleć jak liczyć średni cpuload, bo Windows nie ma tego zaimplementowanego a zwracanie chwilowego `getCpuLoad()` nie jest miarodajne /może "daemonem" odpytywać co sekundę i samemu wyliczać?/
- [ ] Rejestrowanie listenerów (w postaci dostarczonego URL na jakie nod ma robić POST-requesty) gdy status zadania ulegnie zmianie, pojawi się wpis w logu itp.
- [ ] Wzorzec projektowy stanu jeśli chodzi o Task (najpierw zbiór stanów, potem rozrysować graf dla newState(currentState, command ? true : false))
- [ ] Lokalne zarządzanie kolekcją Tasków w tym:
  - [ ] Triggery gdy Docker zwróci kod wyjścia różny od 0 lub przekroczy ustawiony timeout
  - [ ] Automatyczne zrobienie paczki ZIP z plików wyjściowych gdy zadanie się zakończy
  - [ ] Złożenie logów Dockera w jakimś persistence i automatyczne `docker compose down` gdy zadanie się zakończy
  - [ ] Zastanowić się, czy automatycznie czyścić zasoby Dockera (voluminy, obrazy, wirtualne sieci itp.) gdy zadanie się zakończy
  - [ ] Co robić w przypadku kolizji zasobów (na przykład gdy dwa taski mapują ten sam port tego samego interfejsu sieciowego)
  - [ ] Automatyczne sprzątanie zasobów Dockera gdy wyskoczy błąd w `docker-compose.yml` lub `Dockerfile`
- [ ] CommandExecutor
  - [ ] asynchroniczne (nie blokujące REST) wykonywanie zadań z kolejkowaniem i obsługą triggerów zmiany stanu
  - [ ] dobra obsługa timeoutów (to co jest teraz, czasem się wykoleja i nie można na tym polegać, więc na razie odpuszczone) 
- [X] Status i informacje o dostępnych zasobach (ilość zadań, ilość uruchomionych zadań, pamięć, wolna przestrzeń dysku, obciążenie procesora itp.) dla loadbalancera
- [X] Kompletne API do FTP:
- [x] Osobne API dla Dockera (aby nie trzeba było wysyłać surowych poleceń) /podstawy zrobione/
  - [X] Obsługa pobierania logów (czy zrobić to poprzez zapisanie do pliku i podania tego pliku, czy strumieniowo)
