package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class DwarfShaman extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DwarfShaman.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental);

    public DwarfShaman()
    {
        super(DATA);

        Initialize(2, 0, 3);
        SetUpgrade(4, 0, 0);
        SetScaling(1, 0, 1);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.GoblinSlayer);
        SetSpellcaster();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.ChannelOrb(new Earth(), true);

        if (HasSynergy())
        {
            GameActions.Top.Draw(1)
            .SetFilter(AbstractCard::canUpgrade, true)
            .AddCallback(cards ->
            {
                for (AbstractCard card : cards)
                {
                    if (card.canUpgrade())
                    {
                        card.upgrade();
                        card.flash();
                    }
                }
            });
        }
    }
}