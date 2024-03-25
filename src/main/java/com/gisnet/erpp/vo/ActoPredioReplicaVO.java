package com.gisnet.erpp.vo;

import java.util.List;

import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.PrelacionPredio;

public class ActoPredioReplicaVO {

    private ActoPredio newActoPredio;
    private ActoPredio parentActoPredio;
    private Boolean modificable;
    private List<PrelacionPredio> prelacionPredio;





    public ActoPredio getNewActoPredio(){

        return this.newActoPredio;
    }

    public void setNewActoPredio(ActoPredio newActoPredio ){

        this.newActoPredio=newActoPredio;
    }

    public ActoPredio getParentActoPredio(){

        return this.parentActoPredio;
    }

    public void setParentActoPredio(ActoPredio parentActoPredio ){

        this.parentActoPredio=parentActoPredio;
    }

    public Boolean getModificable(){

        return this.modificable;
    }

    public void setModificable(Boolean modificable ){

        this.modificable=modificable;
    }

	public List<PrelacionPredio> getPrelacionPredio() {
		return prelacionPredio;
	}

	public void setPrelacionPredio(List<PrelacionPredio> prelacionPredio) {
		this.prelacionPredio = prelacionPredio;
	}

}
