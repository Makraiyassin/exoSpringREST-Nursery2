package be.bstorm.akimts.rest.bxl.model.dto;

import be.bstorm.akimts.rest.bxl.model.entities.Personne;
import be.bstorm.akimts.rest.bxl.model.entities.Tuteur;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class TuteurDTO {
    private long id;
    private String nom;
    private String prenom;
    private String adresse;
    private String numTel;
//    private Set<EnfantDTO> enfants;
    private Set<Long> enfantsIds;

//    public static class EnfantDTO{
//        private long id;
//        private String nom;
//        private String prenom;
//        private LocalDate dateNaiss;
//        private boolean proprete;
//        private List<String> allergies;
//    }

    public static TuteurDTO toDto(Tuteur tuteur){
        TuteurDTO tuteurDTO = new TuteurDTO();
        tuteurDTO.setId(tuteur.getId());
        tuteurDTO.setNom(tuteur.getNom());
        tuteurDTO.setPrenom(tuteur.getPrenom());
        tuteurDTO.setAdresse(tuteur.getAdresse());
        tuteurDTO.setNumTel(tuteur.getNumTel());
        tuteurDTO.setEnfantsIds(tuteur.getEnfants().stream().map(Personne::getId).collect(Collectors.toSet()));
        return tuteurDTO;
    }
}
