package main.message;

import java.time.LocalDate;
import java.util.ArrayList;

import manager.datamanager.freetimemanager.Interval;

public class FreeTimeSearchMessage implements Message{
	
	private ArrayList<Interval> freeTimeList;
	private LocalDate date;

	public FreeTimeSearchMessage(LocalDate date, ArrayList<Interval> list){
		this.date = date;
		this.freeTimeList = list;
	}
	
	public Type getType() {
		return Type.FREE_TIME_SEARCH_SUCCESSFUL;
	}
	
	public LocalDate getDate(){
		return date;
	}
	
	public ArrayList<Interval> getFreeTimelist(){
		return freeTimeList;
	}

	
}
