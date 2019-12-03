package com.realeigenvalue.jcar.helper;

public class Location {
	public int x;
	public int y;
	public Location() {
		
	}
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Location(Location location) {
		setLocation(location);
	}
	public void setLocation(Location location) {
		this.x = location.x;
		this.y = location.y;
	}
	public boolean equals(Location location) {
		return (this.x == location.x) && (this.y == location.y);
	}
	public String toString() {
		return "[" + this.x + "," + this.y + "]";
	}
}