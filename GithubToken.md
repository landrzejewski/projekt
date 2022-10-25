# Projekt wspólny

Przeionsłem dane autoryzacyjne do pliku
`server/src/main/resources/github.properties`
i dodałem ten plik do `.gitignore` żeby GitHub nie blokował konta jeśli ktoś przez pomyłkę (co mi się zdarzyło dwa razy :D) wyeksportuje aktualny token.

Zawartość `github.properties` powinna być postaci:
```
github.url = https://github.com/
github.user = java-dev-pro-project
github.token = *tu wartość tokena*
```