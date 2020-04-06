package eatyourbeets.interfaces.subscribers;

public interface OnReceiveEmeraldBonus
{
    float GetEmeraldMaxHPBonus(float bonus);

    int GetEmeraldMetallicizeBonus(int bonus);

    int GetEmeraldRegenBonus(int bonus);

    int GetEmeraldStrengthBonus(int bonus);
}
