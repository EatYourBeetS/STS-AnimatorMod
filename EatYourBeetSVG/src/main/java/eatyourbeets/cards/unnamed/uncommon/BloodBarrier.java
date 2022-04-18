package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.UnnamedPower;
import eatyourbeets.utilities.GameActions;

public class BloodBarrier extends UnnamedCard
{
    public static final EYBCardData DATA = Register(BloodBarrier.class)
            .SetMaxCopies(2)
            .SetPower(0, CardRarity.UNCOMMON);

    public BloodBarrier()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainMetallicize(magicNumber);
        GameActions.Bottom.StackPower(new BloodBarrierPower(p, 2));
    }

    public static class BloodBarrierPower extends UnnamedPower
    {
        public BloodBarrierPower(AbstractCreature owner, int amount)
        {
            super(owner, BloodBarrier.DATA);

            Initialize(amount);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            for (AbstractCreature c : GetAllies())
            {
                final float pitch = c.isPlayer ? 1 : 0;
                GameActions.Bottom.LoseHP(player, c, amount, AttackEffects.DAGGER)
                .CanKill(!c.isPlayer)
                .IgnoreTempHP(true)
                .SetSoundPitch(pitch, pitch)
                .SetDuration(0.15f, false);
            }
            flashWithoutSound();
        }
    }
}