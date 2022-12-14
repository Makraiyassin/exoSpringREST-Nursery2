package be.bstorm.akimts.rest.bxl.service.impl;

import be.bstorm.akimts.rest.bxl.exceptions.ElementNotFoundException;
import be.bstorm.akimts.rest.bxl.exceptions.UpdateTutorNotFoundException;
import be.bstorm.akimts.rest.bxl.mapper.EnfantMapper;
import be.bstorm.akimts.rest.bxl.model.entities.Enfant;
import be.bstorm.akimts.rest.bxl.model.entities.Tuteur;
import be.bstorm.akimts.rest.bxl.model.forms.EnfantUpdateForm;
import be.bstorm.akimts.rest.bxl.repository.EnfantRepository;
import be.bstorm.akimts.rest.bxl.repository.TuteurRepository;
import be.bstorm.akimts.rest.bxl.service.EnfantService;
import be.bstorm.akimts.rest.bxl.service.TuteurService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
//@Primary
public class EnfantServiceImpl implements EnfantService {

    private final EnfantRepository repository;
    private final TuteurRepository tuteurRepository;
    private final EnfantMapper enfantMapper;

    private final TuteurService tuteurService;

    public EnfantServiceImpl(EnfantRepository repository, TuteurRepository tuteurRepository, EnfantMapper enfantMapper, TuteurService tuteurService) {
        this.repository = repository;
        this.tuteurRepository = tuteurRepository;
        this.enfantMapper = enfantMapper;
        this.tuteurService = tuteurService;
    }

    @Override
    public Enfant create(Enfant toInsert) {
        if( toInsert == null)
            throw new IllegalArgumentException("inserted child cannot be null");

        toInsert.setId(null);

        return repository.save(toInsert);
    }

    @Override
    public Enfant getOne(Long id) {
        return repository.findById(id)
                .orElseThrow(()->new ElementNotFoundException(Enfant.class,id));
    }

    @Override
    public List<Enfant> getAll() {
        return repository.findAll();
    }

    @Override
    public Enfant update(Long id, Enfant toUpdate) {
        if(toUpdate == null || id == null)
            throw new IllegalArgumentException("params cannot be null");

        if( !repository.existsById(id) )
            throw new EntityNotFoundException();

        toUpdate.setId(id);
        return repository.save(toUpdate);
    }

    @Override
    public Enfant update(Long id, EnfantUpdateForm form) {
        if(form == null || id == null)
            throw new IllegalArgumentException("params cannot be null");

        if( !repository.existsById(id) )
            throw new EntityNotFoundException();

        Enfant toUpdate = enfantMapper.toEntity(form);

        if(form.getTuteurs()!= null && !form.getTuteurs().isEmpty()){
            Set<Long> formTuteursIds = form.getTuteurs();
            try {
                toUpdate.setTuteurs(formTuteursIds.stream().map(tuteurService::getOne).collect(Collectors.toSet()));
            }
            catch (ElementNotFoundException ex){
                throw new UpdateTutorNotFoundException(Tuteur.class);
            }

        }
        toUpdate.setId(id);
        return repository.save(toUpdate);
    }

    public Enfant updatePart(Long id, EnfantUpdateForm form) {
        if(form == null || id == null)
            throw new IllegalArgumentException("params cannot be null");

        Enfant toUpdate = getOne(id);

        if(form.getNom() != null) toUpdate.setNom(form.getNom());
        if(form.getPrenom() != null) toUpdate.setPrenom(form.getPrenom());
        if(form.getDateNaiss() != null) toUpdate.setDateNaissance(form.getDateNaiss());
        if(form.getAllergies() != null) toUpdate.setAllergies(form.getAllergies());
        if(form.isPropre() != toUpdate.isPropre()) toUpdate.setPropre(form.isPropre());

        if(form.getTuteurs()!= null && !form.getTuteurs().isEmpty()){
            Set<Long> formTuteursIds = form.getTuteurs();
            formTuteursIds.forEach(tId ->{
                if(!tuteurRepository.existsById(tId)) throw new UpdateTutorNotFoundException(Tuteur.class, tId);
            });
            toUpdate.setTuteurs(
                    formTuteursIds.stream().map(tuteurService::getOne).collect(Collectors.toSet())
            );
        }


        if( !repository.existsById(id) )
            throw new EntityNotFoundException();

        toUpdate.setId(id);
        return repository.save(toUpdate);
    }

    @Override
    public Enfant delete(Long id) {
        Enfant enfant = getOne(id);
        repository.delete(enfant);
        enfant.setId(0L);
        return enfant;
    }
}
