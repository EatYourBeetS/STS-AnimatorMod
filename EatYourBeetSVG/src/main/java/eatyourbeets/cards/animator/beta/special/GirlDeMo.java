package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class GirlDeMo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GirlDeMo.class).SetSkill(3, CardRarity.SPECIAL, EYBCardTarget.None).SetSeries(CardSeries.AngelBeats);

    public GirlDeMo()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetAffinity_Star(2);
        SetHarmonic(true);
        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(1).AddCallback(() -> {
            GameActions.Bottom.SelectFromHand(name, player.hand.size(), true).AddCallback(cards -> {
                for (AbstractCard c : cards)
                {
                    GameActions.Top.Motivate(c, 1);
                }
            });
        });

        for (int i = 0; i < secondaryValue; i++) {
            Affinity lowest = JUtils.FindMin(Affinity.Basic(), CombatStats.Affinities::GetPowerAmount);
            GameActions.Bottom.StackAffinityPower(lowest, magicNumber, false);
        }
    }
}