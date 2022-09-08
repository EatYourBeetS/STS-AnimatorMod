package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.powers.affinity.animatorClassic.*;
import eatyourbeets.resources.GR;

public abstract class AnimatorClassicAffinityPower extends AbstractAffinityPower
{
    protected AnimatorClassicAffinityPower(Affinity affinity, String powerID, String symbol)
    {
        super(affinity, powerID, symbol);
    }

    public static AnimatorClassicAffinityPower CreatePower(Affinity affinity)
    {
        switch (affinity)
        {
            case Red: return new ForcePower();
            case Green: return new AgilityPower();
            case Blue: return new IntellectPower();
            case Light: return new BlessingPower();
            case Dark: return new CorruptionPower();

            default: throw new RuntimeException("Invalid enum value: " + affinity.name());
        }
    }

    public float ApplyScaling(float multi)
    {
        return amount * multi * 0.5f;
    }

    public float ApplyScaling(EYBCard card, float base)
    {
        return base + ApplyScaling(card.affinities.GetScaling(affinity, true));
    }

    public AbstractPlayer.PlayerClass GetPlayerClass()
    {
        return GR.AnimatorClassic.PlayerClass;
    }
}