package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomCurses;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

import java.util.concurrent.atomic.AtomicInteger;

public class Kyubey extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kyubey.class)
            .SetSkill(0, CardRarity.RARE, EYBCardTarget.None)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage();
    public static final int SOUL_COST = 3;

    public Kyubey()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(2);
        SetAffinity_Dark(2);
        SetAffinity_Silver(1);

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.GainEnergy(secondaryValue);

        AtomicInteger soul = new AtomicInteger();
        for (int i = 0; i < SOUL_COST; i++) {
            GameActions.Bottom.SelectFromHand(name, 1, true)
                    .SetFilter(c -> c instanceof AnimatorCard && ((AnimatorCard) c).cooldown != null && ((AnimatorCard) c).cooldown.cardConstructor != null)
                    .AddCallback(cards -> {
                        for (AbstractCard c : cards) {
                            if (c instanceof AnimatorCard) {
                                ((AnimatorCard) c).cooldown.ProgressCooldown(1);
                                c.flash();
                                soul.addAndGet(1);
                            }
                        }
            });
        }
        if (soul.get() < secondaryValue) {
            GameActions.Bottom.Purge(this).ShowEffect(true);
            GameActions.Bottom.Add(new CreateRandomCurses(1, player.hand));
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.GainCorruption(secondaryValue);
        }
    }
}