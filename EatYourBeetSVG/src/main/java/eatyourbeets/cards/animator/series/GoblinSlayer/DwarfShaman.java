package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class DwarfShaman extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(DwarfShaman.class, EYBCardBadge.Synergy);

    public DwarfShaman()
    {
        super(ID, 1, CardRarity.COMMON, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(2, 0, 0);
        SetUpgrade(4, 0, 0);

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