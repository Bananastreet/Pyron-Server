package io.xeros.model.PermanentAttributes;

 public class PermanentAttributeKey<T> extends AttributeKeyAdapter<T> {

    public PermanentAttributeKey(T defaultValue, String serializationKey) {
        super(defaultValue, AttributeSerializationType.PERMANENT, serializationKey);
    }

    @Override
    public String toString() {
        return String.format("default=%s, key=%s", defaultValue(), serializationKeyOrNull());
    }

}
