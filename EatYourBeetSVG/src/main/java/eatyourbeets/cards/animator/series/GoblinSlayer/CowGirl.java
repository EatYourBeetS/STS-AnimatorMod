package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class CowGirl extends AnimatorCard
{
    public static final EYBCardData DATA = Register(CowGirl.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public CowGirl()
    {
        super(DATA);

        Initialize(0, 0, 4);

        SetAffinity_Earth();

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.Motivate();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.FetchFromPile(name, 1, player.drawPile)
        .SetOptions(false, false)
        .SetFilter(c -> c.type == CardType.ATTACK && c instanceof AnimatorCard && ((AnimatorCard) c).attackType == EYBAttackType.Normal).AddCallback(cards -> {
            for (AbstractCard c : cards) {
                GameUtilities.ModifyDamage(c,magicNumber,true);
            }
        });
    }
}