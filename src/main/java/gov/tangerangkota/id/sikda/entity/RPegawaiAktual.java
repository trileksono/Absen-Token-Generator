package gov.tangerangkota.id.sikda.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by manjaro on 07/02/18.
 */
@Entity
@Table(name = "r_pegawai_aktual")
public class RPegawaiAktual implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "id")
  private Integer id;

  @Basic(optional = false)
  @Column(name = "id_pegawai")
  private int idPegawai;

  @Column(name = "nip")
  private String nip;

  @Basic(optional = false)
  @Column(name = "nip_baru")
  private String nipBaru;

  @Basic(optional = false)
  @Column(name = "nama_pegawai")
  private String namaPegawai;

  @Column(name = "status_kepegawaian")
  private String statusKepegawaian;

  public RPegawaiAktual() {
  }

  public RPegawaiAktual(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public int getIdPegawai() {
    return idPegawai;
  }

  public void setIdPegawai(int idPegawai) {
    this.idPegawai = idPegawai;
  }

  public String getNip() {
    return nip;
  }

  public void setNip(String nip) {
    this.nip = nip;
  }

  public String getNipBaru() {
    return nipBaru;
  }

  public void setNipBaru(String nipBaru) {
    this.nipBaru = nipBaru;
  }

  public String getNamaPegawai() {
    return namaPegawai;
  }

  public void setNamaPegawai(String namaPegawai) {
    this.namaPegawai = namaPegawai;
  }


  public String getStatusKepegawaian() {
    return statusKepegawaian;
  }

  public void setStatusKepegawaian(String statusKepegawaian) {
    this.statusKepegawaian = statusKepegawaian;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RPegawaiAktual that = (RPegawaiAktual) o;

    if (idPegawai != that.idPegawai) {
      return false;
    }

    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }
    if (nip != null ? !nip.equals(that.nip) : that.nip != null) {
      return false;
    }
    if (nipBaru != null ? !nipBaru.equals(that.nipBaru) : that.nipBaru != null) {
      return false;
    }
    if (namaPegawai != null ? !namaPegawai.equals(that.namaPegawai) : that.namaPegawai != null) {
      return false;
    }

    return statusKepegawaian != null ? !statusKepegawaian.equals(that.statusKepegawaian)
        : that.statusKepegawaian != null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + idPegawai;
    result = 31 * result + (nip != null ? nip.hashCode() : 0);
    result = 31 * result + (nipBaru != null ? nipBaru.hashCode() : 0);
    result = 31 * result + (namaPegawai != null ? namaPegawai.hashCode() : 0);

    result = 31 * result + (statusKepegawaian != null ? statusKepegawaian.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "RPegawaiAktual{" +
        "id=" + id +
        ", idPegawai=" + idPegawai +
        ", namaPegawai='" + namaPegawai + '\'' +
        ", statusKepegawaian='" + statusKepegawaian + '\'' +
        '}';
  }
}