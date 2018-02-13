package gov.tangerangkota.id;

import gov.tangerangkota.id.opendata.entity.UbinaTokenThl;
import gov.tangerangkota.id.opendata.repo.UbinaTokenThlDAO;
import gov.tangerangkota.id.sikda.entity.RPegawaiAktual;
import gov.tangerangkota.id.sikda.repo.RPegawaiAktualDAO;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by manjaro on 12/02/18.
 */
class TokenJobBase {

  Logger mLogger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  RPegawaiAktualDAO mPegawaiAktualDAO;

  @Autowired
  UbinaTokenThlDAO mTokenThlDAO;


  private Observable<List<List<HashMap<String, String>>>> generateToken(Integer totalPage) {
    String alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    Set<String> stringSet = new HashSet<>();
    return Observable.range(0, (totalPage * 510) * 2)
        .map(integer -> {
          stringSet.add(RandomStringUtils.random(4, alphabet.toCharArray()));
          return integer;
        })
        .toList()
        .toObservable()
        .map(integers -> stringSet)
        .map(ArrayList::new)
        .flatMap(strings -> Observable.range(0, strings.size()).filter(s -> s % 2 == 0)
            .map(integer -> {
              HashMap<String, String> map = new HashMap<>();
              map.put("masuk", strings.get(integer));
              if (integer != strings.size() - 1) {
                map.put("pulang", strings.get(integer + 1));
              } else {
                map.put("pulang", strings.get(integer));
              }
              return map;
            })
        )
        .toList()
        .toObservable()
        .flatMap(
            (Function<List<HashMap<String, String>>, ObservableSource<?>>) hashMaps -> Observable.fromIterable(hashMaps)
                .buffer(500)).toList().toObservable()
        .map((Function<List<Object>, Object>) objects -> objects)
        .map(objects -> (List<List<HashMap<String, String>>>) objects);
  }

  Observable<List<UbinaTokenThl>> zipObservable(List<List<RPegawaiAktual>> datas, Integer totalPages,
      @Nullable Date tanggal) {
    Observable<List<RPegawaiAktual>> obData = Observable.fromIterable(datas);
    return generateToken(totalPages)
        .flatMap(lists -> Observable.fromIterable(lists).zipWith(obData, (hashMaps, rPegawaiAktuals) -> {
          Result result = new Result();
          result.setListToken(hashMaps);
          result.setListPegawai(rPegawaiAktuals);
          return result;
        }))
        .flatMap(result -> {
          Observable<HashMap<String, String>> obToken = Observable.fromIterable(result.getListToken());
          Observable<RPegawaiAktual> obPegawai = Observable.fromIterable(result.getListPegawai());
          return obToken
              .zipWith(obPegawai, (map, pegawaiAktual) -> {
                UbinaTokenThl thl = new UbinaTokenThl();
                thl.setNip(pegawaiAktual.getNipBaru());
                thl.setIdPegawai(pegawaiAktual.getIdPegawai());
                thl.setTanggal(tanggal);
                thl.setTokenMasuk(map.get("masuk"));
                thl.setTokenPulang(map.get("pulang"));
                return thl;
              }).toList().toObservable();
        })
        .onErrorReturn(throwable -> {
          throwable.printStackTrace();
          return new ArrayList<>();
        });
  }

  class Result {

    List<HashMap<String, String>> listToken;
    List<RPegawaiAktual> listPegawai;

    List<HashMap<String, String>> getListToken() {
      return listToken;
    }

    void setListToken(List<HashMap<String, String>> listToken) {
      this.listToken = listToken;
    }

    List<RPegawaiAktual> getListPegawai() {
      return listPegawai;
    }

    void setListPegawai(List<RPegawaiAktual> listPegawai) {
      this.listPegawai = listPegawai;
    }
  }
}
