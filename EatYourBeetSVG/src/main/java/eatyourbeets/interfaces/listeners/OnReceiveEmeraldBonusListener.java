package eatyourbeets.interfaces.listeners;

public interface OnReceiveEmeraldBonusListener
{
    float GetEmeraldMaxHPBonus(float bonus);

    int GetEmeraldMetallicizeBonus(int bonus);

    int GetEmeraldRegenBonus(int bonus);

    int GetEmeraldStrengthBonus(int bonus);
}
