package com.example.tp3webservices.controleur;

import com.example.tp3webservices.exceptions.AccessIllegalAUneQuestionException;
import com.example.tp3webservices.exceptions.LoginDejaUtiliseException;
import com.example.tp3webservices.exceptions.QuestionInexistanteException;
import com.example.tp3webservices.exceptions.UtilisateurInexistantException;
import com.example.tp3webservices.facade.FacadeApplication;
import com.example.tp3webservices.facade.FacadeUtilisateurs;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class SuiviControleur {
    //@Autowired
    private final FacadeUtilisateurs facadeUtilisateurs;
    private final FacadeApplication facadeApplication;

    //@Autowired
    public SuiviControleur(FacadeUtilisateurs facadeUtilisateurs, FacadeApplication facadeApplication) {
        this.facadeUtilisateurs = facadeUtilisateurs;
        this.facadeApplication = facadeApplication;
    }

    record UtilisateurDTO(@Email String email, @Size(min = 4, max = 20) String password){}

    @GetMapping("/hello")
    public ResponseEntity<String> test(Principal principal) {
        return ResponseEntity.ok(principal.toString());
    }


    @PostMapping("/utilisateurs")
    public void register(@RequestBody UtilisateurDTO u) throws LoginDejaUtiliseException {
        facadeUtilisateurs.inscrireUtilisateur(u.email, u.password);
    }

    @PostMapping(value = "/utilisateur/{id}/question")
    public ResponseEntity<String> addQuestion(Principal principal,
                                              @PathVariable int id,
                                              @RequestBody String question){
        try{
            int idUtilisateur = facadeUtilisateurs.getUtilisateurIntId(principal.getName());
            if (id==idUtilisateur){
                String idQuestion = facadeApplication.ajouterUneQuestion(idUtilisateur,question);
                return ResponseEntity.created(
                        URI.create("/api/utilisateur/"+idUtilisateur+"/question/"+idQuestion)
                ).body(
                        facadeApplication.getQuestionByIdPourUnUtilisateur(idUtilisateur,idQuestion)
                                .getLibelleQuestion()
                );
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (UtilisateurInexistantException | QuestionInexistanteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AccessIllegalAUneQuestionException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
