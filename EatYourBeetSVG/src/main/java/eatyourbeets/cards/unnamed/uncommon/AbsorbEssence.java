package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;

public class AbsorbEssence extends UnnamedCard
{
    public static final EYBCardData DATA = Register(AbsorbEssence.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.Minion);

    public AbsorbEssence()
    {
        super(DATA);

        Initialize(0, 0, 6, 1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(player, m, magicNumber, DamageInfo.DamageType.NORMAL, AttackEffects.POISON).SetVFXColor(Colors.Violet(1));
        GameActions.Bottom.GainEnergyNextTurn(1);
        GameActions.Bottom.DrawNextTurn(1);
    }
}