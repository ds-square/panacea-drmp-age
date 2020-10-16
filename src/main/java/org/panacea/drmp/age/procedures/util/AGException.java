package org.panacea.drmp.age.procedures.util;

public class AGException extends Exception {
	public AGException() { super(); }
	public AGException(String message) { super("[AttackGraphException] " + message); }
	public AGException(String message, Throwable cause) { super("[AttackGraphException] " + message, cause); }
	public AGException(Throwable cause) { super(cause); }
}
