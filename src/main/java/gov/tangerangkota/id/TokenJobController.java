package gov.tangerangkota.id;

import gov.tangerangkota.id.opendata.entity.UbinaTokenThl;
import gov.tangerangkota.id.sikda.entity.RPegawaiAktual;
import io.jmnarloch.spring.boot.rxjava.async.ObservableSseEmitter;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by manjaro on 07/02/18.
 */
@RestController
public class TokenJobController extends TokenJobBase {

  @PostMapping(produces = "application/json")
  public ObservableSseEmitter<HashMap<String, Object>> createToken(
      @RequestParam(value = "start_date") String start,
      @RequestParam(value = "end_date", required = false) String end
  ) throws ParseException {
    LocalDate startDate = LocalDate.parse(start);
    LocalDate endDate;
    Sort sort = new Sort(Direction.ASC, "idPegawai");

    Page<RPegawaiAktual> p = mPegawaiAktualDAO.getAllPegawaiThl(new PageRequest(0, 500, sort));
    List<List<RPegawaiAktual>> datas = new ArrayList<>();

    for (int x = 0; x < p.getTotalPages(); x++) {
      datas.add(mPegawaiAktualDAO.getAllPegawaiThl(new PageRequest(x, 500, sort)).getContent());
    }

    if (end != null) {
      endDate = LocalDate.parse(end);
      Long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
      return new ObservableSseEmitter<>(0L, MediaType.APPLICATION_JSON, Observable
          .range(0, numOfDaysBetween.intValue())
          .map(data -> {
            Date tanggal = Date.from(startDate.plusDays(data).atStartOfDay(ZoneId.systemDefault()).toInstant());
            mTokenThlDAO.deleteByTanggal(tanggal);
            return tanggal;
          })
          .flatMap(localDate -> zipObservable(datas, p.getTotalPages(), localDate))
          .subscribeOn(Schedulers.from(Executors.newFixedThreadPool(2)))
          .flatMap((Function<List<UbinaTokenThl>, ObservableSource<?>>) ubinaTokenThls -> Observable
              .fromCallable(() -> mTokenThlDAO.save(ubinaTokenThls)).subscribeOn(Schedulers.computation()))
          .toList().toObservable()
          .map(objects -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("Success", true);
            return map;
          })
          .onErrorReturn(throwable -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("Success", false);
            return map;
          })
      );
    } else {
      Date tanggal = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
      return new ObservableSseEmitter<>(0L, MediaType.APPLICATION_JSON,
          Observable.fromCallable(() -> {
            mTokenThlDAO.deleteByTanggal(tanggal);
            return true;
          }).flatMap(t -> zipObservable(datas, p.getTotalPages(), tanggal)
              .flatMap((Function<List<UbinaTokenThl>, ObservableSource<?>>) ubinaTokenThls -> Observable
                  .fromCallable(() -> mTokenThlDAO.save(ubinaTokenThls))
                  .subscribeOn(Schedulers.io()))
              .toList().toObservable()
          ).map(objects -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("Success", true);
            return map;
          }).onErrorReturn(throwable -> {
            throwable.printStackTrace();
            HashMap<String, Object> map = new HashMap<>();
            map.put("Success", false);
            return map;
          })
      );
    }
  }
}
