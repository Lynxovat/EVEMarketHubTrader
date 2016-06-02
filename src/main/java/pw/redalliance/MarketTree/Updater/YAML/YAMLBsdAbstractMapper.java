package pw.redalliance.MarketTree.Updater.YAML;

public abstract class YAMLBsdAbstractMapper<KeyT, ValT> implements YAMLBsdMapper<KeyT, ValT> {
    protected KeyT key;
    protected ValT value;

    @Override
    public KeyT getKey() {
        return this.key;
    }

    @Override
    public ValT getValue() {
        return this.value;
    }
}