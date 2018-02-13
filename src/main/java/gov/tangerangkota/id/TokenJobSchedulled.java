package gov.tangerangkota.id;

import gov.tangerangkota.id.opendata.entity.UbinaTokenThl;
import gov.tangerangkota.id.sikda.entity.RPegawaiAktual;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by manjaro on 12/02/18.
 */
@Component
public class TokenJobSchedulled extends TokenJobBase {

  @Scheduled(cron = "0 15 1 * * ?")
//  @Scheduled(cron = "0 0/3 * * * ?")
  private void doCreateToken() {
    if (!mTokenThlDAO.findByTanggal(new Date()).isEmpty()) {
      mLogger.info("** Job Token THL not running, token was created **");
      return;
    }
    mLogger.info("** Job Token THL running on " + new Date() + " **");

    Sort sort = new Sort(Direction.ASC, "idPegawai");
    Page<RPegawaiAktual> p = mPegawaiAktualDAO.getAllPegawaiThl(new PageRequest(0, 500, sort));
    List<List<RPegawaiAktual>> datas = new ArrayList<>();

    for (int x = 0; x < p.getTotalPages(); x++) {
      datas.add(mPegawaiAktualDAO.getAllPegawaiThl(new PageRequest(x, 500, sort)).getContent());
    }
    zipObservable(datas, p.getTotalPages(), new Date())
        .flatMap((Function<List<UbinaTokenThl>, ObservableSource<?>>) ubinaTokenThls -> Observable
            .fromCallable(() -> mTokenThlDAO.save(ubinaTokenThls)).subscribeOn(Schedulers.io()))
        .toList()
        .doOnError(throwable -> {
          throwable.printStackTrace();
          mLogger.info("** Job running with error **");
        })
        .subscribe(objects -> mLogger.info("** Job has finish on " + new Date() + " **"));
  }

  @Bean
  public Executor poolScheduler() {
    return new SimpleAsyncTaskExecutor();
  }
}
