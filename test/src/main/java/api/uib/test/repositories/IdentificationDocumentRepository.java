package api.uib.test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import api.uib.test.entities.IdentificationDocument;

public interface IdentificationDocumentRepository extends JpaRepository<IdentificationDocument, Long> {
}
