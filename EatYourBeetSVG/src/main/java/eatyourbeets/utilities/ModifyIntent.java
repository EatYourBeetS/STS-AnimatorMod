package eatyourbeets.utilities;

public class ModifyIntent
{
    public enum ModifyIntentType{
        Weak,
        Shackles,
        EnchantedArmor
    }

    public ModifyIntentType type;
    public int amount;

    public ModifyIntent(ModifyIntentType type, int amount)
    {
        this.type = type;
        this.amount = amount;
    }
}
