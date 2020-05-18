package model;

import org.primefaces.model.timeline.TimelineEvent;

public class TimeLineEx extends TimelineEvent{
	
	private Evento evento;

	public TimeLineEx(Evento evento)  {
		super();
		setData(evento.getTitulo());
		setStartDate(evento.getDataInicio());
		this.evento = evento;
				
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	
	
	
	
}
