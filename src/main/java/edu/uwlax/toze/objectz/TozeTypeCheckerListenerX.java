package edu.uwlax.toze.objectz;

public interface TozeTypeCheckerListenerX
{
    public void clearTypeErrors();

    public void typeError(String id, String msg, TozeToken token);

    public void setFocusIfHas(String id);
}
