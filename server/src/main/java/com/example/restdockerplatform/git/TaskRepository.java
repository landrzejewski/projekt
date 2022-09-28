package com.example.restdockerplatform.git;

import org.eclipse.jgit.errors.RepositoryNotFoundException;

import java.util.List;

public interface TaskRepository {

    /*
        Taski możliwe do wykonania są zdefiniowane jako projekty.
        Metoda służy do wyświetlenia wszystkich projektów.
        Metoda wykonywana przez admina
     */
    List<String> listTasks();

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
    void assignTaskToUser(String taskId, String userId);

    void getTask(String userId, String taskId);

    /*
        Metoda służy do tego, aby umieścić danego task przypisany do użytkownika (tj branch
        o nazwie równej ID użytkownika w repozytorium zdalnym
     */

    void saveTask(String userId, String taskId) throws RepositoryNotFoundException;
}
