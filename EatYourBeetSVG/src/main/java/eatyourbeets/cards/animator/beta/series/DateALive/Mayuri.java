package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Mayuri extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Mayuri.class).SetAttack(2, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.Random).SetSeriesFromClassPackage();

    public Mayuri()
    {
        super(DATA);

        Initialize(7, 3, 2, 1);
        SetUpgrade(3, 1, 0);
        SetAffinity_Light(1, 1, 2);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return other.rarity.equals(CardRarity.BASIC);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.BLUNT_HEAVY);
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.SelectFromHand(name, secondaryValue, false)
                .SetOptions(true, true, true)
                .SetMessage(RetainCardsAction.TEXT[0])
                .SetFilter(c -> c.tags.contains(CardTags.STARTER_DEFEND))
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        AbstractCard card = cards.get(0);
                        GameUtilities.Retain(card);
                        if (info.IsSynergizing && info.Synergies.GetLevel(Affinity.Blue, true) > 1 && CombatStats.TryActivateSemiLimited(cardID)) {
                            card.baseBlock += magicNumber;
                        }
                    }
                });
    }
}