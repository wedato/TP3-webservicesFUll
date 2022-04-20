package com.example.tp3webservices.config;

import com.example.tp3webservices.facade.FacadeUtilisateurs;
import com.example.tp3webservices.modele.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomUserDetailsService implements UserDetailsService {

    private static final String[] ROLES_PROF =  {"ETUDIANT","PROF"};
    private static final String[] ROLES_ETUDIANT =  {"ETUDIANT"};
    private PasswordEncoder passwordEncoder;
    @Autowired
    FacadeUtilisateurs facadeUtilisateurs;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = facadeUtilisateurs.getUtilisateurByLogin(username);
        String[] roles = getRolesProfOuEtu(username);
        if (utilisateur==null){
            throw new UsernameNotFoundException("User"+username+"not found");
        }
        if (roles == null){
            throw new RuntimeException();
        }
        return User.builder()
                .username(utilisateur.getLogin())
                .password(passwordEncoder.encode(utilisateur.getMotDePasse()))
                .roles(roles)
                .build();

    }

    public static String[] getRolesProfOuEtu(String emailAdress)  {
        if (emailAdress.split("@")[1].equals("univ-orleans.fr"))
            return ROLES_PROF;
        if (emailAdress.split("@")[1].equals("etu.univ-orleans.fr"))
            return ROLES_ETUDIANT;
        return null;

    }

    public static void main(String[] args) {
        String mail = "jonathanbaltaci@univ-orleans.fr";


        if (mail.split("@")[1].equals("univ-orleans.fr"))
            System.out.println("le prof !");
        else System.out.println("pas le prof");
    }
}
