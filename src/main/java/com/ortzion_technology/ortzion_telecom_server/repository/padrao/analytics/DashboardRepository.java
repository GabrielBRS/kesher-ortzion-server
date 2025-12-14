package com.ortzion_technology.ortzion_telecom_server.repository.padrao.analytics;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.analytics.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {

    @Query(
            value = """
        select dr
        from Dashboard dr
        where
        dr.tipoPessoa = :tipoUsuario
        and dr.idSubjectus = :idSubjectus
        and dr.idDepartamento = :idDepartamento
        and dr.idCanalMensageria = :idCanalMensageria
    """
    )
    Dashboard pegarDashboardMulticonta(
                                             @Param("tipoUsuario") Integer tipoUsuario,
                                             @Param("idSubjectus") Long idSubjectus,
                                             @Param("idDepartamento") Integer idDepartamento,
                                             @Param("idCanalMensageria") Integer idCanalMensageria);

    @Query(
            value = """
        select dr
        from Dashboard dr
        where
        dr.tipoPessoa = :tipoUsuario
        and dr.idSubjectus = :idSubjectus
        and dr.idDepartamento = :idDepartamento
        and dr.idColaborador = :idColaborador
        and dr.idCanalMensageria = :idCanalMensageria
    """
    )
    Dashboard pegarDashboardColaborador(
                                       @Param("tipoUsuario") Integer tipoUsuario,
                                       @Param("idSubjectus") Long idSubjectus,
                                       @Param("idDepartamento") Integer idDepartamento,
                                        @Param("idColaborador") Long idColaborador,
                                       @Param("idCanalMensageria") Integer idCanalMensageria);

}
