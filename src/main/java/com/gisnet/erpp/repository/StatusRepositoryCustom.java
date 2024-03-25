package com.gisnet.erpp.repository;

import java.util.List;
import java.util.Set;

import com.gisnet.erpp.domain.Status;

public interface StatusRepositoryCustom {
	public abstract List<Status> getAllUsable();
}
