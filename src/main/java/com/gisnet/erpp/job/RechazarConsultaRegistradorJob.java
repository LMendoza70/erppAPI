package com.gisnet.erpp.job;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Status;
import com.gisnet.erpp.service.ActoService;
import com.gisnet.erpp.service.PrelacionService;
import com.gisnet.erpp.service.StatusService;
import com.gisnet.erpp.util.Constantes;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RechazarConsultaRegistradorJob {

    private static final Logger log = LoggerFactory.getLogger(RechazarConsultaRegistradorJob.class);

    private final PrelacionService prelacionService;
    private final StatusService statusService;

    private final ActoService actoService;

    public RechazarConsultaRegistradorJob(PrelacionService prelacionService, StatusService statusService, ActoService actoService) {
        this.prelacionService = prelacionService;
        this.statusService = statusService;
        this.actoService = actoService;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Mexico_City")
    public void rechazarConsultaRegistradorConStatusSuspendido() {

        log.info("Iniciando job para rechazar consultas de registrador con mas de 10 dias");

        Date actualDate = new Date();

        Status status = statusService.findOne(17L);

        List<Prelacion> prelacionesSuspendidasPorRegistrador = prelacionService.findAllByStatus(status);

        List<Prelacion> prelacionesRechazadas = prelacionesSuspendidasPorRegistrador
                .stream()
                .filter(prelacion -> Objects.equals(prelacion.getStatus().getId(), Constantes.Status.SUSPENDIDA_REGISTRADOR.getIdStatus())
                        && prelacion.getPrelacionesParaSuspensiones()
                        .stream()
                        .anyMatch(suspension -> diasHabiles(suspension.getFechaSuspension(), actualDate) >= 10))
                .collect(Collectors.toList());

        prelacionesRechazadas.forEach(prelacion -> {

            Acto acto = actoService.findByPrelacionId(prelacion.getId()).get(0);
            actoService.rechazarActo(acto.getId(), 1L, 1L, "No se subsano el tiempo");
        });

        log.info("Prelaciones rechazadas: {}", prelacionesRechazadas.size());
    }

    public int diasHabiles(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;

        //Return 0 if start and end are the same
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 0;
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        do {
            //excluding start date
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

        return workDays;
    }
}
