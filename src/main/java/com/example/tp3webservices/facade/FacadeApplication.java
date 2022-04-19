package com.example.tp3webservices.facade;

import com.example.tp3webservices.exceptions.AccessIllegalAUneQuestionException;
import com.example.tp3webservices.exceptions.QuestionInexistanteException;
import com.example.tp3webservices.exceptions.UtilisateurInexistantException;
import com.example.tp3webservices.modele.Question;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacadeApplication {
    /**
     * Les utilisateurs ne sont pas stockés ici, on n'utilise que leur identifiant integer
     * On stocke ici toutes les questions posées par chaque utilisateur
     */
    private Map<Integer, Collection<Question>> utilisateursQuestionsMap;

    /**
     * Map de toutes les questions posées
     */
    private Map<String,Question> questionsMap;

    public FacadeApplication() {
        utilisateursQuestionsMap = new HashMap<Integer, Collection<Question>>();
        questionsMap = new HashMap<String, Question>();

    }

    /**
     * Permet à un idUtilisateur de poser une question
     * @param idUtilisateur
     * @param question
     * @return l'identifiant string aléatoire de la question créée
     */
    public String ajouterUneQuestion(int idUtilisateur, String question) {
        Question question1 = new Question(idUtilisateur,question);
        questionsMap.put(question1.getIdQuestion(),question1);
        if (utilisateursQuestionsMap.containsKey(idUtilisateur)) {
            this.utilisateursQuestionsMap.get(idUtilisateur).add(question1);
        }
        else {
            Collection<Question> questions = new ArrayList<Question>();
            questions.add(question1);
            this.utilisateursQuestionsMap.put(idUtilisateur,questions);
        }
        return question1.getIdQuestion();
    }

    /**
     * Permet à un utilisateur de répondre à une question
     * @param idQuestion
     * @param reponse
     * @throws QuestionInexistanteException
     */
    public void repondreAUneQuestion(String idQuestion, String reponse) throws QuestionInexistanteException {
        if (this.questionsMap.containsKey(idQuestion)) {
            this.questionsMap.get(idQuestion).setReponse(reponse);
        }
        else {
            throw new QuestionInexistanteException();
        }
    }

    /**
     * Permet de récupérer toutes les questions en attente de réponse
     * @return
     */

    public Collection<Question> getQuestionsSansReponses(){
        return this.questionsMap.values().stream().filter(q ->Objects.isNull(
                q.getReponse()) || q.getReponse().isBlank()).collect(Collectors.<Question>toList());
    }

    /**
     * Permet à un utilisateur de récupérer toutes les questions qu'il a posées pour lesquelles
     * quelqu'un a répondu
     * @param idUtilisateur
     * @return
     * @throws UtilisateurInexistantException
     */
    public Collection<Question> getQuestionsAvecReponsesByUser(int idUtilisateur) throws UtilisateurInexistantException {
        if (this.utilisateursQuestionsMap.containsKey(idUtilisateur)) {
            return this.utilisateursQuestionsMap.get(idUtilisateur)
                    .stream().filter(q -> Objects.nonNull(q.getReponse())
                            && (!q.getReponse().isBlank())).collect(Collectors.toList());
        }
        else {
            throw new UtilisateurInexistantException();
        }
    }

    /**
     * Permet à un utilisateur de récupérer toutes les questions qu'il a posées pour lesquelles
     * personne n'a répondu
     * @param idUtilisateur
     * @return
     * @throws UtilisateurInexistantException
     */
    public Collection<Question> getQuestionsSansReponsesByUser(int idUtilisateur) throws UtilisateurInexistantException {
        if (this.utilisateursQuestionsMap.containsKey(idUtilisateur)) {
            return this.utilisateursQuestionsMap.get(idUtilisateur)
                    .stream()
                    .filter(q -> Objects.isNull(q.getReponse()) || q.getReponse().isBlank()).collect(Collectors.toList());
        }
        else {
            throw new UtilisateurInexistantException();

        }
    }

    /**
     * Permet de récupérer toutes les questions posées par un utilisateur
     * @param idUtilisateur
     * @return
     * @throws UtilisateurInexistantException
     */
    public Collection<Question> getToutesLesQuestionsByUser(int idUtilisateur) throws UtilisateurInexistantException {
        if (this.utilisateursQuestionsMap.containsKey(idUtilisateur)) {
            return this.utilisateursQuestionsMap.get(idUtilisateur);
        }
        else {
            throw new UtilisateurInexistantException();

        }
    }

    /**
     * Permet de récupérer l'ensemble des questions posées
     * @return
     */
    public Collection<Question> getToutesLesQuestions() {
        return questionsMap.values();
    }

    /**
     * Permet de récupérer une question à partir du moment où
     * cette personne appartient à l'utilisateur qui l'a posée
     * @param idUtilisateur
     * @param idQuestion
     * @return
     * @throws QuestionInexistanteException
     * @throws AccessIllegalAUneQuestionException
     * @throws UtilisateurInexistantException
     */
    public Question getQuestionByIdPourUnUtilisateur(int idUtilisateur,String idQuestion) throws QuestionInexistanteException, AccessIllegalAUneQuestionException, UtilisateurInexistantException {
        Question q = questionsMap.get(idQuestion);
        if (Objects.isNull(q))
            throw new QuestionInexistanteException();
        Collection<Question> questionsIdUtilisateur = this.utilisateursQuestionsMap.get(idUtilisateur);

        if (Objects.isNull(questionsIdUtilisateur)) {
            throw new UtilisateurInexistantException();
        }


        if (questionsIdUtilisateur.contains(q)) {
            return q;
        }
        else {
            throw new AccessIllegalAUneQuestionException();
        }

    }
}
