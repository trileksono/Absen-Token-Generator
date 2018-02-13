package gov.tangerangkota.id.opendata.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Created by manjaro on 07/02/18.
 */
@Entity
@Table(name = "ubina_token_thl")
public class UbinaTokenThl implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "id_token")
  private Integer idToken;

  @Basic(optional = false)
  @Column(name = "nip")
  private String nip;

  @Basic(optional = false)
  @Column(name = "id_pegawai")
  private int idPegawai;

  @Column(name = "tanggal")
  @Temporal(TemporalType.DATE)
  private Date tanggal;

  @Column(name = "token_masuk")
  private String tokenMasuk;

  @Column(name = "token_pulang")
  private String tokenPulang;

  public UbinaTokenThl() {
  }

  public UbinaTokenThl(Integer idToken) {
    this.idToken = idToken;
  }

  public UbinaTokenThl(Integer idToken, String nip, int idPegawai) {
    this.idToken = idToken;
    this.nip = nip;
    this.idPegawai = idPegawai;
  }

  public Integer getIdToken() {
    return idToken;
  }

  public void setIdToken(Integer idToken) {
    this.idToken = idToken;
  }

  public String getNip() {
    return nip;
  }

  public void setNip(String nip) {
    this.nip = nip;
  }

  public int getIdPegawai() {
    return idPegawai;
  }

  public void setIdPegawai(int idPegawai) {
    this.idPegawai = idPegawai;
  }

  public Date getTanggal() {
    return tanggal;
  }

  public void setTanggal(Date tanggal) {
    this.tanggal = tanggal;
  }

  public String getTokenMasuk() {
    return tokenMasuk;
  }

  public void setTokenMasuk(String tokenMasuk) {
    this.tokenMasuk = tokenMasuk;
  }

  public String getTokenPulang() {
    return tokenPulang;
  }

  public void setTokenPulang(String tokenPulang) {
    this.tokenPulang = tokenPulang;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idToken != null ? idToken.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof UbinaTokenThl)) {
      return false;
    }
    UbinaTokenThl other = (UbinaTokenThl) object;
    if ((this.idToken == null && other.idToken != null) || (this.idToken != null && !this.idToken.equals(other.idToken))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "UbinaTokenThl{" +
        "idToken=" + idToken +
        ", nip=" + nip +
        ", idPegawai=" + idPegawai +
        ", tanggal=" + tanggal +
        ", tokenMasuk=" + tokenMasuk +
        ", tokenPulang=" + tokenPulang +
        '}';
  }
}
