package gov.tangerangkota.id.sikda.repo;

import gov.tangerangkota.id.sikda.entity.RPegawaiAktual;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by manjaro on 07/02/18.
 */
public interface RPegawaiAktualDAO extends CrudRepository<RPegawaiAktual,Integer> {

  @Query(value = "select a from RPegawaiAktual a where a.statusKepegawaian = 'thl'")
  Page<RPegawaiAktual> getAllPegawaiThl(Pageable pageable);

  @Query(value = "select a from RPegawaiAktual a where a.statusKepegawaian = 'thl'")
  List<RPegawaiAktual> getAllPegawaiThl();
}
