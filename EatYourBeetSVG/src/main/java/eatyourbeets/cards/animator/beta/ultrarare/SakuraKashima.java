package eatyourbeets.cards.animator.beta.ultrarare;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.Miracle;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

public class SakuraKashima extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(SakuraKashima.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None).SetSeries(CardSeries.Rewrite);
    static
    {
        DATA.AddPreview(new Miracle(), false);
    }

    private static final CardEffectChoice choices = new CardEffectChoice();

    public SakuraKashima()
    {
        super(DATA);

        Initialize(0, 0, 2, 80);
        SetUpgrade(0, 0, 0, -20);
        SetAffinity_Blue(2, 0, 0);

        SetExhaust(true);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.GainIntellect(magicNumber, false);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        String[] text = DATA.Strings.EXTENDED_DESCRIPTION;

        if (choices.TryInitialize(this))
        {
            choices.AddEffect(text[0], this::Effect1);
            choices.AddEffect(JUtils.Format(text[1], secondaryValue), this::Effect2);
        }

        choices.Select(1, m);
    }

    private void Effect1(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.MakeCardInHand(new Miracle());
    }

    private void Effect2(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Callback(() -> {
            int numMiraclesToMake = BaseMod.MAX_HAND_SIZE - player.hand.size();

            for (int i=0; i<numMiraclesToMake; i++)
            {
                GameActions.Bottom.MakeCardInHand(new Miracle());
            }

            GameActions.Bottom.StackPower(new SelfDamagePower(p, secondaryValue));
        });
    }
}