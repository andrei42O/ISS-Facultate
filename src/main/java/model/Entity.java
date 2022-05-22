package model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Entity<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 6529685098267751237L;

    protected T ID;

    public T getID() {
        return ID;
    }

    public void setID(T ID) {
        this.ID = ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Entity)) return false;
        Entity<?> entity = (Entity<?>)o;
        return getID().equals(entity.getID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    @Override
    public String toString() {
        return "Entity{" + "ID=" + ID + '}';
    }
}
