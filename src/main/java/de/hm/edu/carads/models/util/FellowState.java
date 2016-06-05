package de.hm.edu.carads.models.util;

/**
 * Dieses Enum repreasentiert die drei moeglichen Status einer Buchungsanfrage.
 * @author BK
 *
 */
public enum FellowState {
	ASKED,	// Angefragt, noch nicht beantwortet.
	REJECTED, //Der Fahrer hat das Angebot abgelehnt.
	ACCEPTED; //Der Fahrer hat das Angebot angenommen.
}
