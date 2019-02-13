/**
 *
 */
package com.broadsoft.ccone.rest.client.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * @author svytla
 */
public enum AgentState {

    IDLE("Idle"), AVAILABLE("Available"), RINGING("Ringing"), CONNECTED("Connected"), WRAPUP("WrapUp"), CONFERENCING(
            "Conferencing"), CONSULTING("Consulting"), NOTRESPONDING("NotResponding"), OTHER("other");

    private String name;

    AgentState(final String name) {
        this.name = name;
    }

    public static AgentState fromString(final String fname) {

        AgentState aType = null;

        final String name = StringUtils.lowerCase(fname);

        switch (name) {
        case "idle":
            aType = AgentState.IDLE;
            break;
        case "available":
            aType = AgentState.AVAILABLE;
            break;
        case "ringing":
            aType = AgentState.RINGING;
            break;
        case "connected":
            aType = AgentState.CONNECTED;
            break;
        case "wrapUp":
            aType = AgentState.WRAPUP;
            break;
        case "conferencing":
            aType = AgentState.CONFERENCING;
            break;
        case "notresponding":
            aType = AgentState.NOTRESPONDING;
            break;

        default:
            aType = AgentState.OTHER;
            break;
        // throw new NullPointerException(String.format("No state associated with the value %s ", fname));
        }

        return aType;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
