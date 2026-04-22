package Infnet.Assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Infnet.Assessment.model.Organizacao;

@Repository
public interface OrganizacaoRepository extends JpaRepository<Organizacao, Long> { 
}