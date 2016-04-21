package net.samagames.hub.interactions.magicchests;

class MagicChestKey
{
    public enum Stars { ONE, TWO, THREE, FOUR, FIVE }
    public enum Variant { NORMAL }

    private final Stars value;
    private final Variant variant;

    MagicChestKey(Stars value, Variant variant)
    {
        this.value = value;
        this.variant = variant;
    }

    public Stars getValue()
    {
        return this.value;
    }

    public Variant getVariant()
    {
        return this.variant;
    }
}
