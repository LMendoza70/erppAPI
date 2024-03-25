package com.gisnet.erpp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.gisnet.erpp.job.AnalisisPorSistemaJob;
import com.gisnet.erpp.repository.ParametroRepository;

@Configuration  
public class QuartzConfiguration {
	@Autowired
	AnalisisPorSistemaJob analisisPorSistemaJob;
	
	@Autowired
	ParametroRepository parametroRepository;
	
	//TODO borrar, No se usa por el momento
	
	@Bean
	public MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean() {
		MethodInvokingJobDetailFactoryBean obj = new MethodInvokingJobDetailFactoryBean();
		obj.setTargetObject(analisisPorSistemaJob);;
		obj.setTargetMethod("procesar");
		return obj;
	}
		 
	@Bean
	public CronTriggerFactoryBean cronTriggerFactoryBean(){
		CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
		stFactory.setJobDetail(methodInvokingJobDetailFactoryBean().getObject());		
		stFactory.setName("triggerAutomatico");
		stFactory.setGroup("grupo");
		stFactory.setCronExpression(parametroRepository.findByCve("QUARTZ_CRON_PRELACIONES").getValor());
		return stFactory;
	}
	
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		boolean iniciar = Boolean.valueOf(parametroRepository.findByCve("QUARTZ_INICIAR").getValor());
		if (iniciar){
			scheduler.setTriggers(cronTriggerFactoryBean().getObject());
		}
		return scheduler;
	}
	
	
	//JADV-SUSPENSION
	@Bean
	public MethodInvokingJobDetailFactoryBean SuspensionInvokingJobDetailFactoryBean() {
		MethodInvokingJobDetailFactoryBean obj = new MethodInvokingJobDetailFactoryBean();
		obj.setTargetObject(analisisPorSistemaJob);;
		obj.setTargetMethod("DepurarPrelacionesSuspendidas");
		return obj;
	}
	
	@Bean
	public CronTriggerFactoryBean SuspensionCronTriggerFactoryBean(){
		CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
		stFactory.setJobDetail(SuspensionInvokingJobDetailFactoryBean().getObject());		
		stFactory.setName("triggerAutomatico_suspencion");
		stFactory.setGroup("grupo");
		stFactory.setCronExpression(parametroRepository.findByCve("QUARTZ_CRON_SUSPENSION").getValor());
		return stFactory;
	}
	
	@Bean
	public SchedulerFactoryBean SuspensionSchedulerFactoryBean() {
		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		boolean iniciar = Boolean.valueOf(parametroRepository.findByCve("QUARTZ_INICIAR").getValor());
		if (iniciar){
			scheduler.setTriggers(SuspensionCronTriggerFactoryBean().getObject());
		}
		return scheduler;
	}
	
} 
