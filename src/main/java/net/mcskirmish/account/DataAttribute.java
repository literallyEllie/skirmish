package net.mcskirmish.account;

import com.google.common.collect.Lists;
import org.bson.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DataAttribute implements Serializable {

    private String string;
    private int integer;
    private long vLong;
    private boolean bool;
    private List<String> list;
    private Map<String, String> map;

    public DataAttribute(String string) {
        asS(string);
    }

    public DataAttribute(int integer) {
        asI(integer);
    }

    public DataAttribute(long vLong) {
        asL(vLong);
    }

    public DataAttribute(boolean bool) {
        asB(bool);
    }

    public DataAttribute(List<String> list) {
        asLi(list);
    }

    public DataAttribute(Map<String, String> map) {
        asM(map);
    }

    public String asS() {
        return string;
    }

    public void asS(String string) {
        this.string = string;
    }

    public int asI() {
        return integer;
    }

    public void asI(int integer) {
        this.integer = integer;
    }

    public long asL() {
        return vLong;
    }

    public void asL(long vLong) {
        this.vLong = vLong;
    }

    public boolean asB() {
        return bool;
    }

    public void asB(boolean bool) {
        this.bool = bool;
    }

    public List<String> asLi() {
        return list;
    }

    public void asLi(List<String> list) {
        this.list = list;
    }

    public Map<String, String> asM() {
        return map;
    }

    public void asM(Map<String, String> map ){
        this.map = map;
    }

}
