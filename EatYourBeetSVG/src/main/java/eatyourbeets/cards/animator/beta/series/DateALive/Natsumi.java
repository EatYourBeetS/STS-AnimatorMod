package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Natsumi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Natsumi.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.Random)
            .SetSeriesFromClassPackage();

    public Natsumi()
    {
        super(DATA);

        Initialize(2, 0, 2, 2);
        SetUpgrade(1,0, 1, 0);
        SetAffinity_Blue(1, 0, 2);
        SetExhaust(true);

        SetAffinityRequirement(Affinity.Blue, 7);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.FIRE).forEach(d -> d
        .SetOptions(true, false));
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SelectFromHand(name, magicNumber, false)
        .SetOptions(true, true, true)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
        .AddCallback(cards ->
        {
            boolean upgrade = TrySpendAffinity(Affinity.Blue);
            int gainAmount = 0;
            for (AbstractCard card : cards)
            {
                if (!GameUtilities.IsHindrance(card)) {
                    gainAmount += secondaryValue;
                }
                AbstractCard replacement = AbstractDungeon.getCard(CardRarity.UNCOMMON);
                if (upgrade) {
                    replacement.upgrade();
                }
                GameActions.Bottom.ReplaceCard(card.uuid, replacement);
            }
            if (gainAmount > 0) {
                GameActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.Sorcery, gainAmount);
            }
        });

    }
}