package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions._legacy.common.DrawAndUpgradeCardAction;
import eatyourbeets.actions.basic.DrawCards;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.metadata.Spellcaster;
import eatyourbeets.orbs.Earth;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;

public class DwarfShaman extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(DwarfShaman.class.getSimpleName(), EYBCardBadge.Synergy);

    public DwarfShaman()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(2, 0, 0);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.GoblinSlayer);
    }


    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + (Spellcaster.GetScaling() * 2));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActionsHelper2.ChannelOrb(new Earth(), true);

        if (HasActiveSynergy())
        {
            GameActionsHelper2.AddToTop(new DrawCards(1, true)
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
            }));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }
}