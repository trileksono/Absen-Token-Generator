package gov.tangerangkota.id.opendata.repo;

import gov.tangerangkota.id.opendata.entity.UbinaTokenThl;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by manjaro on 07/02/18.
 */
public interface UbinaTokenThlDAO extends CrudRepository<UbinaTokenThl, Integer> {

  @Modifying
  @Transactional
  @Query("delete from UbinaTokenThl where tanggal=?1")
  void deleteByTanggal(Date date);

  @Query
  List<UbinaTokenThl> findByTanggal(Date date);
}
