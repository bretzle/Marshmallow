package marshmallow.database.transformers;

import marshmallow.database.collection.DataRow;

import javax.annotation.Nullable;

public class Transformer {

    protected DataRow data;
    protected boolean hasData;
    private boolean hasBeenChecked;

    public Transformer(DataRow data) {
        this.data = data;
        this.hasBeenChecked = false;
    }

    @Nullable
    public DataRow getRawData() {
        return data;
    }

    public final boolean hasData() {
        if (!hasBeenChecked) {
            hasData = check();
            hasBeenChecked = true;
        }
        return hasData;
    }

    protected boolean check() {
        return data != null;
    }

    protected void reset() {
        this.data = null;
    }

    @Override
    public String toString() {
        if (data != null) {
            return data.toString();
        }
        return super.toString();
    }
}
