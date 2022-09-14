package com.example.restdockerplatform;

import java.io.IOException;
import java.util.List;

public interface TaskRepository {

    /*
        Taski możliwe do wykonania są zdefiniowane jako projekty.
        Metoda służy do wyświetlenia wszystkich projektów.
        Metoda wykonywana przez admina
     */
    List<String> listTasks();

    /*
        Metoda służy do tego, żeby z plików w danym katalogu utwożyć repozytorium
        i dodać je do gita
     */
    void createTask(String taskName, String dir);

    /*
        Przypisanie taska do użytkownika odbywa się, poprzez stworzenie w projekcie brancha z id użytkownika
        Metoda służy do tego, aby wyświetlić wszystkie projekty, które zawierają branch o nazwie
        odpowiadającej id użytkownika
     */
    List<String> listUserTasks(String userId);

    /*
        Metoda służy do tego, aby w danym repozytorium utwożyć branch o nazwie userId
        i umieścić go w repozytorium
     */
    void assignTaskToUser(String taskId, String userId, String workDir);

    /*
        Metoda służąca do tego, aby sklonować projekt taskName i zrobić checkout brancha taskId
     */
    void getTask(String userId, String taskId, String workDir);

    /*
        Metoda służy do tego, aby umieścić danego task przypisany do użytkownika (tj branch
        o nazwie równej ID użytkownika w repozytorium zdalnym
     */
    void saveTask(String userId, String taskId, String workDir);
}
